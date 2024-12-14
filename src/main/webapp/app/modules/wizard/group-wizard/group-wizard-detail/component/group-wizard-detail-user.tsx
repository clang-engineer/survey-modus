import React from 'react';

import { Box, Checkbox, Chip, FormControl, InputLabel, ListItemText, MenuItem, Select } from '@mui/material';

import { FormikProps } from 'formik';
import CancelIcon from '@mui/icons-material/Cancel';
import { SelectChangeEvent } from '@mui/material/Select';
import MuiSelectMenuProps from 'app/modules/wizard/group-wizard/group-wizard-detail/mui-select-menu-props';
import { IUser } from 'app/shared/model/user.model';

const GroupWizardDetailUser = (props: { formik: FormikProps<any>; userList: IUser[] }) => {
  const { formik, userList } = props;

  return (
    <FormControl fullWidth>
      <InputLabel id="multiple-users-label">User</InputLabel>
      <Select
        labelId="multiple-users-label"
        id="multiple-users"
        multiple
        label="User"
        value={formik.values.users.map(item => item.id) || []}
        onChange={(event: SelectChangeEvent<any>) => {
          const {
            target: { value },
          } = event;
          formik.setFieldValue(
            'users',
            value.map((item: any) => {
              return { id: item };
            })
          );
        }}
        renderValue={selected => (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
            {selected.map(value => {
              const user = userList.find(item => item.id === value);
              return (
                <Chip
                  key={value}
                  label={`${user.login}`}
                  variant="outlined"
                  clickable
                  deleteIcon={<CancelIcon onMouseDown={event => event.stopPropagation()} />}
                  onDelete={() => {
                    formik.setFieldValue(
                      'users',
                      formik.values.users.filter((item: any) => item.id !== value)
                    );
                  }}
                />
              );
            })}
          </Box>
        )}
        MenuProps={MuiSelectMenuProps}
      >
        {userList.map(user => (
          <MenuItem
            key={user.id}
            value={user.id}
            // style={getStyles(user, personName, theme)}
          >
            <Checkbox size="small" checked={formik.values.users.map(item => item.id).indexOf(user.id) > -1} />
            <ListItemText primary={`${user.login}`} />
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default GroupWizardDetailUser;
