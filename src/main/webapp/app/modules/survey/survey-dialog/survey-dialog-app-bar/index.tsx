import React from 'react';

import { AppBar, Button, IconButton, Toolbar, Typography } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { FormikProps } from 'formik';
import { IForm } from 'app/shared/model/form.model';
import { useAppSelector } from 'app/config/store';

import { IconMessages } from '@tabler/icons';

interface IDialogAppBarProps {
  form: IForm;
  formik: FormikProps<Record<string, any>>;
  onResolve: () => void;
  onClickMessagesButton: () => void;
}

const DialogAppBar = (props: IDialogAppBarProps) => {
  const { form, formik } = props;

  const loading = useAppSelector(state => state.documentReducer.loading);
  const updating = useAppSelector(state => state.documentReducer.updating);

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
        <Typography sx={{ ml: 2, flex: 1 }} variant="h4" color="inherit">
          {form.title}
        </Typography>
        <IconButton
          color="inherit"
          onClick={() => {
            onClickMessagesButton();
          }}
          disabled={loading || updating}
        >
          <IconMessages />
        </IconButton>
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
