import React from 'react';
import { Box, Checkbox, FormControlLabel } from '@mui/material';
import { IField } from 'app/shared/model/field.model';
import { FormikProps } from 'formik';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import SurveyModalFieldTitle from 'app/modules/survey-modal/component/survey-modal-field-title';

interface ISurveyModalRadioFieldProps {
  field: IField;
  formik: FormikProps<Record<string, any>>;
}

const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

const SurveyModalRadioField = (props: ISurveyModalRadioFieldProps) => {
  const { field, formik } = props;

  return (
    <SubCard title={<SurveyModalFieldTitle title={field.title} description={field.description} />}>
      <Box display="flex">
        {field.lookups?.map((lookup, index) => (
          <FormControlLabel
            value={lookup}
            control={
              <Checkbox
                {...label}
                size="small"
                checked={formik.values[field.id].split(';').includes(lookup)}
                onChange={e => {
                  if (e.target.checked) {
                    formik.setFieldValue(`${field.id}`, `${formik.values[field.id]};${lookup}`);
                  } else {
                    formik.setFieldValue(`${field.id}`, formik.values[field.id].replace(`;${lookup}`, ''));
                  }
                }}
              />
            }
            label={lookup}
            labelPlacement="end"
          />
        ))}
      </Box>
    </SubCard>
  );
};

export default SurveyModalRadioField;
