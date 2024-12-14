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
    {
      id: 'wizard-company',
      title: 'company wizard',
      icon: IconSitemap,
      type: 'item',
      url: '/wizard/company',
    },
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
