import React from 'react';

import { FormControl, InputLabel, MenuItem, Select } from '@mui/material';

import { FormikProps } from 'formik';
import { IForm } from 'app/shared/model/form.model';
import { translate } from 'react-jhipster';
import { ICategory } from 'app/shared/model/category.model';

interface IFormCategorySelectProps {
  formik: FormikProps<IForm>;
  categories: ICategory[];
}

const FormCategorySelect = (props: IFormCategorySelectProps) => {
  const { formik, categories } = props;

  return (
    <FormControl component="fieldset" fullWidth error={formik.touched.category && Boolean(formik.errors.category)} variant="outlined">
      <InputLabel id="form-category-label"> {translate('exformmakerApp.form.category')}</InputLabel>
      <Select
        labelId="form-category-label"
        id="form-category"
        name="category"
        value={formik.values.category?.id}
        onChange={e => {
          formik.setFieldValue('category', { id: e.target.value });
        }}
        label={translate('exformmakerApp.form.category')}
      >
        <MenuItem value="-" disabled>
          <em>None</em>
        </MenuItem>
        {categories.map(category => (
          <MenuItem key={category.id} value={category.id}>
            {category.title}
          </MenuItem>
        ))}
      </Select>
    </FormControl>
  );
};

export default FormCategorySelect;
