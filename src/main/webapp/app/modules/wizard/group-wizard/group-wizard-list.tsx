import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/group/group.reducer';
import { Alert, Box, Button, ButtonGroup, Grid, IconButton, Tooltip, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { IGroup } from 'app/shared/model/group.model';
import { IconBuildingStore, IconPencil, IconTrash, IconUsers } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import CheckIcon from '@mui/icons-material/Check';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import GroupUserTooltipContent from 'app/modules/wizard/group-wizard/component/group-user-tootip-content';
import GroupCompaniesTooltipContent from 'app/modules/wizard/group-wizard/component/group-companies-tooltip-content';

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
const GroupWizardList = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const theme = useTheme();

  const user = useAppSelector(state => state.authentication.account);
  const groupList = useAppSelector(state => state.group.entities);
  const loading = useAppSelector(state => state.group.loading);

  useEffect(() => {
    getAllEntities();
  }, []);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: 'id,desc',
        query: `userId.equals=${user.id}`,
      })
    );
  };

  const handleSyncList = () => {
    getAllEntities();
  };

  const SubCardTitle = (props: { group: IGroup }) => {
    const { group } = props;

    return (
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Typography variant="h4">{group.title}</Typography>
        <ButtonGroup variant="text" size="small">
          <Button onClick={() => navigate(`/wizard/group/${group.id}/edit`)}>
            <IconPencil size={'1rem'} strokeWidth={1.5} />
          </Button>
          <Button onClick={() => navigate(`/wizard/group/${group.id}/delete`)}>
            <IconTrash size={'1rem'} strokeWidth={1.5} color={theme.palette.error.light} />
          </Button>
        </ButtonGroup>
      </Box>
    );
  };

  return (
    <Grid container spacing={gridSpacing}>
      <Grid item xs={12} id="group-heading" data-cy="GroupHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="exformmakerApp.group.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            variant="contained"
            color="primary"
            size="small"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => navigate('/wizard/group/new')}
          >
            <FontAwesomeIcon icon="plus" /> &nbsp;
            <Translate contentKey="exformmakerApp.group.home.createLabel">Create new Group</Translate>
          </Button>
        </Box>
      </Grid>
      <Grid item xs={12} container spacing={gridSpacing - 1}>
        {groupList && groupList.length > 0
          ? groupList.map((group, i) => (
              <Grid item xs={12} md={6} xl={4} key={i}>
                <SubCard title={<SubCardTitle group={group} />}>
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
              </Grid>
            ))
          : !loading && (
              <Grid item xs={12}>
                <Alert icon={<CheckIcon fontSize="inherit" />} severity="warning">
                  <Translate contentKey="exformmakerApp.group.home.notFound">No Groups found</Translate>
                </Alert>
              </Grid>
            )}
      </Grid>
    </Grid>
  );
};

export default GroupWizardList;
