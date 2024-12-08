import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompanyForm from './company-form';
import CompanyFormDetail from './company-form-detail';
import CompanyFormUpdate from './company-form-update';
import CompanyFormDeleteDialog from './company-form-delete-dialog';

const CompanyFormRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompanyForm />} />
    <Route path="new" element={<CompanyFormUpdate />} />
    <Route path=":id">
      <Route index element={<CompanyFormDetail />} />
      <Route path="edit" element={<CompanyFormUpdate />} />
      <Route path="delete" element={<CompanyFormDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompanyFormRoutes;
