import { createAsyncThunk, createSlice, isPending, isRejected, isFulfilled } from '@reduxjs/toolkit';
import { defaultValue, IDocument } from 'app/shared/model/document.model';
import axios from 'axios';
import { serializeAxiosError } from 'app/shared/reducers/reducer.utils';

const initialState = {
  loading: false,
  errorMessage: null,
  documents: [],
  document: defaultValue,
  updating: false,
  updateSuccess: false,
};

export const getDocumentById = createAsyncThunk(
  'document/fetch_document',
  async (props: { collectionId: string; documentId: string }) => {
    const requestUrl = `api/collections/${props.collectionId}/documents/${props.documentId}`;
    return axios.get<IDocument>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getDocumentsByFormId = createAsyncThunk(
  'document/fetch_documents',
  async (props: { collectionId: string; formId: string }) => {
    const requestUrl = `api/collections/${props.collectionId}/documents?formId=${props.formId}`;
    return axios.get<IDocument[]>(requestUrl);
  }
);

export const DocumentSlice = createSlice({
  name: 'document',
  initialState,
  reducers: {
    reset() {
      return initialState;
    },
  },
  extraReducers(builder) {
    builder
      .addCase(getDocumentById.fulfilled, (state, action) => {
        state.loading = false;
        state.document = action.payload.data;
      })
      .addMatcher(isFulfilled(getDocumentsByFormId), (state, action) => {
        const { data, headers } = action.payload;
        return {
          ...state,
          loading: false,
          documents: data,
        };
      })
      .addMatcher(isPending(getDocumentsByFormId, getDocumentById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isRejected(getDocumentsByFormId, getDocumentById), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = DocumentSlice.actions;
export default DocumentSlice.reducer;
