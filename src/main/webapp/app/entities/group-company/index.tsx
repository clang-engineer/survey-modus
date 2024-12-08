import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserPoint from './group-company';
import GroupCompanyDetail from './group-company-detail';
import GroupCompanyUpdate from './group-company-update';
import GroupCompanyDeleteDialog from './group-company-delete-dialog';

const UserPointRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserPoint />} />
    <Route path="new" element={<GroupCompanyUpdate />} />
    <Route path=":id">
      <Route index element={<GroupCompanyDetail />} />
      <Route path="edit" element={<GroupCompanyUpdate />} />
      <Route path="delete" element={<GroupCompanyDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserPointRoutes;
