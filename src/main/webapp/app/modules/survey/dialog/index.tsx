import React, { useEffect } from 'react';
import { Dialog } from '@mui/material';
import { IForm } from 'app/shared/model/form.model';
import { IField } from 'app/shared/model/field.model';
import { useFormik } from 'formik';
import { useAppDispatch } from 'app/config/store';
import { createSurvey, updateSurvey } from 'app/modules/survey/survey.reducer';
import { ISurvey } from 'app/shared/model/survey.model';
import { ICompany } from 'app/shared/model/company.model';
import SurveyDialogAppBar from 'app/modules/survey/dialog/survey-dialog-app-bar';
import SurveyDialogContent from 'app/modules/survey/dialog/survey-dialog-content';
import SlideTransition from 'app/shared/component/slide-transition';

interface IFieldWizardPreviewModalProps {
  company: ICompany;
  form: IForm;
  fields: IField[];
  survey?: ISurvey;
}

const SurveyModal =
  (props: IFieldWizardPreviewModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const dispatch = useAppDispatch();

    const { company, form, fields, survey } = props;

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
        if (survey && survey.id) {
          dispatch(
            updateSurvey({
              survey: {
                id: survey.id,
                companyId: company.id,
                formId: form.id,
                fields: mappedFields,
              },
            })
          );
        } else {
          dispatch(
            createSurvey({
              survey: { companyId: company.id, formId: form.id, fields: mappedFields },
            })
          );
        }
      },
    });

    useEffect(() => {
      if (survey && survey.id) {
        const doc = survey.fields.reduce((acc, field) => {
          acc[field.key] = field.value;
          return acc;
        }, {});
        formik.setValues(doc);
      }
    }, [survey]);

    return (
      <Dialog
        fullScreen
        open={isOpen}
        onClose={handleClose}
        TransitionComponent={SlideTransition}
        sx={{ '& .MuiPaper-root': { padding: 0 } }}
      >
        <SurveyDialogAppBar form={form} formik={formik} survey={survey} onResolve={onResolve} />
        <SurveyDialogContent fields={fields} formik={formik} />
      </Dialog>
    );
  };

export default SurveyModal;
