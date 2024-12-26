import React from 'react';
import { FormControl, Grid, MenuItem, Select, Typography } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';

interface ISurveyModalSelectBoxProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalSelectField = (props: ISurveyModalSelectBoxProps) => {
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
            <Select
              value={formik.values[field.id]}
              onChange={e => {
                formik.setFieldValue(`${field.id}`, e.target.value);
              }}
              label={field.title}
              variant="standard"
              fullWidth
            >
              <MenuItem value="-" disabled>
                <em>None</em>
              </MenuItem>
              {field.lookups.map(option => (
                <MenuItem key={option} value={option}>
                  {option}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
      </Grid>
    </Grid>
  );
};

export default SurveyModalSelectField;
