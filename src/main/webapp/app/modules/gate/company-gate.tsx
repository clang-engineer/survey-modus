import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCompanyList } from 'app/entities/company/company.reducer';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { Grid } from '@mui/material';

import { useNavigate } from 'react-router-dom';

import { IconCircle } from '@tabler/icons';

import { useTheme } from '@mui/material/styles';
import { ICompany } from 'app/shared/model/company.model';

const CompanyGate = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const theme = useTheme();

  const user = useAppSelector(state => state.authentication.account);
  const companies = useAppSelector(state => state.company.entities);

  useEffect(() => {
    if (companies.length === 0) {
      dispatch(
        getCompanyList({
          query: `userId.equals=${user.id}`,
        })
      );
    }
  }, []);

  useEffect(() => {
    if (companies.length === 1) {
      navigate(`/gate/form`, {
        state: {
          company: companies[0],
          forms: companies[0].forms,
        },
      });
    }
  }, []);

  const isAccessibleCompany = (company: ICompany) => {
    return company.activated === true && company.forms.length > 0 && company.forms.some(form => form.activated);
  };

  return (
    <Grid container spacing={2}>
      {companies.map((company, index) => (
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
          onClick={() => {
            if (!isAccessibleCompany(company)) {
              alert('Company is not accessible');
              return;
            }
            navigate(`/gate/form`, {
              state: {
                company: company,
                forms: company.forms,
              },
            });
          }}
        >
          <SubCard
            key={index}
            title={
              <>
                <IconCircle size={'0.5rem'} fill={isAccessibleCompany(company) ? theme.palette.success.main : theme.palette.error.main} />{' '}
                &nbsp;
                {company.title}
              </>
            }
          >
            <p>{company.description}</p>
          </SubCard>
        </Grid>
      ))}
    </Grid>
  );
};

export default CompanyGate;
