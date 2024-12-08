import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import GroupUser from './group-user';
import GroupUserDetail from './group-user-detail';
import GroupUserUpdate from './group-user-update';
import GroupUserDeleteDialog from './group-user-delete-dialog';

const GroupUserRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<GroupUser />} />
    <Route path="new" element={<GroupUserUpdate />} />
    <Route path=":id">
      <Route index element={<GroupUserDetail />} />
      <Route path="edit" element={<GroupUserUpdate />} />
      <Route path="delete" element={<GroupUserDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GroupUserRoutes;
