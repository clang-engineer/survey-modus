import React from 'react';

import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';

const icons = {
  IconBrandChrome,
  IconHelp,
  IconSitemap,
};

const entities: NavItemType[] = [
  {
    id: 'entities',
    title: 'entities',
    type: 'collapse',
    icon: icons.IconSitemap,
    children: [
      {
        id: 'group',
        title: 'group',
        type: 'item',
        url: '/group',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'group-user',
        title: 'group-user',
        type: 'item',
        url: '/group-user',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'company',
        title: 'company',
        type: 'item',
        url: '/company',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'group-company',
        title: 'group-company',
        type: 'item',
        url: '/group-company',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'category',
        title: 'category',
        type: 'item',
        url: '/category',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'form',
        title: 'form',
        type: 'item',
        url: '/form',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
    ],
  },
];
export default entities;
