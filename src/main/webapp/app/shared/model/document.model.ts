export interface IDocument {
  _id?: string;
  formId?: number;
  [key: string]: any;
}

export const defaultValue: Readonly<IDocument> = {};
