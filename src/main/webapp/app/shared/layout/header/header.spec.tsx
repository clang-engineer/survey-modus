import React from 'react';
import { render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { MemoryRouter } from 'react-router-dom';

import initStore from 'app/config/store';
import Header from './header';

describe('Header', () => {
  let mountedWrapper;
  const devProps = {
    isAuthenticated: true,
    isAdmin: true,
    currentLocale: 'en',
    isInProduction: false,
    isOpenAPIEnabled: true,
  };
  const prodProps = {
    ...devProps,
    isInProduction: true,
    isOpenAPIEnabled: false,
  };
  const userProps = {
    ...prodProps,
    isAdmin: false,
  };
  const guestProps = {
    ...prodProps,
    isAdmin: false,
    isAuthenticated: false,
  };

  const wrapper = (props = devProps) => {
    if (!mountedWrapper) {
      const store = initStore();
      const { container } = render(
        <Provider store={store}>
          <MemoryRouter>
            <Header {...props} />
          </MemoryRouter>
        </Provider>
      );
      mountedWrapper = container.innerHTML;
    }
    return mountedWrapper;
  };

  beforeEach(() => {
    mountedWrapper = undefined;
  });

  // All tests will go here
  it('Renders a Header component with LoadingBar, Navbar, Nav and dev ribbon.', () => {
    const html = wrapper();

    // Find Navbar component
    expect(html).toContain('navbar');
    // Find AdminMenu component
    expect(html).toContain('admin-menu');
    // Find EntitiesMenu component
    expect(html).toContain('entity-menu');
    // Find AccountMenu component
    expect(html).toContain('account-menu');
  });

  it('Renders a Header component in prod profile with logged in User', () => {
    const html = wrapper(userProps);

    // Find Navbar component
    expect(html).toContain('navbar');
    // Not find AdminMenu component
    expect(html).not.toContain('admin-menu');
    // Find EntitiesMenu component
    expect(html).toContain('entity-menu');
    // Find AccountMenu component
    expect(html).toContain('account-menu');
  });

  it('Renders a Header component in prod profile with no logged in User', () => {
    const html = wrapper(guestProps);

    // Find Navbar component
    expect(html).toContain('navbar');
    // Not find AdminMenu component
    expect(html).not.toContain('admin-menu');
    // Not find EntitiesMenu component
    expect(html).not.toContain('entity-menu');
    // Find AccountMenu component
    expect(html).toContain('account-menu');
  });
});
