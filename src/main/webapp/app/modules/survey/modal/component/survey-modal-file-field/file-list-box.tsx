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
      <Typography variant="h6" gutterBottom>
        <strong> Uploaded Files: </strong>
      </Typography>
      {files.length > 0 ? (
        files.map((file: IFile, index) => (
          <Box display="flex" alignItems="center" key={index}>
            <Typography variant="caption">
              {file.name} ({file.size} bytes)
            </Typography>
            &nbsp;
            <IconButton
              color="primary"
              size="small"
              onClick={() => {
                props.onDownloadButtonClick(file);
              }}
            >
              <IconDownload size={'10'} />
            </IconButton>
            <IconButton
              size="small"
              onClick={() => {
                props.onRemoveButtonClick(file);
              }}
            >
              <IconTrash size={'10'} />
            </IconButton>
          </Box>
        ))
      ) : (
        <Box>
          <Typography variant="caption" color="textSecondary">
            No files uploaded
          </Typography>
        </Box>
      )}
    </Box>
  );
};

export default FileFieldListBox;
