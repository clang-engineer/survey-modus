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

const DataSource = () => {
  const dispatch = useAppDispatch();
  const loading = useAppSelector(state => state.field.loading);
  const fieldEntities = useAppSelector(state => state.field.entities);
  const documents = useAppSelector(state => state.documentReducer.documents);
  const formEntity = useAppSelector(state => state.form.entity);
  const companyEntity = useAppSelector(state => state.company.entity);

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
        document: document,
      })
    )();
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
                {fieldEntities.map(field => (
                  <TableCell key={field.id} align="center">
                    {field.title}
                  </TableCell>
                ))}
                <TableCell />
              </TableRow>
            </TableHead>
            <TableBody>
              {documents.map((row, index) => (
                <TableRow key={index}>
                  <TableCell width="100" align="center">
                    {row[DOCUMENT_ID]}
                  </TableCell>
                  {fieldEntities.map(field => (
                    <TableCell key={field.id} align="center">
                      {row[field.id]}
                    </TableCell>
                  ))}
                  <TableCell width="100">
                    <ButtonGroup size="small" variant="text">
                      <Button
                        onClick={() => {
                          onEditButtonClick(row);
                        }}
                      >
                        {' '}
                        <IconEdit size={'1rem'} />{' '}
                      </Button>
                      <Button
                        onClick={() => {
                          onDeleteButtonClick(row);
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
