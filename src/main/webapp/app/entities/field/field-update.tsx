import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getForms } from 'app/entities/form/form.reducer';
import { createEntity, getEntity, reset, updateEntity } from './field.reducer';

import * as yup from 'yup';
import { useFormik } from 'formik';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';

import {
  Button,
  ButtonGroup,
  Checkbox,
  FormControl,
  FormControlLabel,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';
import { IField } from 'app/shared/model/field.model';
import FieldAttributeUpdate from 'app/entities/field/component/field-attribute-update';
import type from 'app/shared/model/enumerations/type.model';
import FieldDisplayUpdate from 'app/entities/field/component/field-display-update';

export const FieldUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const forms = useAppSelector(state => state.form.entities);
  const fieldEntity = useAppSelector(state => state.field.entity);
  const loading = useAppSelector(state => state.field.loading);
  const updating = useAppSelector(state => state.field.updating);
  const updateSuccess = useAppSelector(state => state.field.updateSuccess);

  const formik = useFormik<IField>({
    initialValues: {
      id: 0,
      title: '',
      description: '',
      activated: false,
      form: { id: 0 },
      attribute: {
        type: type.TEXT,
        defaultValue: '',
      },
      display: {
        orderNo: 0,
      },
    },
    validationSchema: yup.object({
      id: yup.string(),
      title: yup
        .string()
        .min(5, translate('entity.validation.minlength', { min: 5 }))
        .max(100, translate('entity.validation.maxlength', { max: 100 }))
        .required(translate('entity.validation.required')),
      description: yup.string(),
      activated: yup.boolean().required(translate('entity.validation.required')),
      form: yup.object({
        id: yup.number().required(translate('entity.validation.required')),
      }),
      attribute: yup.object({}),
      display: yup.object({}),
    }),
    onSubmit(values) {
      saveEntity(values);
    },
  });

  const handleClose = () => {
    navigate('/field' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getForms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew) {
      formik.setValues(fieldEntity);
    }
  }, [fieldEntity]);

  const saveEntity = values => {
    const entity = {
      ...fieldEntity,
      ...values,
      // form: forms.find(it => it.id.toString() === values.form.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const MainCardTitle = () => {
    return (
      <Typography variant="h4" id="exformmakerApp.field.home.createOrEditLabel" data-cy="FieldCreateUpdateHeading">
        <Translate contentKey="exformmakerApp.field.home.createOrEditLabel">Create or edit a Field</Translate>
        {!isNew ?? (
          <Typography variant="caption" display="block" gutterBottom>
            `: ${fieldEntity.id}`
          </Typography>
        )}
      </Typography>
    );
  };

  return (
    <MainCard title={<MainCardTitle />}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={gridSpacing}>
          <Grid item xs={12}>
            {!isNew ? (
              <TextField
                fullWidth
                id="field-id"
                name="id"
                label={translate('global.field.id')}
                value={fieldEntity.id}
                onChange={formik.handleChange}
                disabled
                variant="outlined"
              />
            ) : null}
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="field-title"
              name="title"
              label={translate('exformmakerApp.field.title')}
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
              id="field-description"
              name="description"
              label={translate('exformmakerApp.field.description')}
              value={formik.values.description}
              onChange={formik.handleChange}
              error={formik.touched.description && Boolean(formik.errors.description)}
              helperText={formik.touched.description && formik.errors.description}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <FormControlLabel
              control={<Checkbox id="field-activated" name="activated" checked={formik.values.activated} onChange={formik.handleChange} />}
              label={translate('exformmakerApp.field.activated')}
            />
          </Grid>
          <Grid item xs={12}>
            <FormControl component="fieldset" fullWidth error={formik.touched.form && Boolean(formik.errors.form)} variant="outlined">
              <InputLabel id="field-form-label"> {translate('exformmakerApp.field.form')}</InputLabel>
              <Select
                labelId="field-form-label"
                id="field-form"
                name="form"
                value={formik.values.form?.id}
                onChange={e => {
                  formik.setFieldValue('form', { id: e.target.value });
                }}
                label={translate('exformmakerApp.field.form')}
              >
                <MenuItem value="-" disabled>
                  <em>None</em>
                </MenuItem>
                {forms.map(form => (
                  <MenuItem key={form.id} value={form.id}>
                    {form.title}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <FieldAttributeUpdate formik={formik} />
          </Grid>
          <Grid item xs={12}>
            <FieldDisplayUpdate formik={formik} />
          </Grid>
          <Grid item xs={12}>
            <ButtonGroup size="small">
              <Button id="cancel-save" data-cy="entityCreateCancelButton" onClick={() => navigate('/field')} color="primary">
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

export default FieldUpdate;
