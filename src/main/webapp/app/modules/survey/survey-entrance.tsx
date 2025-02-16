import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { fetchAuthorizedCompanies } from 'app/entities/company/company.reducer';

import SubCard from 'app/berry/ui-component/cards/SubCard';

import { Box, Grid, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { IconCircle, IconRoute } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import { ICompany } from 'app/shared/model/company.model';

import { toast } from 'react-toastify';
import { gridSpacing } from 'app/berry/store/constant';

const SurveyEntrance = () => {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const theme = useTheme();

  const user = useAppSelector(state => state.authentication.account);
  const companies = useAppSelector(state => state.company.entities);

  useEffect(() => {
    if (companies.length === 0 && user) {
      dispatch(fetchAuthorizedCompanies());
    }
  }, [companies, user]);

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
    <Box
      sx={{
        flexGrow: 1,
        p: 3,
        height: '100vh',
        backgroundColor: theme.palette.background.paper,
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'flex-start',
        '& .MuiCard-root': {
          cursor: 'pointer',
        },
      }}
    >
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12} sx={{ display: 'flex', alignItems: 'center' }}>
          <IconRoute size={'1rem'} />
          &nbsp;
          <Typography variant="h4">Survey Router</Typography>
        </Grid>
        {companies
          .filter(c => c)
          .sort((a, b) => a.orderNo - b.orderNo)
          .map((company, index) => (
            <Grid
              key={index}
              item
              xs={6}
              // sm={6}
              lg={4}
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
                    <IconCircle
                      size={'0.5rem'}
                      fill={isAccessibleCompany(company) ? theme.palette.success.main : theme.palette.error.main}
                    />
                    &nbsp;
                    {company.title}
                  </>
                }
              >
                <p>{company.description}</p>
              </SubCard>
              <br />
            </Grid>
          ))}
      </Grid>
    </Box>
  );
};

export default SurveyEntrance;
