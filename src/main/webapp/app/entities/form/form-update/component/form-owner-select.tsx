import React from 'react';

import { FormControl, InputLabel, MenuItem, Select } from '@mui/material';

import { FormikProps } from 'formik';
import { IForm } from 'app/shared/model/form.model';
import { IUser } from 'app/shared/model/user.model';
import { translate } from 'react-jhipster';

interface IFormOwnerSelectProps {
  formik: FormikProps<IForm>;
  users: IUser[];
}

const FormOwnerSelect = (props: IFormOwnerSelectProps) => {
  const { formik, users } = props;

  return (
    <FormControl component="fieldset" fullWidth error={formik.touched.user && Boolean(formik.errors.user)} variant="outlined">
      <InputLabel id="form-user-label"> {translate('surveymodusApp.form.user')}</InputLabel>
      <Select
        labelId="form-user-label"
        id="form-user"
        name="user"
        value={formik.values.user?.id}
        onChange={e => {
          formik.setFieldValue('user', { id: e.target.value });
        }}
        label={translate('surveymodusApp.form.user')}
      >
        <MenuItem value="-" disabled>
          <em>None</em>
        </MenuItem>
        {users.map(user => (
          <MenuItem key={user.id} value={user.id}>
            {user.login}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default FormOwnerSelect;
