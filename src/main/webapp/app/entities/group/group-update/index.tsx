import React, { useEffect } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from '../group.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';

import { Button, ButtonGroup, Checkbox, FormControlLabel, Grid, TextField, Typography } from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';
import GroupUserMultiselect from 'app/entities/group/group-update/component/group-user-multiselect';
import GroupCompanyMultiselect from 'app/entities/group/group-update/component/group-company-multiselect';
import groupUpdateFormik from 'app/entities/group/group-update/component/group-update.formik';
import GroupOwnerSelect from 'app/entities/group/group-update/component/group-owner-select';

export const GroupUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const location = useLocation();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const user = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const companies = useAppSelector(state => state.company.entities);
  const groupEntity = useAppSelector(state => state.group.entity);
  const loading = useAppSelector(state => state.group.loading);
  const updating = useAppSelector(state => state.group.updating);
  const updateSuccess = useAppSelector(state => state.group.updateSuccess);

  const fromWizardPath = location.pathname.includes('wizard');
  const fromAdminPath = !fromWizardPath;
  const LIST_PATH = fromAdminPath ? '/entities/group' : '/wizard/group';

  const handleClose = () => {
    navigate(LIST_PATH + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    if (fromAdminPath) {
      dispatch(getCompanies({}));
    } else {
      dispatch(getCompanies({ sort: 'id,asc', query: `userId.equals=${user.id}` }));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew) {
      formik.setValues(groupEntity);
    }

    if (fromWizardPath) {
      formik.setFieldValue('user', user);
    }
  }, [groupEntity]);

  const saveEntity = values => {
    const entity = {
      ...groupEntity,
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
      <Typography variant="h4" id="surveyModusApp.group.home.createOrEditLabel" data-cy="GroupCreateUpdateHeading" gutterBottom>
        <Translate contentKey="surveyModusApp.group.home.createOrEditLabel">Create or edit a Group </Translate> &nbsp;
        {!isNew && `(ID: ${groupEntity.id})`}
      </Typography>
    );
  };

  const formik = groupUpdateFormik({ saveEntity });

  return (
    <MainCard title={<MainCardTitle />}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={gridSpacing}>
          <Grid item xs={12}>
            {!isNew ? (
              <TextField
                fullWidth
                id="group-id"
                name="id"
                label={translate('global.field.id')}
                value={groupEntity.id}
                onChange={formik.handleChange}
                disabled
                variant="outlined"
              />
            ) : null}
          </Grid>
          <Grid item xs={12}>
            <TextField
              fullWidth
              id="group-title"
              name="title"
              label={translate('surveyModusApp.group.title')}
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
              id="group-description"
              name="description"
              label={translate('surveyModusApp.group.description')}
              value={formik.values.description}
              onChange={formik.handleChange}
              error={formik.touched.description && Boolean(formik.errors.description)}
              helperText={formik.touched.description && formik.errors.description}
              variant="outlined"
            />
          </Grid>
          <Grid item xs={12}>
            <FormControlLabel
              control={<Checkbox id="group-activated" name="activated" checked={formik.values.activated} onChange={formik.handleChange} />}
              label={translate('surveyModusApp.group.activated')}
            />
          </Grid>
          {fromAdminPath && (
            <Grid item xs={12}>
              <GroupOwnerSelect formik={formik} users={users} />
            </Grid>
          )}
          <Grid item xs={12}>
            <GroupUserMultiselect formik={formik} users={users} />
          </Grid>
          <Grid item xs={12}>
            <GroupCompanyMultiselect formik={formik} companies={companies} />
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

export default GroupUpdate;
