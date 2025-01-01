import React from 'react';
import { AppBar, Box, Button, Dialog, Grid, IconButton, Slide, Toolbar, Typography } from '@mui/material';

import CloseIcon from '@mui/icons-material/Close';

import { TransitionProps } from '@mui/material/transitions';
import { IForm } from 'app/shared/model/form.model';
import { IField } from 'app/shared/model/field.model';
import { FormikProps, useFormik } from 'formik';
import SurveyModalTextField from 'app/modules/survey-modal/component/survey-modal-text-field';
import SurveyModalDateField from 'app/modules/survey-modal/component/survey-modal-date-field';
import NoContentBox from 'app/shared/component/no-content-box';
import type from 'app/shared/model/enumerations/type.model';
import SurveyModalSelectField from 'app/modules/survey-modal/component/survey-modal-select-field';
import SurveyModalRadioField from 'app/modules/survey-modal/component/survey-modal-radio-field';
import SurveyModalCheckboxField from 'app/modules/survey-modal/component/survey-modal-checkbox-field';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { useTheme } from '@mui/material/styles';
import SurveyModalFileField from 'app/modules/survey-modal/component/survey-modal-file-field';
import { useAppDispatch } from 'app/config/store';
import { createDocument } from 'app/modules/document/document.reducer';
import { defaultValue, IDocument } from 'app/shared/model/document.model';
import { ICompany } from 'app/shared/model/company.model';

interface IFieldWizardPreviewModalProps {
  company: ICompany;
  form: IForm;
  fields: IField[];
}

const Transition = React.forwardRef(function Transition(
  props: TransitionProps & {
    children: React.ReactElement<unknown>;
  },
  ref: React.Ref<unknown>
) {
  return <Slide direction="up" ref={ref} {...props} />;
});

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

const SurveyModal =
  (props: IFieldWizardPreviewModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const theme = useTheme();
    const dispatch = useAppDispatch();

    const { company, form, fields } = props;

    const handleClose = () => {
      onReject();
    };

    const formik = useFormik<IDocument>({
      initialValues: fields.reduce(
        (acc, field) => {
          acc[field.id] = '';
          return acc;
        },
        { ...defaultValue }
      ),
      // validationSchema: fields.reduce((acc, field) => {
      //   acc[field.id] = yup.string();
      //   return acc;
      // }, {}),
      onSubmit(values) {
        dispatch(
          createDocument({
            collectionId: form.category.id,
            document: { ...values, companyId: company.id, formId: form.id },
          })
        );
      },
    });

    return (
      <Dialog
        fullScreen
        open={isOpen}
        onClose={handleClose}
        TransitionComponent={Transition}
        sx={{
          '& .MuiPaper-root': { padding: 0 },
        }}
      >
        <AppBar sx={{ position: 'relative' }}>
          <Toolbar>
            <IconButton
              edge="start"
              color="inherit"
              onClick={() => {
                formik.resetForm();
                handleClose();
              }}
              aria-label="close"
            >
              <CloseIcon />
            </IconButton>
            <Typography sx={{ ml: 2, flex: 1 }} variant="h4" color="inherit">
              {form.title}
            </Typography>
            <Button
              color="inherit"
              onClick={() => {
                formik.handleSubmit();
              }}
            >
              save
            </Button>
          </Toolbar>
        </AppBar>
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
      </Dialog>
    );
  };

export default SurveyModal;
