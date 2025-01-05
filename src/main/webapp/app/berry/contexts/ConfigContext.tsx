import React, { createContext, ReactNode } from 'react';

// project import
import defaultConfig from 'app/berry/config';
import useLocalStorage from 'app/berry/hooks/useLocalStorage';

// types
import { PaletteMode } from '@mui/material';
import { CustomizationProps } from 'app/berry/types/config';

// initial state
const initialState: CustomizationProps = {
  ...defaultConfig,
  surveyInfo: {
    company: {},
    forms: [],
  },
  onChangeLayout: () => {},
  onChangeDrawer: () => {},
  onChangeMenuType: () => {},
  onChangePresetColor: () => {},
  onChangeLocale: () => {},
  onChangeRTL: () => {},
  onChangeContainer: () => {},
  onChangeFontFamily: () => {},
  onChangeBorderRadius: () => {},
  onChangeOutlinedField: () => {},
  onChangeSurveyInfo: () => {},
};

// ==============================|| CONFIG CONTEXT & PROVIDER ||============================== //

const ConfigContext = createContext(initialState);

type ConfigProviderProps = {
  children: ReactNode;
};

function ConfigProvider({ children }: ConfigProviderProps) {
  const [config, setConfig] = useLocalStorage('berry-config-ts', {
    layout: initialState.layout,
    drawerType: initialState.drawerType,
    fontFamily: initialState.fontFamily,
    borderRadius: initialState.borderRadius,
    outlinedFilled: initialState.outlinedFilled,
    navType: initialState.navType,
    presetColor: initialState.presetColor,
    locale: initialState.locale,
    rtlLayout: initialState.rtlLayout,
    surveyInfo: {
      company: {},
      forms: [],
    },
  });

  const onChangeLayout = (layout: string) => {
    setConfig({
      ...config,
      layout,
    });
  };

  const onChangeDrawer = (drawerType: string) => {
    setConfig({
      ...config,
      drawerType,
    });
  };

  const onChangeMenuType = (navType: PaletteMode) => {
    setConfig({
      ...config,
      navType,
    });
  };

  const onChangePresetColor = (presetColor: string) => {
    setConfig({
      ...config,
      presetColor,
    });
  };

  const onChangeLocale = (locale: string) => {
    setConfig({
      ...config,
      locale,
    });
  };

  const onChangeRTL = (rtlLayout: boolean) => {
    setConfig({
      ...config,
      rtlLayout,
    });
  };

  const onChangeContainer = () => {
    setConfig({
      ...config,
      container: !config.container,
    });
  };

  const onChangeFontFamily = (fontFamily: string) => {
    setConfig({
      ...config,
      fontFamily,
    });
  };

  const onChangeBorderRadius = (event: Event, newValue: number | number[]) => {
    setConfig({
      ...config,
      borderRadius: newValue as number,
    });
  };

  const onChangeOutlinedField = (outlinedFilled: boolean) => {
    setConfig({
      ...config,
      outlinedFilled,
    });
  };

  const onChangeSurveyInfo = (surveyInfo: any) => {
    setConfig({
      ...config,
      surveyInfo,
    });
  };

  return (
    <ConfigContext.Provider
      value={{
        ...config,
        onChangeLayout,
        onChangeDrawer,
        onChangeMenuType,
        onChangePresetColor,
        onChangeLocale,
        onChangeRTL,
        onChangeContainer,
        onChangeFontFamily,
        onChangeBorderRadius,
        onChangeOutlinedField,
        onChangeSurveyInfo,
      }}
    >
      {children}
    </ConfigContext.Provider>
  );
}

export { ConfigProvider, ConfigContext };
