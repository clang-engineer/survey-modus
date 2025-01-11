import React from 'react';
import { Box, Grid, IconButton, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import { IconBuildingStore, IconUsers } from '@tabler/icons';

import { useTheme } from '@mui/material/styles';
import { useAppSelector } from 'app/config/store';
import GroupUserTooltipContent from 'app/modules/wizard/group-wizard/group-wizard-list/group-user-tootip-content';
import GroupCompaniesTooltipContent from 'app/modules/wizard/group-wizard/group-wizard-list/group-companies-tooltip-content';
import GroupWizardSubcardTitle from 'app/modules/wizard/group-wizard/group-wizard-list/group-wizard-subcard-title';
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

const GroupWizardGridContainer = () => {
  const theme = useTheme();

  const groupList = useAppSelector(state => state.group.entities);

  return (
    <Grid item xs={12} container spacing={gridSpacing - 1}>
      {groupList
        .filter(g => g)
        .sort((a, b) => a.orderNo - b.orderNo)
        .map((group, i) => (
          <WizardStyledGrid item xs={12} md={6} xl={4} key={i} activated={group.activated}>
            <SubCard title={<GroupWizardSubcardTitle group={group} />}>
              <Grid container spacing={gridSpacing}>
                <Grid item xs={12} marginBottom={2}>
                  <Typography variant="body1" color="text.primary">
                    {group.description}
                  </Typography>
                </Grid>
                <Grid item xs={12} display={'flex'} justifyContent={'flex-end'}>
                  <Box display="flex" alignItems="center">
                    <CustomWidthTooltip
                      title={<GroupUserTooltipContent users={group.users} />}
                      sx={{ fontSize: '30' }}
                      slotProps={slotProps}
                    >
                      <IconButton>
                        <IconUsers size={'15px'} strokeWidth={1.2} />
                      </IconButton>
                    </CustomWidthTooltip>
                    <Typography variant="caption" color="text.primary">
                      {group.users?.length ?? 0}
                    </Typography>
                  </Box>
                  &nbsp;&nbsp;
                  <Box display="flex" alignItems="center">
                    <CustomWidthTooltip
                      title={<GroupCompaniesTooltipContent companies={group.companies} />}
                      sx={{ fontSize: '30' }}
                      slotProps={slotProps}
                    >
                      <IconButton>
                        <IconBuildingStore size={'15px'} strokeWidth={1.2} />
                      </IconButton>
                    </CustomWidthTooltip>
                    <Typography variant="caption" color="text.primary">
                      {group.companies?.length ?? 0}
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

export default GroupWizardGridContainer;
