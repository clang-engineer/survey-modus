import * as React from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';

import { TextField } from '@mui/material';
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

    React.useEffect(() => {
      if (file) {
        setLocalComment(file['comment']);
      }
    }, [file]);

    const handleClose = () => {
      onReject();
    };

    return (
      <Dialog open={isOpen} onClose={handleClose}>
        <DialogContent>
          <DialogContentText minWidth="400px">
            <TextField
              id="comment"
              name="comment"
              label={`${file.name}`}
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
            size="small"
            onClick={() => {
              handleClose();
            }}
          >
            Cancel
          </Button>
          <Button
            size="small"
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
