import React from 'react';

import { FormControl, InputLabel, MenuItem, Select } from '@mui/material';

import { FormikProps } from 'formik';
import { IGroup } from 'app/shared/model/group.model';
import { IUser } from 'app/shared/model/user.model';
import { translate } from 'react-jhipster';

interface IGroupOwnerSelectProps {
  formik: FormikProps<IGroup>;
  users: IUser[];
}

const GroupOwnerSelect = (props: IGroupOwnerSelectProps) => {
  const { formik, users } = props;

  return (
    <FormControl component="fieldset" fullWidth error={formik.touched.user && Boolean(formik.errors.user)} variant="outlined">
      <InputLabel id="group-user-label"> {translate('surveyModusApp.group.user')}</InputLabel>
      <Select
        labelId="group-user-label"
        id="group-user"
        name="user"
        value={formik.values.user?.id}
        onChange={e => {
          formik.setFieldValue('user', { id: e.target.value });
        }}
        label={translate('surveyModusApp.group.user')}
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

export default GroupOwnerSelect;
