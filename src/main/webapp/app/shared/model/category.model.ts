export interface ICategory {
  id?: number;
  title?: string;
  description?: string | null;
  activated?: boolean | null;
}

export const defaultValue: Readonly<ICategory> = {
  activated: false,
};
