import axios from 'axios';

import configureStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import sinon from 'sinon';

import reducer, { createDocument, deleteDocument, getDocumentById, getDocumentsByFormId, reset, updateDocument } from './document.reducer';
import { defaultValue } from 'app/shared/model/document.model';

describe('Document reducer tests', () => {
  function isEmpty(element): boolean {
    if (element instanceof Array) {
      return element.length === 0;
    } else {
      return Object.keys(element).length === 0;
    }
  }

  const initialState = {
    loading: false,
    errorMessage: null,
    documents: [],
    document: defaultValue,
    updating: false,
    updateSuccess: false,
  };

  function testInitialState(state) {
    expect(state).toMatchObject({
      loading: false,
      errorMessage: null,
      updating: false,
      updateSuccess: false,
    });
    expect(isEmpty(state.documents));
    expect(isEmpty(state.document));
  }

  function testMultipleTypes(types, payload, testFunction, error?) {
    types.forEach(e => {
      testFunction(reducer(undefined, { type: e, payload, error }));
    });
  }

  describe('Common', () => {
    it('should return the initial state', () => {
      testInitialState(reducer(undefined, { type: 'any' }));
    });
  });

  describe('Requests', () => {
    it('should reset state to initial state', () => {
      expect(reducer({ ...initialState, loading: true }, reset())).toEqual({
        ...initialState,
      });
    });

    it('should set state to loading', () => {
      testMultipleTypes([getDocumentsByFormId.pending.type, getDocumentById.pending.type], {}, state => {
        expect(state).toMatchObject({
          errorMessage: null,
          updateSuccess: false,
          loading: true,
        });
      });
    });

    it('should set state to updating', () => {
      testMultipleTypes([createDocument.pending.type, updateDocument.pending.type, deleteDocument.pending.type], {}, state => {
        expect(state).toMatchObject({
          errorMessage: null,
          updateSuccess: false,
          updating: true,
        });
      });
    });
  });

  describe('Failures', () => {
    it('should set state to loading and reset error message', () => {
      testMultipleTypes(
        [
          getDocumentsByFormId.rejected.type,
          getDocumentById.rejected.type,
          createDocument.rejected.type,
          updateDocument.rejected.type,
          deleteDocument.rejected.type,
        ],
        'some error',
        state => {
          expect(state).toMatchObject({
            errorMessage: 'some error',
            updateSuccess: false,
            loading: false,
          });
        },
        { message: 'some error' }
      );
    });
  });

  describe('Successes', () => {
    it('should fetch all documents', () => {
      const payload = {
        data: [
          {
            id: '1',
            name: 'document',
          },
        ],
      };
      const state = reducer(undefined, { type: getDocumentsByFormId.fulfilled.type, payload });
      expect(state).toMatchObject({
        loading: false,
        documents: payload.data,
      });
    });

    it('should fetch a single document', () => {
      const payload = {
        data: {
          id: '1',
          name: 'document',
        },
      };
      const state = reducer(undefined, { type: getDocumentById.fulfilled.type, payload });
      expect(state).toMatchObject({
        loading: false,
        document: payload.data,
      });
    });

    it('should create a document', () => {
      const payload = {
        data: {
          id: '1',
          name: 'document',
        },
      };
      const state = reducer(undefined, { type: createDocument.fulfilled.type, payload });
      expect(state).toMatchObject({
        updating: false,
        updateSuccess: true,
        document: payload.data,
      });
    });

    it('should update a document', () => {
      const payload = {
        data: {
          id: '1',
          name: 'document',
        },
      };
      const state = reducer(undefined, { type: updateDocument.fulfilled.type, payload });
      expect(state).toMatchObject({
        updating: false,
        updateSuccess: true,
        document: payload.data,
      });
    });

    it('should delete a document', () => {
      const state = reducer(undefined, { type: deleteDocument.fulfilled.type });
      expect(state).toMatchObject({
        updating: false,
        updateSuccess: true,
      });
    });
  });

  describe('Actions', () => {
    let store;

    const resolvedObject = { value: 'whatever' };
    beforeEach(() => {
      const mockStore = configureStore([thunk]);
      store = mockStore({});
      axios.get = sinon.stub().returns(Promise.resolve(resolvedObject));
      axios.post = sinon.stub().returns(Promise.resolve(resolvedObject));
      axios.put = sinon.stub().returns(Promise.resolve(resolvedObject));
      axios.patch = sinon.stub().returns(Promise.resolve(resolvedObject));
      axios.delete = sinon.stub().returns(Promise.resolve(resolvedObject));
    });

    it('dispatches FETCH_DOCUMENT_LIST actions', async () => {
      const expectedActions = [
        {
          type: getDocumentsByFormId.pending.type,
        },
        {
          type: getDocumentsByFormId.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(getDocumentsByFormId({ collectionId: 1, formId: 1 }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });

    it('dispatches FETCH_DOCUMENT actions', async () => {
      const expectedActions = [
        {
          type: getDocumentById.pending.type,
        },
        {
          type: getDocumentById.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(getDocumentById({ collectionId: 1, documentId: '1' }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });

    it('dispatches CREATE_DOCUMENT actions', async () => {
      const expectedActions = [
        { type: createDocument.pending.type },
        { type: getDocumentsByFormId.pending.type },
        { type: createDocument.fulfilled.type, payload: resolvedObject },
      ];
      await store.dispatch(createDocument({ collectionId: 1, document: { formId: 1 } }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });

    it('dispatches UPDATE_DOCUMENT actions', async () => {
      const expectedActions = [
        { type: updateDocument.pending.type },
        { type: getDocumentsByFormId.pending.type },
        { type: updateDocument.fulfilled.type, payload: resolvedObject },
      ];
      await store.dispatch(updateDocument({ collectionId: 1, document: { id: '1' } }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });

    it('dispatches DELETE_DOCUMENT actions', async () => {
      const expectedActions = [
        { type: deleteDocument.pending.type },
        { type: getDocumentsByFormId.pending.type },
        { type: deleteDocument.fulfilled.type, payload: resolvedObject },
      ];
      await store.dispatch(deleteDocument({ collectionId: 1, document: { _id: '1', formId: 1 } }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });
  });
});
