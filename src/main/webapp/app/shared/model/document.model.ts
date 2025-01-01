export interface IDocument {
  _id?: string;
  form_id?: string;
  [key: string]: any;
}

export const defaultValue: Readonly<IDocument> = {};
