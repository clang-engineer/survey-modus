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

  const authorizedItems = () => {
    const items: NavItemType[] = [];

    if (hasAnyAuthority(authorities, [AUTHORITIES.ADMIN, AUTHORITIES.USER, AUTHORITIES.STAFF])) {
      items.push(survey);
    }

    if (hasAnyAuthority(authorities, [AUTHORITIES.ADMIN, AUTHORITIES.USER])) {
      items.push(wizard);
    }

    if (hasAnyAuthority(authorities, [AUTHORITIES.ADMIN])) {
      items.push(system);
    }

    return items;
  };

  return {
    items: authorizedItems(),
  };
};

export default CustomNavItems;
