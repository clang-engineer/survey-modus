import { useNavigate } from 'react-router-dom';

// project imports
import useAuth from 'app/berry/hooks/useAuth';
import { DASHBOARD_PATH } from 'app/berry/config';
import { GuardProps } from 'app/berry/types';
import { useEffect } from 'react';

// ==============================|| GUEST GUARD ||============================== //

/**
 * Guest guard for routes having no auth required
 * @param {PropTypes.node} children children element/node
 */

const GuestGuard = ({ children }: GuardProps) => {
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isLoggedIn) {
      navigate(DASHBOARD_PATH, { replace: true });
    }
  }, [isLoggedIn, navigate]);

  return children;
};

export default GuestGuard;
