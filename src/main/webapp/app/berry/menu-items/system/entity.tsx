import React from 'react';

import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';

const icons = {
  IconBrandChrome,
  IconHelp,
  IconSitemap,
};

const entity: NavItemType[] = [
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
        breadcrumbs: true,
      },
      {
        id: 'group-user',
        title: 'group-user',
        type: 'item',
        url: '/group-user',
        icon: icons.IconBrandChrome,
        breadcrumbs: true,
      },
      {
        id: 'company',
        title: 'company',
        type: 'item',
        url: '/company',
        icon: icons.IconBrandChrome,
        breadcrumbs: true,
      },
      {
        id: 'group-company',
        title: 'group-company',
        type: 'item',
        url: '/group-company',
        icon: icons.IconBrandChrome,
        breadcrumbs: true,
      },
      {
        id: 'category',
        title: 'category',
        type: 'item',
        url: '/category',
        icon: icons.IconBrandChrome,
        breadcrumbs: true,
      },
      {
        id: 'form',
        title: 'form',
        type: 'item',
        url: '/form',
        icon: icons.IconBrandChrome,
        breadcrumbs: true,
      },
      {
        id: 'field',
        title: 'field',
        type: 'item',
        url: '/field',
        icon: icons.IconBrandChrome,
        breadcrumbs: true,
      },
    ],
  },
];
export default entity;
