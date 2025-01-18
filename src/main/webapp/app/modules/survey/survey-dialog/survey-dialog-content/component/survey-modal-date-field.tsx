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

  const fieldId = String(field.id);
  return (
    <FormControl fullWidth id={fieldId}>
      <LocalizationProvider dateAdapter={AdapterDateFns}>
        <DatePicker
          defaultValue={new Date()}
          value={new Date(formik.values[fieldId])}
          onChange={(newValue: Date | null) => {
            formik.setFieldValue(fieldId, newValue);
          }}
          format={'yyyy-MM-dd'}
          slotProps={{
            textField: {
              variant: 'standard',
              name: fieldId,
              error: formik.touched[fieldId] && Boolean(formik.errors[fieldId]),
            },
          }}
        />
      </LocalizationProvider>
    </FormControl>
  );
};

export default SurveyModalDateField;
