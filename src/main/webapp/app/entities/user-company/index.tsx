import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserPoint from './user-company';
import UserCompanyDetail from './user-company-detail';
import UserCompanyUpdate from './user-company-update';
import UserCompanyDeleteDialog from './user-company-delete-dialog';

const UserPointRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserPoint />} />
    <Route path="new" element={<UserCompanyUpdate />} />
    <Route path=":id">
      <Route index element={<UserCompanyDetail />} />
      <Route path="edit" element={<UserCompanyUpdate />} />
      <Route path="delete" element={<UserCompanyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserPointRoutes;
