import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';

import { Box, IconButton, TextField } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import PerfectScrollbar from 'react-perfect-scrollbar';

import { IconSend } from '@tabler/icons';
import { FormikProps } from 'formik';
import { useAppSelector } from 'app/config/store';
import MessageBox from 'app/modules/survey/survey-dialog/survey-chat-dialog/message-box';

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

  React.useLayoutEffect(() => {
    if (isOpen) {
      scrollToBottom();
    }
  }, [isOpen, messages]);

  const handleOpen = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    setIsOpen(false);
  };

  const scrollToBottom = () => {
    setTimeout(() => {
      const dialog = document.querySelector('.dialog-content-scrollable');
      if (dialog) {
        dialog.scrollTo({ top: dialog.scrollHeight, behavior: 'auto' });
      } else {
        console.warn('Scroll target not found.');
      }
    }, 0);
  };

  const onMessageSend = () => {
    formik.setFieldValue('messages', [
      ...messages,
      {
        message: comment,
        createdBy: account.login,
        createdDate: new Date(),
      },
    ]);
    formik.submitForm();
  };

  const onMessageDelete = (index: number) => {
    const newMessages = [...messages];
    newMessages.splice(index, 1);
    formik.setFieldValue('messages', newMessages);
    formik.submitForm();
  };

  return (
    <Dialog
      open={isOpen}
      onClose={handleClose}
      sx={{
        '& .dialog-content-scrollable': {
          padding: '8px',
          marginBottom: 1,
        },
      }}
    >
      <DialogContent className="dialog-content-scrollable" sx={{ '& .ps__rail-y': { display: 'none' } }}>
        <PerfectScrollbar>
          <DialogContentText maxWidth="500px" maxHeight="600px">
            <Box width="500px">
              {messages
                .filter(m => m)
                .sort((a, b) => a.createdDate - b.createdDate)
                .map((message, index) => {
                  return <MessageBox key={index} messages={messages} currentIndex={index} onDeleteMessage={onMessageDelete} />;
                })}
            </Box>
          </DialogContentText>
        </PerfectScrollbar>
      </DialogContent>
      <DialogActions>
        <TextField
          id="comment"
          name="comment"
          fullWidth
          size="small"
          value={comment}
          onChange={e => setComment(e.target.value)}
          onKeyDown={e => {
            if (e.key === 'Enter') {
              onMessageSend();
            }
          }}
        />
        <IconButton
          color="primary"
          size="small"
          onClick={() => {
            onMessageSend();
          }}
        >
          <IconSend size={'1rem'} />
        </IconButton>
      </DialogActions>
    </Dialog>
  );
});

export default DocumentChatDialog;
