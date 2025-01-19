import React from 'react';

import AnimateButton from 'app/berry/ui-component/extended/AnimateButton';

import { Box, IconButton, Typography } from '@mui/material';

import { IconDatabase, IconPlaylistAdd } from '@tabler/icons';
import { IForm } from 'app/shared/model/form.model';
import { ICompany } from 'app/shared/model/company.model';
import SurveyModal from 'app/modules/survey/dialog';

import { create } from 'react-modal-promise';
import { useAppSelector } from 'app/config/store';

const GateTitle = () => {
  const companyEntity = useAppSelector(state => state.company.entity);
  const formEntity = useAppSelector(state => state.form.entity);
  const fieldEntities = useAppSelector(state => state.field.entities);

  const onClickCreateButton = () => {
    create(
      SurveyModal({
        company: companyEntity,
        form: formEntity,
        fields: fieldEntities.filter(field => field.activated),
      })
    )();
  };

  return (
    <Box display="flex" alignItems="center" justifyContent="space-between">
      <Box display="flex" alignItems="center">
        <Typography variant="h4">
          <IconDatabase size={'1rem'} /> {formEntity.title}
        </Typography>
        &nbsp;&nbsp;
        <Typography variant="caption">
          ({companyEntity.title}, {formEntity.description})
        </Typography>
      </Box>
      <Box>
        <AnimateButton>
          <IconButton color="primary" size="small" onClick={onClickCreateButton}>
            <IconPlaylistAdd />
          </IconButton>
        </AnimateButton>
      </Box>
    </Box>
  );
};

export default GateTitle;
