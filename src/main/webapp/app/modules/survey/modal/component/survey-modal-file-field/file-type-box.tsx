import React from 'react';

import { Box, Typography } from '@mui/material';

const FileTypeBox = () => {
  return (
    <Box display="flex" flexDirection="column">
      <Typography variant="caption" color="textSecondary">
        <strong>Accepted file types:</strong> .png, .jpg, .jpeg, .gif, .pdf, .doc, .docx, .xls, .xlsx, .ppt, .pptx, .txt, .zip
      </Typography>
      <Typography variant="caption" color="textSecondary">
        <strong>Max file size:</strong> 10MB
      </Typography>
    </Box>
  );
};

export default FileTypeBox;
