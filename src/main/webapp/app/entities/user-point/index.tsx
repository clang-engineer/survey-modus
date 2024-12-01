import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserPoint from './user-point';
import UserPointDetail from './user-point-detail';
import UserPointUpdate from './user-point-update';
import UserPointDeleteDialog from './user-point-delete-dialog';

const UserPointRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserPoint />} />
    <Route path="new" element={<UserPointUpdate />} />
    <Route path=":id">
      <Route index element={<UserPointDetail />} />
      <Route path="edit" element={<UserPointUpdate />} />
      <Route path="delete" element={<UserPointDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserPointRoutes;
