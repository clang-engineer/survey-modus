import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserPoint from './user-group';
import UserGroupDetail from './user-group-detail';
import UserGroupUpdate from './user-group-update';
import UserGroupDeleteDialog from './user-group-delete-dialog';

const UserPointRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserPoint />} />
    <Route path="new" element={<UserGroupUpdate />} />
    <Route path=":id">
      <Route index element={<UserGroupDetail />} />
      <Route path="edit" element={<UserGroupUpdate />} />
      <Route path="delete" element={<UserGroupDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserPointRoutes;
