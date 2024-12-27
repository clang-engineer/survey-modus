import React, { useEffect, useState } from 'react';
import { Link as BaseLink, useLocation, useNavigate } from 'react-router-dom';
import { getSortState, JhiItemCount, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './field.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { Box, Button, Link, Table, TableBody, TableCell, TableHead, TableRow, Typography, ButtonGroup } from '@mui/material';
import Pagination from '@mui/material/Pagination';
import { IconArrowsSort, IconEye, IconPencil, IconTrash, IconRefresh } from '@tabler/icons';

export const Field = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const fieldList = useAppSelector(state => state.field.entities);
  const loading = useAppSelector(state => state.field.loading);
  const totalItems = useAppSelector(state => state.field.totalItems);

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
      <Box id="field-heading" data-cy="FieldHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="surveymodusApp.field.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            variant="contained"
            color="primary"
            size="small"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => navigate('/field/new')}
          >
            <FontAwesomeIcon icon="plus" /> &nbsp;
            <Translate contentKey="surveymodusApp.field.home.createLabel">Create new Field</Translate>
          </Button>
        </Box>
      </Box>
      {fieldList && fieldList.length > 0 ? (
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
                <Translate contentKey="surveymodusApp.field.id">ID</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('title')}>
                <Translate contentKey="surveymodusApp.field.title">Title</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('description')}>
                <Translate contentKey="surveymodusApp.field.description">Description</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('activated')}>
                <Translate contentKey="surveymodusApp.field.activated">Activated</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('form')}>
                <Translate contentKey="surveymodusApp.field.form">Form</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell />
            </TableRow>
          </TableHead>
          <TableBody>
            {fieldList.map((field, i) => (
              <TableRow key={`entity-${i}`} data-cy="entityTable">
                <TableCell>
                  <Link component={BaseLink} to={`/field/${field.id}`}>
                    {field.id}
                  </Link>
                </TableCell>
                <TableCell>{field.title}</TableCell>
                <TableCell>{field.description}</TableCell>
                <TableCell>{field.activated ? 'true' : 'false'}</TableCell>
                <TableCell>{field.form ? field.form.title : ''}</TableCell>
                <TableCell className="text-end">
                  <ButtonGroup size={'small'}>
                    <Button data-cy="entityDetailsButton" color={'primary'} onClick={() => navigate(`/field/${field.id}`)}>
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
                          `/field/${field.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
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
                          `/field/${field.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
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
            <Translate contentKey="surveymodusApp.field.home.notFound">No Fields found</Translate>
          </div>
        )
      )}
      {totalItems ? (
        <Box
          display="flex"
          justifyContent="flex-end"
          alignItems="center"
          className={`${(fieldList && fieldList.length > 0) ?? 'd-none'} mt-3`}
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

export default Field;
