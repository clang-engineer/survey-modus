import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import GroupWizardRoutes from 'app/modules/wizard/group-wizard/index';

const WizardRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="group/*" element={<GroupWizardRoutes />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default WizardRoutes;
