import reducer, { getDocumentById, getDocumentsByFormId, reset } from './document.reducer';
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
    it('should set state to loading', () => {
      testMultipleTypes([getDocumentsByFormId.pending.type, getDocumentById.pending.type], {}, state => {
        expect(state).toMatchObject({
          errorMessage: null,
          updateSuccess: false,
          loading: true,
        });
      });
    });

    it('should reset state to initial state', () => {
      expect(reducer({ ...initialState, loading: true }, reset())).toEqual({
        ...initialState,
      });
    });
  });

  describe('Failures', () => {
    it('should set state to loading and reset error message', () => {
      testMultipleTypes(
        [getDocumentsByFormId.rejected.type, getDocumentById.rejected.type],
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
  });
});
