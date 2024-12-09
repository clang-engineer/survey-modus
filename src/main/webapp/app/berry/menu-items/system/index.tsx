import React from 'react';

import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';
import entities from 'app/berry/menu-items/system/entities';

const system: NavItemType = {
  id: 'system',
  title: 'system',
  icon: IconBrandChrome,
  type: 'group',
  children: [...entities],
};

export default system;
