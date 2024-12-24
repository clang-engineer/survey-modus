import { IForm } from 'app/shared/model/form.model';

enum type {
  TEXT = 'TEXT',
  RADIO = 'RADIO',
  SELECT_BOX = 'SELECT_BOX',
  CHECK_BOX = 'CHECK_BOX',
  DATE = 'DATE',
  TIME = 'TIME',
  DATETIME = 'DATETIME',
  BOOLEAN = 'BOOLEAN',
  INTEGER = 'INTEGER',
  FLOAT = 'FLOAT',
}

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

export { type };
