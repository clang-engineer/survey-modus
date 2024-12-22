import React, { useEffect, useState } from 'react';
import { Link as BaseLink, useLocation, useNavigate } from 'react-router-dom';
import { getSortState, JhiItemCount, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from './company.reducer';
import MainCard from 'app/berry/ui-component/cards/MainCard';
import { Box, Button, Link, Table, TableBody, TableCell, TableHead, TableRow, Typography } from '@mui/material';
import Pagination from '@mui/material/Pagination';
import { IconArrowsSort, IconEye, IconPencil, IconTrash } from '@tabler/icons';
import ButtonGroup from '@mui/material/ButtonGroup';

export const Company = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );

  const companyList = useAppSelector(state => state.company.entities);
  const loading = useAppSelector(state => state.company.loading);
  const totalItems = useAppSelector(state => state.company.totalItems);

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
      <Box id="company-heading" data-cy="CompanyHeading">
        <Box display="flex" justifyContent="flex-end" alignItems="center">
          <Button className="me-2" variant="contained" color="secondary" size="small" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> &nbsp;
            <Translate contentKey="exformmakerApp.company.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Button
            variant="contained"
            color="primary"
            size="small"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            onClick={() => navigate('/company/new')}
          >
            <FontAwesomeIcon icon="plus" /> &nbsp;
            <Translate contentKey="exformmakerApp.company.home.createLabel">Create new Company</Translate>
          </Button>
        </Box>
      </Box>
      {companyList && companyList.length > 0 ? (
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
                <Translate contentKey="exformmakerApp.company.id">ID</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('title')}>
                <Translate contentKey="exformmakerApp.company.title">Title</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('description')}>
                <Translate contentKey="exformmakerApp.company.description">Description</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('activated')}>
                <Translate contentKey="exformmakerApp.company.activated">Activated</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand" onClick={sort('user.login')}>
                <Translate contentKey="exformmakerApp.company.user">User</Translate>
                &nbsp; <IconArrowsSort size={'1rem'} />
              </TableCell>
              <TableCell className="hand">
                <Translate contentKey="exformmakerApp.company.forms">Forms</Translate>
              </TableCell>
              <TableCell className="hand">
                <Translate contentKey="exformmakerApp.company.staffs">Staffs</Translate>
              </TableCell>
              <TableCell />
            </TableRow>
          </TableHead>
          <TableBody>
            {companyList.map((company, i) => (
              <TableRow key={`entity-${i}`} data-cy="entityTable">
                <TableCell>
                  <Link component={BaseLink} to={`/company/${company.id}`}>
                    {company.id}
                  </Link>
                </TableCell>
                <TableCell>{company.title}</TableCell>
                <TableCell>{company.description}</TableCell>
                <TableCell>{company.activated ? 'true' : 'false'}</TableCell>
                <TableCell>{company.user ? company.user.login : ''}</TableCell>
                <TableCell>
                  {company.forms
                    ? company.forms.map((val, j) => (
                        <Link component={BaseLink} to={`/form/${val.id}`} key={j}>
                          {val.title}
                          {j === company.forms.length - 1 ? '' : ', '}
                        </Link>
                      ))
                    : null}
                </TableCell>
                <TableCell>
                  {company.staffs
                    ? company.staffs.map((val, j) => (
                        <Typography key={j} variant={'subtitle2'} color={'inherit'}>
                          {val.name}
                          {j === company.staffs.length - 1 ? '' : ', '}
                        </Typography>
                      ))
                    : null}
                </TableCell>
                <TableCell className="text-end">
                  <ButtonGroup size={'small'}>
                    <Button data-cy="entityDetailsButton" color={'primary'} onClick={() => navigate(`/company/${company.id}`)}>
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
                          `/company/${company.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
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
                          `/company/${company.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`
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
            <Translate contentKey="exformmakerApp.company.home.notFound">No Companys found</Translate>
          </div>
        )
      )}
      {totalItems ? (
        <Box
          display="flex"
          justifyContent="flex-end"
          alignItems="center"
          className={`${(companyList && companyList.length > 0) ?? 'd-none'} mt-3`}
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

export default Company;
