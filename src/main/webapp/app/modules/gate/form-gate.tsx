import React, { useEffect } from 'react';

import { Grid } from '@mui/material';

import { useNavigate, useParams } from 'react-router-dom';
import useGateConfig from 'app/modules/gate/gate.config';
import { NavItemType } from 'app/berry/types';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity as getForm } from 'app/entities/form/form.reducer';
import { getEntity as getCompany } from 'app/entities/company/company.reducer';
import { CreateCompanyNavItems } from 'app/modules/gate/nav-item.utils';
import SubCard from 'app/berry/ui-component/cards/SubCard';

const FormGate = () => {
  const dispatch = useAppDispatch();
  const { setMenuItems } = useGateConfig();
  const { companyId, formId } = useParams<{ companyId: string; formId: string }>();

  const companyEntity = useAppSelector(state => state.company.entity);
  const formEntity = useAppSelector(state => state.form.entity);

  useEffect(() => {
    formId && dispatch(getForm(formId));
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

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <SubCard title={formEntity.title}>{formEntity.description}</SubCard>
      </Grid>
    </Grid>
  );
};

export default FormGate;
