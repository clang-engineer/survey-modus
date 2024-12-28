import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getFormList } from 'app/entities/form/form.reducer';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { Grid } from '@mui/material';

const FormGate = () => {
  const dispatch = useAppDispatch();
  const user = useAppSelector(state => state.authentication.account);
  const formList = useAppSelector(state => state.form.entities);

  useEffect(() => {
    if (formList.length === 0) {
      dispatch(
        getFormList({
          query: `userId.equals=${user.id}`,
        })
      );
    }
  }, []);

  return (
    <Grid container spacing={2}>
      {formList.map((form, index) => (
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
          <SubCard key={index} title={form.title} content={form.description}>
            test
          </SubCard>
        </Grid>
      ))}
    </Grid>
  );
};

export default FormGate;
