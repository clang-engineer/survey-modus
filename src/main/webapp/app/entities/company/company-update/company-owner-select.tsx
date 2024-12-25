import React from 'react';
import { translate } from 'react-jhipster';

import { FormControl, InputLabel, MenuItem, Select } from '@mui/material';

import { FormikProps } from 'formik';
import { ICompany } from 'app/shared/model/company.model';
import { IUser } from 'app/shared/model/user.model';

interface ICompanyOwnerSelectProps {
  formik: FormikProps<ICompany>;
  users: IUser[];
}

const CompanyOwnerSelect = (props: ICompanyOwnerSelectProps) => {
  const { formik, users } = props;

  return (
    <FormControl component="fieldset" fullWidth error={formik.touched.user && Boolean(formik.errors.user)} variant="outlined">
      <InputLabel id="company-user-label"> {translate('exformmakerApp.company.user')}</InputLabel>
      <Select
        labelId="company-user-label"
        id="company-user"
        name="user"
        value={formik.values.user?.id}
        onChange={e => {
          formik.setFieldValue('user', { id: e.target.value });
        }}
        label={translate('exformmakerApp.company.user')}
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

export default CompanyOwnerSelect;
