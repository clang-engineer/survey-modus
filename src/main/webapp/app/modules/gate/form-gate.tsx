import React, { useEffect } from 'react';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { Grid } from '@mui/material';

import { useLocation, useNavigate } from 'react-router-dom';
import { IForm } from 'app/shared/model/form.model';
import { ICompany } from 'app/shared/model/company.model';

const FormGate = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const state = location.state;

  const company = state ? (['company'] as ICompany) : {};
  const forms = state ? (['forms'] as IForm[]) : [];

  useEffect(() => {
    if (!company || !forms || forms.length === 0) {
      navigate('/gate/company');
    }
  }, []);
  return (
    <Grid container spacing={2}>
      {forms.map((form, index) => (
        <Grid
          item
          xs={12}
          sm={6}
          md={4}
          lg={3}
          sx={{
            '& .MuiCard-root': {
              minHeight: '300px',
            },
          }}
        >
          <SubCard key={index} title={form.title}>
            test
          </SubCard>
        </Grid>
      ))}
    </Grid>
  );
};

export default FormGate;
