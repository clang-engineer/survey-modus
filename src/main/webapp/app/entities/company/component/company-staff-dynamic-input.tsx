import React, { useRef } from 'react';
import { Box, ButtonGroup, Grid, IconButton, Typography } from '@mui/material';
import { IconPencil, IconTrash, IconUserPlus } from '@tabler/icons';
import CompanyStaffUpdateModal from 'app/entities/company/component/company-staff-update-modal';

const CompanyStaffDynamicInput = (props: { formik: any; staffs: any }) => {
  const { formik, staffs } = props;

  const [localStaffs, setLocalStaffs] = React.useState(staffs);

  const addModalRef = useRef<any>(null);

  React.useEffect(() => {
    // dummy
    // const dummy = [
    //   { name: 'staff1', email: 'staff1@test.com', phone: '1234567890', active: true },
    //   { name: 'staff2', email: 'staff2@test.com', phone: '1234567890', active: true },
    //   { name: 'staff3', email: 'staff3@test.com', phone: '1234567890', active: true },
    //   { name: 'staff4', email: 'staff4@test.com', phone: '1234567890', active: true },
    //   { name: 'staff5', email: 'staff5@test.com', phone: '1234567890', active: true },
    //   { name: 'staff6', email: 'staff6@test.com', phone: '1234567890', active: true },
    //   { name: 'staff7', email: 'staff7@test.com', phone: '1234567890', active: true },
    //   { name: 'staff8', email: 'staff8@test.com', phone: '1234567890', active: true },
    //   { name: 'staff9', email: 'staff9@test.com', phone: '1234567890', active: true },
    // ];

    setLocalStaffs(staffs);
  }, [staffs]);

  return (
    <Grid container spacing={1}>
      <Grid item xs={12}>
        <ButtonGroup variant="outlined" size="small">
          <IconButton
            onClick={() => {
              addModalRef.current.open();
            }}
          >
            <IconUserPlus size={'1rem'} />
            &nbsp;
            <Typography variant="caption">staff</Typography>
          </IconButton>
        </ButtonGroup>
      </Grid>
      <Grid item xs={12}>
        <Grid container spacing={1}>
          {localStaffs.map((staff: any, index: number) => {
            return (
              <Grid item xs={12} md={3}>
                <Box sx={{ border: '1px dotted #ccc', padding: 2, borderRadius: 2 }}>
                  <Box>
                    <Typography variant="h4">Staff {index + 1}</Typography>
                    <Typography>Name: {staff.name}</Typography>
                    <Typography>Email: {staff.email}</Typography>
                    <Typography>Phone: {staff.phone}</Typography>
                    <Typography>Active: {staff.active ? 'true' : 'false'}</Typography>
                  </Box>
                  <Box display="flex" justifyContent="flex-end">
                    <ButtonGroup variant="text" size="small">
                      <IconButton
                        onClick={() => {
                          addModalRef.current.open(staff);
                        }}
                      >
                        {' '}
                        <IconPencil size={'1rem'} />{' '}
                      </IconButton>
                      <IconButton>
                        {' '}
                        <IconTrash size={'1rem'} />{' '}
                      </IconButton>
                    </ButtonGroup>
                  </Box>
                </Box>
              </Grid>
            );
          })}
          <CompanyStaffUpdateModal ref={addModalRef} formik={formik} />
        </Grid>
      </Grid>
    </Grid>
  );
};

export default CompanyStaffDynamicInput;
