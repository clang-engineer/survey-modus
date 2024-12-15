import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Translate, translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities as getCompanys } from 'app/entities/company/company.reducer';
import { getEntity, reset } from 'app/entities/group/group.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { IconArrowBackUp, IconDeviceFloppy } from '@tabler/icons';

import { Button, ButtonGroup, Checkbox, FormControlLabel, Grid, TextField, Typography } from '@mui/material';
import Loader from 'app/berry/ui-component/Loader';
import { gridSpacing } from 'app/berry/store/constant';
import groupWizardDetailFormik from 'app/modules/wizard/group-wizard/group-wizard-detail/group-wizard-detail.formik';

import { getEntities as getGroupCompanys } from 'app/entities/group-company/group-company.reducer';
import { getEntities as getGroupUsers } from 'app/entities/group-user/group-user.reducer';
import GroupWizardDetailCompanys from 'app/modules/wizard/group-wizard/group-wizard-detail/component/group-wizard-detail.companys';
import GroupWizardDetailUsers from 'app/modules/wizard/group-wizard/group-wizard-detail/component/group-wizard-detail.users';

export const GroupWizardDetail = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const currentUser = useAppSelector(state => state.authentication.account);
  const users = useAppSelector(state => state.userManagement.users);
  const companys = useAppSelector(state => state.company.entities);
  const groupEntity = useAppSelector(state => state.group.entity);
  const groupCompanyList = useAppSelector(state => state.groupCompany.entities);
  const groupUserList = useAppSelector(state => state.groupUser.entities);
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
    dispatch(getGroupCompanys({ query: `groupId.equals=${id}` }));
    dispatch(getGroupUsers({ query: `groupId.equals=${id}` }));
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
        companys: groupCompanyList.filter(gc => companys.some(c => c.id === gc.company.id)).map(gc => gc.company),
        users: groupUserList.filter(gu => users.some(u => u.id === gu.user.id)).map(gu => gu.user),
      });
    }
  }, [groupEntity, companys, groupCompanyList, groupUserList]);

  const MainCardTitle = () => {
    return (
      <Typography variant="h4" id="exformmakerApp.group.home.createOrEditLabel">
        <Translate contentKey="exformmakerApp.group.home.createOrEditLabel">Create or edit a Group</Translate>
        {!isNew && (
          <Typography variant="caption" display="block" gutterBottom>
            {groupEntity.id}
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
            <GroupWizardDetailCompanys formik={formik} companys={companys} />
          </Grid>
          <Grid item xs={12}>
            <GroupWizardDetailUsers formik={formik} users={users} />
          </Grid>
          <Grid item xs={12}>
            <ButtonGroup size="small">
              <Button id="cancel-save" data-cy="entityCreateCancelButton" onClick={() => navigate('/wizard/group')} color="primary">
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
