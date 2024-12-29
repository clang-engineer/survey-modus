import React, { memo, useEffect } from 'react';

// material-ui
import { useTheme } from '@mui/material/styles';
import { Typography, useMediaQuery } from '@mui/material';

// project imports
import NavGroup from './NavGroup';
import useConfig from 'app/berry/hooks/useConfig';
import { Menu } from 'app/berry/menu-items/widget';

import LAYOUT_CONST from 'app/berry/constant';
import { HORIZONTAL_MAX_ITEM } from 'app/berry/config';

// types
import { NavItemType } from 'app/berry/types';
import useGateConfig from 'app/modules/gate/gate.config';

// ==============================|| SIDEBAR MENU LIST ||============================== //

const MenuList = () => {
  const theme = useTheme();

  const matchDownMd = useMediaQuery(theme.breakpoints.down('md'));

  const { menuItems } = useGateConfig();

  useEffect(() => {
    handlerMenuItem();
    // eslint-disable-next-line
  }, []);

  let getMenu = Menu();
  const handlerMenuItem = () => {
    const isFound = menuItems.some(element => {
      if (element.id === 'widget') {
        return true;
      }
      return false;
    });

    if (getMenu?.id !== undefined && !isFound) {
      menuItems.splice(1, 0, getMenu);
    }
  };

  // last menu-item to show in horizontal menu bar
  const lastItem = !matchDownMd ? HORIZONTAL_MAX_ITEM : null;

  let lastItemIndex = menuItems.length - 1;
  let remItems: NavItemType[] = [];
  let lastItemId: string;

  if (lastItem && lastItem < menuItems.length) {
    lastItemId = menuItems[lastItem - 1].id!;
    lastItemIndex = lastItem - 1;
    remItems = menuItems.slice(lastItem - 1, menuItems.length).map(item => ({
      title: item.title,
      elements: item.children,
    }));
  }

  const navItems = menuItems.slice(0, lastItemIndex + 1).map(item => {
    switch (item.type) {
      case 'group':
        return <NavGroup key={item.id} item={item} lastItem={lastItem!} remItems={remItems} lastItemId={lastItemId} />;
      default:
        return (
          <Typography key={item.id} variant="h6" color="error" align="center">
            Menu Items Error
          </Typography>
        );
    }
  });

  return <>{navItems}</>;
};

export default memo(MenuList);
