import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { level } from 'app/shared/model/enumerations/level.model';
import { createEntity, getEntity, reset, updateEntity } from './point.reducer';

export const PointUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const pointEntity = useAppSelector(state => state.point.entity);
  const loading = useAppSelector(state => state.point.loading);
  const updating = useAppSelector(state => state.point.updating);
  const updateSuccess = useAppSelector(state => state.point.updateSuccess);
  const levelValues = Object.keys(level);

  const handleClose = () => {
    navigate('/point' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...pointEntity,
      ...values,
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
          type: 'EASY',
          ...pointEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="exformmakerApp.point.home.createOrEditLabel" data-cy="PointCreateUpdateHeading">
            <Translate contentKey="exformmakerApp.point.home.createOrEditLabel">Create or edit a Point</Translate>
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
                  id="point-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('exformmakerApp.point.title')}
                id="point-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: {
                    value: 20,
                    message: translate('entity.validation.minlength', { min: 20 }),
                  },
                  maxLength: {
                    value: 100,
                    message: translate('entity.validation.maxlength', { max: 100 }),
                  },
                }}
              />
              <ValidatedField
                label={translate('exformmakerApp.point.description')}
                id="point-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('exformmakerApp.point.activated')}
                id="point-activated"
                name="activated"
                data-cy="activated"
                check
                type="checkbox"
              />
              <ValidatedField label={translate('exformmakerApp.point.type')} id="point-type" name="type" data-cy="type" type="select">
                {levelValues.map(level => (
                  <option value={level} key={level}>
                    {translate('exformmakerApp.level.' + level)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/point" replace color="info">
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

export default PointUpdate;
