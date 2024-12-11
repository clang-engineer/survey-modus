import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './group.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { Button, ButtonGroup, Grid, Stack, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import { IconArrowBack, IconPencil } from '@tabler/icons';

export const GroupDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const groupEntity = useAppSelector(state => state.group.entity);
  return (
    <MainCard
      title={
        <Typography variant="h4">
          <Translate contentKey="exformmakerApp.group.detail.title">Group</Translate> [<b>{groupEntity.id}</b>]
        </Typography>
      }
    >
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="global.field.id">ID</Translate>
            </Typography>
            <Typography>{groupEntity.id}</Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.group.title">Title</Translate>
            </Typography>
            <Typography> {groupEntity.title} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.group.description">Description</Translate>
            </Typography>
            <Typography> {groupEntity.description} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.group.activated">Activated</Translate>
            </Typography>
            <Typography> {groupEntity.activated ? 'true' : 'false'} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.group.user">User</Translate>
            </Typography>
            <Typography> {groupEntity.user ? groupEntity.user.login : ''} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <ButtonGroup variant="contained" size="small">
            <Button onClick={() => navigate('/group')} data-cy="entityDetailsBackButton">
              <IconArrowBack size={'1rem'} />
              <Translate contentKey="entity.action.back">Back</Translate>
            </Button>
            <Button onClick={() => navigate(`/group/${groupEntity.id}/edit`)} color="secondary">
              <IconPencil size={'1rem'} />
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </Button>
          </ButtonGroup>
        </Grid>
      </Grid>
    </MainCard>
  );
};

export default GroupDetail;
