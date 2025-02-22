import React from 'react';

// material-ui
import { Grid, Stack, Switch, TextField } from '@mui/material';

// project imports
import useConfig from 'app/berry/hooks/useConfig';
import SubCard from 'app/berry/ui-component/cards/SubCard';

const InputFilled = () => {
  const { outlinedFilled, onChangeOutlinedField } = useConfig();

  return (
    <SubCard title="Input Outlined With Filled">
      <Grid item xs={12} container spacing={2} alignItems="center">
        <Grid item>
          <Stack spacing={2}>
            <Switch
              checked={outlinedFilled}
              onChange={(event: React.ChangeEvent<HTMLInputElement>) => onChangeOutlinedField(event.target.checked)}
              inputProps={{ 'aria-label': 'controlled' }}
            />
            <TextField fullWidth id="outlined-basic" label={outlinedFilled ? 'With Background' : 'Without Background'} />
          </Stack>
        </Grid>
      </Grid>
    </SubCard>
  );
};

export default InputFilled;
