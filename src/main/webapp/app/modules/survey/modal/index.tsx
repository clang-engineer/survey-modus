import React, { useEffect } from 'react';
import { Box, Dialog, Grid, Slide, Typography } from '@mui/material';

import { TransitionProps } from '@mui/material/transitions';
import { IForm } from 'app/shared/model/form.model';
import { IField } from 'app/shared/model/field.model';
import { FormikProps, useFormik } from 'formik';
import SurveyModalTextField from 'app/modules/survey/modal/component/survey-modal-text-field';
import SurveyModalDateField from 'app/modules/survey/modal/component/survey-modal-date-field';
import NoContentBox from 'app/shared/component/no-content-box';
import type from 'app/shared/model/enumerations/type.model';
import SurveyModalSelectField from 'app/modules/survey/modal/component/survey-modal-select-field';
import SurveyModalRadioField from 'app/modules/survey/modal/component/survey-modal-radio-field';
import SurveyModalCheckboxField from 'app/modules/survey/modal/component/survey-modal-checkbox-field';
import SubCard from 'app/berry/ui-component/cards/SubCard';

import { useTheme } from '@mui/material/styles';
import SurveyModalFileField from 'app/modules/survey/modal/component/survey-modal-file-field';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { createDocument, updateDocument } from 'app/modules/document/document.reducer';
import { IDocument } from 'app/shared/model/document.model';
import { ICompany } from 'app/shared/model/company.model';
import DialogAppBar from 'app/modules/survey/modal/dialog-app-bar';

interface IFieldWizardPreviewModalProps {
  company: ICompany;
  form: IForm;
  fields: IField[];
  document?: IDocument;
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

    const loading = useAppSelector(state => state.documentReducer.loading);
    const updating = useAppSelector(state => state.documentReducer.updating);
    const updateSuccess = useAppSelector(state => state.documentReducer.updateSuccess);

    const { company, form, fields, document } = props;

    const handleClose = () => {
      onReject();
    };

    const formik = useFormik<{ [key: string]: any }>({
      initialValues: fields.reduce((acc, field) => {
        acc[field.id] = null;
        return acc;
      }, {}),
      // validationSchema: fields.reduce((acc, field) => {
      //   acc[field.id] = yup.string();
      //   return acc;
      // }, {}),
      onSubmit(values) {
        const mappedFields: Array<{
          key: string;
          value: any;
        }> = Object.keys(values)
          .filter(key => values[key] !== null)
          .map(key => ({
            key,
            value: values[key],
          }));
        if (document && document.id) {
          dispatch(
            updateDocument({
              collectionId: form.category.id,
              document: {
                id: document.id,
                companyId: company.id,
                formId: form.id,
                fields: mappedFields,
              },
            })
          );
        } else {
          dispatch(
            createDocument({
              collectionId: form.category.id,
              document: { companyId: company.id, formId: form.id, fields: mappedFields },
            })
          );
        }
      },
    });

    useEffect(() => {
      if (document && document.id) {
        const doc = document.fields.reduce((acc, field) => {
          acc[field.key] = field.value;
          return acc;
        }, {});
        formik.setValues(doc);
      }
    }, [document]);

    return (
      <Dialog fullScreen open={isOpen} onClose={handleClose} TransitionComponent={Transition} sx={{ '& .MuiPaper-root': { padding: 0 } }}>
        <DialogAppBar form={form} formik={formik} onResolve={onResolve} />
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
