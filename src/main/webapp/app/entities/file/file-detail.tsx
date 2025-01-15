import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './file.reducer';

export const FileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const fileEntity = useAppSelector(state => state.file.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="fileDetailsHeading">
          <Translate contentKey="surveyModusApp.file.detail.title">File</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{fileEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="surveyModusApp.file.title">Title</Translate>
            </span>
          </dt>
          <dd>{fileEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="surveyModusApp.file.description">Description</Translate>
            </span>
          </dt>
          <dd>{fileEntity.description}</dd>
          <dt>
            <span id="activated">
              <Translate contentKey="surveyModusApp.file.activated">Activated</Translate>
            </span>
          </dt>
          <dd>{fileEntity.activated ? 'true' : 'false'}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="surveyModusApp.file.type">Type</Translate>
            </span>
          </dt>
          <dd>{fileEntity.type}</dd>
          <dt>
            <Translate contentKey="surveyModusApp.file.user">User</Translate>
          </dt>
          <dd>{fileEntity.user ? fileEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/file" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entities/file/${fileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FileDetail;
