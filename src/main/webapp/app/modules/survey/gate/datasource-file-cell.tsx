import React from 'react';
import { IFile } from 'app/shared/model/file.model';

import { Box, Divider, IconButton, Tooltip, Typography } from '@mui/material';

import { IconFileDownload } from '@tabler/icons';

import { useTheme } from '@mui/material/styles';
import { downloadFileFromServer } from 'app/modules/survey/file-manage-utils';

interface IDataSourceFileCellProps {
  files: IFile[];
}

const DataSourceFileCell = (props: IDataSourceFileCellProps) => {
  const theme = useTheme();
  const { files } = props;

  const TooltipTitle = (tooltipProps: { file: IFile }) => {
    const { file } = tooltipProps;

    return (
      <Box
        sx={{
          '& .MuiTypography-root': {
            display: 'block',
            whiteSpace: 'nowrap',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            color: 'inherit',
          },
        }}
      >
        <Typography variant="caption">{file.name}</Typography>
        <Divider sx={{ my: 0.5 }} />
        <Typography variant="caption">{file['comment'] ? file['comment'] : 'No comment'}</Typography>
      </Box>
    );
  };

  return (
    <Box>
      {files.map(file => (
        <Tooltip key={file.id} title={<TooltipTitle file={file} />} placement="top" arrow>
          <IconButton
            sx={{
              color: theme.palette.text.primary,
              '&:hover': {
                color: theme.palette.primary.dark,
              },
            }}
            onClick={() => {
              downloadFileFromServer(file);
            }}
          >
            <IconFileDownload size={'1rem'} />
          </IconButton>
        </Tooltip>
      ))}
    </Box>
  );
};

export default DataSourceFileCell;
