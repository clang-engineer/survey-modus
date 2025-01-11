import React from 'react';
import { Box, Button, ButtonGroup, Typography } from '@mui/material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';

import { useTheme } from '@mui/material/styles';
import useConfig from 'app/berry/hooks/useConfig';
import { createAndUpdateEntities, getEntities } from 'app/entities/form/form.reducer';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';
import WizardListUpdateModal from 'app/modules/wizard/component/wizard-list-update-modal';

interface IWizardListToolbarProps {
  items: any[];
  onSyncListClick: () => void;
  onModalOpenClick: () => void;
  onAddNewClick: () => void;
  loading: boolean;
}

const WizardListToolbar = (props: IWizardListToolbarProps) => {
  const config = useConfig();
  const theme = useTheme();

  const { loading } = props;

  return (
    <Box
      display="flex"
      justifyContent="space-between"
      alignItems="center"
      p={2}
      style={{
        backgroundColor: theme.palette.background.default,
        borderRadius: config.borderRadius,
      }}
    >
      <Typography variant="h4">
        <Translate contentKey="surveyModusApp.form.home.title">Forms</Translate>
      </Typography>
      <ButtonGroup variant="text" aria-label="text button group" size="small">
        <AnimateButton>
          <Button
            color="primary"
            onClick={() => {
              props.onModalOpenClick();
            }}
            disabled={loading}
          >
            <FontAwesomeIcon icon="cog" />
          </Button>
        </AnimateButton>
        <AnimateButton>
          <Button color="secondary" onClick={() => props.onSyncListClick()} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
          </Button>
        </AnimateButton>
        <AnimateButton>
          <Button color="info" onClick={() => props.onAddNewClick()}>
            <FontAwesomeIcon icon="plus" />
          </Button>
        </AnimateButton>
      </ButtonGroup>
    </Box>
  );
};

export default WizardListToolbar;
