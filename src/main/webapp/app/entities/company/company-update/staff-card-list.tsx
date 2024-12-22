import React, { useRef } from 'react';
import { Alert, Box, ButtonGroup, Divider, Grid, IconButton, Typography } from '@mui/material';
import { IconPencil, IconTrash, IconUserCircle, IconUserPlus } from '@tabler/icons';
import CompanyStaffUpdateModal from 'app/entities/company/company-update/staff-update-modal';

import { FormikProps } from 'formik';

const StaffCardList = (props: { formik: FormikProps<any> }) => {
  const { formik } = props;

  const [localStaffs, setLocalStaffs] = React.useState([]);

  const staffUpdateModal = useRef<any>(null);

  React.useEffect(() => {
    setLocalStaffs(formik.values.staffs);
  }, [formik.values.staffs]);

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
              <Grid item xs={12} md={3}>
                <Box sx={{ border: '1px dotted #ccc', padding: 2, borderRadius: 2 }}>
                  <Box>
                    <Typography variant="h5" gutterBottom>
                      <IconUserCircle size={'1rem'} /> {staff.name}
                    </Typography>
                    <Divider
                      sx={{
                        marginBottom: 1,
                      }}
                    />
                    <Typography>Email: {staff.email}</Typography>
                    <Typography>Phone: {staff.phone}</Typography>
                    <Typography>Active: {staff.activated ? 'Yes' : 'No'}</Typography>
                  </Box>
                  <Box display="flex" justifyContent="flex-end">
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
                          formik.setFieldValue(
                            'staffs',
                            formik.values.staffs.filter((_, i) => i !== index)
                          );
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
