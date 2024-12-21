import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import GroupWizardRoutes from 'app/modules/wizard/group-wizard/index';
import CompanyWizardRoutes from 'app/modules/wizard/company-wizard';

const WizardRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="group/*" element={<GroupWizardRoutes />} />
      <Route path="company/*" element={<CompanyWizardRoutes />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default WizardRoutes;
