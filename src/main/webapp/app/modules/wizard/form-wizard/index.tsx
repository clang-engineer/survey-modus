import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import FormWizardList from 'app/modules/wizard/form-wizard/forrm-wizard-list';
import Index from 'app/entities/form/form-update';

const FormWizardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<FormWizardList />} />
    <Route path="new" element={<Index />} />
    <Route path=":id">
      <Route path="edit" element={<Index />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FormWizardRoutes;
