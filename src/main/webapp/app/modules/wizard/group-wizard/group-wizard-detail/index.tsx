import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getCompanys } from 'app/entities/company/company.reducer';
import { getEntity, reset } from 'app/entities/group/group.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';
import { Theme } from '@mui/material/styles';
import Autocomplete from '@mui/material/Autocomplete';
import { CheckBox, CheckBoxOutlineBlank } from '@mui/icons-material';

import { Button, ButtonGroup, Checkbox, FormControlLabel, Grid, TextField, Typography } from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';
import groupWizardDetailFormik from 'app/modules/wizard/group-wizard/group-wizard-detail/group-wizard-detail.formik';

export const GroupWizardDetail = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const currentUser = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const companys = useAppSelector(state => state.company.entities);
  const groupEntity = useAppSelector(state => state.group.entity);
  const loading = useAppSelector(state => state.group.loading);
  const updating = useAppSelector(state => state.group.updating);
  const updateSuccess = useAppSelector(state => state.group.updateSuccess);

  const formik = groupWizardDetailFormik({
    groupEntity,
    user: currentUser,
    isNew,
    dispatch,
  });

  const handleClose = () => {
    navigate('/group' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCompanys({ query: `userId.equals=${currentUser.id}` }));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  useEffect(() => {
    if (!isNew) {
      formik.setValues({
        ...groupEntity,
        companys: [],
        users: [],
      });
    }
  }, [groupEntity]);

  function getStyles(id: string, idArray: readonly string[], theme: Theme) {
    return {
      fontWeight: idArray?.includes(id) ? theme.typography.fontWeightMedium : theme.typography.fontWeightRegular,
    };
  }

  const MainCardTitle = () => {
    return (
      <Typography variant="h4" id="exformmakerApp.group.home.createOrEditLabel" data-cy="GroupCreateUpdateHeading">
        <Translate contentKey="exformmakerApp.group.home.createOrEditLabel">Create or edit a Group</Translate>
        {!isNew ?? (
          <Typography variant="caption" display="block" gutterBottom>
            `: ${groupEntity.id}`
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
              label={translate('exformmakerApp.group.title')}
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
              label={translate('exformmakerApp.group.description')}
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
              label={translate('exformmakerApp.group.activated')}
            />
          </Grid>
          <Grid item xs={12}>
            <Autocomplete
              multiple
              id="multi-companys"
              disableCloseOnSelect
              options={companys}
              getOptionLabel={option => option.title}
              defaultValue={formik.values.companys}
              renderOption={(props, option, { selected }) => (
                <li {...props}>
                  <Checkbox
                    icon={<CheckBoxOutlineBlank fontSize="small" />}
                    checkedIcon={<CheckBox fontSize="small" />}
                    style={{ marginRight: 8 }}
                    checked={selected}
                  />
                  {option.title}
                </li>
              )}
              renderInput={params => <TextField {...params} label="companys" placeholder="search company" />}
            />
          </Grid>
          <Grid item xs={12}>
            <Autocomplete
              multiple
              id="multi-users"
              disableCloseOnSelect
              options={users}
              getOptionLabel={option => option.login}
              defaultValue={formik.values.users}
              renderOption={(props, option, { selected }) => (
                <li {...props}>
                  <Checkbox
                    icon={<CheckBoxOutlineBlank fontSize="small" />}
                    checkedIcon={<CheckBox fontSize="small" />}
                    style={{ marginRight: 8 }}
                    checked={selected}
                  />
                  {option.login}
                </li>
              )}
              renderInput={params => <TextField {...params} label="users" placeholder="search user" />}
            />
          </Grid>
          <Grid item xs={12}>
            <ButtonGroup size="small">
              <Button id="cancel-save" data-cy="entityCreateCancelButton" onClick={() => navigate('/group')} color="primary">
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

export default GroupWizardDetail;
