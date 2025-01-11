import React from 'react';

import { IGroup } from 'app/shared/model/group.model';
import { useNavigate } from 'react-router-dom';
import { IconPencil, IconTrash } from '@tabler/icons';

import { Box, Button, ButtonGroup, Typography } from '@mui/material';

import { useTheme } from '@mui/material/styles';

const GroupWizardSubcardTitle = (props: { group: IGroup }) => {
  const navigate = useNavigate();
  const theme = useTheme();

  const { group } = props;

  return (
    <Box display="flex" justifyContent="space-between" alignItems="center">
      <Typography variant="h4">{group.title}</Typography>
      <ButtonGroup variant="text" size="small">
        <Button onClick={() => navigate(`/wizard/group/${group.id}/edit`)}>
          <IconPencil size={'1rem'} strokeWidth={1.5} />
        </Button>
        <Button onClick={() => navigate(`/wizard/group/${group.id}/delete`)}>
          <IconTrash size={'1rem'} strokeWidth={1.5} color={theme.palette.error.light} />
        </Button>
      </ButtonGroup>
    </Box>
  );
};

export default GroupWizardSubcardTitle;
