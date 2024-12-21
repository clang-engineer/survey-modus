import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/group/group.reducer';
import { Alert, Box, Button, ButtonGroup, Grid, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { IGroup } from 'app/shared/model/group.model';
import { IconPencil, IconTrash, IconUsers, IconBuildingStore } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import CheckIcon from '@mui/icons-material/Check';

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
                      <Typography variant="body1" color="text.primary">
                        <IconUsers size={20} strokeWidth={1.5} /> {group.users?.length ?? 0}
                      </Typography>
                      &nbsp;&nbsp;
                      <Typography variant="body1" color="text.primary">
                        <IconBuildingStore size={20} strokeWidth={1.5} /> {group.groups?.length ?? 0}
                      </Typography>
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
