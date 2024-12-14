import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCompanys } from 'app/entities/company/company.reducer';
import { getEntities as getGroups } from 'app/entities/group/group.reducer';
import { createEntity, getEntity, reset, updateEntity } from './group-company.reducer';

export const GroupCompanyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const groups = useAppSelector(state => state.group.entities);
  const companys = useAppSelector(state => state.company.entities);
  const groupCompanyEntity = useAppSelector(state => state.groupCompany.entity);
  const loading = useAppSelector(state => state.groupCompany.loading);
  const updating = useAppSelector(state => state.groupCompany.updating);
  const updateSuccess = useAppSelector(state => state.groupCompany.updateSuccess);

  const handleClose = () => {
    navigate('/group-company' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGroups({}));
    dispatch(getCompanys({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...groupCompanyEntity,
      ...values,
      group: groups.find(it => it.id.toString() === values.group.toString()),
      company: companys.find(it => it.id.toString() === values.company.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...groupCompanyEntity,
          group: groupCompanyEntity?.group?.id,
          company: groupCompanyEntity?.company?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="exformmakerApp.groupCompany.home.createOrEditLabel" data-cy="GroupCompanyCreateUpdateHeading">
            <Translate contentKey="exformmakerApp.groupCompany.home.createOrEditLabel">Create or edit a GroupCompany</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="group-company-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                id="group-company-group"
                name="group"
                data-cy="group"
                label={translate('exformmakerApp.groupCompany.group')}
                type="select"
                required
              >
                <option value="" key="0" />
                {groups
                  ? groups.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <ValidatedField
                id="group-company-company"
                name="company"
                data-cy="company"
                label={translate('exformmakerApp.groupCompany.company')}
                type="select"
                required
              >
                <option value="" key="0" />
                {companys
                  ? companys.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/group-company" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default GroupCompanyUpdate;
