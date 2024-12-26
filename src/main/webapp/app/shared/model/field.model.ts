import { IForm } from 'app/shared/model/form.model';
import type from 'app/shared/model/enumerations/type.model';

interface IFieldAttribute {
  type: type;
  defaultValue: string;
}

interface IFieldDisplay {
  orderNo: number;
}

export interface IField {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  form?: IForm;
  attribute?: IFieldAttribute;
  display?: IFieldDisplay;
  lookups?: string[];
}

export const defaultValue: Readonly<IField> = {
  activated: false,
  attribute: {
    type: type.TEXT,
    defaultValue: '',
  },
  display: {
    orderNo: 0,
  },
  lookups: [],
};
