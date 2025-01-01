import React, { useEffect } from 'react';
import { useAppSelector } from 'app/config/store';
import { IField } from 'app/shared/model/field.model';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import NoContentBox from 'app/shared/component/no-content-box';
import Loader from 'app/berry/ui-component/Loader';

const DataSource = () => {
  const loading = useAppSelector(state => state.field.loading);
  const fieldEntities = useAppSelector(state => state.field.entities);
  const documents = useAppSelector(state => state.documentReducer.documents);

  return (
    <>
      {fieldEntities.length === 0 || documents.length === 0 ? (
        <NoContentBox />
      ) : (
        <TableContainer>
          <Table size="small">
            <TableHead>
              <TableRow>
                {fieldEntities.map(field => (
                  <TableCell key={field.id} align="center">
                    {field.title}
                  </TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {documents.map((row, index) => (
                <TableRow key={index}>
                  {fieldEntities.map(field => (
                    <TableCell key={field.id} align="center">
                      {row[field.id]}
                    </TableCell>
                  ))}
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
