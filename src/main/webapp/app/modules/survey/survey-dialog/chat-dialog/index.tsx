import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';

import { Grid, IconButton, TextField } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import PerfectScrollbar from 'react-perfect-scrollbar';

import { IconSend } from '@tabler/icons';
import { FormikProps } from 'formik';

interface IDocumentChatModalProps {
  formik: FormikProps<Record<string, any>>;
}

const DocumentChatModal =
  (props: IDocumentChatModalProps) =>
  ({ isOpen, onResolve, onReject }) => {
    const { formik } = props;

    const theme = useTheme();

    const [messages, setMessages] = React.useState([]);
    const [comment, setComment] = React.useState('');

    React.useEffect(() => {
      if (formik.values.messages) {
        setMessages(formik.values.messages);
      }
    }, [formik]);

    const handleClose = () => {
      onReject();
    };

    return (
      <Dialog open={isOpen} onClose={handleClose}>
        <DialogContent>
          <PerfectScrollbar options={{ suppressScrollX: true }}>
            <DialogContentText>
              <Grid container spacing={1} minWidth="400px" minHeight="400px">
                {messages.map((message, index) => {
                  return (
                    <Grid item xs={12} key={index}>
                      {index + 1}. {message}
                    </Grid>
                  );
                })}
              </Grid>
            </DialogContentText>
          </PerfectScrollbar>
        </DialogContent>
        <DialogActions>
          <TextField id="comment" name="comment" fullWidth size="small" value={comment} onChange={e => setComment(e.target.value)} />
          <IconButton
            size="small"
            onClick={() => {
              formik.setFieldValue('messages', [...messages, comment]);
            }}
          >
            <IconSend size={'1rem'} />
          </IconButton>
        </DialogActions>
      </Dialog>
    );
  };

export default DocumentChatModal;
