import React from 'react';

import Autocomplete from '@mui/material/Autocomplete';
import { CheckBox, CheckBoxOutlineBlank } from '@mui/icons-material';
import TextField from '@mui/material/TextField';
import { FormikProps } from 'formik';
import { ICompany } from 'app/shared/model/company.model';
import Checkbox from '@mui/material/Checkbox';

const GroupWizardDetailCompanys = (props: { formik: FormikProps<any>; companys: Array<ICompany> }) => {
  const { formik, companys } = props;

  return (
    <Autocomplete
      multiple
      id="multi-companys"
      disableCloseOnSelect
      options={companys}
      getOptionLabel={option => option.title}
      value={formik.values.companys}
      onChange={(event, newValue) => {
        formik.setFieldValue('companys', newValue);
      }}
      renderOption={(renderProps, option, { selected }) => {
        const isChecked = formik.values.companys.some(c => c.id === option.id);
        return (
          <li
            {...renderProps}
            onClick={() => {
              if (isChecked) {
                formik.setFieldValue(
                  'companys',
                  formik.values.companys.filter(c => c.id !== option.id)
                );
              } else {
                formik.setFieldValue('companys', [...formik.values.companys, option]);
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
      renderInput={params => <TextField {...params} label="companys" placeholder="search company" />}
    />
  );
};

export default GroupWizardDetailCompanys;
