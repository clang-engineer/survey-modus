export interface IStaff {
  firstName?: string;
  lastName?: string;
  email?: string;
  activated?: boolean;
  langKey?: string;
  phone?: string;
}

export const defaultValue: Readonly<IStaff> = {
  activated: false,
};
