import React from 'react';
import { AppBar, Box, Button, Dialog, Divider, FormControl, Grid, IconButton, Slide, TextField, Toolbar, Typography } from '@mui/material';

import CloseIcon from '@mui/icons-material/Close';

import { TransitionProps } from '@mui/material/transitions';
import { IForm } from 'app/shared/model/form.model';
import { IField } from 'app/shared/model/field.model';
import { gridSpacing } from 'app/berry/store/constant';

interface IFieldWizardPreviewModalProps {
  form: IForm;
  fields: IField[];
}

const Transition = React.forwardRef(function Transition(
  props: TransitionProps & {
    children: React.ReactElement<unknown>;
  },
  ref: React.Ref<unknown>
) {
  return <Slide direction="up" ref={ref} {...props} />;
});

const SurveyModal =
  (props: IFieldWizardPreviewModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const { form, fields } = props;

    const handleClose = () => {
      onReject();
    };

    return (
      <Dialog fullScreen open={isOpen} onClose={handleClose} TransitionComponent={Transition}>
        <AppBar sx={{ position: 'relative' }}>
          <Toolbar>
            <IconButton edge="start" color="inherit" onClick={handleClose} aria-label="close">
              <CloseIcon />
            </IconButton>
            <Typography sx={{ ml: 2, flex: 1 }} variant="h6" component="div">
              {form.title}
            </Typography>
            <Button autoFocus color="inherit" onClick={handleClose}>
              save
            </Button>
          </Toolbar>
        </AppBar>
        <Grid container spacing={gridSpacing} padding={3}>
          {fields.map((field, index) => (
            <Grid item xs={12} key={field.id}>
              <Box display="flex">
                <Typography variant="h5">{field.title}</Typography> &nbsp;&nbsp;
                <Typography variant="caption">{field.description}</Typography>
              </Box>
              <Box>
                <FormControl fullWidth>
                  <TextField
                    id={`field-${field.id}`}
                    label={field.title}
                    // value={formData[field.id]}
                    onChange={e => {}}
                    variant="standard"
                    fullWidth
                  />
                </FormControl>
              </Box>
              {index < fields.length - 1 && <Divider />}
            </Grid>
          ))}
        </Grid>
      </Dialog>
    );
  };

export default SurveyModal;
