import Palette from 'app/berry/themes/palette';
import config from 'app/berry/config';

// a little function to help us with reordering the result
const reorder = (list, startIndex, endIndex) => {
  const result = Array.from(list);
  const [removed] = result.splice(startIndex, 1);
  result.splice(endIndex, 0, removed);

  return result;
};

const grid = 8;

const getItemStyle = (isDragging, draggableStyle) => {
  const theme = Palette(config.navType, config.presetColor);

  return {
    // some basic styles to make the items look a bit nicer
    userSelect: 'none',
    padding: grid * 2,
    margin: `0 0 ${grid}px 0`,

    // change background colour if dragging
    background: isDragging ? theme.palette.secondary.light : theme.palette.grey[50],

    border: '1px dashed #000',
    borderRadius: '5px',
    shadow: '0 0 10px rgba(0, 0, 0, 0.5)',

    // styles we need to apply on draggables
    ...draggableStyle,
  };
};

const getListStyle = isDraggingOver => {
  const theme = Palette(config.navType, config.presetColor);

  return {
    background: isDraggingOver ? theme.palette.primary.light : theme.palette.background.paper,
  };
};

export { reorder, getItemStyle, getListStyle };
