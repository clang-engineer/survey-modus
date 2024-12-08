// third-party
// project imports
import snackbarReducer from './slices/snackbar';
import menuReducer from './slices/menu';

// ==============================|| COMBINE REDUCER ||============================== //

const berryReducer = {
  snackbar: snackbarReducer,
  menu: menuReducer,
};

export default berryReducer;
