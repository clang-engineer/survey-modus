import React, { useEffect } from 'react';

import { Box, Grid, IconButton, Typography } from '@mui/material';

import { useParams } from 'react-router-dom';
import useGateConfig from 'app/modules/gate/gate.config';
import { NavItemType } from 'app/berry/types';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity as getForm } from 'app/entities/form/form.reducer';
import { getEntity as getCompany } from 'app/entities/company/company.reducer';
import { CreateCompanyNavItems } from 'app/modules/gate/nav-item.utils';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import DataSource from 'app/modules/gate/form-gate/datasource';
import { IconDatabase, IconPlaylistAdd } from '@tabler/icons';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';
import { getEntities as getFieldList } from 'app/entities/field/field.reducer';
import SurveyModal from 'app/modules/survey-modal';

import { create } from 'react-modal-promise';

const FormGate = () => {
  const dispatch = useAppDispatch();
  const { setMenuItems } = useGateConfig();
  const { companyId, formId } = useParams<{ companyId: string; formId: string }>();

  const companyEntity = useAppSelector(state => state.company.entity);
  const formEntity = useAppSelector(state => state.form.entity);
  const fieldEntities = useAppSelector(state => state.field.entities);

  useEffect(() => {
    formId && dispatch(getForm(formId));
    formId && dispatch(getFieldList({ query: `formId.equals=${formId}` }));
  }, [formId]);

  useEffect(() => {
    companyId && dispatch(getCompany(companyId));
  }, [companyId]);

  useEffect(() => {
    const menuItems: NavItemType[] = CreateCompanyNavItems(companyEntity);
    setMenuItems(menuItems);

    return () => {
      setMenuItems([]);
    };
  }, [companyEntity]);

  const onClickCreateButton = () => {
    create(SurveyModal({ form: formEntity, fields: fieldEntities.filter(field => field.activated) }))();
  };

  const CardTitle = () => {
    return (
      <Box display="flex" alignItems="center" justifyContent="space-between">
        <Box display="flex" alignItems="center">
          <Typography variant="h4">
            <IconDatabase size={'1rem'} /> {formEntity.title}
          </Typography>
          &nbsp;&nbsp;
          <Typography variant="caption">
            ({companyEntity.title}, {formEntity.description})
          </Typography>
        </Box>
        <Box>
          <AnimateButton>
            <IconButton color="primary" size="small" onClick={onClickCreateButton}>
              <IconPlaylistAdd />
            </IconButton>
          </AnimateButton>
        </Box>
      </Box>
    );
  };

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <SubCard title={<CardTitle />}>
          <DataSource />
        </SubCard>
      </Grid>
    </Grid>
  );
};

export default FormGate;
