import reducer, { reset } from './document.reducer';

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
    document: null,
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
  });
});
