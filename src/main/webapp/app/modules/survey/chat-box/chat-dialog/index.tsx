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
import { useAppSelector } from 'app/config/store';
import MessageBox from 'app/modules/survey/dialog/survey-chat-dialog/message-box';
import SlideTransition from 'app/shared/component/slide-transition';
import PaperComponent from 'app/shared/component/draggable-dialog';

const ChatDialog = ({ isOpen, onResolve, onReject }) => {
  const companyEntity = useAppSelector(state => state.company.entity);

  const theme = useTheme();
  const account = useAppSelector(state => state.authentication.account);

  const [messages, setMessages] = React.useState([]);
  const [comment, setComment] = React.useState('');

  const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

  React.useLayoutEffect(() => {
    if (isOpen) {
      scrollToBottom();
    }
  }, [isOpen, messages]);

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
    setComment('');
  };

  const onMessageDelete = (index: number) => {
    const newMessages = [...messages];
    newMessages.splice(index, 1);
  };

  const handleClose = () => {
    onReject();
  };

  return (
    <Dialog
      TransitionComponent={SlideTransition}
      PaperComponent={!fullScreen ? PaperComponent : undefined}
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
        id="draggable-dialog-title"
        style={{
          cursor: 'move',
          backgroundColor: theme.palette.grey[100],
          padding: '8px',
          borderBottom: `1px solid ${theme.palette.grey[300]}`,
        }}
      >
        <Typography variant="h6" component="div" style={{ padding: '8px' }}>
          {companyEntity?.title} Chat
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
};

export default ChatDialog;
