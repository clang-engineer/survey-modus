import React from 'react';

import { CheckBox, CheckBoxOutlineBlank } from '@mui/icons-material';
import { Autocomplete, Checkbox, TextField } from '@mui/material';

const GroupUserMultiselect = (props: { formik: any; forms: any }) => {
  const { formik, forms } = props;

  return (
    <Autocomplete
      multiple
      id="multi-forms"
      disableCloseOnSelect
      options={forms}
      getOptionLabel={option => option.title}
      value={formik.values.forms}
      onChange={(event, newValue) => {
        formik.setFieldValue('forms', newValue);
      }}
      renderOption={(props, option, { selected }) => {
        const isChecked = formik.values.forms?.some((c: any) => c.id === option.id);
        return (
          <li
            {...props}
            onClick={() => {
              if (isChecked) {
                formik.setFieldValue(
                  'forms',
                  formik.values.forms.filter((c: any) => c.id !== option.id)
                );
              } else {
                formik.setFieldValue('forms', [...formik.values.forms, option]);
              }
            }}
          >
            <Checkbox
              icon={<CheckBoxOutlineBlank fontSize="small" />}
              checkedIcon={<CheckBox fontSize="small" />}
              style={{ marginRight: 8 }}
              checked={isChecked}
            />
            {option.title}
          </li>
        );
      }}
      renderInput={params => <TextField {...params} label="forms" placeholder="search user" />}
    />
  );
};

export default GroupUserMultiselect;
