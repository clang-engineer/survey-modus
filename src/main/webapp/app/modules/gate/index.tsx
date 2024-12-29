import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompanyGate from './company-gate';
import FormGate from 'app/modules/gate/form-gate';
import { GateProvider } from 'app/modules/gate/gate.config';
import GateLayout from 'app/modules/gate/layout';

const GateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route
      element={
        <GateProvider>
          <GateLayout />
        </GateProvider>
      }
    >
      <Route path="companies" element={<CompanyGate />} />
      <Route path="companies/:companyId/forms/:formId" element={<FormGate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GateRoutes;
