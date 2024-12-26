import React from 'react';
import { Box, Typography } from '@mui/material';

interface ISurveyModalTextFieldProps {
  title: string;
  description: string;
}

const SurveyModalTextField = (props: ISurveyModalTextFieldProps) => {
  const { title, description } = props;
  return (
    <Box display="flex">
      <Typography variant="h5">{title}</Typography> &nbsp;&nbsp;
      <Typography variant="caption">{description}</Typography>
    </Box>
  );
};

export default SurveyModalTextField;
