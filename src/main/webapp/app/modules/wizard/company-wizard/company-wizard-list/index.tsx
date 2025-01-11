import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/company/company.reducer';
import { Alert, Grid, Typography } from '@mui/material';
import { gridSpacing } from 'app/berry/store/constant';
import CheckIcon from '@mui/icons-material/Check';
import CompanyGridContainer from 'app/modules/wizard/company-wizard/company-wizard-list/company-grid-container';
import WizardListToolbar from 'app/modules/wizard/component/wizard-list-title';
import WizardListUpdateModal from 'app/modules/wizard/component/wizard-list-update-modal';

const CompanyWizardList = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const user = useAppSelector(state => state.authentication.account);
  const companyList = useAppSelector(state => state.company.entities);
  const loading = useAppSelector(state => state.company.loading);

  const wizardListUpdateModalRef = React.useRef(null);

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

  return (
    <Grid container spacing={gridSpacing}>
      <Grid item xs={12} id="company-heading" data-cy="CompanyHeading">
        <WizardListToolbar
          title={
            <Typography variant="h4">
              <Translate contentKey="surveyModusApp.company.home.title">Companies</Translate>
            </Typography>
          }
          items={companyList}
          onSyncListClick={getAllEntities}
          onModalOpenClick={() => {
            wizardListUpdateModalRef.current?.open();
          }}
          onAddNewClick={() => {
            navigate('/wizard/company/new');
          }}
          loading={loading}
        />
      </Grid>
      <Grid item xs={12} container spacing={gridSpacing - 1}>
        {companyList && companyList.length > 0 ? (
          <CompanyGridContainer />
        ) : (
          <Grid item xs={12}>
            <Alert icon={<CheckIcon fontSize="inherit" />} severity="warning">
              <Translate contentKey="surveyModusApp.company.home.notFound">No Companys found</Translate>
            </Alert>
          </Grid>
        )}
      </Grid>
      <WizardListUpdateModal
        ref={wizardListUpdateModalRef}
        items={companyList}
        onSave={items => {
          // dispatch(createAndUpdateEntities(items));
        }}
      />
    </Grid>
  );
};

export default CompanyWizardList;
