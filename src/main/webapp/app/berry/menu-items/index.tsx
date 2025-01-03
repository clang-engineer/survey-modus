import { NavItemType } from 'app/berry/types';
import system from 'app/berry/menu-items/system';
import wizard from 'app/berry/menu-items/wizard';
import survey from 'app/berry/menu-items/survey';

// ==============================|| MENU ITEMS ||============================== //

const menuItems: { items: NavItemType[] } = {
  items: [survey, wizard, system],
};

export default menuItems;
