import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { createEntitySlice, EntityState, IQueryParams, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { defaultValue, IMessage } from 'app/shared/model/message.model';

const initialState: EntityState<IMessage> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

const apiUrl = 'api/messages';

// Actions

export const getEntities = createAsyncThunk('message/fetch_entity_list', async ({ page, size, sort, query }: IQueryParams) => {
  // const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}cacheBuster=${new Date().getTime()}`;
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}&` : '?'}${query || ''}`;
  return axios.get<IMessage[]>(requestUrl);
});

export const getEntity = createAsyncThunk(
  'message/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IMessage>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const createEntity = createAsyncThunk(
  'message/create_entity',
  async (entity: IMessage, thunkAPI) => {
    const result = await axios.post<IMessage>(apiUrl, cleanEntity(entity));
    const query = `?companyId.equals=${entity.companyId}`;
    thunkAPI.dispatch(getEntities({ query }));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateEntity = createAsyncThunk(
  'message/update_entity',
  async (entity: IMessage, thunkAPI) => {
    const result = await axios.put<IMessage>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    const query = `?companyId.equals=${entity.companyId}`;
    thunkAPI.dispatch(getEntities({ query }));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const partialUpdateEntity = createAsyncThunk(
  'message/partial_update_entity',
  async (entity: IMessage, thunkAPI) => {
    const result = await axios.patch<IMessage>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteEntity = createAsyncThunk(
  'message/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IMessage>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const createAndUpdateEntities = createAsyncThunk(
  'message/create_and_update_entity',
  (entities: IMessage[], thunkAPI) => {
    return axios.put<IMessage[]>(`${apiUrl}/all`, [...entities.map(entity => cleanEntity(entity))]);
  },
  { serializeError: serializeAxiosError }
);

// slice

export const MessageSlice = createEntitySlice({
  name: 'message',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data, headers } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data,
          totalItems: parseInt(headers['x-total-count'], 10),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isFulfilled(createAndUpdateEntities), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entities = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity, createAndUpdateEntities), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = MessageSlice.actions;

// Reducer
export default MessageSlice.reducer;
