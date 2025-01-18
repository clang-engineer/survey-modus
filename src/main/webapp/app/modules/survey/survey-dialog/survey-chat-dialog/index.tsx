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
import { useAppSelector } from 'app/config/store';

import dayjs from 'dayjs';

interface IDocumentChatModalProps {
  formik: FormikProps<Record<string, any>>;
}

const DocumentChatDialog = React.forwardRef((props: IDocumentChatModalProps, ref: React.Ref<any>) => {
  React.useImperativeHandle(ref, () => ({
    open: handleOpen,
    close: handleClose,
  }));

  const { formik } = props;

  const theme = useTheme();
  const account = useAppSelector(state => state.authentication.account);

  const [isOpen, setIsOpen] = React.useState(false);
  const [messages, setMessages] = React.useState([]);
  const [comment, setComment] = React.useState('');

  React.useEffect(() => {
    if (formik.values.messages) {
      setMessages(formik.values.messages);
    }
  }, [formik]);

  const handleOpen = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    setIsOpen(false);
  };

  const MessageBox = (props: { message: any }) => {
    const { message } = props;

    return (
      <Grid item xs={12}>
        <Grid container spacing={1}>
          <Grid item xs={12}>
            <strong>{message.createdBy}</strong>
          </Grid>
          <Grid item xs={12}>
            {message.message}
          </Grid>
          <Grid item xs={12}>
            <small>{dayjs(message.createdDate).format('YYYY-MM-DD HH:mm')}</small>
          </Grid>
        </Grid>
      </Grid>
    );
  };

  return (
    <Dialog open={isOpen} onClose={handleClose}>
      <DialogContent>
        <PerfectScrollbar options={{ suppressScrollX: true }}>
          <DialogContentText>
            <Grid container spacing={1} minWidth="400px" minHeight="400px">
              {messages.map((message, index) => {
                return <MessageBox key={index} message={message} />;
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
            formik.setFieldValue('messages', [
              ...messages,
              {
                message: comment,
                createdBy: account.login,
                createdDate: new Date(),
              },
            ]);
          }}
        >
          <IconSend size={'1rem'} />
        </IconButton>
      </DialogActions>
    </Dialog>
  );
});

export default DocumentChatDialog;
