import { IUser } from 'app/shared/model/user.model';
import { level } from 'app/shared/model/enumerations/level.model';

export interface IFile {
  id?: number;
  filename?: string;
  filepath?: string | null;
  hashKey?: string | null;
}

export const defaultValue: Readonly<IFile> = {};
