import React from 'react';
import { ICompany } from 'app/shared/model/company.model';

import { Box, Button, ButtonGroup, Typography } from '@mui/material';

import { useNavigate } from 'react-router-dom';

import { IconPencil, IconTrash } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';

const SubCardTitle = (props: { company: ICompany }) => {
  const { company } = props;

  const navigate = useNavigate();
  const theme = useTheme();

  return (
    <Box display="flex" justifyContent="space-between" alignItems="center">
      <Typography variant="h4">{company.title}</Typography>
      <ButtonGroup variant="text" size="small">
        <Button onClick={() => navigate(`/wizard/company/${company.id}/edit`)}>
          <IconPencil size={'1rem'} strokeWidth={1.5} />
        </Button>
        <Button onClick={() => navigate(`/wizard/company/${company.id}/delete`)}>
          <IconTrash size={'1rem'} strokeWidth={1.5} color={theme.palette.error.light} />
        </Button>
      </ButtonGroup>
    </Box>
  );
};

export default SubCardTitle;
