import React, { useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createEntity, getEntity, reset, updateEntity } from './file.reducer';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';

import { Button, ButtonGroup, Checkbox, FormControlLabel, Grid, TextField, Typography } from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';

import { useFormik } from 'formik';
import * as yup from 'yup';
import MainCard from 'app/berry/ui-component/cards/MainCard';

export const FileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const fileEntity = useAppSelector(state => state.file.entity);
  const loading = useAppSelector(state => state.file.loading);
  const updating = useAppSelector(state => state.file.updating);
  const updateSuccess = useAppSelector(state => state.file.updateSuccess);

  const handleClose = () => {
    navigate('/entities/file' + location.search);
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
      formik.setValues(fileEntity);
    }
  }, [fileEntity]);

  const saveEntity = values => {
    const entity = {
      ...fileEntity,
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
      <Typography variant="h4" id="surveyModusApp.file.home.createOrEditLabel" data-cy="FileCreateUpdateHeading" gutterBottom>
        <Translate contentKey="surveyModusApp.file.home.createOrEditLabel">Create or edit a File </Translate> &nbsp;
        {!isNew && `(ID: ${fileEntity.id})`}
      </Typography>
    );
  };

  const formik = useFormik({
    initialValues: {
      id: null,
      filename: '',
      filepath: '',
      hashKey: false,
    },
    validationSchema: yup.object({
      id: yup.number().nullable(true),
      filename: yup
        .string()
        .max(100, translate('entity.validation.maxlength', { max: 100 }))
        .required(translate('entity.validation.required')),
      filepath: yup.string(),
      hashKey: yup.boolean().required(translate('entity.validation.hashKey')),
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
                id="file-id"
                name="id"
                label={translate('global.field.id')}
                value={fileEntity.id}
                onChange={formik.handleChange}
                disabled
                variant="outlined"
              />
            ) : null}
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="file-filename"
              name="filename"
              label={translate('surveyModusApp.file.filename')}
              value={formik.values.filename}
              onChange={formik.handleChange}
              error={formik.touched.filename && Boolean(formik.errors.filename)}
              helperText={formik.touched.filename && formik.errors.filename}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="file-filepath"
              name="filepath"
              label={translate('surveyModusApp.file.filepath')}
              value={formik.values.filepath}
              onChange={formik.handleChange}
              error={formik.touched.filepath && Boolean(formik.errors.filepath)}
              helperText={formik.touched.filepath && formik.errors.filepath}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="file-hashKey"
              name="hashKey"
              label={translate('surveyModusApp.file.hashKey')}
              value={formik.values.hashKey}
              onChange={formik.handleChange}
              error={formik.touched.hashKey && Boolean(formik.errors.hashKey)}
              helperText={formik.touched.hashKey && formik.errors.hashKey}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <ButtonGroup size="small">
              <Button id="cancel-save" data-cy="entityCreateCancelButton" onClick={() => navigate('/entities/file')} color="primary">
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

export default FileUpdate;
