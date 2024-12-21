import React from 'react';
import { Box, Grid, Typography } from '@mui/material';
import { IconClipboardData } from '@tabler/icons';
import { IForm } from 'app/shared/model/form.model';

const GroupFormsTooltipContent = (props: { forms: IForm[] }) => {
  const { forms } = props;

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Typography variant="h5" color="text.primary" sx={{ textDecoration: 'underline' }}>
          Forms
        </Typography>
      </Grid>
      <Grid item xs={12}>
        {forms.length == 0 ? (
          <Box display="flex" alignItems="center" marginBottom={1}>
            <Typography variant="body2" color="text.primary">
              No forms
            </Typography>
          </Box>
        ) : (
          forms.map((form, i) => (
            <Box key={i} display="flex" alignItems="center" marginBottom={1}>
              <Typography variant="body2" color="text.primary">
                <IconClipboardData size={'1rem'} strokeWidth={1.5} /> &nbsp;
                {form.title}
              </Typography>
            </Box>
          ))
        )}
      </Grid>
    </Grid>
  );
};

export default GroupFormsTooltipContent;
