import React from 'react';
import { FormControl, MenuItem, Select } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import SurveyModalFieldTitle from 'app/modules/survey-modal/component/survey-modal-field-title';

interface ISurveyModalSelectBoxProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalSelectField = (props: ISurveyModalSelectBoxProps) => {
  const { field, formik } = props;

  return (
    <SubCard title={<SurveyModalFieldTitle title={field.title} description={field.description} />}>
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
    </SubCard>
  );
};

export default SurveyModalSelectField;
