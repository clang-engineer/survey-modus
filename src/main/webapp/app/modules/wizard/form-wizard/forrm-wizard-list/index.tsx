import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/form/form.reducer';
import { Alert, Box, Button, ButtonGroup, Grid, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import CheckIcon from '@mui/icons-material/Check';
import FormGridContainer from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-grid-container';
import useFormWizardConfig from 'app/modules/wizard/form-wizard/form-wizard.config';

import { IconAlignBoxLeftTop } from '@tabler/icons';

import { useTheme } from '@mui/material/styles';
import useConfig from 'app/berry/hooks/useConfig';
import FormWizardListToolbar from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-list-title';

const FormWizardList = () => {
  const dispatch = useAppDispatch();

  const { setItems } = useFormWizardConfig();

  const user = useAppSelector(state => state.authentication.account);
  const formList = useAppSelector(state => state.form.entities);

  useEffect(() => {
    getAllEntities();
  }, []);

  useEffect(() => {
    setItems(formList.filter(f => f).sort((a, b) => a.orderNo - b.orderNo));
  }, [formList]);

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
