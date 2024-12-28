import React, { useEffect, useMemo } from 'react';
import { Outlet } from 'react-router-dom';

// material-ui
import { styled, Theme, useTheme } from '@mui/material/styles';
import { AppBar, Box, Container, CssBaseline, Toolbar, useMediaQuery } from '@mui/material';

// project imports
import Header from './Header';
import Sidebar from './Sidebar';
import HorizontalBar from './HorizontalBar';
import Breadcrumbs from 'app/berry/ui-component/extended/Breadcrumbs';

import navigation from 'app/berry/menu-items';
import LAYOUT_CONST from 'app/berry/constant';
import useConfig from 'app/berry/hooks/useConfig';
import { drawerWidth } from 'app/berry/store/constant';
import { openDrawer } from 'app/berry/store/slices/menu';

// assets
import { IconChevronRight } from '@tabler/icons';
import { useAppDispatch, useAppSelector } from 'app/config/store';

interface MainStyleProps {
  theme: Theme;
  open: boolean;
}

// styles
const Main = styled('main', { shouldForwardProp: prop => prop !== 'open' })<MainStyleProps>(({ theme, open }: MainStyleProps) => ({
  ...theme.typography.mainContent,
  borderBottomLeftRadius: 0,
  borderBottomRightRadius: 0,
  ...(!open && {
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.shorter + 200,
    }),
    [theme.breakpoints.up('md')]: {
      marginLeft: '20px',
      width: `calc(100% - ${drawerWidth}px)`,
      marginTop: 135,
    },
    [theme.breakpoints.down('md')]: {
      marginLeft: '20px',
      width: `calc(100% - ${drawerWidth}px)`,
      padding: '16px',
      marginTop: 88,
    },
    [theme.breakpoints.down('sm')]: {
      marginLeft: '10px',
      width: `calc(100% - ${drawerWidth}px)`,
      padding: '16px',
      marginRight: '10px',
      marginTop: 88,
    },
  }),
  ...(open && {
    // 'margin 538ms cubic-bezier(0.4, 0, 1, 1) 0ms',
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.shorter + 200,
    }),
    marginLeft: '20px',
    marginTop: 135,
    width: `calc(100% - ${drawerWidth}px)`,
    [theme.breakpoints.up('md')]: {
      marginTop: 135,
    },
    [theme.breakpoints.down('md')]: {
      marginLeft: '20px',
      marginTop: 88,
    },
    [theme.breakpoints.down('sm')]: {
      marginLeft: '10px',
      marginTop: 88,
    },
  }),
}));

// ==============================|| MAIN LAYOUT ||============================== //

const MainLayout = () => {
  const theme = useTheme();

  const matchDownMd = useMediaQuery(theme.breakpoints.down('md'));

  const dispatch = useAppDispatch();
  const { drawerOpen } = useAppSelector(state => state.menu);
  const { drawerType, container } = useConfig();

  useEffect(() => {
    if (drawerType === LAYOUT_CONST.DEFAULT_DRAWER) {
      dispatch(openDrawer(true));
    } else {
      dispatch(openDrawer(false));
    }
  }, [drawerType]);

  useEffect(() => {
    if (drawerType === LAYOUT_CONST.DEFAULT_DRAWER) {
      dispatch(openDrawer(true));
    }
  }, []);

  useEffect(() => {
    if (matchDownMd) {
      dispatch(openDrawer(true));
    }
  }, [matchDownMd]);

  const header = useMemo(
    () => (
      <Toolbar sx={{ p: !matchDownMd ? '10px' : '16px' }}>
        <Header />
      </Toolbar>
    ),
    [matchDownMd]
  );

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBar enableColorOnDark position="fixed" color="inherit" elevation={0} sx={{ bgcolor: theme.palette.background.default }}>
        {header}
      </AppBar>

      {matchDownMd ? <Sidebar /> : <HorizontalBar />}

      <Main theme={theme} open={drawerOpen}>
        <Container maxWidth={container ? 'lg' : false} {...(!container && { sx: { px: { xs: 0 } } })}>
          {/* breadcrumb */}
          <Breadcrumbs separator={IconChevronRight} navigation={navigation} icon title rightAlign />
          <Outlet />
        </Container>
      </Main>
    </Box>
  );
};

export default MainLayout;
