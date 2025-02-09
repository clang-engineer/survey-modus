import thunk from 'redux-thunk';
import axios from 'axios';
import sinon from 'sinon';
import { Storage } from 'react-jhipster';
import configureStore from 'redux-mock-store';

import reducer, {
  authenticate,
  authenticateStaffAccount,
  authError,
  clearAuth,
  clearAuthentication,
  clearAuthToken,
  getAccount,
  getSession,
  getStaffAccount,
  getStaffSession,
  initialState,
  login,
  loginStaff,
  logout,
  logoutSession,
} from 'app/shared/reducers/authentication';
import { setLocale, updateLocale } from 'app/shared/reducers/locale';

describe('Authentication reducer tests', () => {
  function isEmpty(element): boolean {
    if (element instanceof Array) {
      return element.length === 0;
    } else {
      return Object.keys(element).length === 0;
    }
  }

  function testInitialState(state) {
    expect(state).toMatchObject({
      loading: false,
      isAuthenticated: false,
      errorMessage: null,
      loginSuccess: false,
      loginError: false,
      showModalLogin: false,
      redirectMessage: null,
    });
    expect(isEmpty(state.account));
  }

  function testMultipleTypes(types, payload, testFunction, error?) {
    types.forEach(e => {
      testFunction(reducer(undefined, { type: e, payload, error }));
    });
  }

  describe('Common tests', () => {
    it('should return the initial state', () => {
      testInitialState(reducer(undefined, { type: '' }));
    });
  });

  describe('Requests', () => {
    it('should detect a request', () => {
      testMultipleTypes(
        [authenticate.pending.type, authenticateStaffAccount.pending.type, getAccount.pending.type, getStaffAccount.pending.type],
        {},
        state => {
          expect(state).toMatchObject({
            loading: true,
          });
        }
      );
    });
  });

  describe('Failure', () => {
    it('should set a message in errorMessage', () => {
      testMultipleTypes(
        [authenticate.rejected.type, authenticateStaffAccount.rejected.type, getAccount.rejected.type, getStaffAccount.rejected.type],
        'some message',
        state => {
          expect(state).toMatchObject({
            errorMessage: 'some message',
            loading: false,
          });
          expect(isEmpty(state));
        },
        {
          message: 'some message',
        }
      );
    });
  });

  describe('Success', () => {
    it('should detect a success on login', () => {
      testMultipleTypes([authenticate.fulfilled.type, authenticateStaffAccount.fulfilled.type], {}, state => {
        expect(state).toMatchObject({
          loading: false,
          loginError: false,
          loginSuccess: true,
          showModalLogin: false,
        });
      });
    });

    it('should detect a success on get session and be authenticated', () => {
      testMultipleTypes([getAccount.fulfilled.type, getStaffAccount.fulfilled.type], { data: { activated: true } }, state => {
        expect(state).toMatchObject({
          isAuthenticated: true,
          loading: false,
          account: { activated: true },
        });
      });
    });

    it('should detect a success on get session and not be authenticated', () => {
      testMultipleTypes([getAccount.fulfilled.type, getStaffAccount.fulfilled.type], { data: { activated: false } }, state => {
        expect(state).toMatchObject({
          isAuthenticated: false,
          loading: false,
          account: { activated: false },
        });
      });
    });
  });

  describe('Other cases', () => {
    it('should properly reset the current state when a logout is requested', () => {
      const toTest = reducer(undefined, logoutSession());
      expect(toTest).toMatchObject({
        loading: false,
        isAuthenticated: false,
        loginSuccess: false,
        loginError: false,
        showModalLogin: true,
        errorMessage: null,
        redirectMessage: null,
      });
      expect(isEmpty(toTest));
    });

    it('should properly define an error message and change the current state to display the login modal', () => {
      const message = 'redirect me please';
      const toTest = reducer(undefined, authError(message));
      expect(toTest).toMatchObject({
        loading: false,
        isAuthenticated: false,
        loginSuccess: false,
        loginError: false,
        showModalLogin: true,
        errorMessage: null,
        redirectMessage: message,
      });
      expect(isEmpty(toTest));
    });

    it('should clear authentication', () => {
      const toTest = reducer({ ...initialState, isAuthenticated: true }, clearAuth());
      expect(toTest).toMatchObject({
        loading: false,
        showModalLogin: true,
        isAuthenticated: false,
      });
    });
  });

  describe('Actions', () => {
    let store;

    const resolvedObject = { value: 'whatever' };
    beforeEach(() => {
      const mockStore = configureStore([thunk]);
      store = mockStore({
        authentication: { account: { langKey: 'en' } },
        locale: { loadedLocales: ['en'] },
      });
      axios.get = sinon.stub().returns(Promise.resolve(resolvedObject));
    });

    it('dispatches GET_SESSION_PENDING and GET_SESSION_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: getAccount.pending.type,
        },
        {
          type: getAccount.fulfilled.type,
          payload: resolvedObject,
        },
        {
          type: setLocale.pending.type,
        },
        updateLocale('en'),
        {
          type: setLocale.fulfilled.type,
          payload: 'en',
        },
      ];
      await store.dispatch(getSession());
      expect(store.getActions()).toMatchObject(expectedActions);
    });

    it('dispatches GET_STAFF_SESSION_PENDING and GET_STAFF_SESSION_FULFILLED actions', async () => {
      const expectedActions = [
        {
          type: getStaffAccount.pending.type,
        },
        {
          type: getStaffAccount.fulfilled.type,
          payload: resolvedObject,
        },
        {
          type: setLocale.pending.type,
        },
        updateLocale('en'),
        {
          type: setLocale.fulfilled.type,
          payload: 'en',
        },
      ];
      await store.dispatch(getStaffSession());
      expect(store.getActions()).toMatchObject(expectedActions);
    });

    it('dispatches LOGOUT actions', async () => {
      const expectedActions = [logoutSession()];
      await store.dispatch(logout());
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
    });

    it('dispatches CLEAR_AUTH actions', async () => {
      const expectedActions = [authError('message'), clearAuth()];
      await store.dispatch(clearAuthentication('message'));
      expect(store.getActions()).toEqual(expectedActions);
    });

    it('dispatches LOGIN, GET_SESSION and SET_LOCALE success and request actions', async () => {
      const loginResponse = { headers: { authorization: 'auth' } };
      axios.post = sinon.stub().returns(Promise.resolve(loginResponse));
      const expectedActions = [
        {
          type: authenticate.pending.type,
        },
        {
          type: authenticate.fulfilled.type,
          payload: loginResponse,
        },
        {
          type: getAccount.pending.type,
        },
      ];
      await store.dispatch(login('test', 'test'));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });

    it('dispatches LOGIN_STAFF, GET_STAFF_SESSION and SET_LOCALE success and request actions', async () => {
      const loginResponse = { headers: { authorization: 'auth' } };
      axios.post = sinon.stub().returns(Promise.resolve(loginResponse));
      const expectedActions = [
        {
          type: authenticateStaffAccount.pending.type,
        },
        {
          type: authenticateStaffAccount.fulfilled.type,
          payload: loginResponse,
        },
        {
          type: getStaffAccount.pending.type,
        },
      ];
      await store.dispatch(loginStaff('test', 'test'));
      expect(store.getActions()[0]).toMatchObject(expectedActions[0]);
      expect(store.getActions()[1]).toMatchObject(expectedActions[1]);
      expect(store.getActions()[2]).toMatchObject(expectedActions[2]);
    });
  });

  describe('clearAuthToken', () => {
    let store;
    beforeEach(() => {
      const mockStore = configureStore([thunk]);
      store = mockStore({ authentication: { account: { langKey: 'en' } } });
    });
    it('clears the session token on clearAuthToken', async () => {
      const AUTH_TOKEN_KEY = 'jhi-authenticationToken';
      const loginResponse = { headers: { authorization: 'Bearer TestToken' } };
      axios.post = sinon.stub().returns(Promise.resolve(loginResponse));

      await store.dispatch(login('test', 'test'));
      expect(Storage.session.get(AUTH_TOKEN_KEY)).toBe('TestToken');
      expect(Storage.local.get(AUTH_TOKEN_KEY)).toBe(undefined);
      clearAuthToken();
      expect(Storage.session.get(AUTH_TOKEN_KEY)).toBe(undefined);
      expect(Storage.local.get(AUTH_TOKEN_KEY)).toBe(undefined);
    });
    it('clears the local storage token on clearAuthToken', async () => {
      const AUTH_TOKEN_KEY = 'jhi-authenticationToken';
      const loginResponse = { headers: { authorization: 'Bearer TestToken' } };
      axios.post = sinon.stub().returns(Promise.resolve(loginResponse));

      await store.dispatch(login('user', 'user', true));
      expect(Storage.session.get(AUTH_TOKEN_KEY)).toBe(undefined);
      expect(Storage.local.get(AUTH_TOKEN_KEY)).toBe('TestToken');
      clearAuthToken();
      expect(Storage.session.get(AUTH_TOKEN_KEY)).toBe(undefined);
      expect(Storage.local.get(AUTH_TOKEN_KEY)).toBe(undefined);
    });
  });
});
