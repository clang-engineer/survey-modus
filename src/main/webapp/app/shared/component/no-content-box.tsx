import React from 'react';

import { Box, Typography } from '@mui/material';

import { useTheme } from '@mui/material/styles';

interface INoContentBoxProps {
  title?: string;
  height?: string | number;
}

const NoContentBox = (props: INoContentBoxProps) => {
  const theme = useTheme();

  const { title, height } = props;

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      sx={{
        height: height ? height : '100%',
        backgroundColor: theme.palette.grey[50],
        borderRadius: 1,
        borderStyle: 'dashed',
        borderColor: theme.palette.grey[300],
      }}
    >
      <Typography variant="h5">{title ? title : 'No Content Available'}</Typography>
    </Box>
  );
};

export default NoContentBox;
