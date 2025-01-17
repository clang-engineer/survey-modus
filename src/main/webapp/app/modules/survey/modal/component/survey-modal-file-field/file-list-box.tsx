import React from 'react';

import { Box, IconButton, Typography, TextField } from '@mui/material';
import { IFile } from 'app/shared/model/file.model';

import { IconDownload, IconTrash, IconMessage } from '@tabler/icons';

import { FormikProps } from 'formik';

interface IFileFieldListBoxProps {
  files: IFile[];
  formik: FormikProps<Record<string, any>>;
  onRemoveButtonClick: (file: IFile) => void;
  onDownloadButtonClick: (file: IFile) => void;
  onFileCommentButtonClick: (file: IFile) => void;
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
              <IconDownload size={'12'} />
            </IconButton>
            <IconButton
              size="small"
              onClick={() => {
                props.onRemoveButtonClick(file);
              }}
            >
              <IconTrash size={'12'} />
            </IconButton>
            <IconButton
              size="small"
              onClick={() => {
                props.onFileCommentButtonClick(file);
              }}
            >
              <IconMessage size={'12'} />
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
