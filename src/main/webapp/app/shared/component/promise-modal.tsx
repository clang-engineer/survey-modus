import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

interface IPromiseModalProps {
  title?: string;
  content?: string;
  resolveButtonText?: string;
  rejectButtonText?: string;
}

const PromiseModal =
  (props: IPromiseModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const { title, content, resolveButtonText = 'yes', rejectButtonText = 'no' } = props;

    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

    const handleClose = () => {
      onReject();
    };

    return (
      <Dialog fullScreen={fullScreen} open={isOpen} onClose={handleClose} aria-labelledby="responsive-dialog-title">
        {title && <DialogTitle id="responsive-dialog-title">{title}</DialogTitle>}
        {content && (
          <DialogContent>
            <DialogContentText>{content}</DialogContentText>
          </DialogContent>
        )}
        <DialogActions>
          {rejectButtonText && (
            <Button
              onClick={() => {
                onResolve(false);
                handleClose();
              }}
            >
              {rejectButtonText}
            </Button>
          )}
          {resolveButtonText && (
            <Button
              onClick={() => {
                onResolve(true);
                handleClose();
              }}
              autoFocus
            >
              {resolveButtonText}
            </Button>
          )}
        </DialogActions>
      </Dialog>
    );
  };

export default PromiseModal;
