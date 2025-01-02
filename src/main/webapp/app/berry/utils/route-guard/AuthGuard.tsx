import React from 'react';
import { useNavigate } from 'react-router-dom';

// project imports
import useAuth from 'app/berry/hooks/useAuth';
import { GuardProps } from 'app/berry/types';
import { useEffect } from 'react';

// ==============================|| AUTH GUARD ||============================== //

/**
 * Authentication guard for routes
 * @param {PropTypes.node} children children element/node
 */
const AuthGuard = ({ children }: GuardProps) => {
  const { isLoggedIn } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn) {
      navigate('login', { replace: true });
    }
  }, [isLoggedIn, navigate]);

  return children;
};

export default AuthGuard;
