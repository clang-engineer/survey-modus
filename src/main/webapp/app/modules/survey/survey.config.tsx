import React, { createContext, ReactNode, useContext, useState } from 'react';
import { NavItemType } from 'app/berry/types';
import { ICompany } from 'app/shared/model/company.model';

export const SurveyProvider = (props: { children: ReactNode }) => {
  const [menuItems, setMenuItems] = useState<NavItemType[]>([]);

  const value = { menuItems, setMenuItems };

  return <SurveyContext.Provider value={value}>{props.children}</SurveyContext.Provider>;
};

// context for field wizard
const SurveyContext = createContext<
  | {
      menuItems: NavItemType[];
      setMenuItems: (value: NavItemType[]) => void;
    }
  | undefined
>(undefined);

const useSurveyConfig = () => useContext(SurveyContext);

export default useSurveyConfig;
