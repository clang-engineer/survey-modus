import axios from 'axios';

import configureStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import sinon from 'sinon';

import reducer, { createSurvey, deleteSurvey, getSurveyById, getSurveysByCompanyIdAndFormId, reset, updateSurvey } from './survey.reducer';
import { defaultValue } from 'app/shared/model/survey.model';

describe('Survey reducer tests', () => {
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
    surveys: [],
    survey: defaultValue,
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
    expect(isEmpty(state.surveys));
    expect(isEmpty(state.survey));
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
      testMultipleTypes([getSurveysByCompanyIdAndFormId.pending.type, getSurveyById.pending.type], {}, state => {
        expect(state).toMatchObject({
          errorMessage: null,
          updateSuccess: false,
          loading: true,
        });
      });
    });

    it('should set state to updating', () => {
      testMultipleTypes([createSurvey.pending.type, updateSurvey.pending.type, deleteSurvey.pending.type], {}, state => {
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
          getSurveysByCompanyIdAndFormId.rejected.type,
          getSurveyById.rejected.type,
          createSurvey.rejected.type,
          updateSurvey.rejected.type,
          deleteSurvey.rejected.type,
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
    it('should fetch all surveys', () => {
      const payload = {
        data: [
          {
            id: '1',
            name: 'survey',
          },
        ],
      };
      const state = reducer(undefined, { type: getSurveysByCompanyIdAndFormId.fulfilled.type, payload });
      expect(state).toMatchObject({
        loading: false,
        surveys: payload.data,
      });
    });

    it('should fetch a single survey', () => {
      const payload = {
        data: {
          id: '1',
          name: 'survey',
        },
      };
      const state = reducer(undefined, { type: getSurveyById.fulfilled.type, payload });
      expect(state).toMatchObject({
        loading: false,
        survey: payload.data,
      });
    });

    it('should create a survey', () => {
      const payload = {
        data: {
          id: '1',
          name: 'survey',
        },
      };
      const state = reducer(undefined, { type: createSurvey.fulfilled.type, payload });
      expect(state).toMatchObject({
        updating: false,
        updateSuccess: true,
        survey: payload.data,
      });
    });

    it('should update a survey', () => {
      const payload = {
        data: {
          id: '1',
          name: 'survey',
        },
      };
      const state = reducer(undefined, { type: updateSurvey.fulfilled.type, payload });
      expect(state).toMatchObject({
        updating: false,
        updateSuccess: true,
        survey: payload.data,
      });
    });

    it('should delete a survey', () => {
      const state = reducer(undefined, { type: deleteSurvey.fulfilled.type });
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

    it('dispatches FETCH_SURVEY_LIST actions', async () => {
      const expectedActions = [
        {
          type: getSurveysByCompanyIdAndFormId.pending.type,
        },
        {
          type: getSurveysByCompanyIdAndFormId.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(getSurveysByCompanyIdAndFormId({ collectionId: 1, companyId: 1, formId: 1 }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });

    it('dispatches FETCH_SURVEY actions', async () => {
      const expectedActions = [
        {
          type: getSurveyById.pending.type,
        },
        {
          type: getSurveyById.fulfilled.type,
          payload: resolvedObject,
        },
      ];
      await store.dispatch(getSurveyById({ collectionId: 1, surveyId: '1' }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
    });

    it('dispatches CREATE_SURVEY actions', async () => {
      const expectedActions = [
        { type: createSurvey.pending.type },
        { type: getSurveysByCompanyIdAndFormId.pending.type },
        { type: createSurvey.fulfilled.type, payload: resolvedObject },
      ];
      await store.dispatch(createSurvey({ collectionId: 1, survey: { companyId: 1, formId: 1 } }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });

    it('dispatches UPDATE_SURVEY actions', async () => {
      const expectedActions = [
        { type: updateSurvey.pending.type },
        { type: getSurveysByCompanyIdAndFormId.pending.type },
        { type: updateSurvey.fulfilled.type, payload: resolvedObject },
      ];
      await store.dispatch(updateSurvey({ collectionId: 1, survey: { id: '1', companyId: 1, formId: 1 } }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });

    it('dispatches DELETE_SURVEY actions', async () => {
      const expectedActions = [
        { type: deleteSurvey.pending.type },
        { type: getSurveysByCompanyIdAndFormId.pending.type },
        { type: deleteSurvey.fulfilled.type, payload: resolvedObject },
      ];
      await store.dispatch(deleteSurvey({ collectionId: 1, survey: { id: '1', companyId: 1, formId: 1 } }));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });
  });
});
