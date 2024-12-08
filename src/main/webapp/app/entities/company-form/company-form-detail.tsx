import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './company-form.reducer';

export const CompanyFormDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const companyFormEntity = useAppSelector(state => state.companyForm.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="companyFormDetailsHeading">
          <Translate contentKey="exformmakerApp.companyForm.detail.title">companyForm</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{companyFormEntity.id}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.companyForm.form">Form</Translate>
          </dt>
          <dd>{companyFormEntity.form ? companyFormEntity.form.title : ''}</dd>
          <dt>
            <Translate contentKey="exformmakerApp.companyForm.company">Company</Translate>
          </dt>
          <dd>{companyFormEntity.company ? companyFormEntity.company.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/company-form" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/company-form/${companyFormEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompanyFormDetail;
