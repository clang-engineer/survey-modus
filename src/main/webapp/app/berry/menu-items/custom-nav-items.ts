// project import
// types
import { NavItemType } from 'app/berry/types';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import wizard from 'app/berry/menu-items/wizard';
import system from 'app/berry/menu-items/system';
import survey from 'app/berry/menu-items/survey';
import { IForm } from 'app/shared/model/form.model';

// ==============================|| MENU ITEMS ||============================== //

export interface ICustomNavItems {
  authorities: string[];
  surveyInfo: {
    forms: IForm[];
  };
}

const CustomNavItems: (props: ICustomNavItems) => { items: NavItemType[] } = props => {
  const { authorities, surveyInfo } = props;

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

  const surveyFormItems = () => {
    const items: NavItemType[] = [];

    const formGroup = {
      id: 'form-group',
      title: 'Forms',
      type: 'group',
      children: [],
    };

    const children: NavItemType[] = surveyInfo.forms.map(f => {
      return {
        id: `form-${f.id}`,
        title: f.title,
        type: 'item',
        url: `/form/${f.id}/companies`,
      };
    });

    formGroup.children = children;
    items.push(formGroup);

    return items;
  };

  if (surveyInfo.forms && surveyInfo.forms.length > 0) {
    return {
      items: surveyFormItems(),
    };
  } else {
    return {
      items: authorizedItems(),
    };
  }
};

export default CustomNavItems;
