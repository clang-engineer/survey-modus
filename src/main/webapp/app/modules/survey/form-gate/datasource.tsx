import React from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { Button, ButtonGroup, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import NoContentBox from 'app/shared/component/no-content-box';
import Loader from 'app/berry/ui-component/Loader';
import { IconEdit, IconTrash } from '@tabler/icons';
import { create } from 'react-modal-promise';
import PromiseModal from 'app/shared/component/promise-modal';
import { deleteDocument } from 'app/modules/document/document.reducer';
import { DOCUMENT_ID, IDocument } from 'app/shared/model/document.model';
import SurveyModal from 'app/modules/survey-modal';
import { IField } from 'app/shared/model/field.model';
import type from 'app/shared/model/enumerations/type.model';
import dayjs from 'dayjs';

const DataSource = () => {
  const dispatch = useAppDispatch();
  const loading = useAppSelector(state => state.field.loading);
  const fieldEntities = useAppSelector(state => state.field.entities);
  const documents = useAppSelector<IDocument[]>(state => state.documentReducer.documents);
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
        dispatch(deleteDocument({ collectionId: formEntity.category.id, document: row }));
      }
    });
  };

  const onEditButtonClick = (document: IDocument) => {
    create(
      SurveyModal({
        company: companyEntity,
        form: formEntity,
        fields: fieldEntities.filter(field => field.activated),
        document,
      })
    )();
  };

  const getFormattedDocumentValue = (document: IDocument, field: IField) => {
    const value = document.fields.find(f => String(f.key) === String(field.id))?.value;

    switch (field.attribute.type) {
      case type.DATE:
        return dayjs(value).format('YYYY-MM-DD');
      case type.FILE:
        return value && value.length > 0 ? value.map(file => file.filename).join(', ') : '';
      default:
        return value;
    }
  };

  return (
    <>
      {fieldEntities.length === 0 || documents.length === 0 ? (
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
              {documents.map((document, index) => (
                <TableRow key={index}>
                  <TableCell width="100" align="center">
                    {document[DOCUMENT_ID]}
                  </TableCell>
                  {localFields.map(field => (
                    <TableCell key={field.id} align="center">
                      {getFormattedDocumentValue(document, field)}
                    </TableCell>
                  ))}
                  <TableCell width="100">
                    <ButtonGroup size="small" variant="text">
                      <Button
                        onClick={() => {
                          onEditButtonClick(document);
                        }}
                      >
                        {' '}
                        <IconEdit size={'1rem'} />{' '}
                      </Button>
                      <Button
                        onClick={() => {
                          onDeleteButtonClick(document);
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
