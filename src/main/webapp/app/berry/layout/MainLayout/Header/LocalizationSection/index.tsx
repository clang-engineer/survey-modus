import React, { useEffect, useRef, useState } from 'react';

// material-ui
import { useTheme } from '@mui/material/styles';
import {
  Avatar,
  Box,
  ClickAwayListener,
  Grid,
  List,
  ListItemButton,
  ListItemText,
  Paper,
  Popper,
  Typography,
  useMediaQuery,
} from '@mui/material';

// project imports
import Transitions from 'app/berry/ui-component/extended/Transitions';

// assets
import TranslateTwoToneIcon from '@mui/icons-material/TranslateTwoTone';
import useConfig from 'app/berry/hooks/useConfig';
import { languages, locales } from 'app/config/translation';
import { setLocale } from 'app/shared/reducers/locale';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Storage, Translate } from 'react-jhipster';

// ==============================|| LOCALIZATION ||============================== //

const LocalizationSection = () => {
  const { borderRadius, locale, onChangeLocale } = useConfig();

  const theme = useTheme();
  const dispatch = useAppDispatch();
  const matchesXs = useMediaQuery(theme.breakpoints.down('md'));

  const [open, setOpen] = useState(false);
  const anchorRef = useRef<any>(null);
  const currentLocale = useAppSelector(state => state.locale.currentLocale);

  // const handleListItemClick = (
  //   event: React.MouseEvent<HTMLAnchorElement> | React.MouseEvent<HTMLDivElement, MouseEvent> | undefined,
  //   lng: string
  // ) => {
  //   setLanguage(lng);
  //   onChangeLocale(lng);
  //   setOpen(false);
  // };
  const handleLocaleChange = (
    event: React.MouseEvent<HTMLAnchorElement> | React.MouseEvent<HTMLDivElement, MouseEvent> | undefined,
    lng: string
  ) => {
    Storage.session.set('locale', lng);
    dispatch(setLocale(lng));
  };

  const handleToggle = () => {
    setOpen(prevOpen => !prevOpen);
  };

  const handleClose = (event: MouseEvent | TouchEvent) => {
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
      <Box
        sx={{
          ml: 2,
          [theme.breakpoints.down('md')]: {
            ml: 1,
          },
        }}
      >
        <Avatar
          variant="rounded"
          sx={{
            ...theme.typography.commonAvatar,
            ...theme.typography.mediumAvatar,
            border: '1px solid',
            borderColor: theme.palette.mode === 'dark' ? theme.palette.dark.main : theme.palette.primary.light,
            background: theme.palette.mode === 'dark' ? theme.palette.dark.main : theme.palette.primary.light,
            color: theme.palette.primary.dark,
            transition: 'all .2s ease-in-out',
            '&[aria-controls="menu-list-grow"],&:hover': {
              borderColor: theme.palette.primary.main,
              background: theme.palette.primary.main,
              color: theme.palette.primary.light,
            },
          }}
          ref={anchorRef}
          aria-controls={open ? 'menu-list-grow' : undefined}
          aria-haspopup="true"
          title="user"
          onClick={handleToggle}
          color="inherit"
        >
          {currentLocale === 'en' && <TranslateTwoToneIcon sx={{ fontSize: '1.3rem' }} />}
          {currentLocale !== 'en' && (
            <Typography variant="h5" sx={{ textTransform: 'uppercase' }} color="inherit">
              {currentLocale}
            </Typography>
          )}
          {/*{language !== 'en' && (*/}
          {/*  <Typography variant="h5" sx={{textTransform: 'uppercase'}} color="inherit">*/}
          {/*    {language}*/}
          {/*  </Typography>*/}
          {/*)}*/}
          {/*{language === 'en' && <TranslateTwoToneIcon sx={{fontSize: '1.3rem'}}/>}*/}
        </Avatar>
      </Box>

      <Popper
        placement={matchesXs ? 'bottom-start' : 'bottom'}
        open={open}
        anchorEl={anchorRef.current}
        role={undefined}
        transition
        disablePortal
        modifiers={[
          {
            name: 'offset',
            options: {
              offset: [matchesXs ? 0 : 0, 20],
            },
          },
        ]}
      >
        {({ TransitionProps }) => (
          <ClickAwayListener onClickAway={handleClose}>
            <Transitions position={matchesXs ? 'top-left' : 'top'} in={open} {...TransitionProps}>
              <Paper elevation={16}>
                {open && (
                  <List
                    component="nav"
                    sx={{
                      width: '100%',
                      minWidth: 200,
                      maxWidth: 280,
                      bgcolor: theme.palette.background.paper,
                      borderRadius: `${borderRadius}px`,
                      [theme.breakpoints.down('md')]: {
                        maxWidth: 250,
                      },
                    }}
                  >
                    {locales.map(locale => (
                      <ListItemButton key={locale} selected={locale === currentLocale} onClick={event => handleLocaleChange(event, locale)}>
                        <ListItemText
                          primary={
                            <Grid container>
                              <Typography color="textPrimary">{languages[locale].name}</Typography>
                              <Typography variant="caption" color="textSecondary" sx={{ ml: '8px' }}>
                                {languages[locale].name}
                              </Typography>
                            </Grid>
                          }
                        />
                      </ListItemButton>
                    ))}
                  </List>
                )}
              </Paper>
            </Transitions>
          </ClickAwayListener>
        )}
      </Popper>
    </>
  );
};

export default LocalizationSection;
