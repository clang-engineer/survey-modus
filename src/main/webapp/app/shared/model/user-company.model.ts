import { IUser } from 'app/shared/model/user.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IUserCompany {
  id?: number;
  user?: IUser;
  company?: ICompany;
}

export const defaultValue: Readonly<IUserCompany> = {};
