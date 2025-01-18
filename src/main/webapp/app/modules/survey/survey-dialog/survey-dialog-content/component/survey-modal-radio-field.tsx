import React from 'react';
import { FormControl, FormControlLabel, Radio, RadioGroup } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';
import NoContentBox from 'app/shared/component/no-content-box';

interface ISurveyModalRadioFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalRadioField = (props: ISurveyModalRadioFieldProps) => {
  const { field, formik } = props;

  if (!field.lookups || field.lookups.length === 0) {
    return <NoContentBox />;
  }

  return (
    <FormControl>
      <RadioGroup row aria-labelledby="survey-modla-radio-field" name="row-radio-buttons-group">
        {field.lookups.map((lookup, index) => (
          <FormControlLabel
            key={index}
            value={lookup}
            control={<Radio size="small" />}
            label={lookup}
            checked={formik.values[field.id] === lookup}
            onChange={e => {
              formik.setFieldValue(`${field.id}`, lookup);
            }}
          />
        ))}
      </RadioGroup>
    </FormControl>
  );
};

export default SurveyModalRadioField;
