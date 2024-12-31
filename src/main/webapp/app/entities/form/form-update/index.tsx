import React, { useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from '../form.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';
import { getEntities as getCategoryList } from 'app/entities/category/category.reducer';

import { Button, ButtonGroup, Checkbox, FormControlLabel, Grid, TextField, Typography } from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';
import formUpdateFormik from 'app/entities/form/form-update/component/form-update.formik';
import FormOwnerSelect from 'app/entities/form/form-update/component/form-owner-select';
import FormCategorySelect from 'app/entities/form/form-update/component/form-category-select';

export const FormUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const user = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const formEntity = useAppSelector(state => state.form.entity);
  const loading = useAppSelector(state => state.form.loading);
  const updating = useAppSelector(state => state.form.updating);
  const updateSuccess = useAppSelector(state => state.form.updateSuccess);
  const categories = useAppSelector(state => state.category.entities);

  const fromWizardPath = location.pathname.includes('wizard');
  const fromAdminPath = !fromWizardPath;
  const LIST_PATH = fromAdminPath ? '/entities/form' : '/wizard/form';

  const handleClose = () => {
    navigate(LIST_PATH + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCategoryList({}));

    if (fromAdminPath) {
      dispatch(getUsers({}));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew) {
      formik.setValues(formEntity);
    }

    if (fromWizardPath) {
      formik.setFieldValue('user', user);
    }
  }, [formEntity]);

  const saveEntity = values => {
    const entity = {
      ...formEntity,
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
      <Typography variant="h4" id="surveyModusApp.form.home.createOrEditLabel" data-cy="FormCreateUpdateHeading" gutterBottom>
        <Translate contentKey="surveyModusApp.form.home.createOrEditLabel">Create or edit a Form </Translate> &nbsp;
        {!isNew && `(ID: ${formEntity.id})`}
      </Typography>
    );
  };

  const formik = formUpdateFormik({ saveEntity });

  return (
    <MainCard title={<MainCardTitle />}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={gridSpacing}>
          <Grid item xs={12}>
            {!isNew ? (
              <TextField
                fullWidth
                id="form-id"
                name="id"
                label={translate('global.field.id')}
                value={formEntity.id}
                onChange={formik.handleChange}
                disabled
                variant="outlined"
              />
            ) : null}
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="form-title"
              name="title"
              label={translate('surveyModusApp.form.title')}
              value={formik.values.title}
              onChange={formik.handleChange}
              error={formik.touched.title && Boolean(formik.errors.title)}
              helperText={formik.touched.title && formik.errors.title}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="form-description"
              name="description"
              label={translate('surveyModusApp.form.description')}
              value={formik.values.description}
              onChange={formik.handleChange}
              error={formik.touched.description && Boolean(formik.errors.description)}
              helperText={formik.touched.description && formik.errors.description}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <FormControlLabel
              control={<Checkbox id="form-activated" name="activated" checked={formik.values.activated} onChange={formik.handleChange} />}
              label={translate('surveyModusApp.form.activated')}
            />
          </Grid>
          {fromAdminPath && (
            <Grid item xs={12}>
              <FormOwnerSelect formik={formik} users={users} />
            </Grid>
          )}
          <Grid item xs={12}>
            <FormCategorySelect formik={formik} categories={categories} />
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

export default FormUpdate;
