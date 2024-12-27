import React, { useEffect } from 'react';
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  Divider,
  FormControl,
  FormControlLabel,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  Switch,
  TextField,
  Typography,
} from '@mui/material';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { IField } from 'app/shared/model/field.model';
import { IconArrowsDown, IconDeviceFloppy } from '@tabler/icons';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';
import FieldLookupUpdate from 'app/entities/field/component/field-lookup-update';
import type, { isLookupType } from 'app/shared/model/enumerations/type.model';
import fieldWizardUpdateFormik from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-update.formik';

interface IFieldWizardUpdateModalProps {
  field: IField;
  items: IField[];
  setItems: (items: IField[]) => void;
}

const FieldWizardUpdateModal =
  (props: IFieldWizardUpdateModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const { field, items } = props;

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

    const formik = fieldWizardUpdateFormik({ items, setItems: props.setItems, handleClose });

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
                      <InputLabel id="attribute.type">Type</InputLabel>
                      <Select
                        labelId="attribute.type"
                        id="attribute.type"
                        name="attribute.type"
                        value={formik.values.attribute?.type}
                        onChange={formik.handleChange}
                        error={formik.touched.attribute?.type && Boolean(formik.errors.attribute?.type)}
                        variant="standard"
                      >
                        {Object.keys(type).map(key => (
                          <MenuItem key={key} value={key}>
                            {type[key]}
                          </MenuItem>
                        ))}
                      </Select>
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
                  {isLookupType(formik.values.attribute?.type) && (
                    <>
                      <Grid item xs={12} style={{ marginTop: theme.spacing(3) }}>
                        <Divider variant="middle">
                          <IconArrowsDown size={'1rem'} /> &nbsp; Lookup Values{' '}
                        </Divider>
                      </Grid>
                      <Grid item xs={12}>
                        <FieldLookupUpdate formik={formik} />
                      </Grid>
                    </>
                  )}
                </Grid>
              </Grid>
            </Grid>
          </form>
        </DialogContent>
        <DialogActions>
          <AnimateButton>
            <Button
              onClick={() => {
                formik.handleSubmit();
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
