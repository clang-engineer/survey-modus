import React, { useEffect, useState } from 'react';
import { IField } from 'app/shared/model/field.model';

import { Box, Button, ButtonGroup, Grid, TextField } from '@mui/material';

import { FormikProps } from 'formik';

import { IconCodeMinus, IconCodePlus } from '@tabler/icons';
import NoContentBox from 'app/shared/component/no-content-box';
import FormikErrorText from 'app/shared/component/formik-error-text';
import { useTheme } from '@mui/material/styles';

interface IFieldLookupUpdateProps {
  formik: FormikProps<IField>;
}

const FieldLookupUpdate = (props: IFieldLookupUpdateProps) => {
  const theme = useTheme();
  const { formik } = props;

  const [localLookups, setLocalLookups] = useState<string[]>([]);

  useEffect(() => {
    setLocalLookups(formik.values.lookups || []);
  }, [formik.values.lookups]);

  const addLookup = () => {
    setLocalLookups([...localLookups, '']);
    formik.setFieldValue('lookups', [...localLookups, '']);
  };

  const removeLookup = (index: number) => {
    setLocalLookups(localLookups.filter((_, i) => i !== index));
    formik.setFieldValue(
      'lookups',
      localLookups.filter((_, i) => i !== index)
    );
  };

  const isLookupsEmpty = !formik.values.lookups || formik.values.lookups.length === 0;

  return (
    <Box
      sx={{
        borderWidth: 1,
        borderColor: theme.palette.grey[300],
        borderRadius: 2,
        borderStyle: 'dashed',
        width: '100%',
        padding: 2,
      }}
    >
      <Grid container spacing={2}>
        {formik.values.lookups?.map((lookup, index) => (
          <Grid item xs={12} key={index}>
            <Box display="flex" alignItems="center">
              <TextField
                fullWidth
                id={`field-lookups-${index}-key`}
                label={`lookup-${index}`}
                name={`lookups[${index}]`}
                value={lookup}
                onChange={formik.handleChange}
                error={formik.touched.lookups && Boolean(formik.errors.lookups)}
              />
              {index === formik.values.lookups.length - 1 && (
                <ButtonGroup orientation="vertical" variant="text" sx={{ '& .MuiButtonBase-root': { padding: 0 } }}>
                  <Button onClick={addLookup}>
                    <IconCodePlus size={'1rem'} />
                  </Button>
                  <Button
                    onClick={() => {
                      removeLookup(index);
                    }}
                  >
                    <IconCodeMinus size={'1rem'} />
                  </Button>
                </ButtonGroup>
              )}
            </Box>
          </Grid>
        ))}
        <Grid container item xs={12}>
          {isLookupsEmpty && (
            <NoContentBox
              title={
                <Button onClick={addLookup} variant="text" color="primary" startIcon={<IconCodePlus size={'1rem'} />}>
                  {' '}
                  Add Lookup{' '}
                </Button>
              }
              height={100}
            />
          )}
        </Grid>
        <FormikErrorText formik={formik} fieldName={'lookups'} />
      </Grid>
    </Box>
  );
};

export default FieldLookupUpdate;
