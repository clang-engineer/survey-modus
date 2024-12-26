import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import FieldWizardList from 'app/modules/wizard/field-wizard/field-wizard-list';
import { FieldWizardProvider } from 'app/modules/wizard/field-wizard/field-wizard.config';

const FieldWizardRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route
      index
      element={
        <FieldWizardProvider>
          <FieldWizardList />
        </FieldWizardProvider>
      }
    />
  </ErrorBoundaryRoutes>
);

export default FieldWizardRoutes;
