import React from 'react';

import { Box, Typography } from '@mui/material';

import { useTheme } from '@mui/material/styles';

interface INoContentBoxProps {
  title?: string | JSX.Element;
  height?: string | number;
}

const NoContentBox = (props: INoContentBoxProps) => {
  const theme = useTheme();

  const { title, height = 100 } = props;

  return (
    <Box
      display="flex"
      justifyContent="center"
      alignItems="center"
      sx={{
        width: '100%',
        height: height ? height : '100%',
        backgroundColor: theme.palette.grey[50],
        borderRadius: 1,
        borderStyle: 'dotted',
        borderColor: theme.palette.grey[300],
        borderWidth: 2,
      }}
    >
      <Typography variant="h5">{title ? title : 'No Content Available'}</Typography>
    </Box>
  );
};

export default NoContentBox;
