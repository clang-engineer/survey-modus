import React from 'react';

// third-party
import { FormattedMessage } from 'react-intl';

// assets
import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';

// constant
const icons = {
  IconBrandChrome,
  IconHelp,
  IconSitemap,
};

// ==============================|| SAMPLE PAGE & DOCUMENTATION MENU ITEMS ||============================== //

const other: NavItemType = {
  id: 'sample-docs-roadmap',
  icon: IconBrandChrome,
  type: 'group',
  children: [
    {
      id: 'sample-page',
      title: 'sample-page',
      type: 'item',
      url: '/sample-page',
      icon: icons.IconBrandChrome,
      breadcrumbs: false,
    },
    {
      id: 'documentation',
      title: 'documentation',
      type: 'item',
      url: 'https://codedthemes.gitbook.io/berry/',
      icon: icons.IconHelp,
      external: true,
      target: true,
    },
    {
      id: 'roadmap',
      title: 'roadmap',
      type: 'item',
      url: 'https://codedthemes.gitbook.io/berry/roadmap',
      icon: icons.IconSitemap,
      external: true,
      target: true,
    },
  ],
};

export default other;
