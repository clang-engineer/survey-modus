import React from 'react';
import { Box, Grid, IconButton, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import CompanySubcardTitle from 'app/modules/wizard/company-wizard/company-wizard-list/company-subcard-title';
import { IconFolders } from '@tabler/icons';

import { useTheme } from '@mui/material/styles';
import { useAppSelector } from 'app/config/store';
import CompanyFormTooltipContent from 'app/modules/wizard/company-wizard/component/company-form-tootip-content';
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

const CompanyGridContainer = () => {
  const theme = useTheme();

  const companyList = useAppSelector(state => state.company.entities);

  return (
    <Grid item xs={12} container spacing={gridSpacing - 1}>
      {companyList
        .filter(f => f)
        .sort((a, b) => a.orderNo - b.orderNo)
        .map((company, i) => (
          <WizardStyledGrid item xs={12} md={6} xl={4} key={i} activated={company.activated}>
            <SubCard title={<CompanySubcardTitle company={company} />}>
              <Grid container spacing={gridSpacing}>
                <Grid item xs={12} marginBottom={2}>
                  <Typography variant="body1" color="text.primary">
                    {company.description}
                  </Typography>
                </Grid>
                <Grid item xs={12} display={'flex'} justifyContent={'flex-end'}>
                  <Box display="flex" alignItems="center">
                    <CustomWidthTooltip
                      title={<CompanyFormTooltipContent forms={company.forms} />}
                      sx={{ fontSize: '30' }}
                      slotProps={slotProps}
                    >
                      <IconButton>
                        <IconFolders size={'15px'} strokeWidth={1.2} />
                      </IconButton>
                    </CustomWidthTooltip>
                    <Typography variant="caption" color="text.primary">
                      {company.forms?.length ?? 0}
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

export default CompanyGridContainer;
