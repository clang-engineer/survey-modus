import React, { useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntities, getEntity, reset, updateEntity } from '../company.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';
import { getEntities as getForms } from 'app/entities/form/form.reducer';

import { Button, ButtonGroup, Checkbox, Divider, FormControlLabel, Grid, TextField, Typography } from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';
import CompanyFormMultiselect from 'app/entities/company/company-update/company-form-multiselect';
import companyUpdateFormik from 'app/entities/company/company-update/company-update.formik';
import StaffCardList from 'app/entities/company/company-update/staff-card-list';
import FormikErrorText from 'app/shared/component/formik-error-text';
import CompanyOwnerSelect from 'app/entities/company/company-update/company-owner-select';

export const CompanyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const user = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const forms = useAppSelector(state => state.form.entities);
  const companyEntity = useAppSelector(state => state.company.entity);
  const loading = useAppSelector(state => state.company.loading);
  const updating = useAppSelector(state => state.company.updating);
  const updateSuccess = useAppSelector(state => state.company.updateSuccess);

  const fromWizardPath = location.pathname.includes('wizard');
  const fromAdminPath = !fromWizardPath;
  const LIST_PATH = fromAdminPath ? '/company' : '/wizard/company';

  const handleClose = () => {
    navigate(LIST_PATH + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    if (fromAdminPath) {
      dispatch(getUsers({}));
      dispatch(getForms({}));
    } else {
      dispatch(getForms({ sort: 'id,asc', query: `userId.equals=${user.id}` }));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew) {
      formik.setValues(companyEntity);
    }

    if (fromWizardPath) {
      formik.setFieldValue('user', user);
    }
  }, [companyEntity]);

  const saveEntity = values => {
    const entity = {
      ...companyEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const MainCardTitle = () => {
    return (
      <Typography variant="h4" id="surveymodusApp.company.home.createOrEditLabel" data-cy="CompanyCreateUpdateHeading" gutterBottom>
        <Translate contentKey="surveymodusApp.company.home.createOrEditLabel">Create or edit a Company </Translate> &nbsp;
        {!isNew && `(ID: ${companyEntity.id})`}
      </Typography>
    );
  };

  const formik = companyUpdateFormik({ saveEntity });

  return (
    <MainCard title={<MainCardTitle />}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={gridSpacing}>
          <Grid item xs={12}>
            {!isNew ? (
              <TextField
                fullWidth
                id="company-id"
                name="id"
                label={translate('global.field.id')}
                value={companyEntity.id}
                onChange={formik.handleChange}
                disabled
                variant="outlined"
              />
            ) : null}
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="company-title"
              name="title"
              label={translate('surveymodusApp.company.title')}
              value={formik.values.title}
              onChange={formik.handleChange}
              error={formik.touched.title && Boolean(formik.errors.title)}
              variant="outlined"
            />
            <FormikErrorText formik={formik} fieldName={'title'} />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="company-description"
              name="description"
              label={translate('surveymodusApp.company.description')}
              value={formik.values.description}
              onChange={formik.handleChange}
              error={formik.touched.description && Boolean(formik.errors.description)}
              variant="outlined"
            />
            <FormikErrorText formik={formik} fieldName={'description'} />
          </Grid>
          <Grid item xs={12}>
            <FormControlLabel
              control={
                <Checkbox id="company-activated" name="activated" checked={formik.values.activated} onChange={formik.handleChange} />
              }
              label={translate('surveymodusApp.company.activated')}
            />
            <FormikErrorText formik={formik} fieldName={'activated'} />
          </Grid>
          {fromAdminPath && (
            <Grid item xs={12}>
              <CompanyOwnerSelect formik={formik} users={users} />
              <FormikErrorText formik={formik} fieldName={'user'} />
            </Grid>
          )}
          <Grid item xs={12}>
            <CompanyFormMultiselect formik={formik} forms={forms} />
            <FormikErrorText formik={formik} fieldName={'forms'} />
          </Grid>
          <Grid item xs={12} lg={12}>
            <Divider sx={{ borderStyle: 'dashed' }} />
          </Grid>
          <Grid item xs={12}>
            <StaffCardList formik={formik} />
            <FormikErrorText formik={formik} fieldName={'staffs'} />
          </Grid>
          <Grid item xs={12} lg={12}>
            <Divider sx={{ borderStyle: 'dashed' }} />
          </Grid>
          <Grid item xs={12}>
            <ButtonGroup size="small">
              <Button id="cancel-save" data-cy="entityCreateCancelButton" onClick={() => navigate(LIST_PATH)} color="primary">
                <IconArrowBackUp size={'1rem'} />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="secondary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <IconDeviceFloppy size={'1rem'} /> &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ButtonGroup>
          </Grid>
        </Grid>
      </form>
      {loading ?? <Loader />}
    </MainCard>
  );
};

export default CompanyUpdate;
