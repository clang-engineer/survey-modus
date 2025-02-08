export interface IMessage {
  id?: number;
  companyId?: number;
  content?: string;
  createdBy?: string;
  createdDate?: Date;
}

export const defaultValue: Readonly<IMessage> = {};
