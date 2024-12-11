import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Field from './field';
import FieldDetail from './field-detail';
import FieldUpdate from './field-update';
import FieldDeleteDialog from './field-delete-dialog';

const GroupRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Field />} />
    <Route path="new" element={<FieldUpdate />} />
    <Route path=":id">
      <Route index element={<FieldDetail />} />
      <Route path="edit" element={<FieldUpdate />} />
      <Route path="delete" element={<FieldDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GroupRoutes;
