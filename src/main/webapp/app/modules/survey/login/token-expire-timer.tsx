import React from 'react';

import { Box, Typography } from '@mui/material';

const formatTime = (seconds: number) => {
  const minutes = Math.floor(seconds / 60);
  const remainingSeconds = seconds % 60;
  return `${String(minutes).padStart(2, '0')}:${String(remainingSeconds).padStart(2, '0')}`;
};

const TokenExpireTimer = React.forwardRef((props, ref) => {
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

  const timeoutStatusCommnet = React.useMemo(() => {
    switch (timeLeft) {
      case -1:
        return 'Token not requested';
      case 0:
        return 'Token has expired';
      case 180:
      default:
        return `${formatTime(timeLeft)}`;
    }
  }, [timeLeft]);

  return (
    <Box display="flex" justifyContent="flex-end">
      <Typography variant="body2" color="primary">
        {timeoutStatusCommnet}
      </Typography>
    </Box>
  );
});

export default TokenExpireTimer;
