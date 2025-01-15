import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './file.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { Button, ButtonGroup, Grid, Stack, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import { IconArrowBack, IconPencil } from '@tabler/icons';

export const FileDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fileEntity = useAppSelector(state => state.file.entity);
  return (
    <MainCard
      title={
        <Typography variant="h4">
          <Translate contentKey="surveyModusApp.file.detail.title">File</Translate> [<b>{fileEntity.id}</b>]
        </Typography>
      }
    >
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="global.field.id">ID</Translate>
            </Typography>
            <Typography>{fileEntity.id}</Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.file.title">Title</Translate>
            </Typography>
            <Typography> {fileEntity.title} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.file.description">Description</Translate>
            </Typography>
            <Typography> {fileEntity.description} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.file.activated">Activated</Translate>
            </Typography>
            <Typography> {fileEntity.activated ? 'true' : 'false'} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.file.user">User</Translate>
            </Typography>
            <Typography> {fileEntity.user ? fileEntity.user.login : ''} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.file.users">User List</Translate>
            </Typography>
            <Typography>
              {fileEntity.users
                ? fileEntity.users.map((val, index) => (
                    <span key={val.id}>
                      <>{val.login}</>
                      {index === fileEntity.users.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.file.companies">Company List</Translate>
            </Typography>
            <Typography>
              {fileEntity.companies
                ? fileEntity.companies.map((val, index) => (
                    <span key={val.id}>
                      <>{val.title}</>
                      {index === fileEntity.companies.length - 1 ? '' : ', '}
                    </span>
                  ))
                : null}
            </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <ButtonGroup variant="contained" size="small">
            <Button onClick={() => navigate('/entities/file')} data-cy="entityDetailsBackButton">
              <IconArrowBack size={'1rem'} />
              <Translate contentKey="entity.action.back">Back</Translate>
            </Button>
            <Button onClick={() => navigate(`/entities/file/${fileEntity.id}/edit`)} color="secondary">
              <IconPencil size={'1rem'} />
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </Button>
          </ButtonGroup>
        </Grid>
      </Grid>
    </MainCard>
  );
};

export default FileDetail;
