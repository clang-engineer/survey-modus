import { createAsyncThunk, createSlice, isPending } from '@reduxjs/toolkit';
import { defaultValue } from 'app/shared/model/document.model';
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
  return axios.get(requestUrl);
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
    builder.addMatcher(isPending(getDocumentById), state => {
      state.errorMessage = null;
      state.updateSuccess = false;
      state.loading = true;
    });
  },
});

export const { reset } = DocumentSlice.actions;
export default DocumentSlice.reducer;
