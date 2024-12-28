import React, { Fragment, useEffect, useState } from 'react';

import { styled, useTheme } from '@mui/material/styles';
import { Popper, Typography, useMediaQuery } from '@mui/material';

import NavCollapse from '../NavCollapse';
import NavItem from '../NavItem';

import { NavItemType } from 'app/berry/types';
import VerticalNavGroup from 'app/shared/layout/basic-layout/MenuList/NavGroup/vertical-nav-group';
import HorizontalNavGroup from 'app/shared/layout/basic-layout/MenuList/NavGroup/horizontal-nav-group';

type VirtualElement = {
  getBoundingClientRect: () => ClientRect | DOMRect;
  contextElement?: Element;
};

interface NavGroupProps {
  item: NavItemType;
  lastItem: number;
  remItems: NavItemType[];
  lastItemId: string;
}

const NavGroup = ({ item, lastItem, remItems, lastItemId }: NavGroupProps) => {
  const theme = useTheme();

  const matchDownMd = useMediaQuery(theme.breakpoints.down('md'));
  const [currentItem, setCurrentItem] = useState(item);

  useEffect(() => {
    if (lastItem) {
      if (item.id === lastItemId) {
        const localItem: any = { ...item };
        const elements = remItems.map((ele: NavItemType) => ele.elements);
        localItem.children = elements.flat(1);
        setCurrentItem(localItem);
      } else {
        setCurrentItem(item);
      }
    }
  }, [item, lastItem, matchDownMd]);

  const items = currentItem.children?.map(menu => {
    switch (menu.type) {
      case 'collapse':
        return <NavCollapse key={menu.id} menu={menu} level={1} />;
      case 'item':
        return <NavItem key={menu.id} item={menu} level={1} />;
      default:
        return (
          <Typography key={menu.id} variant="h6" color="error" align="center">
            Menu Items Error
          </Typography>
        );
    }
  });

  return (
    <>
      {matchDownMd ? (
        <VerticalNavGroup items={items} currentItem={currentItem} />
      ) : (
        <HorizontalNavGroup item={item} remItems={remItems} lastItemId={lastItemId} items={items} currentItem={currentItem} />
      )}
    </>
  );
};

export default NavGroup;
