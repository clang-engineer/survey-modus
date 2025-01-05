import React, { useState } from 'react';

import {
  Button,
  ButtonGroup,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  FormControlLabel,
  Grid,
  Switch,
  TextField,
  Typography,
} from '@mui/material';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

import { useFormik } from 'formik';
import * as yup from 'yup';
import FormikErrorText from 'app/shared/component/formik-error-text';
import { IStaff } from 'app/shared/model/staff.model';

const CompanyStaffDynamicInputModal = React.forwardRef((props: { formik: any }, ref) => {
  React.useImperativeHandle(ref, () => ({
    open: modalOpenWithProps,
    close: () => handleClose(),
  }));

  const [clickedIndex, setClickedIndex] = useState(undefined);
  const [isNew, setIsNew] = useState(true);
  const [open, setOpen] = useState(false);

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const modalOpenWithProps = (modalProps: { staff: any; index: number }) => {
    if (modalProps) {
      setIsNew(false);
      setClickedIndex(modalProps.index);
      staffFormik.setValues(modalProps.staff);
    } else {
      setIsNew(true);
      setClickedIndex(undefined);
      staffFormik.resetForm();
    }
    handleOpen();
  };

  const checkUnique = (fieldName: string, value: string) => {
    if (isNew) {
      return !props.formik.values.staffs.some(staff => staff[fieldName] === value);
    } else {
      return !props.formik.values.staffs.some((staff, index) => index !== clickedIndex && staff[fieldName] === value);
    }
  };

  const staffFormik = useFormik<IStaff>({
    initialValues: {
      firstName: '',
      lastName: '',
      email: '',
      activated: true,
      langKey: 'en',
      phone: '',
    },
    validationSchema: yup.object({
      firstName: yup.string().required('First name is required').max(50, 'First name must be at most 50 characters'),
      lastName: yup.string().required('Last name is required').max(50, 'Last name must be at most 50 characters'),
      email: yup
        .string()
        .email('Invalid email')
        .required('Email is required')
        .test('unique', 'Email already exists', value => {
          return checkUnique('email', value);
        }),
      activated: yup.boolean().required('Activated is required'),
      langKey: yup.string().required('Language is required'),
      phone: yup
        .string()
        .required('Phone is required')
        // .matches(/^\d{3}-?\d{3,4}-?\d{4}$/, 'Invalid phone number')
        .matches(/^\d{3}-\d{3,4}-\d{4}$/, 'Invalid phone number (ex: 010-1234-5678)')
        .max(13, 'Phone number must be at most 13 characters')
        .test('unique', 'Phone already exists', value => {
          return checkUnique('phone', value);
        }),
    }),
    onSubmit(values) {
      if (isNew) {
        props.formik.setFieldValue('staffs', [...props.formik.values.staffs, values]);
      } else {
        props.formik.setFieldValue(
          'staffs',
          props.formik.values.staffs.map((staff, index) => (index === clickedIndex ? values : staff))
        );
      }
      handleClose();
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
          <Typography variant="h4">{isNew ? 'New Staff' : 'Update Staff'}</Typography>
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ my: 0 }}>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-name"
                fullWidth
                label="Name"
                value={staffFormik.values.firstName + staffFormik.values.lastName}
                onChange={(e: any) => {
                  const names = e.target.value;

                  if (names.length >= 2 && names.length <= 3) {
                    staffFormik.setFieldValue('firstName', names.substring(0, names.length - 2));
                    staffFormik.setFieldValue('lastName', names.substring(names.length - 2));
                  } else if (names.length > 3) {
                    staffFormik.setFieldValue('firstName', names.substring(0, 2));
                    staffFormik.setFieldValue('lastName', names.substring(2));
                  } else {
                    staffFormik.setFieldValue('firstName', names);
                    staffFormik.setFieldValue('lastName', '');
                  }
                }}
                error={
                  staffFormik.touched.firstName &&
                  Boolean(staffFormik.errors.firstName) &&
                  staffFormik.touched.lastName &&
                  Boolean(staffFormik.errors.lastName)
                }
              />
              <FormikErrorText formik={staffFormik} fieldName={'firstName'} />
              <FormikErrorText formik={staffFormik} fieldName={'lastName'} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-email"
                fullWidth
                label="Email"
                value={staffFormik.values.email}
                onChange={(e: any) => {
                  staffFormik.setFieldValue('email', e.target.value);
                }}
                error={staffFormik.touched.email && Boolean(staffFormik.errors.email)}
              />
              <FormikErrorText formik={staffFormik} fieldName={'email'} />
            </Grid>
            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Switch
                    checked={staffFormik.values.activated}
                    onChange={(e: any) => {
                      staffFormik.setFieldValue('activated', e.target.checked);
                    }}
                    name="activated"
                    color="primary"
                  />
                }
                label="Activated"
              />
              <FormikErrorText formik={staffFormik} fieldName={'activated'} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-lang"
                fullWidth
                label="Language"
                value={staffFormik.values.langKey}
                onChange={(e: any) => {
                  staffFormik.setFieldValue('langKey', e.target.value);
                }}
                error={staffFormik.touched.langKey && Boolean(staffFormik.errors.langKey)}
              />
              <FormikErrorText formik={staffFormik} fieldName={'langKey'} />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-phone"
                fullWidth
                label="Phone"
                placeholder="010-1234-5678"
                value={staffFormik.values.phone}
                onChange={(e: any) => {
                  staffFormik.setFieldValue('phone', e.target.value);
                }}
                error={staffFormik.touched.phone && Boolean(staffFormik.errors.phone)}
              />
              <FormikErrorText formik={staffFormik} fieldName={'phone'} />
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
                {isNew ? 'Add' : 'Update'}
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
