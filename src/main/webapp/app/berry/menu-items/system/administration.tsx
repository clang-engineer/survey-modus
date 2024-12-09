import React from 'react';

import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';

const icons = {
  IconBrandChrome,
  IconHelp,
  IconSitemap,
};

const administration: NavItemType[] = [
  {
    id: 'administration',
    title: 'administration',
    type: 'collapse',
    icon: icons.IconSitemap,
    children: [
      {
        id: 'user-management',
        title: 'user-management',
        type: 'item',
        url: '/admin/user-management',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'tracker',
        title: 'tracker',
        type: 'item',
        url: '/admin/tracker',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'health',
        title: 'health',
        type: 'item',
        url: '/admin/health',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'metrics',
        title: 'metrics',
        type: 'item',
        url: '/admin/metrics',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'configuration',
        title: 'configuration',
        type: 'item',
        url: '/admin/configuration',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'logs',
        title: 'logs',
        type: 'item',
        url: '/admin/logs',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
      {
        id: 'docs',
        title: 'docs',
        type: 'item',
        url: '/admin/docs',
        icon: icons.IconBrandChrome,
        breadcrumbs: false,
      },
    ],
  },
];
export default administration;
