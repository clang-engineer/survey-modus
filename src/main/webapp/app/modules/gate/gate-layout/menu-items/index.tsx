import { NavItemType } from 'app/berry/types';

import { IconSitemap } from '@tabler/icons';
// ==============================|| MENU ITEMS ||============================== //

const wrapper = (item: any) => {
  return {
    id: new Date().getTime().toString(),
    title: '',
    type: 'group',
    children: [item],
  };
};

const menuItems: { items: NavItemType[] } = {
  items: [
    wrapper({
      id: 'forms',
      title: 'forms',
      icon: IconSitemap,
      type: 'item',
      url: '/gate/form',
    }),
    wrapper({
      id: 'datasource',
      title: 'Datasource',
      icon: IconSitemap,
      type: 'item',
      url: '/gate/datasource',
    }),
  ],
};

export default menuItems;
