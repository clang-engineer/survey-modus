import React from 'react';
import { AppBar, Button, Dialog, Grid, IconButton, Slide, Toolbar, Typography } from '@mui/material';

import CloseIcon from '@mui/icons-material/Close';

import { TransitionProps } from '@mui/material/transitions';
import { IForm } from 'app/shared/model/form.model';
import { IField } from 'app/shared/model/field.model';
import { gridSpacing } from 'app/berry/store/constant';
import { FormikProps, useFormik } from 'formik';
import * as yup from 'yup';
import SurveyModalTextField from 'app/modules/survey-modal/component/survey-modal-text-field';
import SurveyModalDateField from 'app/modules/survey-modal/component/survey-modal-date-field';
import NoContentBox from 'app/shared/component/no-content-box';
import type from 'app/shared/model/enumerations/type.model';
import SurveyModalSelectField from 'app/modules/survey-modal/component/survey-modal-select-field';
import SurveyModalRadioField from 'app/modules/survey-modal/component/survey-modal-radio-field';
import SurveyModalCheckboxField from 'app/modules/survey-modal/component/survey-modal-checkbox-field';

interface IFieldWizardPreviewModalProps {
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
    const { form, fields } = props;

    const handleClose = () => {
      onReject();
    };

    const formik = useFormik<Record<string, any>>({
      initialValues: fields.reduce((acc, field) => {
        acc[field.id] = '';
        return acc;
      }, {}),
      validationSchema: fields.reduce((acc, field) => {
        // if (field.required) {
        acc[field.id] = yup.string().required('Required');
        // }
        return acc;
      }, {}),
      onSubmit: values => {
        console.log(values);
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
          // '& .MuiDialogContent-root': { padding: 0 },
          // '& .MuiDialogActions-root': {
          //   justifyContent: 'space-between',
          // },
        }}
      >
        <AppBar sx={{ position: 'relative' }}>
          <Toolbar>
            <IconButton edge="start" color="inherit" onClick={handleClose} aria-label="close">
              <CloseIcon />
            </IconButton>
            <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
              {form.title}
              {JSON.stringify(formik.values)}
            </Typography>
            <Button autoFocus color="inherit" onClick={handleClose}>
              save
            </Button>
          </Toolbar>
        </AppBar>
        <Grid container spacing={gridSpacing} padding={3}>
          {fields.length > 0 ? (
            fields.map((field, index) => {
              return (
                <Grid item xs={12} key={index}>
                  {FormFieldByType(formik, field)}
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
