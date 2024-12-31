import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/form/form.reducer';
import { Alert, Box, Button, ButtonGroup, Grid, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import { IForm } from 'app/shared/model/form.model';
import { IconClipboardList, IconPencil, IconTrash } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import CheckIcon from '@mui/icons-material/Check';
import FormDraggableGrid from 'app/modules/wizard/form-wizard/forrm-wizard-list/form-draggable-grid';

const slotProps = {
  tooltip: {
    sx: {
      color: '#514E6A',
      backgroundColor: '#ffff',
      padding: '10px',
      boxShadow: '0px 4px 16px rgba(0, 0, 0, 0.08)',
    },
  },
};
const FormWizardList = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const theme = useTheme();

  const user = useAppSelector(state => state.authentication.account);
  const formList = useAppSelector(state => state.form.entities);
  const loading = useAppSelector(state => state.form.loading);

  useEffect(() => {
    getAllEntities();
  }, []);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: 'id,desc',
        query: `userId.equals=${user.id}`,
      })
    );
  };

  const handleSyncList = () => {
    getAllEntities();
  };

  const SubCardTitle = (props: { form: IForm }) => {
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

  return (
    <Grid container spacing={gridSpacing}>
      <Grid item xs={12} id="form-heading" data-cy="GroupHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="surveyModusApp.form.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            variant="contained"
            color="primary"
            size="small"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => navigate('/wizard/form/new')}
          >
            <FontAwesomeIcon icon="plus" /> &nbsp;
            <Translate contentKey="surveyModusApp.form.home.createLabel">Create new Group</Translate>
          </Button>
        </Box>
      </Grid>
      <Grid item xs={12}>
        {formList && formList.length > 0 ? (
          <FormDraggableGrid />
        ) : (
          !loading && (
            <Grid item xs={12}>
              <Alert icon={<CheckIcon fontSize="inherit" />} severity="warning">
                <Translate contentKey="surveyModusApp.form.home.notFound">No Groups found</Translate>
              </Alert>
            </Grid>
          )
        )}
      </Grid>
    </Grid>
  );
};

export default FormWizardList;
