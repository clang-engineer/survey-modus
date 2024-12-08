import { useContext } from 'react';
import { ConfigContext } from 'app/berry/contexts/ConfigContext';

// ==============================|| CONFIG - HOOKS  ||============================== //

const useConfig = () => useContext(ConfigContext);

export default useConfig;
