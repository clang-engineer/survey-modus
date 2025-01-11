import { NavItemType } from 'app/berry/types';

import { IconDatabase } from '@tabler/icons';
import { ICompany } from 'app/shared/model/company.model';
import { IForm } from 'app/shared/model/form.model';

const survey = (company: ICompany, forms: IForm[]): NavItemType => {
  const group = {
    id: 'survey',
    title: 'Survey',
    type: 'group',
    children: [],
  };

  const children: NavItemType[] = forms
    .filter(f => f.activated)
    .map(f => {
      return {
        id: `form-${f.id}`,
        title: f.title,
        type: 'item',
        url: `/survey/companies/${company.id}/forms/${f.id}`,
        icon: IconDatabase,
      };
    });

  return {
    ...group,
    children: children,
  };
};

export default survey;
