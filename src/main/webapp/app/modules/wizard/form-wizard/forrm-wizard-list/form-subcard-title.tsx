import React from 'react';
import { Box, Button, ButtonGroup, Typography } from '@mui/material';
import { IForm } from 'app/shared/model/form.model';

import { IconClipboardList, IconPencil, IconTrash } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import { useNavigate } from 'react-router-dom';

const FormSubcardTitle = (props: { form: IForm }) => {
  const theme = useTheme();
  const navigate = useNavigate();
  const { form } = props;

  return (
    <Box display="flex" justifyContent="space-between" alignItems="center">
      <Typography variant="h4">{form.title}</Typography>
      <ButtonGroup variant="text" size="small">
        <Button
          onClick={() =>
            // memo: this is the way to pass data to the next page
            navigate(`/wizard/field`, {
              state: { form },
            })
          }
        >
          <IconClipboardList size={'1rem'} strokeWidth={1.5} color={theme.palette.secondary.main} />
        </Button>
        <Button onClick={() => navigate(`/wizard/form/${form.id}/edit`)}>
          <IconPencil size={'1rem'} strokeWidth={1.5} color={theme.palette.primary.main} />
        </Button>
        <Button onClick={() => navigate(`/wizard/form/${form.id}/delete`)}>
          <IconTrash size={'1rem'} strokeWidth={1.5} color={theme.palette.error.light} />
        </Button>
      </ButtonGroup>
    </Box>
  );
};

export default FormSubcardTitle;
