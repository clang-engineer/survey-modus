import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import GroupWizardList from 'app/modules/wizard/group-wizard/group-wizard-list';
import GroupWizardDetail from 'app/modules/wizard/group-wizard/group-wizard-detail';

const GroupWizardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GroupWizardList />} />
    <Route path=":id">
      <Route index element={<GroupWizardDetail />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GroupWizardRoutes;
