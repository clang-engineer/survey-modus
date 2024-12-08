import React from 'react';
import KerasH from '../../../content/images/logo/keras-h.svg';
// material-ui

/**
 * if you want to use image instead of <svg> uncomment following.
 *
 * import logoDark from 'assets/images/logo-dark.svg';
 * import logo from 'assets/images/logo.svg';
 *
 */
// ==============================|| LOGO SVG ||============================== //

const Logo = () => {
  return <img src={KerasH} alt={'KerasH'} />;
};

export default Logo;
