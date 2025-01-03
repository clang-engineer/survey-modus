import React, { useEffect } from 'react';
import { translate, Translate } from 'react-jhipster';
import { toast } from 'react-toastify';

import { languages, locales } from 'app/config/translation';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getSession } from 'app/shared/reducers/authentication';
import { reset, saveAccountSettings } from './settings.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';

import { Button, FormControl, FormHelperText, Grid, InputLabel, MenuItem, OutlinedInput, Select, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';

import IconSave from '@mui/icons-material/Save';

import { useFormik } from 'formik';
import * as yup from 'yup';
import { useTheme } from '@mui/material/styles';

export const SettingsPage = () => {
  const theme = useTheme();
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const successMessage = useAppSelector(state => state.settings.successMessage);
  const loading = useAppSelector(state => state.settings.loading);

  useEffect(() => {
    dispatch(getSession());
    return () => {
      dispatch(reset());
    };
  }, []);

  useEffect(() => {
    if (!loading && successMessage) {
      toast.success(translate(successMessage));
    }
  }, [successMessage, loading]);

  const onFormikSubmit = values => {
    dispatch(saveAccountSettings({ ...account, ...values }));
  };

  const CardTitle = () => {
    return (
      <Typography variant="h4">
        <Translate contentKey="settings.title" interpolate={{ username: account.login }}>
          User settings for {account.login}
        </Translate>
      </Typography>
    );
  };

  const formik = useFormik({
    initialValues: {
      firstName: account.firstName,
      lastName: account.lastName,
      email: account.email,
      langKey: account.langKey,
    },
    validationSchema: yup.object({
      firstName: yup
        .string()
        .required(translate('settings.messages.validate.firstname.required'))
        .min(1, translate('settings.messages.validate.firstname.minlength'))
        .max(50, translate('settings.messages.validate.firstname.maxlength')),
      lastName: yup
        .string()
        .required(translate('settings.messages.validate.lastname.required'))
        .min(1, translate('settings.messages.validate.lastname.minlength'))
        .max(50, translate('settings.messages.validate.lastname.maxlength')),
      email: yup
        .string()
        .required(translate('global.messages.validate.email.required'))
        .min(5, translate('global.messages.validate.email.minlength'))
        .max(254, translate('global.messages.validate.email.maxlength'))
        .email(translate('global.messages.validate.email.invalid'))
        .matches(/^[^@\s]+@[^@\s]+\.[^@\s]+$/, translate('global.messages.validate.email.invalid')),
    }),
    onSubmit: values => {
      onFormikSubmit(values);
    },
  });

  return (
    <MainCard title={<CardTitle />}>
      <form id="settings-form" onSubmit={formik.handleSubmit}>
        <Grid container spacing={gridSpacing}>
          <Grid item xs={12}>
            <FormControl fullWidth error={Boolean(formik.touched.firstName && formik.errors.firstName)}>
              <InputLabel htmlFor="outlined-adornment-firstName-login">
                <Translate contentKey="settings.form.firstname">First Name</Translate>
              </InputLabel>
              <OutlinedInput
                id="outlined-adornment-firstName-login"
                value={formik.values.firstName}
                name="firstName"
                onBlur={formik.handleBlur}
                onChange={formik.handleChange}
                inputProps={{}}
                label={translate('settings.form.firstname')}
              />
              {formik.touched.firstName && formik.errors.firstName && (
                <FormHelperText error id="standard-weight-helper-text-username-login">
                  <span dangerouslySetInnerHTML={{ __html: formik.errors.firstName }} />
                </FormHelperText>
              )}
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <FormControl fullWidth error={Boolean(formik.touched.lastName && formik.errors.lastName)}>
              <InputLabel htmlFor="outlined-adornment-lastName-login">
                <Translate contentKey="settings.form.lastname">Last Name</Translate>
              </InputLabel>
              <OutlinedInput
                id="outlined-adornment-lastName-login"
                type="lastName"
                value={formik.values.lastName}
                name="lastName"
                onBlur={formik.handleBlur}
                onChange={formik.handleChange}
                inputProps={{}}
                label={translate('settings.form.lastname')}
              />
              {formik.touched.lastName && formik.errors.lastName && (
                <FormHelperText error id="standard-weight-helper-text-username-login">
                  <span dangerouslySetInnerHTML={{ __html: formik.errors.lastName }} />
                </FormHelperText>
              )}
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <FormControl fullWidth error={Boolean(formik.touched.email && formik.errors.email)}>
              <InputLabel htmlFor="outlined-adornment-email-login">
                <Translate contentKey="settings.form.email">Email</Translate>
              </InputLabel>
              <OutlinedInput
                id="outlined-adornment-email-login"
                type="email"
                value={formik.values.email}
                name="email"
                onBlur={formik.handleBlur}
                onChange={formik.handleChange}
                inputProps={{}}
                label={translate('settings.form.email')}
              />
              {formik.touched.email && formik.errors.email && (
                <FormHelperText error id="standard-weight-helper-text-username-login">
                  <span dangerouslySetInnerHTML={{ __html: formik.errors.email }} />
                </FormHelperText>
              )}
            </FormControl>
          </Grid>

          <Grid item xs={12}>
            <FormControl fullWidth error={Boolean(formik.touched.langKey && formik.errors.langKey)}>
              <InputLabel htmlFor="outlined-adornment-langKey-login">
                <Translate contentKey="settings.form.language">Language</Translate>
              </InputLabel>
              <Select
                id="outlined-adornment-langKey-login"
                value={formik.values.langKey}
                name="langKey"
                onBlur={formik.handleBlur}
                onChange={formik.handleChange}
                inputProps={{}}
                label={translate('settings.form.language')}
              >
                {locales.map(locale => (
                  <MenuItem value={locale} key={locale}>
                    {languages[locale].name}
                  </MenuItem>
                ))}
              </Select>
              {formik.touched.langKey && formik.errors.langKey && (
                <FormHelperText error id="standard-weight-helper-text-username-login">
                  <span dangerouslySetInnerHTML={{ __html: formik.errors.langKey }} />
                </FormHelperText>
              )}
            </FormControl>
          </Grid>
          <Grid item xs={12}>
            <Button color="primary" type="submit" variant="contained" size="small" data-cy="submit" startIcon={<IconSave />}>
              <Translate contentKey="settings.form.button">Save</Translate>
            </Button>
          </Grid>
        </Grid>
      </form>
    </MainCard>
  );
};

export default SettingsPage;
