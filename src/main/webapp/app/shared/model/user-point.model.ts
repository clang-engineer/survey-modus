import { IUser } from 'app/shared/model/user.model';
import { IPoint } from 'app/shared/model/point.model';

export interface IUserPoint {
  id?: number;
  user?: IUser;
  point?: IPoint;
}

export const defaultValue: Readonly<IUserPoint> = {};
