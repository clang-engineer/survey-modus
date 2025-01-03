// project import
// types
import { NavItemType } from 'app/berry/types';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import wizard from 'app/berry/menu-items/wizard';
import system from 'app/berry/menu-items/system';
import survey from 'app/berry/menu-items/survey';

// ==============================|| MENU ITEMS ||============================== //

export interface ICustomNavItems {
  authorities: string[];
}

const CustomNavItems: (props: ICustomNavItems) => { items: NavItemType[] } = props => {
  const { authorities } = props;

  const getItems = () => {
    const items: NavItemType[] = [];
    items.push(wizard);
    items.push(system);
    items.push(survey);

    if (hasAnyAuthority(authorities, [AUTHORITIES.ADMIN, AUTHORITIES.USER])) {
      // items.push(download);
    }

    return items;
  };

  return {
    items: getItems(),
  };
};

export default CustomNavItems;
