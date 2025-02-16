import React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import { FormikProps } from 'formik';
import { ISurvey } from 'app/shared/model/survey.model';

import { useTheme } from '@mui/material/styles';

interface ISurveyMemoProps {
  survey: ISurvey;
  formik: FormikProps<Record<string, any>>;
}

const SurveyMemo = React.forwardRef((props: ISurveyMemoProps, ref) => {
  React.useImperativeHandle(ref, () => ({
    openDialog: () => {
      handleClickOpen();
    },
  }));

  const theme = useTheme();

  const { survey, formik } = props;

  const [open, setOpen] = React.useState(false);

  const [memo, setMemo] = React.useState('');

  React.useEffect(() => {
    setMemo(formik.values.memo);
  }, [formik.values.memo]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    setMemo('');
  };

  return (
    <>
      <Dialog
        open={open}
        onClose={handleClose}
        sx={{
          '& .MuiPaper-root': {
            paddingTop: 0,
          },
          '& .MuiDialogContent-root': {
            padding: theme.spacing(1),
            minWidth: 600,
          },
          '& .MuiDialogActions-root': {
            paddingY: 0,
          },
        }}
      >
        <DialogContent>
          <TextField
            autoFocus
            id="memo"
            name="memo"
            label="Memo"
            type="text"
            // variant="standard"
            margin="dense"
            fullWidth
            value={memo}
            multiline
            rows={10}
            onChange={e => {
              setMemo(e.target.value);
            }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button
            type="button"
            onClick={() => {
              props.formik.setFieldValue('memo', memo);
              handleClose();
            }}
          >
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </>
  );
});

export default SurveyMemo;
