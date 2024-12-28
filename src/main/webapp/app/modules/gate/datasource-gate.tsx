import React, { useEffect, useState } from 'react';

import { Grid, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from '@mui/material';
import MainCard from 'app/berry/ui-component/cards/MainCard';

const DatasourceGate = () => {
  const [datasource, setDatasource] = useState([]);

  useEffect(() => {
    setDatasource([
      {
        id: 1,
        name: 'test',
        description: 'test description',
      },
      {
        id: 2,
        name: 'test2',
        description: 'test description2',
      },
    ]);
  }, []);

  return (
    <Grid container spacing={2}>
      <Grid
        item
        xs={12}
        sm={6}
        md={4}
        lg={3}
        sx={{
          '& .MuiCard-root': {
            minHeight: '300px',
          },
        }}
      >
        <MainCard>
          <TableContainer>
            <Table width="100%">
              <TableHead>
                <TableRow>
                  <TableCell>Id</TableCell>
                  <TableCell>Name</TableCell>
                  <TableCell>Description</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {datasource.map((data, index) => (
                  <TableRow key={index}>
                    <TableCell>{data.id}</TableCell>
                    <TableCell>{data.name}</TableCell>
                    <TableCell>{data.description}</TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </MainCard>
      </Grid>
    </Grid>
  );
};

export default DatasourceGate;
