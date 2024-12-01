import { IUser } from 'app/shared/model/user.model';
import { level } from 'app/shared/model/enumerations/level.model';

export interface IPoint {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
  type?: level | null;
  user?: IUser;
}

export const defaultValue: Readonly<IPoint> = {
  activated: false,
};
