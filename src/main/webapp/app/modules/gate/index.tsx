import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompanyGate from './company-gate';
import FormGate from 'app/modules/gate/form-gate';
import DatasourceGate from 'app/modules/gate/datasource-gate';
// import GateDetail from './survey-detail';
// import GateUpdate from './survey-update';
// import GateDeleteDialog from './survey-delete-dialog';

const GateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path="company" element={<CompanyGate />} />
    <Route path="form" element={<FormGate />} />
    <Route path="datasource" element={<DatasourceGate />} />
    {/*<Route path="new" element={<GateUpdate />} />*/}
    {/*<Route path=":id">*/}
    {/*  <Route index element={<GateDetail />} />*/}
    {/*  <Route path="edit" element={<GateUpdate />} />*/}
    {/*  <Route path="delete" element={<GateDeleteDialog />} />*/}
    {/*</Route>*/}
  </ErrorBoundaryRoutes>
);

export default GateRoutes;
