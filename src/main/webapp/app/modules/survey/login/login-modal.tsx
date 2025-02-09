import React from 'react';

import { Button, Dialog, DialogActions, DialogContent, DialogTitle, TextField, Typography } from '@mui/material';

import { useNavigate } from 'react-router-dom';

import * as yup from 'yup';
import { useFormik } from 'formik';

import axios from 'axios';
import { useAppDispatch } from 'app/config/store';
import { loginStaff } from 'app/shared/reducers/authentication';

const SurveyLoginModal = () => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const [step, setStep] = React.useState(0);

  const formik = useFormik({
    enableReinitialize: true,
    initialValues: {
      phone: '',
      otp: '',
    },
    validationSchema: yup.object({
      phone: yup
        .string()
        .required('Required')
        .matches(/^01[0-9]{9,10}$/, 'Please enter a valid mobile number (ex: 01x-xxxx-xxxx)'),
      otp: step === 1 ? yup.number().required('Required') : yup.number(),
    }),
    onSubmit({ phone, otp }, { setFieldError, setSubmitting, setValues }) {
      if (step === 0) {
        submitIssueOTP(phone);
      } else {
        dispatch(loginStaff(phone, otp));
      }
    },
  });

  const submitIssueOTP = (phone: string) => {
    axios
      .post('/api/otp/staff', phone, {
        headers: {
          'Content-Type': 'text/plain',
        },
      })
      .then(response => {
        if (response.status === 200) {
          setStep(1);
        }
      })
      .catch(error => {
        formik.setFieldError('phone', 'Failed to issue OTP (Please check your mobile number)');
        setStep(0);
      });
  };

  const handleClose = () => {
    setStep(0);
    navigate('/');
  };
  const onClickCancel = () => {
    if (step === 0) {
      handleClose();
    } else {
      setStep(0);
    }
  };

  const Title = () => {
    return (
      <DialogTitle>
        <Typography variant="h4">{step === 0 ? 'Input Mobile Number' : 'Input OTP'}</Typography>
        <Typography variant="caption">
          {step === 0 ? 'We will send you an OTP to verify your mobile number' : 'Please enter the OTP sent to your mobile number'}
        </Typography>
      </DialogTitle>
    );
  };

  const DialogForm = () => {
    return (
      <form onSubmit={formik.handleSubmit}>
        {step === 0 && (
          <TextField
            autoFocus
            required
            margin="dense"
            id="name"
            name="phone"
            label="Mobile Number"
            type="text"
            placeholder="9 or 10 digit mobile number (ex: 0101234567)"
            fullWidth
            variant="standard"
            onChange={formik.handleChange}
            value={formik.values.phone}
            error={formik.touched.phone && Boolean(formik.errors.phone)}
            helperText={formik.touched.phone && formik.errors.phone}
          />
        )}
        {step === 1 && (
          <TextField
            autoFocus
            required
            margin="dense"
            id="name"
            name="otp"
            label="OTP"
            type="text"
            fullWidth
            variant="standard"
            onChange={formik.handleChange}
            value={formik.values.otp}
            error={formik.touched.otp && Boolean(formik.errors.otp)}
            helperText={formik.touched.otp && formik.errors.otp}
          />
        )}
      </form>
    );
  };

  return (
    <Dialog open={true} onClose={handleClose}>
      <Title />
      <DialogContent>
        <DialogForm />
      </DialogContent>
      <DialogActions>
        <Button onClick={onClickCancel}>Cancel</Button>
        <Button
          onClick={() => {
            formik.submitForm();
          }}
        >
          {step === 0 ? 'Issue OTP' : 'Verify OTP'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default SurveyLoginModal;
