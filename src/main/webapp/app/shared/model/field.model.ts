import { IForm } from 'app/shared/model/form.model';

export interface IField {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  form?: IForm;
}

export const defaultValue: Readonly<IField> = {
  activated: false,
};
