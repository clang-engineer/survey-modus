import React, { forwardRef, ForwardRefExoticComponent, RefAttributes, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';

// material-ui
import { useTheme } from '@mui/material/styles';
import { useMediaQuery } from '@mui/material';

// project imports
import LAYOUT_CONST from 'app/berry/constant';
import useConfig from 'app/berry/hooks/useConfig';
import { activeID, activeItem, openDrawer } from 'app/berry/store/slices/menu';

// assets
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import { LinkTarget, NavItemType } from 'app/berry/types';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import VerticalNavItem from 'app/modules/gate/gate-layout/MenuList/NavItem/vertical-nav-item';
import HorizontalNavItem from 'app/modules/gate/gate-layout/MenuList/NavItem/horizontal-nav-item';

// ==============================|| SIDEBAR MENU LIST ITEMS ||============================== //

interface NavItemProps {
  item: NavItemType;
  level: number;
}

const NavItem = ({ item, level }: NavItemProps) => {
  const theme = useTheme();
  const matchesSM = useMediaQuery(theme.breakpoints.down('lg'));
  const matchDownMd = useMediaQuery(theme.breakpoints.down('md'));

  const dispatch = useAppDispatch();
  const { pathname } = useLocation();
  const layout = LAYOUT_CONST.HORIZONTAL_LAYOUT;

  const { selectedItem, drawerOpen } = useAppSelector(state => state.menu);
  const isSelected = selectedItem.findIndex(id => id === item.id) > -1;

  const Icon = item?.icon!;
  const itemIcon = item?.icon ? (
    <Icon
      stroke={1.5}
      size={drawerOpen ? '20px' : '24px'}
      style={{ color: isSelected ? theme.palette.secondary.main : theme.palette.text.primary }}
    />
  ) : (
    <FiberManualRecordIcon
      sx={{
        color: isSelected ? theme.palette.secondary.main : theme.palette.text.primary,
        width: selectedItem.findIndex(id => id === item?.id) > -1 ? 8 : 6,
        height: selectedItem.findIndex(id => id === item?.id) > -1 ? 8 : 6,
      }}
      fontSize={level > 0 ? 'inherit' : 'medium'}
    />
  );

  let itemTarget: LinkTarget = '_self';
  if (item.target) {
    itemTarget = '_blank';
  }

  let listItemProps: {
    component: ForwardRefExoticComponent<RefAttributes<HTMLAnchorElement>> | string;
    href?: string;
    target?: LinkTarget;
  } = {
    component: forwardRef((props, ref) => <Link ref={ref} {...props} to={item.url!} target={itemTarget} />),
  };
  if (item?.external) {
    listItemProps = { component: 'a', href: item.url, target: itemTarget };
  }

  const itemHandler = (id: string) => {
    dispatch(activeItem([id]));
    if (matchesSM) dispatch(openDrawer(false));
    dispatch(activeID(id));
  };

  // active menu item on page load
  useEffect(() => {
    const currentIndex = document.location.pathname
      .toString()
      .split('/')
      .findIndex(id => id === item.id);
    if (currentIndex > -1) {
      dispatch(activeItem([item.id]));
    }
    // eslint-disable-next-line
  }, [pathname]);

  return (
    <>
      {layout === LAYOUT_CONST.VERTICAL_LAYOUT || (layout === LAYOUT_CONST.HORIZONTAL_LAYOUT && matchDownMd) ? (
        <VerticalNavItem item={item} level={level} itemIcon={itemIcon} listItemProps={listItemProps} itemHandler={itemHandler} />
      ) : (
        <HorizontalNavItem item={item} level={level} itemIcon={itemIcon} listItemProps={listItemProps} itemHandler={itemHandler} />
      )}
    </>
  );
};

export default NavItem;
