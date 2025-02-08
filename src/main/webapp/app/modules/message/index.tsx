import React, { useState } from 'react';

import { useTheme } from '@mui/material/styles';
import { Fab, IconButton, Tooltip } from '@mui/material';
import { IconSettings } from '@tabler/icons';

import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

import { create } from 'react-modal-promise';
import ChatDialog from 'app/modules/message/chat-dialog';

const ChatBoxFab = () => {
  const theme = useTheme();

  const noFabClick = () => {
    create(ChatDialog)();
  };

  return (
    <>
      <Tooltip title="Leave a message">
        <Fab
          component="div"
          onClick={noFabClick}
          size="medium"
          variant="circular"
          color="secondary"
          sx={{
            borderRadius: 0,
            borderTopLeftRadius: '50%',
            borderBottomLeftRadius: '50%',
            borderTopRightRadius: '50%',
            borderBottomRightRadius: '4px',
            bottom: 10,
            position: 'fixed',
            right: 10,
            zIndex: 1200,
            boxShadow: theme.customShadows.secondary,
          }}
        >
          <AnimateButton type="rotate">
            <IconButton color="inherit" size="large" disableRipple aria-label="live customize">
              <IconSettings />
            </IconButton>
          </AnimateButton>
        </Fab>
      </Tooltip>
    </>
  );
};

export default ChatBoxFab;
