import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCompanyList } from 'app/entities/company/company.reducer';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { Grid } from '@mui/material';

const CompanyGate = () => {
  const dispatch = useAppDispatch();
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
        >
          <SubCard key={index} title={company.title} content={company.description}></SubCard>
        </Grid>
      ))}
    </Grid>
  );
};

export default CompanyGate;
