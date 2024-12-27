import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './company.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { Button, ButtonGroup, Grid, Stack, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import { IconArrowBack, IconPencil } from '@tabler/icons';

export const CompanyDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companyEntity = useAppSelector(state => state.company.entity);
  return (
    <MainCard
      title={
        <Typography variant="h4">
          <Translate contentKey="surveymodusApp.company.detail.title">Company</Translate> [<b>{companyEntity.id}</b>]
        </Typography>
      }
    >
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="global.field.id">ID</Translate>
            </Typography>
            <Typography>{companyEntity.id}</Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveymodusApp.company.title">Title</Translate>
            </Typography>
            <Typography> {companyEntity.title} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveymodusApp.company.description">Description</Translate>
            </Typography>
            <Typography> {companyEntity.description} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveymodusApp.company.activated">Activated</Translate>
            </Typography>
            <Typography> {companyEntity.activated ? 'true' : 'false'} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveymodusApp.company.user">User</Translate>
            </Typography>
            <Typography> {companyEntity.user ? companyEntity.user.login : ''} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveymodusApp.company.forms">Form List</Translate>
            </Typography>
            <Typography>
              {companyEntity.forms
                ? companyEntity.forms.map((val, index) => (
                    <span key={val.id}>
                      <>{val.title}</>
                      {index === companyEntity.forms.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveymodusApp.company.staffs">Staff List</Translate>
            </Typography>
            <Typography>
              {companyEntity.staffs
                ? companyEntity.staffs.map((val, index) => (
                    <span key={val.id}>
                      <>{val.name}</>
                      {index === companyEntity.staffs.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <ButtonGroup variant="contained" size="small">
            <Button onClick={() => navigate('/company')} data-cy="entityDetailsBackButton">
              <IconArrowBack size={'1rem'} />
              <Translate contentKey="entity.action.back">Back</Translate>
            </Button>
            <Button onClick={() => navigate(`/company/${companyEntity.id}/edit`)} color="secondary">
              <IconPencil size={'1rem'} />
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </Button>
          </ButtonGroup>
        </Grid>
      </Grid>
    </MainCard>
  );
};

export default CompanyDetail;
