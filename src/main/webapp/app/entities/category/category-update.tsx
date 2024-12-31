import React, { useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, reset, updateEntity } from './category.reducer';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';

import { Button, ButtonGroup, Checkbox, FormControlLabel, Grid, TextField, Typography } from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';

import { useFormik } from 'formik';
import * as yup from 'yup';
import MainCard from 'app/berry/ui-component/cards/MainCard';

export const CategoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const user = useAppSelector(state => state.authentication.account);
  const categoryEntity = useAppSelector(state => state.category.entity);
  const loading = useAppSelector(state => state.category.loading);
  const updating = useAppSelector(state => state.category.updating);
  const updateSuccess = useAppSelector(state => state.category.updateSuccess);

  const handleClose = () => {
    navigate('/entities/category' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew) {
      formik.setValues(categoryEntity);
    }
  }, [categoryEntity]);

  const saveEntity = values => {
    const entity = {
      ...categoryEntity,
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
      <Typography variant="h4" id="surveyModusApp.category.home.createOrEditLabel" data-cy="CategoryCreateUpdateHeading" gutterBottom>
        <Translate contentKey="surveyModusApp.category.home.createOrEditLabel">Create or edit a Category </Translate> &nbsp;
        {!isNew && `(ID: ${categoryEntity.id})`}
      </Typography>
    );
  };

  const formik = useFormik({
    initialValues: {
      id: null,
      title: '',
      description: '',
      activated: false,
    },
    validationSchema: yup.object({
      id: yup.number().nullable(true),
      title: yup
        .string()
        .min(5, translate('entity.validation.minlength', { min: 5 }))
        .max(100, translate('entity.validation.maxlength', { max: 100 }))
        .required(translate('entity.validation.required')),
      description: yup.string(),
      activated: yup.boolean().required(translate('entity.validation.required')),
    }),
    onSubmit(values) {
      saveEntity(values);
    },
  });

  return (
    <MainCard title={<MainCardTitle />}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={gridSpacing}>
          <Grid item xs={12}>
            {!isNew ? (
              <TextField
                fullWidth
                id="category-id"
                name="id"
                label={translate('global.field.id')}
                value={categoryEntity.id}
                onChange={formik.handleChange}
                disabled
                variant="outlined"
              />
            ) : null}
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="category-title"
              name="title"
              label={translate('surveyModusApp.category.title')}
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
              id="category-description"
              name="description"
              label={translate('surveyModusApp.category.description')}
              value={formik.values.description}
              onChange={formik.handleChange}
              error={formik.touched.description && Boolean(formik.errors.description)}
              helperText={formik.touched.description && formik.errors.description}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <FormControlLabel
              control={
                <Checkbox id="category-activated" name="activated" checked={formik.values.activated} onChange={formik.handleChange} />
              }
              label={translate('surveyModusApp.category.activated')}
            />
          </Grid>
          <Grid item xs={12}>
            <ButtonGroup size="small">
              <Button id="cancel-save" data-cy="entityCreateCancelButton" onClick={() => navigate('/entities/category')} color="primary">
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

export default CategoryUpdate;
