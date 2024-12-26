import React from 'react';
import { FormControl, Grid, TextField, Typography } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';

interface ISurveyModalTextfieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalTextField = (props: ISurveyModalTextfieldProps) => {
  const { field, formik } = props;

  return (
    <Grid item xs={12} key={field.id}>
      <Grid container>
        <Grid item xs={12} style={{ display: 'flex', alignItems: 'center' }}>
          <Typography variant="h5">{field.title}</Typography> &nbsp;&nbsp;
          <Typography variant="caption">{field.description}</Typography>
        </Grid>
        <Grid item xs={12}>
          <FormControl fullWidth>
            <TextField
              id={`field-${field.id}`}
              label={field.title}
              value={formik.values[field.id]}
              onChange={e => formik.setFieldValue(`${field.id}`, e.target.value)}
              error={formik.touched[field.id] && Boolean(formik.errors[field.id])}
              variant="standard"
            />
          </FormControl>
        </Grid>
      </Grid>
    </Grid>
  );
};

export default SurveyModalTextField;
