// project import
import { NavItemType } from 'app/berry/types';

// ==============================|| MENU TYPES  ||============================== //

export type MenuProps = {
  selectedItem: string[];
  selectedID: string | null;
  drawerOpen: boolean;
  error: null;
  menu: NavItemType;
};
