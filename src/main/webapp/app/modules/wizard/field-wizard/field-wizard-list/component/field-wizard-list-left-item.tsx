import React from 'react';
import { getItemStyle } from 'app/modules/wizard/field-wizard/field-wizard-list/field-wizard-dnd.utils';
import FieldWizardListLeftTitle from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-list-left-title';
import FieldWizardUpdateModal from 'app/modules/wizard/field-wizard/field-wizard-list/component/field-wizard-update.modal';

import { Box, ButtonGroup, Grid, IconButton, Switch, Button } from '@mui/material';
import { Draggable } from 'react-beautiful-dnd';
import { create } from 'react-modal-promise';
import { IconPencil, IconTrash } from '@tabler/icons';
import { IField } from 'app/shared/model/field.model';
import useFieldWizardConfig from 'app/modules/wizard/field-wizard/field-wizard.config';
import PromiseModal from 'app/shared/component/promise-modal';

const deleteModal = create(
  PromiseModal({
    title: 'Delete Field',
    content: 'Do you want to delete this field?',
    rejectButtonText: 'Cancel',
    resolveButtonText: 'Delete',
  })
);

const FieldWizardListLeftItem = (props: { item: IField; index: number }) => {
  const { item, index } = props;

  const { items, setItems } = useFieldWizardConfig();

  const handleDelete = id => {
    setItems(items.filter(d => d.id !== id));
  };

  return (
    <Draggable key={item.id} draggableId={`draggable-left-${item.id}`} index={index}>
      {(provided, snapshot) => (
        <Grid
          item
          xs={12}
          ref={provided.innerRef}
          {...provided.draggableProps}
          {...provided.dragHandleProps}
          style={getItemStyle(snapshot.isDragging, provided.draggableProps.style)}
        >
          <Box display="flex" justifyContent="space-between" alignItems="center">
            <FieldWizardListLeftTitle item={item} index={index} />
            <ButtonGroup size="small">
              <IconButton>
                <Switch
                  size="small"
                  checked={item.activated}
                  onChange={e => {
                    const data = { ...items[index] };
                    data.activated = e.target.checked;

                    setItems([...items.slice(0, index), data, ...items.slice(index + 1)]);
                  }}
                  name="activated"
                />
              </IconButton>
              <IconButton
                onClick={() => {
                  create(
                    FieldWizardUpdateModal({
                      field: item,
                      items,
                      setItems,
                    })
                  )();
                }}
              >
                <IconPencil size={'1rem'} />
              </IconButton>
              <IconButton
                onClick={() => {
                  deleteModal().then(result => {
                    if (result) handleDelete(item.id);
                  });
                }}
              >
                <IconTrash size={'0.9rem'} />
              </IconButton>
            </ButtonGroup>
          </Box>
        </Grid>
      )}
    </Draggable>
  );
};

export default FieldWizardListLeftItem;
