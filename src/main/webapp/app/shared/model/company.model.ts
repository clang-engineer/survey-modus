import { IUser } from 'app/shared/model/user.model';
import { IForm } from 'app/shared/model/form.model';

interface IStaff {
  email: string;
  name: string;
  phone: string;
  activated: boolean;
}

export interface ICompany {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  user?: IUser;
  forms?: IForm[];
  staffs?: IStaff[];
}

export const defaultValue: Readonly<ICompany> = {
  activated: false,
  forms: [],
  staffs: [],
};
