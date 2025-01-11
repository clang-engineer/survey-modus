import React, { createContext, ReactNode, useContext, useState } from 'react';
import { IForm } from 'app/shared/model/form.model';

export const FormWizardProvider = (props: { children: ReactNode }) => {
  const [items, setItems] = useState<IForm[]>([]);

  const value = {
    items,
    setItems,
  };

  return <FormWizardContext.Provider value={value}>{props.children}</FormWizardContext.Provider>;
};

const FormWizardContext = createContext<
  | {
      items: IForm[];
      setItems: (items: IForm[]) => void;
    }
  | undefined
>(undefined);

const useFormWizardConfig = () => useContext(FormWizardContext);

export default useFormWizardConfig;
