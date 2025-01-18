import * as React from 'react';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';

import { Box, IconButton, TextField, Typography } from '@mui/material';
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

  const perfectScrollbarRef = React.useRef(null);
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
    scrollToBottom();
  }, [messages]);

  const handleOpen = () => {
    setIsOpen(true);
  };

  const handleClose = () => {
    setIsOpen(false);
  };

  const scrollToBottom = () => {
    setTimeout(() => {
      if (perfectScrollbarRef.current) {
        const psInstance = perfectScrollbarRef.current;
        if (psInstance && psInstance.scrollTo) {
          // PerfectScrollbar의 scrollTo 메서드를 호출
          psInstance.scrollTo(0, psInstance.scrollHeight);
        } else if (psInstance._container) {
          // _container 직접 접근
          const container = psInstance._container;
          container.scrollTop = container.scrollHeight;
        }
      }
    }, 100);
  };

  const MessageBox = (props: { message: any }) => {
    const { message } = props;

    return (
      <Box bgcolor="background.paper" boxShadow={1} marginBottom={1}>
        <Box p={1}>
          <Typography variant="body1">{message.message}</Typography>
        </Box>
        <Box p={1} display="flex">
          <Typography variant="caption">{message.createdBy}</Typography>&nbsp;
          <Typography variant="caption">{dayjs(message.createdDate).format('YYYY-MM-DD HH:mm')}</Typography>
        </Box>
      </Box>
    );
  };

  return (
    <Dialog open={isOpen} onClose={handleClose}>
      <DialogContent
        sx={{
          '& .ps__rail-y': {
            display: 'none',
          },
        }}
      >
        <PerfectScrollbar
          ref={perfectScrollbarRef}
          style={{
            maxHeight: '600px',
            overflowY: 'auto',
          }}
        >
          <DialogContentText>
            <Box width="500px">
              {messages
                .filter(m => m)
                .sort((a, b) => a.createdDate - b.createdDate)
                .map((message, index) => {
                  return <MessageBox key={index} message={message} />;
                })}
            </Box>
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
            formik.submitForm();
          }}
        >
          <IconSend size={'1rem'} />
        </IconButton>
      </DialogActions>
    </Dialog>
  );
});

export default DocumentChatDialog;
