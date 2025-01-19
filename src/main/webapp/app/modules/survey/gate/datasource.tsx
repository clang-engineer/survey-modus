import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, ButtonGroup, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import NoContentBox from 'app/shared/component/no-content-box';
import Loader from 'app/berry/ui-component/Loader';
import { IconEdit, IconTrash } from '@tabler/icons';
import { create } from 'react-modal-promise';
import PromiseModal from 'app/shared/component/promise-modal';
import { deleteSurvey } from 'app/modules/survey/survey.reducer';
import { SURVEY_ID, ISurvey } from 'app/shared/model/survey.model';
import SurveyModal from 'app/modules/survey/dialog';
import { IField } from 'app/shared/model/field.model';
import type from 'app/shared/model/enumerations/type.model';
import dayjs from 'dayjs';
import DataSourceFileCell from 'app/modules/survey/gate/datasource-file-cell';

const DataSource = () => {
  const dispatch = useAppDispatch();
  const loading = useAppSelector(state => state.field.loading);
  const fieldEntities = useAppSelector(state => state.field.entities);
  const surveys = useAppSelector<ISurvey[]>(state => state.survey.surveys);
  const formEntity = useAppSelector(state => state.form.entity);
  const companyEntity = useAppSelector(state => state.company.entity);

  const [localFields, setLocalFields] = React.useState([]);

  React.useEffect(() => {
    setLocalFields(fieldEntities.filter(field => field.activated));
  }, [fieldEntities]);

  const onDeleteButtonClick = row => {
    create(
      PromiseModal({
        title: 'Delete',
        content: 'Are you sure you want to delete this data?',
      })
    )().then(result => {
      if (result) {
        dispatch(deleteSurvey({ collectionId: formEntity.category.id, survey: row }));
      }
    });
  };

  const onEditButtonClick = (survey: ISurvey) => {
    create(
      SurveyModal({
        company: companyEntity,
        form: formEntity,
        fields: fieldEntities.filter(field => field.activated),
        survey,
      })
    )();
  };

  const getFormattedSurveyValue = (survey: ISurvey, field: IField) => {
    const value = survey.fields.find(f => String(f.key) === String(field.id))?.value;

    switch (field.attribute.type) {
      case type.DATE:
        return dayjs(value).format('YYYY-MM-DD');
      case type.FILE: {
        const files = value && value.length > 0 ? value : [];
        return <DataSourceFileCell files={files} />;
      }
      default:
        return value;
    }
  };

  return (
    <>
      {fieldEntities.length === 0 || surveys.length === 0 ? (
        <NoContentBox />
      ) : (
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow>
                <TableCell align="center">#</TableCell>
                {localFields.map(field => (
                  <TableCell key={field.id} align="center">
                    {field.title}
                  </TableCell>
                ))}
                <TableCell />
              </TableRow>
            </TableHead>
            <TableBody>
              {surveys.map((survey, index) => (
                <TableRow key={index}>
                  <TableCell width="100" align="center">
                    {survey[SURVEY_ID]}
                  </TableCell>
                  {localFields.map(field => (
                    <TableCell key={field.id} align="center">
                      {getFormattedSurveyValue(survey, field)}
                    </TableCell>
                  ))}
                  <TableCell width="100">
                    <ButtonGroup size="small" variant="text">
                      <Button
                        onClick={() => {
                          onEditButtonClick(survey);
                        }}
                      >
                        {' '}
                        <IconEdit size={'1rem'} />{' '}
                      </Button>
                      <Button
                        onClick={() => {
                          onDeleteButtonClick(survey);
                        }}
                      >
                        {' '}
                        <IconTrash size={'1rem'} />{' '}
                      </Button>
                    </ButtonGroup>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
      {loading && <Loader />}
    </>
  );
};

export default DataSource;
