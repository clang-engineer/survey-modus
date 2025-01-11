import React from 'react';

import { Button, Dialog, DialogActions, DialogContent, Box, Typography } from '@mui/material';
import useFormWizardConfig from 'app/modules/wizard/form-wizard/form-wizard.config';

const FormListUpdateModal = React.forwardRef((props, ref) => {
  React.useImperativeHandle(ref, () => ({
    open: handleOpen,
    close: handleClose,
  }));

  const [open, setOpen] = React.useState(false);

  const { items } = useFormWizardConfig();

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  return (
    <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
      <DialogContent>
        {items.map((item, index) => {
          return <div key={index}> {item.title} </div>;
        })}
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose} color="primary">
          Cancel
        </Button>
        <Button
          onClick={() => {
            handleClose();
          }}
          color="primary"
        >
          Update
        </Button>
      </DialogActions>
    </Dialog>
  );
});

export default FormListUpdateModal;
