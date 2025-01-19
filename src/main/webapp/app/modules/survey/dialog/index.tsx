import React, { useEffect } from 'react';
import { Dialog } from '@mui/material';
import { IForm } from 'app/shared/model/form.model';
import { IField } from 'app/shared/model/field.model';
import { useFormik } from 'formik';

import { useTheme } from '@mui/material/styles';
import { useAppDispatch } from 'app/config/store';
import { createDocument, updateDocument } from 'app/modules/document/document.reducer';
import { IDocument } from 'app/shared/model/document.model';
import { ICompany } from 'app/shared/model/company.model';
import SurveyDialogAppBar from 'app/modules/survey/dialog/survey-dialog-app-bar';
import SurveyDialogContent from 'app/modules/survey/dialog/survey-dialog-content';
import SurveyChatDialog from 'app/modules/survey/dialog/survey-chat-dialog';
import SlideTransition from 'app/shared/component/slide-transition';

interface IFieldWizardPreviewModalProps {
  company: ICompany;
  form: IForm;
  fields: IField[];
  document?: IDocument;
}

const SurveyModal =
  (props: IFieldWizardPreviewModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const theme = useTheme();
    const dispatch = useAppDispatch();

    const chatDialogRef = React.useRef(null);

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
      <Dialog
        fullScreen
        open={isOpen}
        onClose={handleClose}
        TransitionComponent={SlideTransition}
        sx={{ '& .MuiPaper-root': { padding: 0 } }}
      >
        <SurveyDialogAppBar
          form={form}
          formik={formik}
          document={document}
          onResolve={onResolve}
          onClickMessagesButton={() => {
            chatDialogRef.current.open();
          }}
        />
        <SurveyDialogContent fields={fields} formik={formik} />
        <SurveyChatDialog ref={chatDialogRef} formik={formik} form={form} document={document} />
      </Dialog>
    );
  };

export default SurveyModal;
