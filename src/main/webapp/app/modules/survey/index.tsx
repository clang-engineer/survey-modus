import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SurveyRouter from './survey-router';
import FormGate from 'app/modules/survey/form-gate';
import MinimalLayout from 'app/berry/layout/MinimalLayout';
import MainLayout from 'app/berry/layout/MainLayout';

const GateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route element={<MinimalLayout />}>
      <Route index element={<SurveyRouter />} />
    </Route>
    <Route element={<MainLayout />}>
      <Route path="companies/:companyId/forms/:formId" element={<FormGate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GateRoutes;
