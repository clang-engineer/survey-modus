import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/company/company.reducer';
import { Alert, Box, Button, ButtonGroup, Grid, IconButton, Tooltip, Typography } from '@mui/material';
import SubCard from 'app/berry/ui-component/cards/SubCard';
import { gridSpacing } from 'app/berry/store/constant';
import { ICompany } from 'app/shared/model/company.model';
import { IconBuildingStore, IconPencil, IconTrash, IconFolders } from '@tabler/icons';
import { useTheme } from '@mui/material/styles';
import CheckIcon from '@mui/icons-material/Check';
import { CustomWidthTooltip } from 'app/shared/component/custom-toolip';
import CompanyFormTooltipContent from 'app/modules/wizard/company-wizard/component/company-form-tootip-content';

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
const CompanyWizardList = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();
  const theme = useTheme();

  const user = useAppSelector(state => state.authentication.account);
  const companyList = useAppSelector(state => state.company.entities);
  const loading = useAppSelector(state => state.company.loading);

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

  const SubCardTitle = (props: { company: ICompany }) => {
    const { company } = props;

    return (
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Typography variant="h4">{company.title}</Typography>
        <ButtonGroup variant="text" size="small">
          <Button onClick={() => navigate(`/wizard/company/${company.id}/edit`)}>
            <IconPencil size={'1rem'} strokeWidth={1.5} />
          </Button>
          <Button onClick={() => navigate(`/wizard/company/${company.id}/delete`)}>
            <IconTrash size={'1rem'} strokeWidth={1.5} color={theme.palette.error.light} />
          </Button>
        </ButtonGroup>
      </Box>
    );
  };

  return (
    <Grid container spacing={gridSpacing}>
      <Grid item xs={12} id="company-heading" data-cy="CompanyHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="exformmakerApp.company.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            variant="contained"
            color="primary"
            size="small"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => navigate('/wizard/company/new')}
          >
            <FontAwesomeIcon icon="plus" /> &nbsp;
            <Translate contentKey="exformmakerApp.company.home.createLabel">Create new Company</Translate>
          </Button>
        </Box>
      </Grid>
      <Grid item xs={12} container spacing={gridSpacing - 1}>
        {companyList && companyList.length > 0
          ? companyList.map((company, i) => (
              <Grid item xs={12} md={6} xl={4} key={i}>
                <SubCard title={<SubCardTitle company={company} />}>
                  <Grid container spacing={gridSpacing}>
                    <Grid item xs={12} marginBottom={2}>
                      <Typography variant="body1" color="text.primary">
                        {company.description}
                      </Typography>
                    </Grid>
                    <Grid item xs={12} display={'flex'} justifyContent={'flex-end'}>
                      <Box display="flex" alignItems="center">
                        <CustomWidthTooltip
                          title={<CompanyFormTooltipContent forms={company.forms} />}
                          sx={{ fontSize: '30' }}
                          slotProps={slotProps}
                        >
                          <IconButton>
                            <IconFolders size={'15px'} strokeWidth={1.2} />
                          </IconButton>
                        </CustomWidthTooltip>
                        <Typography variant="caption" color="text.primary">
                          {company.forms?.length ?? 0}
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
                  <Translate contentKey="exformmakerApp.company.home.notFound">No Companys found</Translate>
                </Alert>
              </Grid>
            )}
      </Grid>
    </Grid>
  );
};

export default CompanyWizardList;
