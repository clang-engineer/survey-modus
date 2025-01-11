import React, { useState } from 'react';

import { Box, Button, ButtonGroup, Dialog, DialogActions, DialogContent, Switch, Typography } from '@mui/material';
import useFormWizardConfig from 'app/modules/wizard/form-wizard/form-wizard.config';

import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import { reorder } from 'app/shared/util/dnd-utils';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useTheme } from '@mui/material/styles';
import useConfig from 'app/berry/hooks/useConfig';
import { useAppDispatch } from 'app/config/store';
import { createAndUpdateEntities } from 'app/entities/form/form.reducer';

interface IWizardListUpdateModalProps {
  items: any[];
  onSave: (items: any[]) => void;
}

const WizardListUpdateModal = React.forwardRef((props: IWizardListUpdateModalProps, ref) => {
  React.useImperativeHandle(ref, () => ({
    open: handleOpen,
    close: handleClose,
  }));

  const { items } = props;

  const theme = useTheme();

  const { borderRadius } = useConfig();

  const dndGap = 10;

  const [open, setOpen] = React.useState(false);

  const [localItems, setLocalItems] = useState([]);

  React.useEffect(() => {
    setLocalItems(items);
  }, [items]);

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const onDragEnd = event => {
    const { source, destination } = event;
    // dropped outside the list
    if (!event.destination) {
      return;
    }
    const newItems = reorder(localItems, source.index, destination.index);
    setLocalItems(newItems);
  };

  return (
    <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
      <DialogContent
        style={{
          padding: 0,
        }}
      >
        <DragDropContext onDragEnd={onDragEnd}>
          <Droppable droppableId="form-droppable">
            {(provided1, snapshot1) => (
              <Box
                ref={provided1.innerRef}
                style={{
                  background: snapshot1.isDraggingOver ? theme.palette.grey[200] : theme.palette.background.default,
                  padding: dndGap,
                  width: 500,
                  minHeight: 500,
                }}
                {...provided1.droppableProps}
              >
                {localItems.map((item, index) => (
                  <Draggable key={item.id} draggableId={item.id.toString()} index={index}>
                    {(provided2, snapshot2) => (
                      <Box
                        ref={provided2.innerRef}
                        {...provided2.draggableProps}
                        {...provided2.dragHandleProps}
                        style={{
                          userSelect: 'none',
                          padding: dndGap,
                          margin: `0 0 ${dndGap}px 0`,
                          minHeight: '50px',
                          backgroundColor: snapshot2.isDragging ? theme.palette.grey[300] : theme.palette.grey[100],
                          border: '1px solid #ccc',
                          borderRadius,
                          ...provided2.draggableProps.style,
                        }}
                        display="flex"
                        justifyContent="space-between"
                        alignItems="center"
                        sx={{
                          '.MuiTypography-root': {
                            color: item.activated ? theme.palette.text.primary : theme.palette.text.disabled,
                            textDecoration: item.activated ? 'none' : 'line-through',
                          },
                        }}
                      >
                        <Typography variant="h6">{item.title}</Typography>
                        <Switch
                          size="small"
                          checked={item.activated}
                          onChange={e => {
                            const data = { ...localItems[index] };
                            data.activated = e.target.checked;

                            setLocalItems([...localItems.slice(0, index), data, ...localItems.slice(index + 1)]);
                          }}
                          name="activated"
                        />
                      </Box>
                    )}
                  </Draggable>
                ))}
                {provided1.placeholder}
              </Box>
            )}
          </Droppable>
        </DragDropContext>
      </DialogContent>
      <DialogActions>
        <ButtonGroup size="small" variant="text" aria-label="text button group">
          <Button
            onClick={() => {
              setLocalItems(items);
            }}
          >
            <FontAwesomeIcon icon="undo" />
          </Button>
          <Button
            onClick={() => {
              const reordered = localItems.map((item, index) => {
                return {
                  ...item,
                  orderNo: index,
                };
              });

              props.onSave(reordered);
            }}
          >
            <FontAwesomeIcon icon="save" />
          </Button>
        </ButtonGroup>
      </DialogActions>
    </Dialog>
  );
});

export default WizardListUpdateModal;
