import { IUser } from 'app/shared/model/user.model';
import { IForm } from 'app/shared/model/form.model';

export interface ICompany {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  user?: IUser;
  forms?: IForm[];
}

export const defaultValue: Readonly<ICompany> = {
  activated: false,
  forms: [],
};
