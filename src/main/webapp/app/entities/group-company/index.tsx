import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserPoint from './group-company';
import UserCompanyDetail from './group-company-detail';
import UserCompanyUpdate from './group-company-update';
import UserCompanyDeleteDialog from './group-company-delete-dialog';

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
