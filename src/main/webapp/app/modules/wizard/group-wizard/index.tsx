import React from 'react';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

const GroupWizard = () => {
  return (
    <MainCard title="Group Wizard">
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12}></Grid>
      </Grid>
    </MainCard>
  );
};

export default GroupWizard;
