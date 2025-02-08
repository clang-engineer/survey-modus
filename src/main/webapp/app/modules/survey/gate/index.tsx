import React, { useEffect } from 'react';

import { Grid } from '@mui/material';

import { useParams } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity as getForm } from 'app/entities/form/form.reducer';
import { getEntity as getCompany } from 'app/entities/company/company.reducer';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import DataSource from 'app/modules/survey/gate/datasource';
import { getEntities as getFieldList } from 'app/entities/field/field.reducer';
import { getSurveys } from 'app/modules/survey/survey.reducer';
import useConfig from 'app/berry/hooks/useConfig';
import GateTitle from 'app/modules/survey/gate/gate-title';
import ChatBoxFab from 'app/modules/message';

const Gate = () => {
  const dispatch = useAppDispatch();
  const { onChangeSurveyInfo } = useConfig();
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
    onChangeSurveyInfo({
      company: companyEntity,
      forms: companyEntity.forms,
    });

    return () => {
      onChangeSurveyInfo({ company: {}, forms: [] });
    };
  }, [companyEntity]);

  useEffect(() => {
    if (companyEntity.id && formEntity.id) {
      const query = `companyId.equals=${companyEntity.id}&formId.equals=${formEntity.id}`;
      dispatch(getSurveys({ query }));
    }
  }, [companyEntity, formEntity]);

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <SubCard title={<GateTitle />}>
          <DataSource />
        </SubCard>
        <ChatBoxFab />
      </Grid>
    </Grid>
  );
};

export default Gate;
