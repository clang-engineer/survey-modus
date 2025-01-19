import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { defaultValue, SURVEY_COMPANY_ID, SURVEY_FORM_ID, SURVEY_ID, ISurvey } from 'app/shared/model/survey.model';
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
  async (props: { collectionId: number; surveyId: string }) => {
    const requestUrl = `api/surveys/${props.surveyId}?collectionId=${props.collectionId}`;
    return axios.get<ISurvey>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getSurveysByCompanyIdAndFormId = createAsyncThunk(
  'survey/fetch_surveys',
  async (props: { collectionId: number; companyId: number; formId: number }) => {
    const { collectionId, companyId, formId } = props;
    const requestUrl = `api/surveys?collectionId=${collectionId}&companyId=${companyId}&formId=${formId}`;
    return axios.get<ISurvey[]>(requestUrl);
  }
);

export const createSurvey = createAsyncThunk(
  'survey/create_survey',
  async (props: { collectionId: number; survey: ISurvey }, thunkAPI) => {
    const { collectionId, survey } = props;
    const result = await axios.post<ISurvey>(`api/surveys?collectionId=${collectionId}`, survey);
    thunkAPI.dispatch(
      getSurveysByCompanyIdAndFormId({
        collectionId,
        companyId: survey[SURVEY_COMPANY_ID],
        formId: survey[SURVEY_FORM_ID],
      })
    );
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateSurvey = createAsyncThunk(
  'survey/update_survey',
  async (props: { collectionId: number; survey: ISurvey }, thunkAPI) => {
    const { collectionId, survey } = props;

    const result = await axios.put<ISurvey>(`api/surveys/${survey[SURVEY_ID]}?collectionId=${collectionId}`, props.survey);

    thunkAPI.dispatch(
      getSurveysByCompanyIdAndFormId({
        collectionId,
        companyId: survey[SURVEY_COMPANY_ID],
        formId: survey[SURVEY_FORM_ID],
      })
    );
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteSurvey = createAsyncThunk(
  'survey/delete_survey',
  async (props: { collectionId: number; survey: ISurvey }, thunkAPI) => {
    const { collectionId, survey } = props;

    const requestUrl = `api/surveys/${props.survey[SURVEY_ID]}?collectionId=${collectionId}`;
    const result = await axios.delete(requestUrl);

    thunkAPI.dispatch(
      getSurveysByCompanyIdAndFormId({
        collectionId,
        companyId: survey[SURVEY_COMPANY_ID],
        formId: survey[SURVEY_FORM_ID],
      })
    );

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
      .addMatcher(isFulfilled(getSurveysByCompanyIdAndFormId), (state, action) => {
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
      .addMatcher(isPending(getSurveysByCompanyIdAndFormId, getSurveyById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createSurvey, updateSurvey, deleteSurvey), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(isRejected(getSurveysByCompanyIdAndFormId, getSurveyById, createSurvey, updateSurvey, deleteSurvey), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = SurveySlice.actions;
export default SurveySlice.reducer;
