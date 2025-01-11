import React, { useEffect } from 'react';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/form/form.reducer';
import { Alert, Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import CheckIcon from '@mui/icons-material/Check';
import FormGridContainer from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-grid-container';
import FormWizardListToolbar from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-list-title';

const FormWizardList = () => {
  const dispatch = useAppDispatch();

  const user = useAppSelector(state => state.authentication.account);
  const formList = useAppSelector(state => state.form.entities);

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
        <FormWizardListToolbar />
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
    </Grid>
  );
};

export default FormWizardList;
