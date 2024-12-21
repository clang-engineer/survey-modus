import { IUser } from 'app/shared/model/user.model';
import { ICompany } from 'app/shared/model/company.model';

export interface IGroup {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  user?: IUser;
  users?: IUser[];
  companies?: ICompany[];
}

export const defaultValue: Readonly<IGroup> = {
  activated: false,
};
