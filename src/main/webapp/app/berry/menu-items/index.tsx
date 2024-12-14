import { NavItemType } from 'app/berry/types';
import system from 'app/berry/menu-items/system';
import wizard from 'app/berry/menu-items/wizard';

// ==============================|| MENU ITEMS ||============================== //

const menuItems: { items: NavItemType[] } = {
  items: [wizard, system],
};

export default menuItems;
