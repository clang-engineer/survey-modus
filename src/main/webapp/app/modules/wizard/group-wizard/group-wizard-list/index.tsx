import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createAndUpdateEntities, getEntities } from 'app/entities/group/group.reducer';
import { Alert, Grid, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import CheckIcon from '@mui/icons-material/Check';
import GroupWizardGridContainer from 'app/modules/wizard/group-wizard/group-wizard-list/group-wizard-grid-container';
import WizardListToolbar from 'app/modules/wizard/component/wizard-list-title';
import WizardListUpdateModal from 'app/modules/wizard/component/wizard-list-update-modal';

const GroupWizardList = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const user = useAppSelector(state => state.authentication.account);
  const groupList = useAppSelector(state => state.group.entities);
  const loading = useAppSelector(state => state.group.loading);

  const wizardListUpdateModalRef = React.useRef(null);

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
        <WizardListToolbar
          title={
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.group.home.title">Groups</Translate>
            </Typography>
          }
          items={groupList}
          onSyncListClick={getAllEntities}
          onModalOpenClick={() => {
            wizardListUpdateModalRef.current?.open();
          }}
          onAddNewClick={() => {
            navigate('/wizard/form/new');
          }}
          loading={loading}
        />
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
      <WizardListUpdateModal
        ref={wizardListUpdateModalRef}
        items={groupList}
        onSave={items => {
          dispatch(createAndUpdateEntities(items));
        }}
      />
    </Grid>
  );
};

export default GroupWizardList;
