import React from 'react';

import { AppBar, Box, Button, IconButton, Toolbar, Typography } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { FormikProps } from 'formik';
import { IForm } from 'app/shared/model/form.model';
import { useAppSelector } from 'app/config/store';

import { IconMessages } from '@tabler/icons';
import { IDocument } from 'app/shared/model/document.model';

interface IDialogAppBarProps {
  form: IForm;
  document: IDocument;
  formik: FormikProps<Record<string, any>>;
  onResolve: () => void;
  onClickMessagesButton: () => void;
}

const DialogAppBar = (props: IDialogAppBarProps) => {
  const { form, formik, document } = props;

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
        <Box display="flex" alignItems="center" flexGrow={1} justifyContent="flex-start">
          <Typography variant="h4" color="inherit">
            {form.title} : {document.id}
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
