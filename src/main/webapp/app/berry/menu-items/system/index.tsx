import React from 'react';

import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';
import entity from 'app/berry/menu-items/system/entity';
import administration from 'app/berry/menu-items/system/administration';

const system: NavItemType = {
  id: 'system',
  title: 'system',
  icon: IconBrandChrome,
  type: 'group',
  children: [...entity, ...administration],
};

export default system;
