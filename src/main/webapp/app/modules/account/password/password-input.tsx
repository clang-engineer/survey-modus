import React from 'react';

import { Box, FormControl, FormHelperText, IconButton, InputLabel, OutlinedInput } from '@mui/material';
import { Translate, translate } from 'react-jhipster';
import IconVisibility from '@mui/icons-material/Visibility';
import IconVisibilityOff from '@mui/icons-material/VisibilityOff';

import { FormikProps } from 'formik';

interface IPasswordInputProps {
  formik: FormikProps<any>;
  name: string;
  translateKey: string;
}

const PasswordInput = (props: IPasswordInputProps) => {
  const { name, translateKey } = props;
  const [showPassword, setShowPassword] = React.useState(false);

  return (
    <FormControl fullWidth error={Boolean(props.formik.touched[name] && props.formik.errors[name])}>
      <InputLabel htmlFor={'outlined-adornment-' + translateKey + '-login'}>
        <Translate contentKey={translateKey}>Current Password</Translate>
      </InputLabel>
      <Box sx={{ display: 'flex', flexDirection: 'row' }}>
        <OutlinedInput
          id={'outlined-adornment-' + translateKey + '-login'}
          fullWidth
          value={props.formik.values[name]}
          name={translateKey}
          type={showPassword ? 'text' : 'password'}
          onBlur={props.formik.handleBlur}
          onChange={e => props.formik.setFieldValue(name, e.target.value)}
          inputProps={{}}
          label={translate(translateKey)}
        />
        <IconButton onClick={() => setShowPassword(!showPassword)} size="small">
          {showPassword ? <IconVisibilityOff /> : <IconVisibility />}
        </IconButton>
      </Box>
      {props.formik.touched[name] && props.formik.errors[name] && (
        <FormHelperText error id={'standard-weight-helper-text-' + translateKey + '-login'}>
          <span dangerouslySetInnerHTML={{ __html: props.formik.errors[name] }} />
        </FormHelperText>
      )}
    </FormControl>
  );
};

export default PasswordInput;
