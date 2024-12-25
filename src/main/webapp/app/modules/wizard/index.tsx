import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';
import GroupWizardRoutes from 'app/modules/wizard/group-wizard/index';
import CompanyWizardRoutes from 'app/modules/wizard/company-wizard';
import FormWizardRoutes from 'app/modules/wizard/form-wizard';
import FieldWizardRoutes from 'app/modules/wizard/field-wizard';

const WizardRoutes = () => (
  <div>
    <ErrorBoundaryRoutes>
      <Route path="group/*" element={<GroupWizardRoutes />} />
      <Route path="company/*" element={<CompanyWizardRoutes />} />
      <Route path="form/*" element={<FormWizardRoutes />} />
      <Route path="field/*" element={<FieldWizardRoutes />} />
    </ErrorBoundaryRoutes>
  </div>
);

export default WizardRoutes;
