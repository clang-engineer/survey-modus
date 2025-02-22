import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

// material-ui
import { useTheme } from '@mui/material/styles';
import {
  Avatar,
  Box,
  Card,
  CardContent,
  Chip,
  ClickAwayListener,
  Divider,
  Grid,
  InputAdornment,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  OutlinedInput,
  Paper,
  Popper,
  Stack,
  Switch,
  Typography,
} from '@mui/material';

// third-party
import { FormattedMessage } from 'react-intl';
import PerfectScrollbar from 'react-perfect-scrollbar';

// project imports
import MainCard from 'app/berry/ui-component/cards/MainCard';
import Transitions from 'app/berry/ui-component/extended/Transitions';
import UpgradePlanCard from './UpgradePlanCard';
import useAuth from 'app/berry/hooks/useAuth';
import User1 from 'app/berry/assets/images/users/user-round.svg';

// assets
import { IconLogout, IconSearch, IconSettings, IconBrightness, IconKey } from '@tabler/icons';
import useConfig from 'app/berry/hooks/useConfig';
import { useAppDispatch, useAppSelector } from 'app/config/store';

// ==============================|| PROFILE MENU ||============================== //

const ProfileSection = () => {
  const theme = useTheme();
  const config = useConfig();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const account = useAppSelector(state => state.authentication.account);
  const [sdm, setSdm] = useState(true);
  const [value, setValue] = useState('');
  const [notification, setNotification] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(-1);
  const [open, setOpen] = useState(false);
  /**
   * anchorRef is used on different components and specifying one type leads to other components throwing an error
   * */
  const anchorRef = useRef<any>(null);
  const handleLogout = async () => {
    navigate('/logout');
  };
  const handleListItemClick = (event: React.MouseEvent<HTMLDivElement>, index: number, route: string = '') => {
    setSelectedIndex(index);
    handleClose(event);

    if (route && route !== '') {
      navigate(route);
    }
  };
  const handleToggle = () => {
    setOpen(prevOpen => !prevOpen);
  };
  const handleClose = (event: React.MouseEvent<HTMLDivElement> | MouseEvent | TouchEvent) => {
    if (anchorRef.current && anchorRef.current.contains(event.target)) {
      return;
    }

    setOpen(false);
  };
  const prevOpen = useRef(open);
  useEffect(() => {
    if (prevOpen.current === true && open === false) {
      anchorRef.current.focus();
    }

    prevOpen.current = open;
  }, [open]);

  return (
    <>
      <Chip
        sx={{
          height: '48px',
          alignItems: 'center',
          borderRadius: '27px',
          transition: 'all .2s ease-in-out',
          borderColor: theme.palette.mode === 'dark' ? theme.palette.dark.main : theme.palette.primary.light,
          backgroundColor: theme.palette.mode === 'dark' ? theme.palette.dark.main : theme.palette.primary.light,
          '&[aria-controls="menu-list-grow"], &:hover': {
            borderColor: theme.palette.primary.main,
            background: `${theme.palette.primary.main}!important`,
            color: theme.palette.primary.light,
            '& svg': {
              stroke: theme.palette.primary.light,
            },
          },
          '& .MuiChip-label': {
            lineHeight: 0,
          },
        }}
        icon={
          <Avatar
            src={User1}
            alt="user-images"
            sx={{
              ...theme.typography.mediumAvatar,
              margin: '8px 0 8px 8px !important',
              cursor: 'pointer',
            }}
            ref={anchorRef}
            aria-controls={open ? 'menu-list-grow' : undefined}
            aria-haspopup="true"
            color="inherit"
          />
        }
        label={<IconSettings stroke={1.5} size="24px" color={theme.palette.primary.main} />}
        variant="outlined"
        ref={anchorRef}
        aria-controls={open ? 'menu-list-grow' : undefined}
        aria-haspopup="true"
        onClick={handleToggle}
        color="primary"
        aria-label="user-account"
      />

      <Popper
        placement="bottom"
        open={open}
        anchorEl={anchorRef.current}
        role={undefined}
        transition
        disablePortal
        modifiers={[
          {
            name: 'offset',
            options: {
              offset: [0, 14],
            },
          },
        ]}
      >
        {({ TransitionProps }) => (
          <ClickAwayListener onClickAway={handleClose}>
            <Transitions in={open} {...TransitionProps}>
              <Paper>
                {open && (
                  <MainCard border={false} elevation={16} content={false} boxShadow shadow={theme.shadows[16]}>
                    <Box sx={{ p: 2, pb: 0 }}>
                      <Stack>
                        <Stack direction="row" spacing={0.5} alignItems="center">
                          <Typography variant="h4">Good Morning,</Typography>
                          <Typography component="span" variant="h4" sx={{ fontWeight: 400 }}>
                            {account.firstName}
                            {account.lastName}
                          </Typography>
                        </Stack>
                        <Typography variant="subtitle2">Project Admin</Typography>
                      </Stack>
                      <Divider />
                    </Box>
                    <PerfectScrollbar style={{ height: '100%', maxHeight: 'calc(100vh - 250px)', overflowX: 'hidden' }}>
                      <Box sx={{ p: 2, pt: 0 }}>
                        <Divider />
                        <Card
                          sx={{
                            bgcolor: theme.palette.mode === 'dark' ? theme.palette.dark[800] : theme.palette.primary.light,
                            my: 2,
                          }}
                        >
                          <CardContent>
                            <Grid container spacing={3} direction="column">
                              <Grid item>
                                <Grid item container alignItems="center" justifyContent="space-between">
                                  <Grid item>
                                    <Typography variant="subtitle1">Start DND Mode</Typography>
                                  </Grid>
                                  <Grid item>
                                    <Switch
                                      color="primary"
                                      checked={sdm}
                                      onChange={e => setSdm(e.target.checked)}
                                      name="sdm"
                                      size="small"
                                    />
                                  </Grid>
                                </Grid>
                              </Grid>
                              <Grid item>
                                <Grid item container alignItems="center" justifyContent="space-between">
                                  <Grid item>
                                    <Typography variant="subtitle1">Allow Notifications</Typography>
                                  </Grid>
                                  <Grid item>
                                    <Switch
                                      checked={notification}
                                      onChange={e => setNotification(e.target.checked)}
                                      name="sdm"
                                      size="small"
                                    />
                                  </Grid>
                                </Grid>
                              </Grid>
                              <Grid item>
                                <Grid item container alignItems="center" justifyContent="space-between">
                                  <Grid item>
                                    <Typography variant="subtitle1">
                                      <IconBrightness
                                        stroke={1.5}
                                        size="20"
                                        color={theme.palette.mode === 'dark' ? theme.palette.primary.dark : theme.palette.primary.main}
                                      />
                                      &nbsp; Dark Mode
                                    </Typography>
                                  </Grid>
                                  <Grid item>
                                    <Switch
                                      color="primary"
                                      checked={config.navType === 'light'}
                                      onChange={e => {
                                        if (config.navType === 'light') {
                                          config.onChangeMenuType('dark');
                                        } else {
                                          config.onChangeMenuType('light');
                                        }
                                      }}
                                      name="navType"
                                      size="small"
                                    />
                                  </Grid>
                                </Grid>
                              </Grid>
                            </Grid>
                          </CardContent>
                        </Card>
                        <Divider />
                        <List
                          component="nav"
                          sx={{
                            width: '100%',
                            maxWidth: 350,
                            minWidth: 300,
                            backgroundColor: theme.palette.background.paper,
                            borderRadius: '10px',
                            [theme.breakpoints.down('md')]: {
                              minWidth: '100%',
                            },
                            '& .MuiListItemButton-root': {
                              mt: 0.5,
                            },
                          }}
                        >
                          <ListItemButton
                            sx={{ borderRadius: `${config.borderRadius}px` }}
                            selected={selectedIndex === 0}
                            onClick={(event: React.MouseEvent<HTMLDivElement>) => handleListItemClick(event, 0, '/account/settings')}
                          >
                            <ListItemIcon>
                              <IconSettings stroke={1.5} size="20px" />
                            </ListItemIcon>
                            <ListItemText primary={<Typography variant="body2">Account Settings</Typography>} />
                          </ListItemButton>
                          <ListItemButton
                            sx={{ borderRadius: `${config.borderRadius}px` }}
                            selected={selectedIndex === 1}
                            onClick={(event: React.MouseEvent<HTMLDivElement>) => handleListItemClick(event, 1, '/account/password')}
                          >
                            <ListItemIcon>
                              <IconKey stroke={1.5} size="20px" />
                            </ListItemIcon>
                            <ListItemText primary={<Typography variant="body2">Change Password</Typography>} />
                          </ListItemButton>
                          <ListItemButton
                            sx={{ borderRadius: `${config.borderRadius}px` }}
                            selected={selectedIndex === 4}
                            onClick={handleLogout}
                          >
                            <ListItemIcon>
                              <IconLogout stroke={1.5} size="20px" />
                            </ListItemIcon>
                            <ListItemText primary={<Typography variant="body2">logout</Typography>} />
                          </ListItemButton>
                        </List>
                      </Box>
                    </PerfectScrollbar>
                  </MainCard>
                )}
              </Paper>
            </Transitions>
          </ClickAwayListener>
        )}
      </Popper>
    </>
  );
};

export default ProfileSection;
