import React from 'react';

import { Box, IconButton, Tooltip, Typography } from '@mui/material';
import { IFile } from 'app/shared/model/file.model';

import { IconDownload, IconMessage, IconMessageOff, IconTrash } from '@tabler/icons';

import { FormikProps } from 'formik';

import { useTheme } from '@mui/material/styles';

interface IFileFieldListBoxProps {
  files: IFile[];
  formik: FormikProps<Record<string, any>>;
  onRemoveButtonClick: (file: IFile) => void;
  onDownloadButtonClick: (file: IFile) => void;
  onFileCommentButtonClick: (file: IFile) => void;
}

const FileFieldListBox = (props: IFileFieldListBoxProps) => {
  const theme = useTheme();

  const { files } = props;

  const isCommentExist = React.useCallback((file: IFile) => {
    return file['comment'] && file['comment'].length > 0;
  }, []);

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
            <Tooltip title={file['comment'] || 'No comment'} enterDelay={100} leaveDelay={100} placement="right" arrow>
              <IconButton
                size="small"
                sx={{
                  color: isCommentExist(file) ? theme.palette.text.primary : theme.palette.text.disabled,
                }}
                onClick={() => {
                  props.onFileCommentButtonClick(file);
                }}
              >
                {isCommentExist(file) ? <IconMessage size={'12'} /> : <IconMessageOff size={'12'} />}
              </IconButton>
            </Tooltip>
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
