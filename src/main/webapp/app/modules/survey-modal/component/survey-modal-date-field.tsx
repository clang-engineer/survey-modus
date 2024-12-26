import React from 'react';
import { FormControl } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';

interface ISurveyModalDateFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalDateField = (props: ISurveyModalDateFieldProps) => {
  const { field, formik } = props;

  return (
    <FormControl fullWidth id={field.title}>
      <LocalizationProvider dateAdapter={AdapterDateFns}>
        <DatePicker
          defaultValue={new Date()}
          value={formik.values[field.title]}
          onChange={(newValue: Date | null) => {
            formik.setFieldValue(field.title, newValue);
          }}
          format={'yyyy-MM-dd'}
          slotProps={{
            textField: {
              variant: 'standard',
              name: field.title,
              error: formik.touched[field.title] && Boolean(formik.errors[field.title]),
            },
          }}
        />
      </LocalizationProvider>
    </FormControl>
  );
};

export default SurveyModalDateField;
