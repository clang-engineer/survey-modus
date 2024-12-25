import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { IField } from 'app/shared/model/field.model';
import IconButton from '@mui/material/IconButton';
import { IconEdit, IconTrash, IconDeviceFloppy } from '@tabler/icons';
import ButtonGroup from '@mui/material/ButtonGroup';

interface IFieldWizardUpdateModalProps {
  field: IField;
}

const FieldWizardUpdateModal =
  (props: IFieldWizardUpdateModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const { field } = props;

    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

    const handleClose = () => {
      onReject();
    };

    return (
      <Dialog fullScreen={fullScreen} open={isOpen} onClose={handleClose} aria-labelledby="responsive-dialog-title">
        <DialogTitle id="responsive-dialog-title">{field.id ? 'Update Field' : 'Create Field'}</DialogTitle>
        <DialogContent>
          <DialogContentText>
            {field.id ? 'Are you sure you want to update this Field?' : 'Are you surea you want to create this Field?'}
            {JSON.stringify(field)}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <ButtonGroup variant="text" size="small">
            <IconButton
              onClick={() => {
                onResolve(false);
                handleClose();
              }}
            >
              <IconTrash size={'1rem'} />
            </IconButton>
            <IconButton
              onClick={() => {
                onResolve(true);
                handleClose();
              }}
              autoFocus
            >
              <IconDeviceFloppy size={'1rem'} />
            </IconButton>
          </ButtonGroup>
        </DialogActions>
      </Dialog>
    );
  };

export default FieldWizardUpdateModal;
