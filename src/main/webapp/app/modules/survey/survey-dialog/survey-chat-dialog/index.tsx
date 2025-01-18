import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';

import { IconButton, TextField, Typography, useMediaQuery } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import PerfectScrollbar from 'react-perfect-scrollbar';

import { IconSend, IconX } from '@tabler/icons';
import { FormikProps } from 'formik';
import { useAppSelector } from 'app/config/store';
import MessageBox from 'app/modules/survey/survey-dialog/survey-chat-dialog/message-box';
import { IForm } from 'app/shared/model/form.model';
import { IDocument } from 'app/shared/model/document.model';
import SlideTransition from 'app/shared/component/slide-transition';

interface IDocumentChatModalProps {
  form: IForm;
  document: IDocument;
  formik: FormikProps<Record<string, any>>;
}

const DocumentChatDialog = React.forwardRef((props: IDocumentChatModalProps, ref: React.Ref<any>) => {
  React.useImperativeHandle(ref, () => ({
    open: handleOpen,
    close: handleClose,
  }));

  const { formik, form } = props;

  const theme = useTheme();
  const account = useAppSelector(state => state.authentication.account);

  const [isOpen, setIsOpen] = React.useState(false);
  const [messages, setMessages] = React.useState([]);
  const [comment, setComment] = React.useState('');

  const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

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
    setComment('');
  };

  const onMessageDelete = (index: number) => {
    const newMessages = [...messages];
    newMessages.splice(index, 1);
    formik.setFieldValue('messages', newMessages);
    formik.submitForm();
  };

  return (
    <Dialog
      TransitionComponent={SlideTransition}
      open={isOpen}
      onClose={handleClose}
      sx={{
        '& .MuiPaper-root': {
          padding: 0,
        },
      }}
      fullScreen={fullScreen}
    >
      <DialogTitle
        style={{
          backgroundColor: theme.palette.grey[100],
          padding: '8px',
          borderBottom: `1px solid ${theme.palette.grey[300]}`,
        }}
      >
        <Typography variant="h6" component="div" style={{ padding: '8px' }}>
          {form.title} : {props.document.id}
        </Typography>
        <IconButton style={{ position: 'absolute', right: '8px', top: '8px' }} onClick={handleClose}>
          <IconX size={'1rem'} />
        </IconButton>
      </DialogTitle>
      <DialogContent
        className="dialog-content-scrollable"
        sx={{ '& .ps__rail-y': { display: 'none' } }}
        style={{
          padding: '8px',
          backgroundColor: theme.palette.grey[100],
          width: fullScreen ? '100%' : '500px',
          height: fullScreen ? '100%' : '600px',
        }}
      >
        <PerfectScrollbar>
          <DialogContentText>
            {messages.length === 0 ? (
              <Typography variant="body1" align="center">
                No messages
              </Typography>
            ) : (
              messages
                .filter(m => m)
                .sort((a, b) => a.createdDate - b.createdDate)
                .map((message, index) => {
                  return <MessageBox key={index} messages={messages} currentIndex={index} onDeleteMessage={onMessageDelete} />;
                })
            )}
          </DialogContentText>
        </PerfectScrollbar>
      </DialogContent>
      <DialogActions
        style={{
          backgroundColor: theme.palette.grey[100],
          padding: '8px',
        }}
      >
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
