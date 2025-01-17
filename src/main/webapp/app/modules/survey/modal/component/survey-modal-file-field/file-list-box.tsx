import React from 'react';

import { Box, IconButton, Typography } from '@mui/material';
import { IFile } from 'app/shared/model/file.model';

import { IconDownload, IconTrash } from '@tabler/icons';

interface IFileFieldListBoxProps {
  files: IFile[];
  onRemoveButtonClick: (file: IFile) => void;
  onDownloadButtonClick: (file: IFile) => void;
}
const FileFieldListBox = (props: IFileFieldListBoxProps) => {
  const { files } = props;

  return (
    <Box>
      {files.length > 0 && <Typography variant="h6">Uploaded Files:</Typography>}
      {files.map((file: IFile, index) => (
        <Box display="flex" alignItems="center" key={index}>
          <Typography variant="body2">
            {file.name} ({file.size} bytes)
          </Typography>
          &nbsp;
          <IconButton
            size="small"
            onClick={() => {
              props.onDownloadButtonClick(file);
            }}
          >
            <IconDownload size={'12px'} />
          </IconButton>
          <IconButton
            size="small"
            onClick={() => {
              props.onRemoveButtonClick(file);
            }}
          >
            <IconTrash size={'12px'} />
          </IconButton>
        </Box>
      ))}
    </Box>
  );
};

export default FileFieldListBox;
