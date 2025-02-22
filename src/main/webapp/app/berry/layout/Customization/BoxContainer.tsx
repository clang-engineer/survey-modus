import React from 'react';

// material-ui
import { FormControl, FormControlLabel, Switch } from '@mui/material';

// project imports
import useConfig from 'app/berry/hooks/useConfig';
import SubCard from 'app/berry/ui-component/cards/SubCard';

const BoxContainer = () => {
  const { container, onChangeContainer } = useConfig();

  return (
    <SubCard title="Box Container">
      <FormControl component="fieldset" sx={{ mt: 2 }}>
        <FormControlLabel
          control={
            <Switch checked={container} onChange={() => onChangeContainer()} inputProps={{ 'aria-label': 'controlled-direction' }} />
          }
          label="Container"
        />
      </FormControl>
    </SubCard>
  );
};

export default BoxContainer;
