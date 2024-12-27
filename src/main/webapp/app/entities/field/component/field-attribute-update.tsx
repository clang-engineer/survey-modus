import React from 'react';

import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { IField } from 'app/shared/model/field.model';

import { FormControl, Grid, InputLabel, MenuItem, Select, TextField } from '@mui/material';

import { FormikProps } from 'formik';

import { translate } from 'react-jhipster';
import type from 'app/shared/model/enumerations/type.model';

interface IFieldAttributeUpdateProps {
  formik: FormikProps<IField>;
}

const FieldAttributeUpdate = (props: IFieldAttributeUpdateProps) => {
  const { formik } = props;

  return (
    <SubCard title={translate('surveyModusApp.field.attribute.title')}>
      <Grid container spacing={gridSpacing}>
        <Grid item xs={12}>
          <FormControl
            component="fieldset"
            fullWidth
            error={formik.touched.attribute && Boolean(formik.errors.attribute)}
            variant="outlined"
          >
            <InputLabel id="field-attribute-type-label"> {translate('surveyModusApp.field.attribute.type')}</InputLabel>
            <Select
              fullWidth
              id="field-attribute-type"
              name="attribute.type"
              label={translate('surveyModusApp.field.attribute.type')}
              value={formik.values.attribute?.type}
              onChange={formik.handleChange}
              error={formik.touched.attribute?.type && Boolean(formik.errors.attribute?.type)}
              variant="outlined"
            >
              {Object.keys(type).map(key => {
                return (
                  <MenuItem key={key} value={key}>
                    {type[key]}
                  </MenuItem>
                );
              })}
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12}>
          <TextField
            fullWidth
            id="field-attribute-defaultValue"
            name="attribute.defaultValue"
            label={translate('surveyModusApp.field.attribute.defaultValue')}
            value={formik.values.attribute?.defaultValue}
            onChange={formik.handleChange}
            error={formik.touched.attribute?.defaultValue && Boolean(formik.errors.attribute?.defaultValue)}
            helperText={formik.touched.attribute?.defaultValue && formik.errors.attribute?.defaultValue}
            variant="outlined"
          />
        </Grid>
      </Grid>
    </SubCard>
  );
};

export default FieldAttributeUpdate;
