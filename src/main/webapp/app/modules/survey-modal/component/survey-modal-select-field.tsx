import React from 'react';
import { FormControl, MenuItem, Select } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';
import NoContentBox from 'app/shared/component/no-content-box';

interface ISurveyModalSelectBoxProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalSelectField = (props: ISurveyModalSelectBoxProps) => {
  const { field, formik } = props;

  if (!field.lookups) {
    return <NoContentBox />;
  }

  return (
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
  );
};

export default SurveyModalSelectField;
