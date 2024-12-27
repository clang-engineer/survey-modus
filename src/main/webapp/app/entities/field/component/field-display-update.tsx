import React from 'react';

import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { IField } from 'app/shared/model/field.model';

import { FormControl, Grid, InputLabel, MenuItem, Select, TextField } from '@mui/material';

import { FormikProps } from 'formik';

import { translate } from 'react-jhipster';
import type from 'app/shared/model/enumerations/type.model';

interface IFieldDisplayUpdateProps {
  formik: FormikProps<IField>;
}

const FieldDisplayUpdate = (props: IFieldDisplayUpdateProps) => {
  const { formik } = props;

  return (
    <SubCard title={translate('surveymodusApp.field.display.title')}>
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12}>
          <TextField
            fullWidth
            type="number"
            id="field-display-orderNo"
            name="display.orderNo"
            label={translate('surveymodusApp.field.display.orderNo')}
            value={formik.values.display?.orderNo}
            onChange={formik.handleChange}
            error={formik.touched.display?.orderNo && Boolean(formik.errors.display?.orderNo)}
            helperText={formik.touched.display?.orderNo && formik.errors.display?.orderNo}
            variant="outlined"
          />
        </Grid>
      </Grid>
    </SubCard>
  );
};

export default FieldDisplayUpdate;
