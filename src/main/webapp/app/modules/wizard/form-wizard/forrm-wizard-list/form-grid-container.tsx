import React from 'react';
import { Box, Grid, IconButton, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import FormSubcardTitle from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-subcard-title';
import { IconBook } from '@tabler/icons';

import { useTheme } from '@mui/material/styles';
import { useAppSelector } from 'app/config/store';
import WizardStyledGrid from 'app/modules/wizard/component/wizard-styled-grid';

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
  const theme = useTheme();

  const formList = useAppSelector(state => state.form.entities);

  return (
    <Grid item xs={12} container spacing={gridSpacing - 1}>
      {formList
        .filter(f => f)
        .sort((a, b) => a.orderNo - b.orderNo)
        .map((form, i) => (
          <WizardStyledGrid item xs={12} md={6} xl={4} key={i} activated={form.activated}>
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
          </WizardStyledGrid>
        ))}
    </Grid>
  );
};

export default FormGridContainer;
