import React from 'react';

import { Box, Divider, Typography } from '@mui/material';
import { useTheme } from '@mui/material/styles';
import dayjs from 'dayjs';
import { IconMessage } from '@tabler/icons';

interface IMessageBoxProps {
  messages: any[];
  currentIndex: any;
}

const MessageBox = (props: IMessageBoxProps) => {
  const theme = useTheme();
  const { messages, currentIndex } = props;

  const currentMessage = messages[currentIndex];

  const showDate = currentIndex === 0 || dayjs(currentMessage.createdDate).diff(dayjs(messages[currentIndex - 1].createdDate), 'day') > 0;

  return (
    <>
      {showDate && (
        <Divider sx={{ marginBottom: theme.spacing(2) }}>
          <Typography variant="subtitle2" align="center" sx={{ color: theme.palette.text.secondary }}>
            {dayjs(currentMessage.createdDate).format('YYYY-MM-DD')}
          </Typography>
        </Divider>
      )}
      <Box marginBottom={3}>
        <Box display="flex" alignItems="center" sx={{ color: theme.palette.text.secondary }} marginBottom={1}>
          <IconMessage size={'1rem'} />
          &nbsp;
          <Typography variant="caption">{currentMessage.createdBy}</Typography>&nbsp;
          <Typography variant="caption">{dayjs(currentMessage.createdDate).format('HH:mm:ss')}</Typography>
        </Box>
        <Box
          border={1}
          borderRadius={2}
          padding={2}
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
      </Box>
    </>
  );
};

export default MessageBox;
