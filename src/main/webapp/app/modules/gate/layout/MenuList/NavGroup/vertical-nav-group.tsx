import React, { Fragment } from 'react';

// material-ui
import { useTheme } from '@mui/material/styles';
import { Divider, List, Typography } from '@mui/material';

import { NavItemType } from 'app/berry/types';
import { useAppSelector } from 'app/config/store';

interface VerticalMenuListProps {
  items: JSX.Element[];
  currentItem: NavItemType;
}

const VerticalNavGroup = ({ items, currentItem }: VerticalMenuListProps) => {
  const theme = useTheme();

  const { drawerOpen } = useAppSelector(state => state.menu);

  return (
    <>
      <List
        disablePadding={!drawerOpen}
        subheader={
          currentItem.title &&
          drawerOpen && (
            <Typography variant="caption" sx={{ ...theme.typography.menuCaption }} display="block" gutterBottom>
              {currentItem.title}
              {currentItem.caption && (
                <Typography variant="caption" sx={{ ...theme.typography.subMenuCaption }} display="block" gutterBottom>
                  {currentItem.caption}
                </Typography>
              )}
            </Typography>
          )
        }
      >
        {items}
      </List>

      {/* group divider */}
      {drawerOpen && <Divider sx={{ mt: 0.25, mb: 1.25 }} />}
    </>
  );
};

export default VerticalNavGroup;
