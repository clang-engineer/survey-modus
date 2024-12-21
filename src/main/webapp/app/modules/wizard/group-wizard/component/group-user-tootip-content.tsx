import React from 'react';
import { Box, Grid, Typography } from '@mui/material';
import { IconUser } from '@tabler/icons';
import { IUser } from 'app/shared/model/user.model';

const GroupUsersTooltipContent = (props: { users: IUser[] }) => {
  const { users } = props;

  return (
    <Grid container spacing={2}>
      <Grid item xs={12}>
        <Typography variant="h5" color="text.primary" sx={{ textDecoration: 'underline' }}>
          Users
        </Typography>
      </Grid>
      <Grid item xs={12}>
        {users.length == 0 ? (
          <Box display="flex" alignItems="center" marginBottom={1}>
            <Typography variant="body2" color="text.primary">
              No users
            </Typography>
          </Box>
        ) : (
          users.map((user, i) => (
            <Box key={i} display="flex" alignItems="center" marginBottom={1}>
              <Typography variant="body2" color="text.primary">
                <IconUser size={'1rem'} strokeWidth={1.5} /> &nbsp;
                {user.firstName} ({user.email})
              </Typography>
            </Box>
          ))
        )}
      </Grid>
    </Grid>
  );
};

export default GroupUsersTooltipContent;
