import React from 'react';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import NoContentBox from 'app/shared/component/no-content-box';
import { Box, Grid, Typography } from '@mui/material';

import { useTheme } from '@mui/material/styles';
import { IField } from 'app/shared/model/field.model';
import type from 'app/shared/model/enumerations/type.model';
import SurveyModalTextField from 'app/modules/survey/survey-dialog/survey-dialog-content/component/survey-modal-text-field';
import SurveyModalDateField from 'app/modules/survey/survey-dialog/survey-dialog-content/component/survey-modal-date-field';
import SurveyModalSelectField from 'app/modules/survey/survey-dialog/survey-dialog-content/component/survey-modal-select-field';
import SurveyModalRadioField from 'app/modules/survey/survey-dialog/survey-dialog-content/component/survey-modal-radio-field';
import SurveyModalCheckboxField from 'app/modules/survey/survey-dialog/survey-dialog-content/component/survey-modal-checkbox-field';
import SurveyModalFileField from 'app/modules/survey/survey-dialog/survey-dialog-content/component/survey-modal-file-field';

import { FormikProps } from 'formik';

interface ISurveyDialogContentProps {
  fields: IField[];
  formik: FormikProps<Record<string, any>>;
}

const FormFieldByType = (formik: FormikProps<Record<string, any>>, field: IField) => {
  switch (field.attribute.type) {
    case type.TEXT:
      return <SurveyModalTextField key={field.id} field={field} formik={formik} />;
    case type.DATE:
      return <SurveyModalDateField key={field.id} field={field} formik={formik} />;
    case type.SELECT_BOX:
      return <SurveyModalSelectField key={field.id} field={field} formik={formik} />;
    case type.RADIO:
      return <SurveyModalRadioField key={field.id} field={field} formik={formik} />;
    case type.CHECK_BOX:
      return <SurveyModalCheckboxField key={field.id} field={field} formik={formik} />;
    case type.FILE:
      return <SurveyModalFileField key={field.id} field={field} formik={formik} />;
    default:
      return (
        <Grid item xs={12} key={field.id}>
          <Typography variant="h5">{field.title}</Typography> &nbsp;&nbsp;
          <Typography variant="caption">{field.description}</Typography>
        </Grid>
      );
  }
};

const SurveyDialogContent = (props: ISurveyDialogContentProps) => {
  const theme = useTheme();

  const { fields, formik } = props;

  return (
    <Grid container spacing={2} paddingY={theme.spacing(2)}>
      {fields.length > 0 ? (
        fields
          .sort((a, b) => a.display.orderNo - b.display.orderNo)
          .map((field, index) => {
            return (
              <Grid item xs={12} key={index} marginX={2}>
                <SubCard
                  title={
                    <Box display="flex">
                      <Typography variant="h5">#{index + 1}.</Typography> &nbsp;&nbsp;
                      <Typography variant="h5">{field.title}</Typography> &nbsp;&nbsp;
                      <Typography variant="caption">{field.description}</Typography>
                    </Box>
                  }
                >
                  {FormFieldByType(formik, field)}
                </SubCard>
              </Grid>
            );
          })
      ) : (
        <Grid item xs={12}>
          <NoContentBox title="No Field Available" height="calc(100vh - 72px - 48px)" />
        </Grid>
      )}
    </Grid>
  );
};

export default SurveyDialogContent;
