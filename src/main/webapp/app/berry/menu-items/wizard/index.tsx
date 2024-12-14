import { NavItemType } from 'app/berry/types';

import { IconSitemap } from '@tabler/icons';

const wizard: NavItemType = {
  id: 'wizard',
  title: 'Wizard',
  type: 'group',
  children: [
    {
      id: 'wizard-group',
      title: 'group',
      icon: IconSitemap,
      type: 'item',
      url: '/wizard/group',
    },
  ],
};

export default wizard;
