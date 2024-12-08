import React from 'react';

// third-party
import { FormattedMessage } from 'react-intl';

// assets
import { IconDashboard, IconDeviceAnalytics } from '@tabler/icons';

// type
import { NavItemType } from 'app/berry/types';

const icons = {
  IconDashboard: IconDashboard,
  IconDeviceAnalytics: IconDeviceAnalytics,
};

// ==============================|| MENU ITEMS - DASHBOARD ||============================== //

const dashboard: NavItemType = {
  id: 'dashboard',
  title: 'Dashboard',
  icon: icons.IconDashboard,
  type: 'group',
  children: [
    {
      id: 'default',
      title: 'Default',
      type: 'item',
      url: '/dashboard/default',
      icon: icons.IconDashboard,
      breadcrumbs: false,
    },
    {
      id: 'analytics',
      title: 'Analytics',
      type: 'item',
      url: '/dashboard/analytics',
      icon: icons.IconDeviceAnalytics,
      breadcrumbs: false,
    },
  ],
};

export default dashboard;
