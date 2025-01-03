import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCompanyList } from 'app/entities/company/company.reducer';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { Grid } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { IconCircle } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import { ICompany } from 'app/shared/model/company.model';

import { toast } from 'react-toastify';

const CompanyGate = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const theme = useTheme();

  const user = useAppSelector(state => state.authentication.account);
  const companies = useAppSelector(state => state.company.entities);

  useEffect(() => {
    if (companies.length === 0) {
      dispatch(getCompanyList({ query: `userId.equals=${user.id}` }));
    }
  }, []);

  useEffect(() => {
    if (companies.length === 1) {
      navigateCompanyFirstForm(companies[0]);
    }
  }, []);

  const navigateCompanyFirstForm = (company: ICompany) => {
    if (!isAccessibleCompany(company)) {
      toast.error('No accessible form found for this company');
      return;
    }

    const form = company.forms.filter(f => f.activated)[0];
    navigate(`/survey/companies/${company.id}/forms/${form.id}`);
  };

  const isAccessibleCompany = (company: ICompany) => {
    return company.activated === true && company.forms.length > 0 && company.forms.some(form => form.activated);
  };

  return (
    <Grid container spacing={2}>
      {companies.map((company, index) => (
        <Grid
          key={index}
          item
          xs={12}
          sm={6}
          md={4}
          lg={3}
          sx={{
            '& .MuiCard-root': { minHeight: '300px' },
          }}
          onClick={() => {
            navigateCompanyFirstForm(company);
          }}
        >
          <SubCard
            key={index}
            title={
              <>
                <IconCircle size={'0.5rem'} fill={isAccessibleCompany(company) ? theme.palette.success.main : theme.palette.error.main} />
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
