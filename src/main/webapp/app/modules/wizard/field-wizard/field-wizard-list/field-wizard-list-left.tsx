import React from 'react';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { IField } from 'app/shared/model/field.model';

import { Box, Grid } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

interface IFieldWizardListLeftProps {
  fieldList: IField[];
}

const FieldWizardListLeft = (props: IFieldWizardListLeftProps) => {
  const { fieldList } = props;

  return (
    <MainCard title={'left'}>
      <Grid container spacing={gridSpacing}>
        {fieldList.map((field, index) => (
          <Grid key={index} item xs={12}>
            <Box
              sx={{
                padding: '20px',
                border: '1px solid #E4E9F0',
                borderRadius: '10px',
                backgroundColor: '#fff',
                boxShadow: '0px 4px 16px rgba(0, 0, 0, 0.08)',
              }}
            >
              {field.title}
            </Box>
          </Grid>
        ))}
      </Grid>
    </MainCard>
  );
};

export default FieldWizardListLeft;
