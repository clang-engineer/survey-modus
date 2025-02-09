import React from 'react';

import { useTheme } from '@mui/material/styles';
import { Fab, IconButton, Tooltip } from '@mui/material';
import { IconBrandHipchat } from '@tabler/icons';

import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

import { create } from 'react-modal-promise';
import ChatDialog from 'app/modules/survey/fab/message/chat-dialog';

const ChatBoxFab = () => {
  const theme = useTheme();

  const noFabClick = () => {
    create(ChatDialog)();
  };

  return (
    <>
      <Tooltip title="Leave a message" arrow={true} placement="left">
        <Fab
          component="div"
          onClick={noFabClick}
          size="small"
          variant="circular"
          color="secondary"
          sx={{
            borderRadius: 0,
            borderTopLeftRadius: '50%',
            borderBottomLeftRadius: '50%',
            borderTopRightRadius: '50%',
            borderBottomRightRadius: '4px',
            bottom: 20,
            position: 'fixed',
            right: 20,
            zIndex: 1200,
            boxShadow: theme.customShadows.secondary,
          }}
        >
          <AnimateButton>
            <IconButton color="inherit" size="small" disableRipple aria-label="live customize">
              <IconBrandHipchat />
            </IconButton>
          </AnimateButton>
        </Fab>
      </Tooltip>
    </>
  );
};

export default ChatBoxFab;
