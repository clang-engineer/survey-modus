import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/group/group.reducer';
import { Alert, Box, Button, Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import { useTheme } from '@mui/material/styles';
import CheckIcon from '@mui/icons-material/Check';
import GroupWizardGridContainer from 'app/modules/wizard/group-wizard/group-wizard-list/group-wizard-grid-container';

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

  return (
    <Grid container spacing={gridSpacing}>
      <Grid item xs={12} id="group-heading" data-cy="GroupHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="surveyModusApp.group.home.refreshListLabel">Refresh List</Translate>
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
            <Translate contentKey="surveyModusApp.group.home.createLabel">Create new Group</Translate>
          </Button>
        </Box>
      </Grid>
      <Grid item xs={12} container spacing={gridSpacing - 1}>
        {groupList && groupList.length > 0 ? (
          <GroupWizardGridContainer />
        ) : (
          <Grid item xs={12}>
            <Alert icon={<CheckIcon fontSize="inherit" />} severity="warning">
              <Translate contentKey="surveyModusApp.group.home.notFound">No Groups found</Translate>
            </Alert>
          </Grid>
        )}
      </Grid>
    </Grid>
  );
};

export default GroupWizardList;
