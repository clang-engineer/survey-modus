import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import GroupWizard from 'app/modules/wizard/group-wizard';

const WizardRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="group" element={<GroupWizard />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default WizardRoutes;
