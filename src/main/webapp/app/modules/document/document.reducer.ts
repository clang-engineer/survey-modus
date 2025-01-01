import { createSlice } from '@reduxjs/toolkit';
import { defaultValue } from 'app/shared/model/document.model';

const initialState = {
  loading: false,
  errorMessage: null,
  documents: [],
  document: defaultValue,
  updating: false,
  updateSuccess: false,
};

export const DocumentSlice = createSlice({
  name: 'document',
  initialState,
  reducers: {
    reset() {
      return initialState;
    },
  },
});

export const { reset } = DocumentSlice.actions;
export default DocumentSlice.reducer;
