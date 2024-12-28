import React, { createContext, ReactNode, useContext, useState } from 'react';
import { NavItemType } from 'app/berry/types';

export const GateProvider = (props: { children: ReactNode }) => {
  const [menuItems, setMenuItems] = useState<{ items: NavItemType[] }>({ items: [] });

  const value = {
    menuItems,
    setMenuItems,
  };

  return <GateContext.Provider value={value}>{props.children}</GateContext.Provider>;
};

// context for field wizard
const GateContext = createContext<
  | {
      menuItems: { items: NavItemType[] };
      setMenuItems: (value: { items: NavItemType[] }) => void;
    }
  | undefined
>(undefined);

const useGateConfig = () => useContext(GateContext);

export default useGateConfig;
