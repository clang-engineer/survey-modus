import React from 'react';

import { Link as RouterLink } from 'react-router-dom';

// material-ui
import { Link } from '@mui/material';

// project imports
import Logo from 'app/berry/ui-component/Logo';

// ==============================|| MAIN LOGO ||============================== //

const LogoSection = () => (
  <Link component={RouterLink} to={'/'} aria-label="theme-logo">
    <Logo />
  </Link>
);

export default LogoSection;
