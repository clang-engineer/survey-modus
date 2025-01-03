import { NavItemType } from 'app/berry/types';

import { IconDatabase } from '@tabler/icons';

const survey: NavItemType = {
  id: 'survey',
  title: 'Survey',
  type: 'group',
  children: [
    {
      id: 'survey-companies',
      title: 'companies',
      icon: IconDatabase,
      type: 'item',
      url: '/survey/companies',
    },
  ],
};

export default survey;
