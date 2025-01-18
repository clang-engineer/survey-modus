import React from 'react';

import { Box, Button, ButtonGroup, Divider, Typography } from '@mui/material';
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

  const labelColor = theme.palette.text.secondary;

  return (
    <Box>
      {showDate && (
        <Divider sx={{ marginBottom: theme.spacing(2) }}>
          <Typography variant="subtitle2" align="center">
            {dayjs(currentMessage.createdDate).format('YYYY-MM-DD')}
          </Typography>
        </Divider>
      )}
      <Box marginBottom={2}>
        <Box display="flex" alignItems="center" justifyContent={isCurrentUser ? 'flex-start' : 'flex-end'}>
          <Typography variant="caption">{currentMessage.createdBy}</Typography>
          {isCurrentUser && (
            <ButtonGroup size="small" variant="text">
              <Button>
                <IconPencil size={'12px'} color={labelColor} />
              </Button>
              <Button
                onClick={() => {
                  props.onDeleteMessage(currentIndex);
                }}
              >
                <IconTrash size={'12px'} color={labelColor} />
              </Button>
            </ButtonGroup>
          )}
        </Box>
        <Box
          border={1}
          borderRadius={2}
          padding={2}
          borderColor={theme.palette.grey[300]}
          sx={{
            overflowWrap: 'break-word',
            wordWrap: 'break-word',
            wordBreak: 'break-word',
            hyphens: 'auto',
            backgroundColor: theme.palette.background.default,
          }}
        >
          <Typography variant="body1">{currentMessage.message}</Typography>
        </Box>
        <Box display="flex" justifyContent={isCurrentUser ? 'flex-end' : 'flex-start'}>
          <Typography color={labelColor} variant="caption">
            {dayjs(currentMessage.createdDate).format('HH:mm:ss')}
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default MessageBox;
