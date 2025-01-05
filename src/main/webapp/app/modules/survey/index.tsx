import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompanyGate from './company-gate';
import FormGate from 'app/modules/survey/form-gate';

const GateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path="companies" element={<CompanyGate />} />
    <Route path="companies/:companyId/forms/:formId" element={<FormGate />} />
  </ErrorBoundaryRoutes>
);

export default GateRoutes;
