import { IForm } from 'app/shared/model/form.model';
import type from 'app/shared/model/enumerations/type.model';

interface IFieldAttribute {
  type: type;
  defaultValue: string;
}

export interface IField {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  form?: IForm;
  attribute?: IFieldAttribute;
}

export const defaultValue: Readonly<IField> = {
  activated: false,
};
