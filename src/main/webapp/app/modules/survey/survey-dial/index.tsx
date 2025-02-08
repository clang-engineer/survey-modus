import * as React from 'react';
import Box from '@mui/material/Box';
import Backdrop from '@mui/material/Backdrop';
import SpeedDial from '@mui/material/SpeedDial';
import SpeedDialIcon from '@mui/material/SpeedDialIcon';
import SpeedDialAction from '@mui/material/SpeedDialAction';
import ArticleIcon from '@mui/icons-material/Article';
import useConfig from 'app/berry/hooks/useConfig';
import { useNavigate } from 'react-router-dom';
import { IForm } from 'app/shared/model/form.model';

const SurveyDialog = () => {
  const navigate = useNavigate();
  const { drawerType, container, layout, surveyInfo } = useConfig();

  const [open, setOpen] = React.useState(false);
  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const onClickForm = (form: IForm) => {
    navigate(`/survey/companies/${surveyInfo.company.id}/forms/${form.id}`);
    handleClose();
  };

  return (
    <Box sx={{ position: 'fixed', right: 16, bottom: 16 }}>
      <Backdrop open={open} />
      <SpeedDial ariaLabel="SpeedDial tooltip example" icon={<SpeedDialIcon />} onClose={handleClose} onOpen={handleOpen} open={open}>
        {surveyInfo.forms
          .filter(f => f)
          .sort((a, b) => b.id - a.id)
          .map(form => (
            <SpeedDialAction
              key={form.id}
              icon={<ArticleIcon />}
              tooltipTitle={form.title}
              tooltipOpen
              onClick={() => {
                onClickForm(form);
              }}
            />
          ))}
      </SpeedDial>
    </Box>
  );
};

export default SurveyDialog;
