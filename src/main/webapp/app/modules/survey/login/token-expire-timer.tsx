import React from 'react';

import { Box, IconButton, Typography } from '@mui/material';

import { IconRefresh } from '@tabler/icons';

const formatTime = (seconds: number) => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
};

interface ITokenExpireTimerProps {
  reissueOtp: () => void;
}

const TokenExpireTimer = React.forwardRef((props: ITokenExpireTimerProps, ref) => {
  React.useImperativeHandle(ref, () => ({
    resetTimer() {
      setTimeLeft(180);
    },
  }));

  const [timeLeft, setTimeLeft] = React.useState(-1);

  React.useEffect(() => {
    if (timeLeft === 0 || timeLeft === -1) {
      return;
    }

    const timer = setInterval(() => {
      setTimeLeft(prev => (prev && prev > 0 ? prev - 1 : 0));
    }, 1000);

    return () => clearInterval(timer);
  }, [timeLeft]);

  const comment = React.useMemo(() => {
    switch (timeLeft) {
      case 0:
        return 'Token has expired';
      case 180:
      default:
        return `${formatTime(timeLeft)}`;
    }
  }, [timeLeft]);

  if (timeLeft === -1) {
    return null;
  }

  return (
    <Box display="flex" justifyContent="flex-end" alignItems="center">
      <IconButton
        size="small"
        onClick={() => {
          props.reissueOtp();
          setTimeLeft(180);
        }}
      >
        <IconRefresh size="1rem" />
      </IconButton>
      <Typography variant="body2" color="textSecondary">
        {comment}
      </Typography>
    </Box>
  );
});

export default TokenExpireTimer;
