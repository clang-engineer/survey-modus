// types
import { MenuProps } from 'app/berry/types/menu';
import { createSlice } from '@reduxjs/toolkit';

// project imports
import axios from 'axios';
import { useAppDispatch } from 'app/config/store';

// initial state
const initialState: MenuProps = {
  selectedItem: ['dashboard'],
  selectedID: null,
  drawerOpen: false,
  error: null,
  menu: {},
};

// ==============================|| SLICE - MENU ||============================== //

const menu = createSlice({
  name: 'menu',
  initialState,
  reducers: {
    activeItem(state, action) {
      state.selectedItem = action.payload;
    },

    activeID(state, action) {
      state.selectedID = action.payload;
    },

    openDrawer(state, action) {
      state.drawerOpen = action.payload;
    },

    // has error
    hasError(state, action) {
      state.error = action.payload;
    },

    // get dashboard menu
    getMenuSuccess(state, action) {
      state.menu = action.payload;
    },
  },
});

export default menu.reducer;

export const { activeItem, openDrawer, activeID } = menu.actions;

export function getMenu() {
  const dispatch = useAppDispatch();

  return async () => {
    try {
      const response = await axios.get('/api/menu/widget');
      dispatch(menu.actions.getMenuSuccess(response.data.widget));
    } catch (error) {
      dispatch(menu.actions.hasError(error));
    }
  };
}
