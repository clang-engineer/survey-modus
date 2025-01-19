const SURVEY_ID = 'id';
const SURVEY_OBJECT_ID = '_id';
const SURVEY_COMPANY_ID = 'companyId';
const SURVEY_FORM_ID = 'formId';

interface ISurvey {
  _id?: Record<string, any>;
  id?: string;
  companyId: number;
  formId: number;
  fields?: Array<{ key: string; value: any }>;
}

const defaultValue: Readonly<ISurvey> = {
  companyId: 0,
  formId: 0,
  fields: [],
};

export { ISurvey, SURVEY_ID, SURVEY_OBJECT_ID, SURVEY_COMPANY_ID, SURVEY_FORM_ID, defaultValue };
