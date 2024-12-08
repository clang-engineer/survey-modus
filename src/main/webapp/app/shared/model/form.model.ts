import { IUser } from 'app/shared/model/user.model';
import { ICategory } from 'app/shared/model/category.model';

export interface IForm {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  user?: IUser;
  category?: ICategory;
}

export const defaultValue: Readonly<IForm> = {
  activated: false,
};
