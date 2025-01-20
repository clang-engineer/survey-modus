import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { defaultValue, ISurvey, SURVEY_COMPANY_ID, SURVEY_FORM_ID, SURVEY_ID } from 'app/shared/model/survey.model';
import axios from 'axios';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  surveys: [],
  survey: defaultValue,
  updating: false,
  updateSuccess: false,
};

export const getSurveyById = createAsyncThunk(
  'survey/fetch_survey',
  async (props: { surveyId: string }) => {
    const requestUrl = `api/surveys/${props.surveyId}`;
    return axios.get<ISurvey>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getSurveys = createAsyncThunk('survey/fetch_surveys', async (props: { query: string }) => {
  const requestUrl = `api/surveys?${props.query}`;
  return axios.get<ISurvey[]>(requestUrl);
});

export const createSurvey = createAsyncThunk(
  'survey/create_survey',
  async (props: { survey: ISurvey }, thunkAPI) => {
    const { survey } = props;
    const result = await axios.post<ISurvey>(`api/surveys`, survey);

    const query = `companyId.equals=${survey[SURVEY_COMPANY_ID]}&formId.equals=${survey[SURVEY_FORM_ID]}`;
    thunkAPI.dispatch(getSurveys({ query }));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateSurvey = createAsyncThunk(
  'survey/update_survey',
  async (props: { survey: ISurvey }, thunkAPI) => {
    const { survey } = props;

    const result = await axios.put<ISurvey>(`api/surveys/${survey[SURVEY_ID]}`, props.survey);

    const query = `companyId.equals=${survey[SURVEY_COMPANY_ID]}&formId.equals=${survey[SURVEY_FORM_ID]}`;
    thunkAPI.dispatch(getSurveys({ query }));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteSurvey = createAsyncThunk(
  'survey/delete_survey',
  async (props: { survey: ISurvey }, thunkAPI) => {
    const { survey } = props;

    const requestUrl = `api/surveys/${props.survey[SURVEY_ID]}`;
    const result = await axios.delete(requestUrl);

    const query = `companyId.equals=${survey[SURVEY_COMPANY_ID]}&formId.equals=${survey[SURVEY_FORM_ID]}`;
    thunkAPI.dispatch(getSurveys({ query }));

    return result;
  },
  { serializeError: serializeAxiosError }
);

export const SurveySlice = createSlice({
  name: 'survey',
  initialState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getSurveyById.fulfilled, (state, action) => {
        state.loading = false;
        state.survey = action.payload.data;
      })
      .addCase(deleteSurvey.fulfilled, (state, action) => {
        state.updating = false;
        state.updateSuccess = true;
        state.survey = defaultValue;
      })
      .addMatcher(isFulfilled(getSurveys), (state, action) => {
        const { data, headers } = action.payload;
        return {
          ...state,
          loading: false,
          surveys: data,
        };
      })
      .addMatcher(isFulfilled(createSurvey, updateSurvey), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.survey = action.payload.data;
      })
      .addMatcher(isPending(getSurveys, getSurveyById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createSurvey, updateSurvey, deleteSurvey), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getSurveys, getSurveyById, createSurvey, updateSurvey, deleteSurvey), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = SurveySlice.actions;
export default SurveySlice.reducer;
