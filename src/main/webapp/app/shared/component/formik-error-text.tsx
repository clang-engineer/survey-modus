import React from 'react';
import { Typography } from '@mui/material';

import { FormikProps } from 'formik';

import { IconAlertTriangle } from '@tabler/icons';

const FormikErrorText = (props: { formik: FormikProps<any>; fieldName: string }) => {
  const { formik, fieldName } = props;
  return (
    formik.touched[fieldName] &&
    formik.errors[fieldName] && (
      <Typography variant="caption" color="error" gutterBottom sx={{ display: 'flex', alignItems: 'center' }}>
        <IconAlertTriangle size={16} /> &nbsp;
        {String(formik.errors[fieldName])}
      </Typography>
    )
  );
};

export default FormikErrorText;
