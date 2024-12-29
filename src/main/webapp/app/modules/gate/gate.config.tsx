import React, { createContext, ReactNode, useContext, useState } from 'react';
import { NavItemType } from 'app/berry/types';
import { ICompany } from 'app/shared/model/company.model';

export const GateProvider = (props: { children: ReactNode }) => {
  const [menuItems, setMenuItems] = useState<NavItemType[]>([]);

  const value = { menuItems, setMenuItems };

  return <GateContext.Provider value={value}>{props.children}</GateContext.Provider>;
};

// context for field wizard
const GateContext = createContext<
  | {
      menuItems: NavItemType[];
      setMenuItems: (value: NavItemType[]) => void;
    }
  | undefined
>(undefined);

const useGateConfig = () => useContext(GateContext);

export default useGateConfig;
