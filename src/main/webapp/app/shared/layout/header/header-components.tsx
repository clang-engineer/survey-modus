import React from 'react';
import { Translate } from 'react-jhipster';

import { NavbarBrand, NavItem, NavLink } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppSelector } from 'app/config/store';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <img src="content/images/logo-jhipster.png" alt="Logo" />
  </div>
);

export const Brand = () => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">surveymodus</Translate>
    </span>
    <span className="navbar-version">{VERSION}</span>
  </NavbarBrand>
);

export const Home = () => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);

export const Survey = () => {
  const isAuthenticated = useAppSelector(state => state.authentication.isAuthenticated);

  const linkTo = isAuthenticated ? '/survey' : '/survey/login';

  return (
    <NavItem>
      <NavLink tag={Link} to={linkTo} className="d-flex align-items-center">
        <FontAwesomeIcon icon="home" />
        <span>
          <Translate contentKey="global.menu.survey">Survey</Translate>
          {isAuthenticated ? '' : ' Login'}
        </span>
      </NavLink>
    </NavItem>
  );
};
