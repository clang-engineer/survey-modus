import { IUser } from 'app/shared/model/user.model';
import { IGroup } from 'app/shared/model/group.model';

export interface IUserGroup {
  id?: number;
  user?: IUser;
  group?: IGroup;
}

export const defaultValue: Readonly<IUserGroup> = {};
