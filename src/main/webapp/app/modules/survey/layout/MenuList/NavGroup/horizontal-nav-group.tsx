import React, { Fragment, useEffect, useState } from 'react';

// material-ui
import { styled, useTheme } from '@mui/material/styles';
import { Box, ClickAwayListener, List, ListItemButton, ListItemIcon, ListItemText, Paper, Popper, Typography } from '@mui/material';

// third-party
// project imports
import NavCollapse from '../NavCollapse';
import NavItem from '../NavItem';
import useConfig from 'app/berry/hooks/useConfig';
import Transitions from 'app/berry/ui-component/extended/Transitions';

// assets
import { IconChevronDown, IconChevronRight, IconMinusVertical } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { activeID } from 'app/berry/store/slices/menu';
import { useLocation } from 'react-router-dom';

// mini-menu - wrapper
const PopperStyled = styled(Popper)(({ theme }) => ({
  overflow: 'visible',
  zIndex: 1202,
  minWidth: 180,
  '&:before': {
    content: '""',
    display: 'block',
    position: 'absolute',
    top: 5,
    left: 32,
    width: 12,
    height: 12,
    transform: 'translateY(-50%) rotate(45deg)',
    zIndex: 120,
    borderWidth: '6px',
    borderStyle: 'solid',
    borderColor: `${theme.palette.background.paper}  transparent transparent ${theme.palette.background.paper}`,
  },
}));

// ==============================|| SIDEBAR MENU LIST GROUP ||============================== //

type VirtualElement = {
  getBoundingClientRect: () => ClientRect | DOMRect;
  contextElement?: Element;
};

interface HorizontalMenuListProps {
  item: NavItemType;
  remItems: NavItemType[];
  lastItemId: string;
  items: JSX.Element[];
  currentItem: NavItemType;
}

const HorizontalNavGroup = (props: HorizontalMenuListProps) => {
  const { item, remItems, lastItemId, items, currentItem } = props;

  const theme = useTheme();

  const { selectedID } = useAppSelector(state => state.menu);
  const { borderRadius } = useConfig();
  const { pathname } = useLocation();
  const dispatch = useAppDispatch();

  const [anchorEl, setAnchorEl] = useState<VirtualElement | (() => VirtualElement) | null | undefined>(null);

  const checkOpenForParent = (child: NavItemType[], id: string) => {
    child.forEach((ele: NavItemType) => {
      if (ele.children?.length) {
        checkOpenForParent(ele.children, currentItem.id);
      }
      if (ele.url === pathname) {
        dispatch(activeID(id));
      }
    });
  };

  const checkSelectedOnload = (data: NavItemType) => {
    const childrens = data.children ? data.children : [];
    childrens.forEach((itemCheck: NavItemType) => {
      if (itemCheck.children?.length) {
        checkOpenForParent(itemCheck.children, currentItem.id);
      }
      if (itemCheck.url === pathname) {
        dispatch(activeID(currentItem.id));
      }
    });
  };

  // keep selected-menu on page load and use for horizontal menu close on change routes
  useEffect(() => {
    checkSelectedOnload(currentItem);
    if (openMini) setAnchorEl(null);
  }, [pathname, currentItem]);

  const openMini = Boolean(anchorEl);

  const handleClick = (event: React.MouseEvent<HTMLAnchorElement> | React.MouseEvent<HTMLDivElement, MouseEvent> | undefined) => {
    if (!openMini) {
      setAnchorEl(event?.currentTarget);
    }
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  const Icon = currentItem?.icon;
  const itemIcon = currentItem?.icon ? <Icon stroke={1.5} size="20px" /> : null;

  const moreItems = remItems.map((itemRem: NavItemType, i) => (
    <Fragment key={i}>
      {itemRem.title && (
        <Typography variant="caption" sx={{ pl: 2 }}>
          {itemRem.title}
        </Typography>
      )}
      {itemRem.elements?.map(menu => {
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
      })}
    </Fragment>
  ));

  const popperId = openMini ? `group-pop-${item.id}` : undefined;

  if (currentItem.children && currentItem.children.length === 1) {
    const singleChild = currentItem.children[0];

    return (
      <List
        sx={{
          '& .MuiListItemButton-root': {
            borderRadius: `${borderRadius}px`,
            p: 1,
            my: 0.5,
            mr: 1,
            display: 'flex',
            alignItems: 'center',
            backgroundColor: 'inherit',
          },
        }}
      >
        <NavItem key={singleChild.id} item={singleChild} level={1} />
      </List>
    );
  }

  return (
    <List>
      <ListItemButton
        selected={selectedID === currentItem.id}
        sx={{
          borderRadius: `${borderRadius}px`,
          p: 1,
          my: 0.5,
          mr: 1,
          display: 'flex',
          alignItems: 'center',
          backgroundColor: 'inherit',
        }}
        onMouseEnter={handleClick}
        onClick={handleClick}
        onMouseLeave={handleClose}
        aria-describedby={popperId}
      >
        {itemIcon && (
          <ListItemIcon sx={{ minWidth: 28 }}>
            {currentItem.id === lastItemId ? <IconMinusVertical stroke={1.5} size="20px" /> : itemIcon}
          </ListItemIcon>
        )}
        <ListItemText
          sx={{ mr: 1 }}
          primary={
            <Typography variant={selectedID === currentItem.id ? 'h5' : 'body1'} color="inherit">
              {currentItem.id === lastItemId ? 'more-items' : currentItem.title}
            </Typography>
          }
        />
        {openMini ? <IconChevronDown stroke={1.5} size="16px" /> : <IconChevronRight stroke={1.5} size="16px" />}

        {anchorEl && (
          <PopperStyled
            id={popperId}
            open={openMini}
            anchorEl={anchorEl}
            placement="bottom-start"
            style={{
              zIndex: 2001,
            }}
          >
            {({ TransitionProps }) => (
              <Transitions in={openMini} {...TransitionProps}>
                <Paper
                  sx={{
                    mt: 0.5,
                    py: 1.25,
                    boxShadow: theme.shadows[8],
                    backgroundImage: 'none',
                  }}
                >
                  <ClickAwayListener onClickAway={handleClose}>
                    <Box
                      sx={{
                        maxHeight: 'calc(100vh - 170px)',
                        overflowY: 'auto',
                        '&::-webkit-scrollbar': {
                          opacity: 0,
                          width: 4,
                          '&:hover': {
                            opacity: 0.7,
                          },
                        },
                        '&::-webkit-scrollbar-track': {
                          background: 'transparent',
                        },
                        '&::-webkit-scrollbar-thumb': {
                          background: theme.palette.divider,
                          borderRadius: 4,
                        },
                      }}
                    >
                      {currentItem.id !== lastItemId ? items : moreItems}
                    </Box>
                  </ClickAwayListener>
                </Paper>
              </Transitions>
            )}
          </PopperStyled>
        )}
      </ListItemButton>
    </List>
  );
};

export default HorizontalNavGroup;
