import { NavItemType } from 'app/berry/types';

import { IconSitemap, IconUsers } from '@tabler/icons';

const wizard: NavItemType = {
  id: 'wizard',
  title: 'Wizard',
  type: 'group',
  children: [
    {
      id: 'wizard-form',
      title: 'form wizard',
      icon: IconSitemap,
      type: 'item',
      url: '/wizard/form',
    },
  ],
};

export default wizard;
