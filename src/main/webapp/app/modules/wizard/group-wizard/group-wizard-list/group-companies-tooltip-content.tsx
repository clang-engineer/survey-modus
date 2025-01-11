import React from 'react';
import { Box, Grid, Typography } from '@mui/material';
import { IconBuildingStore } from '@tabler/icons';
import { ICompany } from 'app/shared/model/company.model';

const GroupCompaniesTooltipContent = (props: { companies: ICompany[] }) => {
  const { companies } = props;

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Typography variant="h5" color="text.primary" sx={{ textDecoration: 'underline' }}>
          Companies
        </Typography>
      </Grid>
      <Grid item xs={12}>
        {companies.length === 0 ? (
          <Box display="flex" alignItems="center" marginBottom={1}>
            <Typography variant="body2" color="text.primary">
              No companies
            </Typography>
          </Box>
        ) : (
          companies.map((company, i) => (
            <Box key={i} display="flex" alignItems="center" marginBottom={1}>
              <Typography variant="body2" color="text.primary">
                <IconBuildingStore size={'1rem'} strokeWidth={1.5} /> &nbsp;
                {company.title}
              </Typography>
            </Box>
          ))
        )}
      </Grid>
    </Grid>
  );
};

export default GroupCompaniesTooltipContent;
