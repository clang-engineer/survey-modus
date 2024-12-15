import React from 'react';

import { CheckBox, CheckBoxOutlineBlank } from '@mui/icons-material';
import { Autocomplete, Checkbox, TextField } from '@mui/material';

const GroupWizardDetailUsers = (props: { formik: any; users: any }) => {
  const { formik, users } = props;

  return (
    <Autocomplete
      multiple
      id="multi-users"
      disableCloseOnSelect
      options={users}
      getOptionLabel={option => option.login}
      value={formik.values.users}
      onChange={(event, newValue) => {
        formik.setFieldValue('users', newValue);
      }}
      renderOption={(props, option, { selected }) => {
        const isChecked = formik.values.users.some((c: any) => c.id === option.id);
        return (
          <li
            {...props}
            onClick={() => {
              if (isChecked) {
                formik.setFieldValue(
                  'users',
                  formik.values.users.filter((c: any) => c.id !== option.id)
                );
              } else {
                formik.setFieldValue('users', [...formik.values.users, option]);
              }
            }}
          >
            <Checkbox
              icon={<CheckBoxOutlineBlank fontSize="small" />}
              checkedIcon={<CheckBox fontSize="small" />}
              style={{ marginRight: 8 }}
              checked={isChecked}
            />
            {option.login}
          </li>
        );
      }}
      renderInput={params => <TextField {...params} label="users" placeholder="search user" />}
    />
  );
};

export default GroupWizardDetailUsers;
