import React from 'react';
import { IFile } from 'app/shared/model/file.model';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';
import { downloadFileFromServer } from 'app/modules/survey/modal/component/survey-modal-file-field/file-uploader-utils';

import { Box, Button, Typography } from '@mui/material';

import { IconFile } from '@tabler/icons';

interface IDataSourceFileCellProps {
  files: IFile[];
}

const DataSourceFileCell = (props: IDataSourceFileCellProps) => {
  const { files } = props;

  return (
    <Box display="flex" flexDirection="row" justifyContent="center">
      {files.map(file => (
        <AnimateButton>
          <Button
            variant="text"
            color="primary"
            onClick={() => {
              downloadFileFromServer(file);
            }}
            startIcon={<IconFile size={10} />}
          >
            <Typography variant="caption" color="primary">
              {' '}
              {file.name}{' '}
            </Typography>
          </Button>
        </AnimateButton>
      ))}
    </Box>
  );
};

export default DataSourceFileCell;
