import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import CompanyWizardList from 'app/modules/wizard/company-wizard/company-wizard-list';

const CompanyWizardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompanyWizardList />} />
  </ErrorBoundaryRoutes>
);

export default CompanyWizardRoutes;
