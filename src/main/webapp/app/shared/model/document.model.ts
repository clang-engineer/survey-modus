const DOCUMENT_ID = 'id';
const DOCUMENT_OBJECT_ID = '_id';
const DOCUMENT_COMPANY_ID = 'companyId';
const DOCUMENT_FORM_ID = 'formId';
interface IDocument {
  _id?: Object;
  id?: string;
  companyId: number;
  formId: number;

  [key: string]: any;
}

const defaultValue: Readonly<IDocument> = {
  companyId: 0,
  formId: 0,
};

export { IDocument, DOCUMENT_ID, DOCUMENT_OBJECT_ID, DOCUMENT_COMPANY_ID, DOCUMENT_FORM_ID, defaultValue };
