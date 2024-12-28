import { NavItemType } from 'app/berry/types';
import system from './system';
import wizard from './wizard';

// ==============================|| MENU ITEMS ||============================== //

const menuItems: { items: NavItemType[] } = {
  items: [wizard, system],
};

export default menuItems;
