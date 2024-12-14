import { NavItemType } from 'app/berry/types';

import { IconSitemap, IconUsers } from '@tabler/icons';

const wizard: NavItemType = {
  id: 'wizard',
  title: 'Wizard',
  type: 'group',
  children: [
    {
      id: 'wizard-group',
      title: 'group wizard',
      icon: IconUsers,
      type: 'item',
      url: '/wizard/group',
    },
  ],
};

export default wizard;
