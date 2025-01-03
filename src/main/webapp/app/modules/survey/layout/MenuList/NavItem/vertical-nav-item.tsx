import React, { forwardRef, ForwardRefExoticComponent, RefAttributes, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';

// material-ui
import { useTheme } from '@mui/material/styles';
import { Avatar, ButtonBase, Chip, ListItemButton, ListItemIcon, ListItemText, Typography, useMediaQuery } from '@mui/material';

// project imports
import LAYOUT_CONST from 'app/berry/constant';
import useConfig from 'app/berry/hooks/useConfig';
import { activeID, activeItem, openDrawer } from 'app/berry/store/slices/menu';

// assets
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import { LinkTarget, NavItemType } from 'app/berry/types';
import { useAppDispatch, useAppSelector } from 'app/config/store';

// ==============================|| SIDEBAR MENU LIST ITEMS ||============================== //

interface NavItemProps {
  item: NavItemType;
  level: number;
  itemIcon: JSX.Element;
  listItemProps: {
    component: ForwardRefExoticComponent<RefAttributes<HTMLAnchorElement>> | string;
    href?: string;
    target?: LinkTarget;
  };
  itemHandler: (id: string) => void;
}

const NavItem = (props: NavItemProps) => {
  const { item, level, itemIcon, listItemProps } = props;

  const theme = useTheme();
  const { borderRadius } = useConfig();

  const { selectedItem, drawerOpen } = useAppSelector(state => state.menu);
  const isSelected = selectedItem.findIndex(id => id === item.id) > -1;

  const textColor = theme.palette.mode === 'dark' ? 'grey.400' : 'text.primary';
  const iconSelectedColor = theme.palette.mode === 'dark' && drawerOpen ? 'text.primary' : 'secondary.main';

  return (
    <ListItemButton
      {...listItemProps}
      disabled={item.disabled}
      disableRipple={!drawerOpen}
      sx={{
        zIndex: 1201,
        borderRadius: `${borderRadius}px`,
        mb: 0.5,
        pl: drawerOpen ? `${level * 24}px` : 1.25,
        ...(drawerOpen &&
          level === 1 &&
          theme.palette.mode !== 'dark' && {
            '&:hover': {
              background: theme.palette.secondary.light,
            },
            '&.Mui-selected': {
              background: theme.palette.secondary.light,
              color: iconSelectedColor,
              '&:hover': {
                color: iconSelectedColor,
                background: theme.palette.secondary.light,
              },
            },
          }),
        ...((!drawerOpen || level !== 1) && {
          py: level === 1 ? 0 : 1,
          '&:hover': {
            bgcolor: 'transparent',
          },
          '&.Mui-selected': {
            '&:hover': {
              bgcolor: 'transparent',
            },
            bgcolor: 'transparent',
          },
        }),
      }}
      selected={isSelected}
      onClick={() => props.itemHandler(item.id)}
    >
      <ButtonBase aria-label="theme-icon" sx={{ borderRadius: `${borderRadius}px` }} disableRipple={drawerOpen}>
        <ListItemIcon
          sx={{
            minWidth: level === 1 ? 36 : 18,
            color: isSelected ? iconSelectedColor : textColor,
            ...(!drawerOpen &&
              level === 1 && {
                borderRadius: `${borderRadius}px`,
                width: 46,
                height: 46,
                alignItems: 'center',
                justifyContent: 'center',
                '&:hover': {
                  bgcolor: theme.palette.mode === 'dark' ? theme.palette.secondary.main + 25 : 'secondary.light',
                },
                ...(isSelected && {
                  bgcolor: theme.palette.mode === 'dark' ? theme.palette.secondary.main + 25 : 'secondary.light',
                  '&:hover': {
                    bgcolor: theme.palette.mode === 'dark' ? theme.palette.secondary.main + 30 : 'secondary.light',
                  },
                }),
              }),
          }}
        >
          {itemIcon}
        </ListItemIcon>
      </ButtonBase>

      {(drawerOpen || (!drawerOpen && level !== 1)) && (
        <ListItemText
          primary={
            <Typography variant={isSelected ? 'h5' : 'body1'} color="inherit">
              {item.title}
            </Typography>
          }
          secondary={
            item.caption && (
              <Typography variant="caption" sx={{ ...theme.typography.subMenuCaption }} display="block" gutterBottom>
                {item.caption}
              </Typography>
            )
          }
        />
      )}

      {drawerOpen && item.chip && (
        <Chip
          color={item.chip.color}
          variant={item.chip.variant}
          size={item.chip.size}
          label={item.chip.label}
          avatar={item.chip.avatar && <Avatar>{item.chip.avatar}</Avatar>}
        />
      )}
    </ListItemButton>
  );
};

export default NavItem;
