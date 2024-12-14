import React from 'react';

import { Box, Checkbox, Chip, FormControl, InputLabel, ListItemText, MenuItem, Select } from '@mui/material';

import { FormikProps } from 'formik';
import CancelIcon from '@mui/icons-material/Cancel';
import { SelectChangeEvent } from '@mui/material/Select';
import MuiSelectMenuProps from 'app/modules/wizard/group-wizard/group-wizard-detail/component/mui-select-menu-props';
import { ICompany } from 'app/shared/model/company.model';

const GroupWizardDetailCompany = (props: { formik: FormikProps<any>; companyList: ICompany[] }) => {
  const { formik, companyList } = props;

  return (
    <FormControl fullWidth>
      <InputLabel id="multiple-companys-label">Company</InputLabel>
      <Select
        labelId="multiple-companys-label"
        id="multiple-companys"
        multiple
        label="Company"
        value={formik.values.companys.map(item => item.id) || []}
        onChange={(event: SelectChangeEvent<any>) => {
          const {
            target: { value },
          } = event;
          formik.setFieldValue(
            'companys',
            value.map((item: any) => {
              return { id: item };
            })
          );
        }}
        renderValue={selected => (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
            {selected.map(value => {
              const company = companyList.find(item => item.id === value);
              return (
                <Chip
                  key={value}
                  label={`${company?.title}`}
                  variant="outlined"
                  clickable
                  deleteIcon={<CancelIcon onMouseDown={event => event.stopPropagation()} />}
                  onDelete={() => {
                    formik.setFieldValue(
                      'companys',
                      formik.values.companys.filter((item: any) => item.id !== value)
                    );
                  }}
                />
              );
            })}
          </Box>
        )}
        MenuProps={MuiSelectMenuProps}
      >
        {companyList.map(company => (
          <MenuItem
            key={company.id}
            value={company.id}
            // style={getStyles(company, personName, theme)}
          >
            <Checkbox size="small" checked={formik.values.companys.map(item => item.id).indexOf(company.id) > -1} />
            <ListItemText primary={`${company.title}`} />
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default GroupWizardDetailCompany;
