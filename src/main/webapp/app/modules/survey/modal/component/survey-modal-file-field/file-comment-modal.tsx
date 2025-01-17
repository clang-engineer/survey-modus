import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import useMediaQuery from '@mui/material/useMediaQuery';

import { TextField, Typography } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import { IFile } from 'app/shared/model/file.model';

interface IFileCommentModalProps {
  file: IFile;
}

const FileCommentModal =
  (props: IFileCommentModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const { file } = props;

    const [localComment, setLocalComment] = React.useState<string>('');

    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

    React.useEffect(() => {
      if (file) {
        setLocalComment(file['comment']);
      }
    }, [file]);

    const handleClose = () => {
      onReject();
    };

    return (
      <Dialog fullScreen={fullScreen} open={isOpen} onClose={handleClose} aria-labelledby="responsive-dialog-title">
        <DialogTitle id="responsive-dialog-title">
          <Typography variant="h6">File Comment</Typography>
        </DialogTitle>
        <DialogContent>
          <DialogContentText>
            <TextField
              id="comment"
              name="comment"
              label="Comment"
              multiline
              rows={4}
              fullWidth
              value={localComment}
              onChange={e => setLocalComment(e.target.value)}
            />
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => {
              onResolve(false);
              handleClose();
            }}
          >
            Cancel
          </Button>
          <Button
            onClick={() => {
              onResolve(localComment);
            }}
          >
            Save
          </Button>
        </DialogActions>
      </Dialog>
    );
  };

export default FileCommentModal;
