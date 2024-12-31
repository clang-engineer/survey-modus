import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/entities/point">
        <Translate contentKey="global.menu.entities.point"/>
      </MenuItem>
      <MenuItem icon="asterisk" to="/entities/user-point">
        <Translate contentKey="global.menu.entities.userPoint" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entities/group">
        <Translate contentKey="global.menu.entities.group" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entities/company">
        <Translate contentKey="global.menu.entities.company" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entities/category">
        <Translate contentKey="global.menu.entities.category" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entities/form">
        <Translate contentKey="global.menu.entities.form" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entities/field">
        <Translate contentKey="global.menu.entities.field" />
      </MenuItem>

      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
