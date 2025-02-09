import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import SurveyEntrance from './survey-entrance';
import FormGate from 'app/modules/survey/gate';
import MinimalLayout from 'app/berry/layout/MinimalLayout';
import MainLayout from 'app/berry/layout/MainLayout';
import JhLayout from 'app/shared/layout/jh-layout';

const GateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route element={<MinimalLayout />}>
      <Route index element={<SurveyEntrance />} />
    </Route>
    {/*<Route element={<MainLayout />}>*/}
    <Route element={<JhLayout />}>
      <Route path="companies/:companyId/forms/:formId" element={<FormGate />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GateRoutes;
