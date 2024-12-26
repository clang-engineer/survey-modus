import React, { useEffect } from 'react';
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  FormControl,
  FormControlLabel,
  Grid,
  Switch,
  TextField,
  Typography,
} from '@mui/material';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { defaultValue, IField } from 'app/shared/model/field.model';
import { IconDeviceFloppy } from '@tabler/icons';

import { useFormik } from 'formik';
import * as yup from 'yup';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

interface IFieldWizardUpdateModalProps {
  field: IField;
}

const FieldWizardUpdateModal =
  (props: IFieldWizardUpdateModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const { field } = props;

    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

    useEffect(() => {
      if (isOpen) {
        formik.setValues(field);
      } else {
        formik.resetForm();
      }
    }, [isOpen]);

    const handleClose = () => {
      onReject();
    };

    const formik = useFormik<IField>({
      initialValues: defaultValue,
      validationSchema: yup.object({
        title: yup.string().required('Title is required'),
        description: yup.string().required('Description is required'),
        activated: yup.boolean().required('Activated is required'),
        form: yup.object({}),
        attribute: yup.object({
          type: yup.string().required('Type is required'),
          defaultValue: yup.string().required('Default Value is required'),
        }),
      }),
      onSubmit: values => {
        console.log(values);
      },
    });

    return (
      <Dialog fullScreen={fullScreen} open={isOpen} onClose={handleClose} aria-labelledby="responsive-dialog-title">
        <DialogContent>
          {/*<DialogContentText id="alert-dialog-description" mb={1}/>*/}
          <form onSubmit={formik.handleSubmit}>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <FormControl fullWidth>
                  <TextField
                    fullWidth
                    id="title"
                    name="title"
                    label="Title"
                    value={formik.values.title}
                    onChange={formik.handleChange}
                    error={formik.touched.title && Boolean(formik.errors.title)}
                    helperText={formik.touched.title && formik.errors.title}
                    variant="standard"
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <FormControl fullWidth>
                  <TextField
                    fullWidth
                    id="description"
                    name="description"
                    label="Description"
                    value={formik.values.description}
                    onChange={formik.handleChange}
                    error={formik.touched.description && Boolean(formik.errors.description)}
                    helperText={formik.touched.description && formik.errors.description}
                    variant="standard"
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <FormControl fullWidth>
                  <FormControlLabel
                    control={
                      <Switch
                        id="activated"
                        name="activated"
                        checked={formik.values.activated}
                        onChange={formik.handleChange}
                        size="small"
                      />
                    }
                    label="activated"
                  />
                </FormControl>
              </Grid>
              <Grid item xs={12}>
                <Grid container spacing={2}>
                  <Grid item xs={6}>
                    <FormControl fullWidth>
                      <TextField
                        fullWidth
                        id="attribute.type"
                        name="attribute.type"
                        label="Type"
                        value={formik.values.attribute?.type}
                        onChange={formik.handleChange}
                        error={formik.touched.attribute?.type && Boolean(formik.errors.attribute?.type)}
                        helperText={formik.touched.attribute?.type && formik.errors.attribute?.type}
                        variant="standard"
                      />
                    </FormControl>
                  </Grid>
                  <Grid item xs={6}>
                    <FormControl fullWidth>
                      <TextField
                        fullWidth
                        id="attribute.defaultValue"
                        name="attribute.defaultValue"
                        label="Default Value"
                        value={formik.values.attribute?.defaultValue}
                        onChange={formik.handleChange}
                        error={formik.touched.attribute?.defaultValue && Boolean(formik.errors.attribute?.defaultValue)}
                        helperText={formik.touched.attribute?.defaultValue && formik.errors.attribute?.defaultValue}
                        variant="standard"
                      />
                    </FormControl>
                  </Grid>
                </Grid>
              </Grid>
            </Grid>
          </form>
        </DialogContent>
        <DialogActions>
          <AnimateButton>
            <Button
              onClick={() => {
                // onResolve(true);
                // handleClose();
              }}
              autoFocus
            >
              <IconDeviceFloppy size={'1rem'} /> &nbsp;
              <Typography variant="button">Save</Typography>
            </Button>
          </AnimateButton>
        </DialogActions>
      </Dialog>
    );
  };

export default FieldWizardUpdateModal;
