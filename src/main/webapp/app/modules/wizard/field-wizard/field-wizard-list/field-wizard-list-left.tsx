import React from 'react';

import { Grid } from '@mui/material';

import { Droppable } from 'react-beautiful-dnd';
import { getListStyle } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import NoContentBox from 'app/shared/component/no-content-box';
// import IFieldItem from "app/modules/wizard/field-wizard/field-wizard-list/field-item.model";
import useFieldWizardConfig from 'app/modules/wizard/field-wizard/field-wizard.config';
import FieldWizardListLeftItem from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-list-left-item';

const EmptyDndBox = () => {
  return (
    <Grid item xs={12}>
      <NoContentBox title="Drag and drop fields from the right side" height={150} />
    </Grid>
  );
};

const FieldWizardListLeft = () => {
  const { items } = useFieldWizardConfig();

  return (
    <Droppable droppableId="left">
      {(provided, snapshot) => (
        <Grid container {...provided.droppableProps} ref={provided.innerRef} style={getListStyle(snapshot.isDraggingOver)}>
          {items.length === 0 ? (
            <Grid item xs={12}>
              {' '}
              <EmptyDndBox />{' '}
            </Grid>
          ) : (
            items.map((item, index) => <FieldWizardListLeftItem key={item.id} item={item} index={index} />)
          )}
          {provided.placeholder}
        </Grid>
      )}
    </Droppable>
  );
};

export default FieldWizardListLeft;
