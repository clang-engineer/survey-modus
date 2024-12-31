import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/form/form.reducer';
import { Alert, Box, Button, Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import CheckIcon from '@mui/icons-material/Check';
import FormGridContainer from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-grid-container';

const FormWizardList = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const user = useAppSelector(state => state.authentication.account);
  const formList = useAppSelector(state => state.form.entities);
  const loading = useAppSelector(state => state.form.loading);

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
      <Grid item xs={12} id="form-heading" data-cy="GroupHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="surveyModusApp.form.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            variant="contained"
            color="primary"
            size="small"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => navigate('/wizard/form/new')}
          >
            <FontAwesomeIcon icon="plus" /> &nbsp;
            <Translate contentKey="surveyModusApp.form.home.createLabel">Create new Group</Translate>
          </Button>
        </Box>
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
