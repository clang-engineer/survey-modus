import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/point">
        <Translate contentKey="global.menu.entities.point"/>
      </MenuItem>
      <MenuItem icon="asterisk" to="/user-point">
        <Translate contentKey="global.menu.entities.userPoint" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/group">
        <Translate contentKey="global.menu.entities.group" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/group-user">
        <Translate contentKey="global.menu.entities.groupUser" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/group-company">
        <Translate contentKey="global.menu.entities.groupCompany" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/category">
        <Translate contentKey="global.menu.entities.category" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/form">
        <Translate contentKey="global.menu.entities.form" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/company-form">
        <Translate contentKey="global.menu.entities.companyForm" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/field">
        <Translate contentKey="global.menu.entities.field" />
      </MenuItem>

      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
