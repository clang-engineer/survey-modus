import axios from 'axios';
import sinon from 'sinon';
import configureStore from 'redux-mock-store';
import thunk from 'redux-thunk';
import { TranslatorContext } from 'react-jhipster';

import locale, { addTranslationSourcePrefix, loaded, setLocale, updateLocale } from 'app/shared/reducers/locale';

const defaultLocale = 'en';

describe('Locale reducer tests', () => {
  it('should return the initial state', () => {
    const localeState = locale(undefined, { type: '' });
    expect(localeState).toMatchObject({
      currentLocale: '',
    });
  });

  it('should correctly set the first time locale', () => {
    const localeState = locale(undefined, updateLocale(defaultLocale));
    expect(localeState).toMatchObject({
      currentLocale: defaultLocale,
    });
    expect(TranslatorContext.context.locale).toEqual(defaultLocale);
  });

  it('should correctly detect update in current locale state', () => {
    TranslatorContext.setLocale(defaultLocale);
    expect(TranslatorContext.context.locale).toEqual(defaultLocale);
    const localeState = locale(
      {
        currentLocale: defaultLocale,
        sourcePrefixes: [],
        lastChange: new Date().getTime(),
        loadedKeys: [],
      },
      updateLocale('es')
    );
    expect(localeState).toMatchObject({
      currentLocale: 'es',
    });
    expect(TranslatorContext.context.locale).toEqual('es');
  });

  describe('setLocale reducer', () => {
    describe('with default language loaded', () => {
      let store;
      beforeEach(() => {
        store = configureStore([thunk])({ locale: { loadedLocales: [defaultLocale], loadedKeys: [] } });
        axios.get = sinon.stub().returns(Promise.resolve({ key: 'value' }));
      });

      it('dispatches updateLocale action for default locale', async () => {
        TranslatorContext.setDefaultLocale(defaultLocale);
        expect(Object.keys(TranslatorContext.context.translations)).not.toContainEqual(defaultLocale);

        const expectedActions = [
          {
            type: setLocale.pending.type,
          },
          updateLocale(defaultLocale),
          {
            type: setLocale.fulfilled.type,
            payload: defaultLocale,
          },
        ];

        await store.dispatch(setLocale(defaultLocale));
        expect(store.getActions()).toMatchObject(expectedActions);
      });
    });

    describe('with no language loaded', () => {
      let store;
      beforeEach(() => {
        store = configureStore([thunk])({
          locale: {
            sourcePrefixes: [],
            loadedLocales: [],
            loadedKeys: [],
          },
        });
        axios.get = sinon.stub().returns(Promise.resolve({ key: 'value' }));
      });

      it('dispatches loaded and updateLocale action for default locale', async () => {
        TranslatorContext.setDefaultLocale(defaultLocale);
        expect(Object.keys(TranslatorContext.context.translations)).not.toContainEqual(defaultLocale);

        const expectedActions = [
          {
            type: setLocale.pending.type,
          },
          loaded({ keys: [defaultLocale], locale: defaultLocale }),
          updateLocale(defaultLocale),
          {
            type: setLocale.fulfilled.type,
            payload: defaultLocale,
          },
        ];

        await store.dispatch(setLocale(defaultLocale));
        expect(store.getActions()).toMatchObject(expectedActions);
      });
    });
  });

  describe('addTranslationSourcePrefix reducer', () => {
    const sourcePrefix = 'foo/';

    describe('with no prefixes and keys loaded', () => {
      let store;
      beforeEach(() => {
        store = configureStore([thunk])({
          locale: {
            currentLocale: defaultLocale,
            sourcePrefixes: [],
            loadedLocales: [],
            loadedKeys: [],
          },
        });
        axios.get = sinon.stub().returns(Promise.resolve({ key: 'value' }));
      });

      it('dispatches loaded action with keys and sourcePrefix', async () => {
        const expectedActions = [
          {
            type: addTranslationSourcePrefix.pending.type,
          },
          loaded({ keys: [`${sourcePrefix}${defaultLocale}`], sourcePrefix }),
          {
            type: addTranslationSourcePrefix.fulfilled.type,
            payload: `${sourcePrefix}${defaultLocale}`,
          },
        ];

        await store.dispatch(addTranslationSourcePrefix(sourcePrefix));
        expect(store.getActions()).toMatchObject(expectedActions);
      });
    });

    describe('with prefix already added', () => {
      let store;
      beforeEach(() => {
        store = configureStore([thunk])({
          locale: {
            currentLocale: defaultLocale,
            sourcePrefixes: [sourcePrefix],
            loadedLocales: [],
            loadedKeys: [],
          },
        });
        axios.get = sinon.stub().returns(Promise.resolve({ key: 'value' }));
      });

      it("doesn't dispatches loaded action", async () => {
        const expectedActions = [
          {
            type: addTranslationSourcePrefix.pending.type,
          },
          {
            type: addTranslationSourcePrefix.fulfilled.type,
            payload: `${sourcePrefix}${defaultLocale}`,
          },
        ];

        await store.dispatch(addTranslationSourcePrefix(sourcePrefix));
        expect(store.getActions()).toMatchObject(expectedActions);
      });
    });

    describe('with key already loaded', () => {
      let store;
      beforeEach(() => {
        store = configureStore([thunk])({
          locale: {
            currentLocale: defaultLocale,
            sourcePrefixes: [],
            loadedLocales: [],
            loadedKeys: [`${sourcePrefix}${defaultLocale}`],
          },
        });
        axios.get = sinon.stub().returns(Promise.resolve({ key: 'value' }));
      });

      it("doesn't dispatches loaded action", async () => {
        const expectedActions = [
          {
            type: addTranslationSourcePrefix.pending.type,
          },
          {
            type: addTranslationSourcePrefix.fulfilled.type,
            payload: `${sourcePrefix}${defaultLocale}`,
          },
        ];

        await store.dispatch(addTranslationSourcePrefix(sourcePrefix));
        expect(store.getActions()).toMatchObject(expectedActions);
      });
    });
  });

  describe('loaded reducer', () => {
    describe('with empty state', () => {
      let initialState;
      beforeEach(() => {
        initialState = {
          currentLocale: defaultLocale,
          sourcePrefixes: [],
          loadedLocales: [],
          loadedKeys: [],
        };
      });

      it("and empty parameter, don't adds anything", () => {
        const expectedState = {
          currentLocale: defaultLocale,
          sourcePrefixes: [],
          loadedLocales: [],
          loadedKeys: [],
        };

        const localeState = locale(initialState, loaded({}));
        expect(localeState).toMatchObject(expectedState);
      });

      it('and keys parameter, adds to loadedKeys', () => {
        const expectedState = {
          currentLocale: defaultLocale,
          sourcePrefixes: [],
          loadedLocales: [],
          loadedKeys: ['foo'],
        };

        const localeState = locale(initialState, loaded({ keys: ['foo'] }));
        expect(localeState).toMatchObject(expectedState);
      });

      it('and sourcePrefix parameter, adds to sourcePrefixes', () => {
        const expectedState = {
          currentLocale: defaultLocale,
          sourcePrefixes: ['foo'],
          loadedLocales: [],
          loadedKeys: [],
        };

        const localeState = locale(initialState, loaded({ sourcePrefix: 'foo' }));
        expect(localeState).toMatchObject(expectedState);
      });

      it('and locale parameter, adds to loadedLocales', () => {
        const expectedState = {
          currentLocale: defaultLocale,
          sourcePrefixes: [],
          loadedLocales: ['foo'],
          loadedKeys: [],
        };

        const localeState = locale(initialState, loaded({ locale: 'foo' }));
        expect(localeState).toMatchObject(expectedState);
      });
    });
  });
});
