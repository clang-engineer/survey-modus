import React from 'react';

import { Link as RouterLink } from 'react-router-dom';

// material-ui
import { Link } from '@mui/material';

// project imports
import { DASHBOARD_PATH, STAFF_HOME_PATH } from 'app/berry/config';
import Logo from 'app/berry/ui-component/Logo';

// ==============================|| MAIN LOGO ||============================== //

const LogoSection = () => (
  <Link component={RouterLink} to={STAFF_HOME_PATH} aria-label="theme-logo">
    <Logo />
  </Link>
);

export default LogoSection;
