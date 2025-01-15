import React, { useEffect, useState } from 'react';
import { Link as BaseLink, useLocation, useNavigate } from 'react-router-dom';
import { getSortState, JhiItemCount, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './file.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { Box, Button, Link, Table, TableBody, TableCell, TableHead, TableRow, Typography } from '@mui/material';
import Pagination from '@mui/material/Pagination';
import { IconArrowsSort, IconEye, IconPencil, IconTrash, IconRefresh } from '@tabler/icons';
import ButtonGroup from '@mui/material/ButtonGroup';

export const File = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const fileList = useAppSelector(state => state.file.entities);
  const loading = useAppSelector(state => state.file.loading);
  const totalItems = useAppSelector(state => state.file.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (location.search !== endURL) {
      navigate(`${location.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [location.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  return (
    <MainCard>
      <Box id="file-heading" data-cy="FileHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="surveyModusApp.file.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            variant="contained"
            color="primary"
            size="small"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => navigate('/entities/file/new')}
          >
            <FontAwesomeIcon icon="plus" /> &nbsp;
            <Translate contentKey="surveyModusApp.file.home.createLabel">Create new File</Translate>
          </Button>
        </Box>
      </Box>
      {fileList && fileList.length > 0 ? (
        <Table
          sx={{
            '& .MuiTableCell-head, & .MuiTableCell-body': {
              textAlign: 'center',
            },
          }}
        >
          <TableHead>
            <TableRow>
              <TableCell className="hand" onClick={sort('id')}>
                <Translate contentKey="surveyModusApp.file.id">ID</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('filename')}>
                <Translate contentKey="surveyModusApp.file.filename">filename</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('filepath')}>
                <Translate contentKey="surveyModusApp.file.filepath">filepath</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('hashKey')}>
                <Translate contentKey="surveyModusApp.file.hashKey">hashkey</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell />
            </TableRow>
          </TableHead>
          <TableBody>
            {fileList.map((file, i) => (
              <TableRow key={`entity-${i}`} data-cy="entityTable">
                <TableCell>
                  <Link component={BaseLink} to={`/entities/file/${file.id}`}>
                    {file.id}
                  </Link>
                </TableCell>
                <TableCell>{file.filename}</TableCell>
                <TableCell>{file.filepath}</TableCell>
                <TableCell>{file.hashKey}</TableCell>
                <TableCell className="text-end">
                  <ButtonGroup size={'small'}>
                    <Button data-cy="entityDetailsButton" color={'primary'} onClick={() => navigate(`/entities/file/${file.id}`)}>
                      <IconEye size={'1rem'} />{' '}
                      <Typography variant={'subtitle2'} color={'inherit'}>
                        <Translate contentKey="entity.action.view">View</Translate>
                      </Typography>
                    </Button>
                    <Button
                      data-cy="entityEditButton"
                      color={'secondary'}
                      onClick={() =>
                        navigate(
                          `/entities/file/${file.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
                        )
                      }
                    >
                      <IconPencil size={'1rem'} />{' '}
                      <Typography variant={'subtitle2'} color={'inherit'}>
                        <Translate contentKey="entity.action.edit">Edit</Translate>
                      </Typography>
                    </Button>
                    <Button
                      data-cy="entityDeleteButton"
                      color={'error'}
                      onClick={() =>
                        navigate(
                          `/entities/file/${file.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
                        )
                      }
                    >
                      <IconTrash size={'1rem'} />{' '}
                      <Typography variant={'subtitle2'} color={'inherit'}>
                        <Translate contentKey="entity.action.delete">Delete</Translate>
                      </Typography>
                    </Button>
                  </ButtonGroup>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      ) : (
        !loading && (
          <div className="alert alert-warning">
            <Translate contentKey="surveyModusApp.file.home.notFound">No Files found</Translate>
          </div>
        )
      )}
      {totalItems ? (
        <Box
          display="flex"
          justifyContent="flex-end"
          alignItems="center"
          className={`${(fileList && fileList.length > 0) ?? 'd-none'} mt-3`}
        >
          <Box display="flex" justifyContent="center" className="mt-2">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </Box>
          <Box display="flex" justifyContent="center" className="mt-2">
            <Pagination
              page={paginationState.activePage}
              onChange={(e, page) => handlePagination(page)}
              count={Math.ceil(totalItems / paginationState.itemsPerPage)}
            />
          </Box>
        </Box>
      ) : (
        ''
      )}
    </MainCard>
  );
};

export default File;
