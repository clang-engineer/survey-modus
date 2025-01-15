import React, { useEffect } from 'react';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createAndUpdateEntities, getEntities } from 'app/entities/form/form.reducer';
import { Alert, Box, Grid, IconButton, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import CheckIcon from '@mui/icons-material/Check';
import FormGridContainer from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-grid-container';
import WizardListToolbar from 'app/modules/wizard/component/wizard-list-title';
import { useNavigate } from 'react-router-dom';
import WizardListUpdateModal from 'app/modules/wizard/component/wizard-list-update-modal';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

const FormWizardList = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const user = useAppSelector(state => state.authentication.account);
  const formList = useAppSelector(state => state.form.entities);
  const loading = useAppSelector(state => state.form.loading);

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

  return (
    <Grid container spacing={gridSpacing}>
      <Grid item xs={12} id="form-heading" data-cy="GroupHeading">
        <WizardListToolbar
          title={
            <Box display="flex" justifyContent="space-between" alignItems="center">
              <IconButton
                size="small"
                color="primary"
                onClick={() => {
                  wizardListUpdateModalRef.current?.open();
                }}
                disabled={loading}
              >
                <FontAwesomeIcon icon="cog" size="sm" />
              </IconButton>{' '}
              &nbsp;
              <Typography variant="h4">
                <Translate contentKey="surveyModusApp.form.home.title">Forms</Translate>
              </Typography>
            </Box>
          }
          items={formList}
          onSyncListClick={getAllEntities}
          onAddNewClick={() => {
            navigate('/wizard/form/new');
          }}
          loading={loading}
        />
      </Grid>
      <Grid item xs={12}>
        {formList && formList.length > 0 ? (
          <FormGridContainer />
        ) : (
          <Grid item xs={12}>
            <Alert icon={<CheckIcon fontSize="inherit" />} severity="warning">
              <Translate contentKey="surveyModusApp.form.home.notFound">No Groups found</Translate>
            </Alert>
          </Grid>
        )}
      </Grid>
      <WizardListUpdateModal
        ref={wizardListUpdateModalRef}
        items={formList}
        onSave={items => {
          dispatch(createAndUpdateEntities(items));
        }}
      />
    </Grid>
  );
};

export default FormWizardList;
