import React from 'react';
import { Box, Button, ButtonGroup, Divider, Typography, IconButton } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import dayjs from 'dayjs';
import { IconPencil, IconTrash } from '@tabler/icons';
import { useAppSelector } from 'app/config/store';

interface IMessageBoxProps {
  messages: any[];
  currentIndex: any;
  onDeleteMessage: (index: number) => void;
}

const MessageBox = (props: IMessageBoxProps) => {
  const theme = useTheme();
  const { messages, currentIndex } = props;

  const account = useAppSelector(state => state.authentication.account);

  const currentMessage = messages[currentIndex];
  const showDate = currentIndex === 0 || dayjs(currentMessage.createdDate).diff(dayjs(messages[currentIndex - 1].createdDate), 'day') > 0;

  const isCurrentUser = currentMessage.createdBy === account.login;
  // const isCurrentUser = Math.random() > 0.5;

  const labelColor = theme.palette.text.secondary;

  return (
    <Box marginBottom={theme.spacing(1)}>
      {showDate && (
        <Divider sx={{ marginBottom: theme.spacing(2) }}>
          <Typography variant="subtitle2" align="center">
            {dayjs(currentMessage.createdDate).format('YYYY-MM-DD')}
          </Typography>
        </Divider>
      )}
      {!isCurrentUser ? (
        <Box display="flex">
          <Typography variant="caption">{currentMessage.createdBy}</Typography>
        </Box>
      ) : null}
      <Box display="flex" justifyContent={isCurrentUser ? 'flex-end' : 'flex-start'}>
        {isCurrentUser ? (
          <IconButton
            onClick={() => {
              props.onDeleteMessage(currentIndex);
            }}
          >
            <IconTrash size={'12px'} color={labelColor} />
          </IconButton>
        ) : null}
        <Box
          sx={{
            position: 'relative',
            maxWidth: '70%',
            padding: theme.spacing(1.5),
            borderRadius: isCurrentUser ? '20px 20px 0 20px' : '20px 20px 20px 0',
            backgroundColor: isCurrentUser ? theme.palette.primary.main : theme.palette.grey[300],
            color: isCurrentUser ? theme.palette.primary.contrastText : theme.palette.text.primary,
            boxShadow: theme.shadows[2],
            wordBreak: 'break-word',
          }}
        >
          <Typography variant="body1">{currentMessage.message}</Typography>
        </Box>
      </Box>
      <Box display="flex" justifyContent={isCurrentUser ? 'flex-end' : 'flex-start'}>
        <Typography color={labelColor} variant="caption">
          {dayjs(currentMessage.createdDate).format('HH:mm A')}
        </Typography>
      </Box>
    </Box>
  );
};

export default MessageBox;
