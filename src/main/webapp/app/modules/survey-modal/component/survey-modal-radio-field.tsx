import React from 'react';
import { FormControl, FormControlLabel, FormLabel, Grid, Radio, RadioGroup } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';

interface ISurveyModalRadioFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalRadioField = (props: ISurveyModalRadioFieldProps) => {
  const { field, formik } = props;

  return (
    <Grid item xs={12} key={field.id}>
      <FormControl>
        <FormLabel id="survey-modla-radio-field">{field.title}</FormLabel>
        <RadioGroup row aria-labelledby="survey-modla-radio-field" name="row-radio-buttons-group">
          {field.lookups.map((lookup, index) => (
            <FormControlLabel
              key={index}
              value={lookup}
              control={<Radio size="small" />}
              label={lookup}
              onChange={e => {
                formik.setFieldValue(`${field.id}`, lookup);
              }}
            />
          ))}
        </RadioGroup>
      </FormControl>
    </Grid>
  );
};

export default SurveyModalRadioField;
