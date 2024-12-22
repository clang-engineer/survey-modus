import React, { useState } from 'react';

import { Button, ButtonGroup, Dialog, DialogActions, DialogContent, DialogTitle, Grid, TextField, Typography } from '@mui/material';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

import { useFormik } from 'formik';
import * as yup from 'yup';

const CompanyStaffDynamicInputModal = React.forwardRef((props: { formik: any }, ref) => {
  React.useImperativeHandle(ref, () => ({
    open: staff => handleOpen(staff),
    close: () => handleClose(),
  }));

  const [open, setOpen] = useState(false);

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = staff => {
    if (staff) {
      staffFormik.setValues(staff);
    } else {
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
        .matches(/^[0-9]+$/, 'Phone must be a number')
        .min(10, 'Phone must be at least 10 digits')
        .max(10, 'Phone must be at most 10 digits'),
      activated: yup.boolean().required('Activated is required'),
    }),
    onSubmit: values => {
      console.log(values);
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
          <Typography variant="h4">{staffFormik.values.name ? 'Update Staff' : 'New Staff'}</Typography>
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ my: 0 }}>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-name"
                fullWidth
                label="Name"
                value={staffFormik.values.name}
                onChange={staffFormik.handleChange}
                error={staffFormik.touched.name && Boolean(staffFormik.errors.name)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-email"
                fullWidth
                label="Email"
                value={staffFormik.values.email}
                onChange={staffFormik.handleChange}
                error={staffFormik.touched.email && Boolean(staffFormik.errors.email)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-phone"
                fullWidth
                label="Phone"
                value={staffFormik.values.phone}
                onChange={staffFormik.handleChange}
                error={staffFormik.touched.phone && Boolean(staffFormik.errors.phone)}
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                id="outlined-basic-staff-activated"
                fullWidth
                label="Activated"
                value={staffFormik.values.activated}
                onChange={staffFormik.handleChange}
                error={staffFormik.touched.activated && Boolean(staffFormik.errors.activated)}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <ButtonGroup variant="outlined" size="small">
            <AnimateButton>
              <Button>Create</Button>
            </AnimateButton>
            <Button onClick={handleClose}>Close</Button>
          </ButtonGroup>
        </DialogActions>
      </>
    </Dialog>
  );
});

export default CompanyStaffDynamicInputModal;
