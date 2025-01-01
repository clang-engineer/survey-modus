export interface IDocument {
  _id?: string;
  companyId: number;
  formId: number;

  [key: string]: any;
}

export const defaultValue: Readonly<IDocument> = {
  companyId: 0,
  formId: 0,
};
