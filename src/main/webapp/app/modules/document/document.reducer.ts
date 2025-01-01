import { createAsyncThunk, createSlice, isFulfilled, isPending, isRejected } from '@reduxjs/toolkit';
import { defaultValue, DOCUMENT_COMPANY_ID, DOCUMENT_FORM_ID, DOCUMENT_ID, IDocument } from 'app/shared/model/document.model';
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
  async (props: { collectionId: number; documentId: string }) => {
    const requestUrl = `api/collections/${props.collectionId}/documents/${props.documentId}`;
    return axios.get<IDocument>(requestUrl);
  },
  { serializeError: serializeAxiosError }
);

export const getDocumentsByCompanyIdAndFormId = createAsyncThunk(
  'document/fetch_documents',
  async (props: { collectionId: number; companyId: number; formId: number }) => {
    const { collectionId, companyId, formId } = props;
    const requestUrl = `api/collections/${collectionId}/documents?companyId=${companyId}&formId=${formId}`;
    return axios.get<IDocument[]>(requestUrl);
  }
);

export const createDocument = createAsyncThunk(
  'document/create_document',
  async (props: { collectionId: number; document: IDocument }, thunkAPI) => {
    const { collectionId, document } = props;
    const result = await axios.post<IDocument>(`api/collections/${collectionId}/documents`, document);
    thunkAPI.dispatch(
      getDocumentsByCompanyIdAndFormId({
        collectionId: collectionId,
        companyId: document[DOCUMENT_COMPANY_ID],
        formId: document[DOCUMENT_FORM_ID],
      })
    );
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const updateDocument = createAsyncThunk(
  'document/update_document',
  async (props: { collectionId: number; document: IDocument }, thunkAPI) => {
    const { collectionId, document } = props;

    const result = await axios.put<IDocument>(`api/collections/${collectionId}/documents/${document[DOCUMENT_ID]}`, props.document);

    thunkAPI.dispatch(
      getDocumentsByCompanyIdAndFormId({
        collectionId: collectionId,
        companyId: document[DOCUMENT_COMPANY_ID],
        formId: document[DOCUMENT_FORM_ID],
      })
    );
    return result;
  },
  { serializeError: serializeAxiosError }
);

export const deleteDocument = createAsyncThunk(
  'document/delete_document',
  async (props: { collectionId: number; document: IDocument }, thunkAPI) => {
    const { collectionId, document } = props;

    const requestUrl = `api/collections/${props.collectionId}/documents/${props.document[DOCUMENT_ID]}`;
    const result = await axios.delete(requestUrl);

    thunkAPI.dispatch(
      getDocumentsByCompanyIdAndFormId({
        collectionId: collectionId,
        companyId: document[DOCUMENT_COMPANY_ID],
        formId: document[DOCUMENT_FORM_ID],
      })
    );

    return result;
  },
  { serializeError: serializeAxiosError }
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
      .addCase(deleteDocument.fulfilled, (state, action) => {
        state.updating = false;
        state.updateSuccess = true;
        state.document = defaultValue;
      })
      .addMatcher(isFulfilled(getDocumentsByCompanyIdAndFormId), (state, action) => {
        const { data, headers } = action.payload;
        return {
          ...state,
          loading: false,
          documents: data,
        };
      })
      .addMatcher(isFulfilled(createDocument, updateDocument), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.document = action.payload.data;
      })
      .addMatcher(isPending(getDocumentsByCompanyIdAndFormId, getDocumentById), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createDocument, updateDocument, deleteDocument), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      })
      .addMatcher(
        isRejected(getDocumentsByCompanyIdAndFormId, getDocumentById, createDocument, updateDocument, deleteDocument),
        (state, action) => {
          state.loading = false;
          state.updating = false;
          state.updateSuccess = false;
          state.errorMessage = action.error.message;
        }
      );
  },
});

export const { reset } = DocumentSlice.actions;
export default DocumentSlice.reducer;
