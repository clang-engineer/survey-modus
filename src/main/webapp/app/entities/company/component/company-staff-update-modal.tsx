import React, { useState } from 'react';

import { Button, ButtonGroup, Dialog, DialogActions, DialogContent, DialogTitle, Grid, TextField, Typography } from '@mui/material';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

import { useFormik } from 'formik';
import * as yup from 'yup';
import FormikErrorText from 'app/shared/component/formik-error-text';

const CompanyStaffDynamicInputModal = React.forwardRef((props: { formik: any }, ref) => {
  React.useImperativeHandle(ref, () => ({
    open: staff => handleOpen(staff),
    close: () => handleClose(),
  }));

  const [isNew, setIsNew] = useState(true);
  const [open, setOpen] = useState(false);

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = staff => {
    if (staff) {
      setIsNew(false);
      staffFormik.setValues(staff);
    } else {
      setIsNew(true);
      staffFormik.resetForm();
    }

    setOpen(true);
  };

  const staffFormik = useFormik({
    initialValues: { name: '', email: '', phone: '', activated: false },
    validationSchema: yup.object({
      name: yup
        .string()
        .required('Name is required')
        .min(3, 'Name must be at least 3 characters')
        .max(50, 'Name must be at most 50 characters'),
      email: yup.string().email('Invalid email').required('Email is required'),
      phone: yup
        .string()
        .required('Phone is required')
        .matches(/^[0-9]{3}-[0-9]{3,4}-[0-9]{4}$/, 'Invalid phone number')
        .max(13, 'Phone number must be at most 13 characters'),
      activated: yup.boolean().required('Activated is required'),
    }),
    onSubmit: values => {
      if (isNew) {
        props.formik.setFieldValue('staffs', [...props.formik.values.staffs, values]);
      } else {
        props.formik.setFieldValue(
          'staffs',
          props.formik.values.staffs.map(staff => (staff.email === values.email ? values : staff))
        );
      }
    },
  });

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      sx={{
        '&>div:nth-of-type(3)': {
          '&>div': {
            maxWidth: 400,
          },
        },
      }}
    >
      <>
        <DialogTitle>
          <Typography variant="h4">{isNew ? 'Update Staff' : 'New Staff'}</Typography>
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ my: 0 }}>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-name"
                fullWidth
                label="Name"
                value={staffFormik.values.name}
                onChange={(e: any) => staffFormik.setFieldValue('name', e.target.value)}
                error={staffFormik.touched.name && Boolean(staffFormik.errors.name)}
              />
              <FormikErrorText formik={staffFormik} fieldName={'name'} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-email"
                fullWidth
                label="Email"
                value={staffFormik.values.email}
                onChange={(e: any) => staffFormik.setFieldValue('email', e.target.value)}
                error={staffFormik.touched.email && Boolean(staffFormik.errors.email)}
              />
              <FormikErrorText formik={staffFormik} fieldName={'email'} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-phone"
                fullWidth
                label="Phone"
                placeholder="010-1234-5678"
                value={staffFormik.values.phone}
                onChange={(e: any) => staffFormik.setFieldValue('phone', e.target.value)}
                error={staffFormik.touched.phone && Boolean(staffFormik.errors.phone)}
              />
              <FormikErrorText formik={staffFormik} fieldName={'phone'} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-activated"
                fullWidth
                label="Activated"
                value={staffFormik.values.activated}
                onChange={(e: any) => staffFormik.setFieldValue('activated', e.target.value)}
                error={staffFormik.touched.activated && Boolean(staffFormik.errors.activated)}
              />
              <FormikErrorText formik={staffFormik} fieldName={'activated'} />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <ButtonGroup variant="outlined" size="small">
            <AnimateButton>
              <Button
                onClick={() => {
                  staffFormik.handleSubmit();
                }}
              >
                Create
              </Button>
            </AnimateButton>
            <Button onClick={handleClose}>Close</Button>
          </ButtonGroup>
        </DialogActions>
      </>
    </Dialog>
  );
});

export default CompanyStaffDynamicInputModal;
