import React from 'react';
import { FormControl, TextField } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';

interface ISurveyModalTextFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalTextField = (props: ISurveyModalTextFieldProps) => {
  const { field, formik } = props;

  return (
    <FormControl fullWidth>
      <TextField
        id={`field-${field.id}`}
        value={formik.values[field.id]}
        onChange={e => formik.setFieldValue(`${field.id}`, e.target.value)}
        error={formik.touched[field.id] && Boolean(formik.errors[field.id])}
        variant="standard"
      />
    </FormControl>
  );
};

export default SurveyModalTextField;
