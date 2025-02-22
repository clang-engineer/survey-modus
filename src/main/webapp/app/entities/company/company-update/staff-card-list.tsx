import React, { useRef } from 'react';
import { Alert, Box, ButtonGroup, Divider, Grid, IconButton, Typography, Switch } from '@mui/material';
import { IconPencil, IconTrash, IconUserCircle, IconUserPlus, IconUserOff, IconUser, IconCircle } from '@tabler/icons';
import CompanyStaffUpdateModal from 'app/entities/company/company-update/staff-update-modal';

import { FormikProps } from 'formik';
import { useTheme } from '@mui/material/styles';
import { create } from 'react-modal-promise';
import PromiseModal from 'app/shared/component/promise-modal';

const StaffCardList = (props: { formik: FormikProps<any> }) => {
  const theme = useTheme();
  const { formik } = props;

  const [localStaffs, setLocalStaffs] = React.useState([]);

  const staffUpdateModal = useRef<any>(null);

  React.useEffect(() => {
    setLocalStaffs(formik.values.staffs);
  }, [formik.values.staffs]);

  const getStatusColor = React.useCallback(
    (activated: boolean) => {
      return activated ? theme.palette.success.main : theme.palette.error.main;
    },
    [theme.palette.error.main, theme.palette.success.main]
  );

  const deleteModal = create(
    PromiseModal({
      title: 'Delete Staff',
      content: 'Do you want to delete this staff?',
      rejectButtonText: 'Cancel',
      resolveButtonText: 'Delete',
    })
  );

  return (
    <Grid container spacing={1}>
      <Grid item xs={12}>
        {localStaffs.length === 0 ? (
          <Alert
            severity="warning"
            onClick={() => {
              staffUpdateModal.current.open();
            }}
            sx={{ cursor: 'pointer' }}
          >
            No staffs found. Click here to add new staff.
          </Alert>
        ) : (
          <IconButton
            size="small"
            onClick={() => {
              staffUpdateModal.current.open();
            }}
          >
            <IconUserPlus size={'1rem'} />
          </IconButton>
        )}
      </Grid>
      <Grid item xs={12}>
        <Grid container spacing={1}>
          {localStaffs.map((staff: any, index: number) => {
            return (
              <Grid item xs={12} md={3} key={index}>
                <Box sx={{ border: '1px dotted #ccc', padding: 2, borderRadius: 2 }}>
                  <Box
                    sx={{
                      '& svg': { color: getStatusColor(staff.activated) },
                      '.MuiTypography-root': { textDecoration: staff.activated ? 'none' : 'line-through' },
                    }}
                  >
                    <Typography variant="h5" gutterBottom>
                      <IconCircle size={'0.5rem'} fill={getStatusColor(staff.activated)} /> &nbsp;&nbsp;
                      {staff.firstName} {staff.lastName}
                    </Typography>
                    <Divider
                      sx={{
                        marginBottom: 1,
                      }}
                    />
                    <Typography>Email: {staff.email}</Typography>
                    <Typography>Phone: {staff.phone}</Typography>
                    <Typography>Active: {staff.activated ? 'yes' : 'no'}</Typography>
                  </Box>
                  <Box display="flex" justifyContent="flex-end" alignItems="center">
                    <Switch
                      size="small"
                      checked={staff.activated}
                      onChange={(e: any) => {
                        formik.setFieldValue(
                          'staffs',
                          formik.values.staffs.map((s: any, i: number) => (i === index ? { ...s, activated: e.target.checked } : s))
                        );
                      }}
                      name="activated"
                    />
                    <ButtonGroup variant="text" size="small">
                      <IconButton
                        onClick={() => {
                          staffUpdateModal.current.open({
                            staff,
                            index,
                          });
                        }}
                      >
                        <IconPencil size={'1rem'} />{' '}
                      </IconButton>
                      <IconButton
                        onClick={() => {
                          deleteModal().then(result => {
                            if (result) {
                              formik.setFieldValue(
                                'staffs',
                                formik.values.staffs.filter((_, i) => i !== index)
                              );
                            }
                          });
                        }}
                      >
                        <IconTrash size={'1rem'} />{' '}
                      </IconButton>
                    </ButtonGroup>
                  </Box>
                </Box>
              </Grid>
            );
          })}
          <CompanyStaffUpdateModal ref={staffUpdateModal} formik={formik} />
        </Grid>
      </Grid>
    </Grid>
  );
};

export default StaffCardList;
