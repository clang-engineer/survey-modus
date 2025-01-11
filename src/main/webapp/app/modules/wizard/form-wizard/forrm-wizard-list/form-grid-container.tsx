import React from 'react';
import { Box, Grid, IconButton, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import FormSubcardTitle from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-subcard-title';
import { IconBook } from '@tabler/icons';
import useFormWizardConfig from 'app/modules/wizard/form-wizard/form-wizard.config';

const slotProps = {
  tooltip: {
    sx: {
      color: '#514E6A',
      backgroundColor: '#ffff',
      padding: '10px',
      boxShadow: '0px 4px 16px rgba(0, 0, 0, 0.08)',
    },
  },
};

const FormGridContainer = () => {
  const { items, setItems } = useFormWizardConfig();

  return (
    <Grid item xs={12} container spacing={gridSpacing - 1}>
      {items.map((form, i) => (
        <Grid item xs={12} md={6} xl={4} key={i}>
          <SubCard title={<FormSubcardTitle form={form} />}>
            <Grid container spacing={gridSpacing}>
              <Grid item xs={12} marginBottom={2}>
                <Typography variant="body1" color="text.primary">
                  {form.description}
                </Typography>
              </Grid>
              <Grid item xs={12} display={'flex'} justifyContent={'flex-end'}>
                <Box display="flex" alignItems="center">
                  <CustomWidthTooltip
                    title={
                      <>
                        {form.category.title} <br />
                        {form.category.description}
                      </>
                    }
                    sx={{ fontSize: '30' }}
                    slotProps={slotProps}
                  >
                    <IconButton>
                      <IconBook size={'15px'} strokeWidth={1.2} />
                    </IconButton>
                  </CustomWidthTooltip>
                  <Typography variant="caption" color="text.primary">
                    {form.category.title}
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </SubCard>
        </Grid>
      ))}
    </Grid>
  );
};

export default FormGridContainer;
