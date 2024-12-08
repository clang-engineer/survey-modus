import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './form.reducer';

export const FormDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const formEntity = useAppSelector(state => state.form.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="formDetailsHeading">
          <Translate contentKey="exformmakerApp.form.detail.title">Form</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{formEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="exformmakerApp.form.title">Title</Translate>
            </span>
          </dt>
          <dd>{formEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="exformmakerApp.form.description">Description</Translate>
            </span>
          </dt>
          <dd>{formEntity.description}</dd>
          <dt>
            <span id="activated">
              <Translate contentKey="exformmakerApp.form.activated">Activated</Translate>
            </span>
          </dt>
          <dd>{formEntity.activated ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.form.user">User</Translate>
          </dt>
          <dd>{formEntity.user ? formEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.form.category">Category</Translate>
          </dt>
          <dd>{formEntity.category ? formEntity.category.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/form" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/form/${formEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FormDetail;
