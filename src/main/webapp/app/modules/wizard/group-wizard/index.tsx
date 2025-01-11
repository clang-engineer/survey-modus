import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import GroupWizardList from 'app/modules/wizard/group-wizard/group-wizard-list';
import GroupUpdate from 'app/entities/group/group-update';

const GroupWizardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GroupWizardList />} />
    <Route path="new" element={<GroupUpdate />} />
    <Route path=":id">
      <Route path="edit" element={<GroupUpdate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GroupWizardRoutes;
