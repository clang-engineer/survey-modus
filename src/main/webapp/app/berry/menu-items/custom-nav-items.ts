// project import
// types
import { NavItemType } from 'app/berry/types';
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';
import wizard from 'app/berry/menu-items/wizard';
import system from 'app/berry/menu-items/system';
import survey from 'app/berry/menu-items/survey';
import { IForm } from 'app/shared/model/form.model';
import { ICompany } from 'app/shared/model/company.model';

// ==============================|| MENU ITEMS ||============================== //

export interface ICustomNavItems {
  authorities: string[];
  surveyInfo: {
    company: ICompany;
    forms: IForm[];
  };
}

const CustomNavItems: (props: ICustomNavItems) => { items: NavItemType[] } = props => {
  const { authorities, surveyInfo } = props;

  const authorizedItems = () => {
    const items: NavItemType[] = [];

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

    items.push(survey(surveyInfo.company, surveyInfo.forms));

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
