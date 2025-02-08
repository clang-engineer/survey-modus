import React from 'react';
import { NavDropdown } from './menu-components';
import MenuItem from './menu-item';

export const WizardsMenu = () => (
  <NavDropdown icon="th-list" name={'Wizards'} id="wizard-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/wizard/form">
      Form Wizard
    </MenuItem>
    <MenuItem icon="asterisk" to="/wizard/company">
      Company Wizard
    </MenuItem>
    <MenuItem icon="asterisk" to="/wizard/group">
      Group Wizard
    </MenuItem>
  </NavDropdown>
);
