import * as React from 'react';
import Box from '@mui/material/Box';
import Backdrop from '@mui/material/Backdrop';
import SpeedDial from '@mui/material/SpeedDial';
import SpeedDialAction from '@mui/material/SpeedDialAction';
import useConfig from 'app/berry/hooks/useConfig';
import { useNavigate } from 'react-router-dom';
import { IForm } from 'app/shared/model/form.model';

import { Fab, IconButton, Tooltip } from '@mui/material';

import { styled } from '@mui/material/styles';

import { IconLocation, IconScript } from '@tabler/icons';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

const StyledFab = styled(Fab)({
  borderRadius: 0,
  borderTopLeftRadius: '50%',
  borderBottomLeftRadius: '50%',
  borderTopRightRadius: '50%',
  borderBottomRightRadius: '4px',
  position: 'fixed',
  right: 20,
  bottom: 75,
  zIndex: 1200,
});

const StyledSpeedDial = styled(SpeedDial)(({ theme }) => ({
  position: 'absolute',
  '&.MuiSpeedDial-directionUp, &.MuiSpeedDial-directionLeft': {
    bottom: theme.spacing(2),
    right: theme.spacing(2),
  },
  '&.MuiSpeedDial-directionDown, &.MuiSpeedDial-directionRight': {
    top: theme.spacing(2),
    left: theme.spacing(2),
  },
  '& .MuiSpeedDialAction-staticTooltipLabel': {
    backgroundColor: 'transparent',
    color: 'white',
    textAlign: 'right',
    width: 200,
    border: 'none',
    boxShadow: 'none',
    padding: 0,
    fontWeight: 'bold',
  },
}));

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
    <>
      <Tooltip title="choose form" arrow={true} placement="left">
        <StyledFab
          color={open ? 'primary' : 'secondary'}
          size="small"
          variant="circular"
          onClick={() => {
            setOpen(!open);
          }}
        >
          <AnimateButton>
            <IconButton color="inherit" size="small" disableRipple aria-label="live customize">
              {open ? <IconScript /> : <IconLocation />}
            </IconButton>
          </AnimateButton>
        </StyledFab>
      </Tooltip>
      <Box sx={{ position: 'fixed', right: 0, bottom: 100 }}>
        <Backdrop open={open} />
        <StyledSpeedDial ariaLabel="SpeedDial openIcon example" open={open} hidden={true}>
          {surveyInfo.forms
            .filter(f => f)
            .sort((a, b) => b.id - a.id)
            .map(form => (
              <SpeedDialAction
                key={form.id}
                icon={<IconScript />}
                tooltipTitle={form.title}
                tooltipOpen
                onClick={() => {
                  onClickForm(form);
                }}
              />
            ))}
        </StyledSpeedDial>
      </Box>
    </>
  );
};

export default SurveyDialog;
