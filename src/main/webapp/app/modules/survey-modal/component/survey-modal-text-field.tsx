import React from 'react';
import { FormControl, TextField } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import SurveyModalFieldTitle from 'app/modules/survey-modal/component/survey-modal-field-title';

interface ISurveyModalTextFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const SurveyModalTextField = (props: ISurveyModalTextFieldProps) => {
  const { field, formik } = props;

  return (
    <SubCard title={<SurveyModalFieldTitle title={field.title} description={field.description} />}>
      <FormControl fullWidth>
        <TextField
          id={`field-${field.id}`}
          value={formik.values[field.id]}
          onChange={e => formik.setFieldValue(`${field.id}`, e.target.value)}
          error={formik.touched[field.id] && Boolean(formik.errors[field.id])}
          variant="standard"
        />
      </FormControl>
    </SubCard>
  );
};

export default SurveyModalTextField;
