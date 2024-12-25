import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import FieldWizardList from 'app/modules/wizard/field-wizard/field-wizard-list';

const FieldWizardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FieldWizardList />} />
  </ErrorBoundaryRoutes>
);

export default FieldWizardRoutes;
