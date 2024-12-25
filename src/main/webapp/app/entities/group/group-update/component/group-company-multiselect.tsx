import React from 'react';

import Autocomplete from '@mui/material/Autocomplete';
import { CheckBox, CheckBoxOutlineBlank } from '@mui/icons-material';
import TextField from '@mui/material/TextField';
import { FormikProps } from 'formik';
import { ICompany } from 'app/shared/model/company.model';
import Checkbox from '@mui/material/Checkbox';
import Chip from '@mui/material/Chip';

const GroupCompanyMultiselect = (props: { formik: FormikProps<any>; companies: Array<ICompany> }) => {
  const { formik, companies } = props;

  return (
    <Autocomplete
      multiple
      id="multi-companies"
      disableCloseOnSelect
      options={companies}
      getOptionLabel={option => option.title}
      value={formik.values.companies}
      onChange={(event, newValue) => {
        formik.setFieldValue('companies', newValue);
      }}
      renderOption={(renderProps, option, { selected }) => {
        const isChecked = formik.values.companies.some(c => c.id === option.id);
        return (
          <li
            {...renderProps}
            onClick={() => {
              if (isChecked) {
                formik.setFieldValue(
                  'companies',
                  formik.values.companies.filter(c => c.id !== option.id)
                );
              } else {
                formik.setFieldValue('companies', [...formik.values.companies, option]);
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
      renderInput={params => <TextField {...params} label="companies" placeholder="search company" />}
    />
  );
};

export default GroupCompanyMultiselect;
