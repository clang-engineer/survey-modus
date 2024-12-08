import { ICompany } from 'app/shared/model/company.model';
import { IGroup } from 'app/shared/model/group.model';

export interface IGroupCompany {
  id?: number;
  group?: IGroup;
  company?: ICompany;
}

export const defaultValue: Readonly<IGroupCompany> = {};
