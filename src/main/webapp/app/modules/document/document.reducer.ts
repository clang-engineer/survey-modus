import { createAsyncThunk, createSlice, isPending, isRejected } from '@reduxjs/toolkit';
import { defaultValue, IDocument } from 'app/shared/model/document.model';
import axios from 'axios';

const initialState = {
  loading: false,
  errorMessage: null,
  documents: [],
  document: defaultValue,
  updating: false,
  updateSuccess: false,
};

export const getDocumentById = createAsyncThunk('document/fetch_document', async (props: { collectionId: string; documentId: string }) => {
  const requestUrl = `api/collections/${props.collectionId}/documents/${props.documentId}`;
  return axios.get<IDocument>(requestUrl);
});

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
      .addMatcher(isPending(getDocumentById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isRejected(getDocumentById), (state, action) => {
        state.loading = false;
        state.updating = false;
        state.updateSuccess = false;
        state.errorMessage = action.error.message;
      });
  },
});

export const { reset } = DocumentSlice.actions;
export default DocumentSlice.reducer;
