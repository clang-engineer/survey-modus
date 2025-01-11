import React from 'react';
import { Box, Button, ButtonGroup, Typography } from '@mui/material';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { useNavigate } from 'react-router-dom';

import { useTheme } from '@mui/material/styles';
import useConfig from 'app/berry/hooks/useConfig';
import { getEntities } from 'app/entities/form/form.reducer';
import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';
import FormListUpdateModal from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-list-update-modal';

const FormWizardListToolbar = () => {
  const config = useConfig();
  const theme = useTheme();

  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const formListUpdateModalRef = React.useRef(null);

  const loading = useAppSelector(state => state.form.loading);
  const user = useAppSelector(state => state.authentication.account);

  const handleSyncList = () => {
    dispatch(
      getEntities({
        sort: 'id,desc',
        query: `userId.equals=${user.id}`,
      })
    );
  };

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
              formListUpdateModalRef.current?.open();
            }}
            disabled={loading}
          >
            <FontAwesomeIcon icon="align-left" />
          </Button>
        </AnimateButton>
        <AnimateButton>
          <Button color="secondary" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />
          </Button>
        </AnimateButton>
        <AnimateButton>
          <Button color="info" onClick={() => navigate('/wizard/form/new')}>
            <FontAwesomeIcon icon="plus" />
          </Button>
        </AnimateButton>
      </ButtonGroup>
      <FormListUpdateModal ref={formListUpdateModalRef} />
    </Box>
  );
};

export default FormWizardListToolbar;
