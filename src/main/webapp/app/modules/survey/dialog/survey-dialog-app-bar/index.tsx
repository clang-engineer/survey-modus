import React from 'react';

import { AppBar, Box, Button, IconButton, Toolbar, Typography } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { FormikProps } from 'formik';
import { IForm } from 'app/shared/model/form.model';
import { useAppSelector } from 'app/config/store';

import { IconMessages } from '@tabler/icons';
import { ISurvey } from 'app/shared/model/survey.model';

interface IDialogAppBarProps {
  form: IForm;
  survey: ISurvey;
  formik: FormikProps<Record<string, any>>;
  onResolve: () => void;
  onClickMessagesButton: () => void;
}

const DialogAppBar = (props: IDialogAppBarProps) => {
  const { form, formik, survey } = props;

  const loading = useAppSelector(state => state.survey.loading);
  const updating = useAppSelector(state => state.survey.updating);

  const onClickMessagesButton = () => {
    props.onClickMessagesButton();
  };

  return (
    <AppBar sx={{ position: 'relative' }}>
      <Toolbar>
        <IconButton
          edge="start"
          color="inherit"
          onClick={() => {
            formik.resetForm();
            props.onResolve();
          }}
          aria-label="close"
        >
          <CloseIcon />
        </IconButton>
        <Box display="flex" alignItems="center" flexGrow={1} justifyContent="flex-start">
          <Typography variant="h4" color="inherit">
            {form.title} : {survey?.id}
          </Typography>
          &nbsp;&nbsp;
          <IconButton
            edge="start"
            color="inherit"
            onClick={() => {
              onClickMessagesButton();
            }}
            disabled={loading || updating}
          >
            {' '}
            <IconMessages />
          </IconButton>
        </Box>
        <Button
          color="inherit"
          onClick={() => {
            formik.handleSubmit();
          }}
          disabled={loading || updating}
        >
          save
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default DialogAppBar;
