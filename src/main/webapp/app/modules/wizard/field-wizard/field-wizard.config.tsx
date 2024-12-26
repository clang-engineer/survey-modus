import React, { createContext, ReactNode, useContext, useState } from 'react';
import { IField } from 'app/shared/model/field.model';

// context provider for field wizard
export const FieldWizardProvider = (props: { children: ReactNode }) => {
  const [items, setItems] = useState<IField[]>([]);

  const value = {
    items,
    setItems,
  };

  return <FieldWizardContext.Provider value={value}>{props.children}</FieldWizardContext.Provider>;
};

// context for field wizard
const FieldWizardContext = createContext<
  | {
      items: IField[];
      setItems: (items: IField[]) => void;
    }
  | undefined
>(undefined);

const useFieldWizardConfig = () => useContext(FieldWizardContext);

export default useFieldWizardConfig;
