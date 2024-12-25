import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/form/form.reducer';
import { Alert, Box, Button, ButtonGroup, Grid, IconButton, Tooltip, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { IForm } from 'app/shared/model/form.model';
import { IconBuildingStore, IconPencil, IconTrash, IconClipboardList, IconBook } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import CheckIcon from '@mui/icons-material/Check';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import GroupCompaniesTooltipContent from 'app/modules/wizard/form-wizard/component/form-companies-tooltip-content';

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
              navigate(`/wizard/field`, {
                state: { form: form },
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
            <Translate contentKey="exformmakerApp.form.home.refreshListLabel">Refresh List</Translate>
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
            <Translate contentKey="exformmakerApp.form.home.createLabel">Create new Group</Translate>
          </Button>
        </Box>
      </Grid>
      <Grid item xs={12} container spacing={gridSpacing - 1}>
        {formList && formList.length > 0
          ? formList.map((form, i) => (
              <Grid item xs={12} md={6} xl={4} key={i}>
                <SubCard title={<SubCardTitle form={form} />}>
                  <Grid container spacing={gridSpacing}>
                    <Grid item xs={12} marginBottom={2}>
                      <Typography variant="body1" color="text.primary">
                        {form.description}
                      </Typography>
                    </Grid>
                    <Grid item xs={12} display={'flex'} justifyContent={'flex-end'}>
                      <Box display="flex" alignItems="center">
                        <CustomWidthTooltip
                          title={
                            <>
                              {form.category.title} <br />
                              {form.category.description}
                            </>
                          }
                          sx={{ fontSize: '30' }}
                          slotProps={slotProps}
                        >
                          <IconButton>
                            <IconBook size={'15px'} strokeWidth={1.2} />
                          </IconButton>
                        </CustomWidthTooltip>
                        <Typography variant="caption" color="text.primary">
                          {form.category.title}
                        </Typography>
                      </Box>
                    </Grid>
                  </Grid>
                </SubCard>
              </Grid>
            ))
          : !loading && (
              <Grid item xs={12}>
                <Alert icon={<CheckIcon fontSize="inherit" />} severity="warning">
                  <Translate contentKey="exformmakerApp.form.home.notFound">No Groups found</Translate>
                </Alert>
              </Grid>
            )}
      </Grid>
    </Grid>
  );
};

export default FormWizardList;
