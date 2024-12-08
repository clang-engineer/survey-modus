import { IUser } from 'app/shared/model/user.model';
import { IGroup } from 'app/shared/model/group.model';

export interface IGroupUser {
  id?: number;
  group?: IGroup;
  user?: IUser;
}

export const defaultValue: Readonly<IGroupUser> = {};
