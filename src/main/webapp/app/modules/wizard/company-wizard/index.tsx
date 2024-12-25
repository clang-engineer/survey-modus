import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import CompanyWizardList from 'app/modules/wizard/company-wizard/company-wizard-list';
import CompanyUpdate from 'app/entities/company/company-update';

const CompanyWizardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompanyWizardList />} />
    <Route path="new" element={<CompanyUpdate />} />
    <Route path=":id">
      <Route path="edit" element={<CompanyUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompanyWizardRoutes;
