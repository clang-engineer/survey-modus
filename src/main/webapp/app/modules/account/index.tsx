import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Settings from './settings/settings';
import Password from './password/password';

const AccountRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route path="settings" element={<Settings />} />
    <Route path="password" element={<Password />} />
  </ErrorBoundaryRoutes>
);

export default AccountRoutes;
