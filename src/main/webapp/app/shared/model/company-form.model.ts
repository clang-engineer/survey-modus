import { IForm } from 'app/shared/model/form.model';
import { ICompany } from 'app/shared/model/company.model';

export interface ICompanyForm {
  id?: number;
  company?: ICompany;
  form?: IForm;
}

export const defaultValue: Readonly<ICompanyForm> = {};
