import React, { useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './field.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { Box, Button, ButtonGroup, Grid, Stack, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import { IconArrowBack, IconPencil } from '@tabler/icons';

export const FieldDetail = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fieldEntity = useAppSelector(state => state.field.entity);
  return (
    <MainCard
      title={
        <Typography variant="h4">
          <Translate contentKey="exformmakerApp.field.detail.title">Field</Translate> [<b>{fieldEntity.id}</b>]
        </Typography>
      }
    >
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="global.field.id">ID</Translate>
            </Typography>
            <Typography>{fieldEntity.id}</Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.field.title">Title</Translate>
            </Typography>
            <Typography> {fieldEntity.title} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.field.description">Description</Translate>
            </Typography>
            <Typography> {fieldEntity.description} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.field.activated">Activated</Translate>
            </Typography>
            <Typography> {fieldEntity.activated ? 'true' : 'false'} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.field.form">Form</Translate>
            </Typography>
            <Typography> {fieldEntity.form ? fieldEntity.form.title : ''} </Typography>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <Stack direction="row" spacing={2}>
            <Typography variant="h4">
              <Translate contentKey="exformmakerApp.field.attribute.title">Field Attribute</Translate>
            </Typography>
            <Box border={1} borderRadius={1} p={1} width="100%" sx={{ borderStyle: 'dashed' }}>
              {fieldEntity?.attribute &&
                Object.entries(fieldEntity?.attribute).map(([key, value]) => (
                  <Typography key={key} variant="body2">
                    <>
                      <b>{key}</b>: {value}
                    </>
                  </Typography>
                ))}
            </Box>
          </Stack>
        </Grid>
        <Grid item xs={12}>
          <ButtonGroup variant="contained" size="small">
            <Button onClick={() => navigate('/field')} data-cy="entityDetailsBackButton">
              <IconArrowBack size={'1rem'} />
              <Translate contentKey="entity.action.back">Back</Translate>
            </Button>
            <Button onClick={() => navigate(`/field/${fieldEntity.id}/edit`)} color="secondary">
              <IconPencil size={'1rem'} />
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </Button>
          </ButtonGroup>
        </Grid>
      </Grid>
    </MainCard>
  );
};

export default FieldDetail;
