import { createSlice } from '@reduxjs/toolkit';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
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
  reducers: {},
});
export default DocumentSlice.reducer;
