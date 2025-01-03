import React, { useEffect } from 'react';
import { Translate, translate } from 'react-jhipster';
import { toast } from 'react-toastify';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import PasswordStrengthBar from 'app/shared/layout/password/password-strength-bar';
import { reset, savePassword } from './password.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { Button, Grid, Typography } from '@mui/material';

import { useFormik } from 'formik';
import * as yup from 'yup';
import { gridSpacing } from 'app/berry/store/constant';

import IconSave from '@mui/icons-material/Save';
import PasswordInput from 'app/modules/account/password/password-input';

export const PasswordPage = () => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(reset());
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  const handleValidSubmit = ({ currentPassword, newPassword }) => {
    dispatch(savePassword({ currentPassword, newPassword }));
  };

  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.password.successMessage);
  const errorMessage = useAppSelector(state => state.password.errorMessage);
  const loading = useAppSelector(state => state.password.loading);

  useEffect(() => {
    if (loading) {
      return;
    }
    if (successMessage) {
      toast.success(translate(successMessage));
    } else if (errorMessage) {
      toast.error(translate(errorMessage));
    }
    dispatch(reset());
  }, [successMessage, errorMessage, loading]);

  /* eslint-disable no-useless-escape */
  const pattern =
    /^(?=(?:.*[a-z])(?:.*[A-Z])|(?:.*[a-z])(?:.*[0-9])|(?:.*[a-z])(?:.*[!@#\$%\^&\*])|(?:.*[A-Z])(?:.*[0-9])|(?:.*[A-Z])(?:.*[!@#\$%\^&\*])|(?:.*[0-9])(?:.*[!@#\$%\^&\*]))(?=.{10,})|(?=(?:.*[a-z])(?:.*[A-Z])(?:.*[0-9])|(?:.*[a-z])(?:.*[A-Z])(?:.*[!@#\$%\^&\*])|(?:.*[a-z])(?:.*[0-9])(?:.*[!@#\$%\^&\*])|(?:.*[A-Z])(?:.*[0-9])(?:.*[!@#\$%\^&\*]))(?=.{8,}).*$/;

  const formik = useFormik({
    initialValues: {
      currentPassword: '',
      newPassword: '',
      confirmPassword: '',
    },
    validationSchema: yup.object({
      currentPassword: yup
        .string()
        .required()
        .min(4, translate('global.messages.validate.newpassword.minlength'))
        .max(50, translate('global.messages.validate.newpassword.maxlength')),
      newPassword: yup
        .string()
        .required()
        .min(4, translate('global.messages.validate.newpassword.minlength'))
        .max(50, translate('global.messages.validate.newpassword.maxlength'))
        .matches(pattern, translate('global.messages.validate.newpassword.pattern'))
        .notOneOf([yup.ref('currentPassword')], translate('global.messages.validate.newpassword.different')),
      confirmPassword: yup
        .string()
        .required()
        .oneOf([yup.ref('newPassword')], translate('global.messages.error.dontmatch')),
    }),
    onSubmit(values) {
      handleValidSubmit(values);
    },
  });

  const CardTitle = () => (
    <Typography variant="h4">
      <Translate contentKey="password.title" interpolate={{ username: account.login }}>
        Password for {account.login}
      </Translate>
    </Typography>
  );

  return (
    <MainCard title={<CardTitle />}>
      <form id="password-form" onSubmit={formik.handleSubmit}>
        <Grid container spacing={gridSpacing}>
          <Grid item xs={12}>
            <PasswordInput formik={formik} name="currentPassword" translateKey="global.form.currentpassword.label" />
          </Grid>
          <Grid item xs={12}>
            <PasswordInput formik={formik} name="newPassword" translateKey="global.form.newpassword.label" />
            <PasswordStrengthBar password={formik.values.newPassword} />
          </Grid>
          <Grid item xs={12}>
            <PasswordInput formik={formik} name="confirmPassword" translateKey="global.form.confirmpassword.label" />
          </Grid>
          <Grid item xs={12}>
            <Button size="small" type="submit" variant="contained" color="primary" data-cy="submit" startIcon={<IconSave />}>
              <Translate contentKey="password.form.button">Save</Translate>
            </Button>
          </Grid>
        </Grid>
      </form>
    </MainCard>
  );
};

export default PasswordPage;
