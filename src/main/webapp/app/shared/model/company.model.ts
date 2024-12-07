import { IUser } from 'app/shared/model/user.model';

export interface ICompany {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  user?: IUser;
}

export const defaultValue: Readonly<ICompany> = {
  activated: false,
};
