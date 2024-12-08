import React from 'react';

// third-party
import { FormattedMessage } from 'react-intl';

import { NavItemType } from 'app/berry/types';

// assets
import { IconApps, IconUserCheck, IconBasket, IconMessages, IconLayoutKanban, IconMail, IconCalendar, IconNfc } from '@tabler/icons';

// constant
const icons = {
  IconApps,
  IconUserCheck,
  IconBasket,
  IconMessages,
  IconLayoutKanban,
  IconMail,
  IconCalendar,
  IconNfc,
};

// ==============================|| APPLICATION MENU ITEMS ||============================== //

const application: NavItemType = {
  id: 'application',
  title: 'application',
  icon: icons.IconApps,
  type: 'group',
  children: [
    {
      id: 'users',
      title: 'users',
      type: 'collapse',
      icon: icons.IconUserCheck,
      children: [
        {
          id: 'posts',
          title: 'social-profile',
          type: 'item',
          url: '/user/social-profile/posts',
        },
        {
          id: 'account-profile',
          title: 'account-profile',
          type: 'collapse',
          children: [
            {
              id: 'profile1',
              title: <>"profile" 01</>,
              type: 'item',
              url: '/user/account-profile/profile1',
            },
            {
              id: 'profile2',
              title: <>"profile" 02</>,
              type: 'item',
              url: '/user/account-profile/profile2',
            },
            {
              id: 'profile3',
              title: <>"profile" 03</>,
              type: 'item',
              url: '/user/account-profile/profile3',
            },
          ],
        },
        {
          id: 'user-card',
          title: 'cards',
          type: 'collapse',
          children: [
            {
              id: 'card1',
              title: <>"style" 01</>,
              type: 'item',
              url: '/user/card/card1',
            },
            {
              id: 'card2',
              title: <>"style" 02</>,
              type: 'item',
              url: '/user/card/card2',
            },
            {
              id: 'card3',
              title: <>"style" 03</>,
              type: 'item',
              url: '/user/card/card3',
            },
          ],
        },
        {
          id: 'user-list',
          title: 'list',
          type: 'collapse',
          children: [
            {
              id: 'list1',
              title: <>"style" 01</>,
              type: 'item',
              url: '/user/list/list1',
            },
            {
              id: 'list2',
              title: <>"style" 02</>,
              type: 'item',
              url: '/user/list/list2',
            },
          ],
        },
      ],
    },
    {
      id: 'customer',
      title: 'customer',
      type: 'collapse',
      icon: icons.IconBasket,
      children: [
        {
          id: 'customer-list',
          title: 'customer-list',
          type: 'item',
          url: '/customer/customer-list',
          breadcrumbs: false,
        },
        {
          id: 'order-list',
          title: 'order-list',
          type: 'item',
          url: '/customer/order-list',
          breadcrumbs: false,
        },
        {
          id: 'create-invoice',
          title: 'create-invoice',
          type: 'item',
          url: '/customer/create-invoice',
          breadcrumbs: false,
        },
        {
          id: 'order-details',
          title: 'order-details',
          type: 'item',
          url: '/customer/order-details',
        },
        {
          id: 'product',
          title: 'product',
          type: 'item',
          url: '/customer/product',
          breadcrumbs: false,
        },
        {
          id: 'product-review',
          title: 'product-review',
          type: 'item',
          url: '/customer/product-review',
          breadcrumbs: false,
        },
      ],
    },
    {
      id: 'chat',
      title: 'chat',
      type: 'item',
      icon: icons.IconMessages,
      url: '/app/chat',
    },
    {
      id: 'kanban',
      title: 'Kanban',
      type: 'item',
      icon: icons.IconLayoutKanban,
      url: '/app/kanban/board',
    },
    {
      id: 'mail',
      title: 'mail',
      type: 'item',
      icon: icons.IconMail,
      url: '/app/mail',
    },
    {
      id: 'calendar',
      title: 'calendar',
      type: 'item',
      url: '/app/calendar',
      icon: icons.IconCalendar,
      breadcrumbs: false,
    },
    {
      id: 'contact',
      title: 'contact',
      type: 'collapse',
      icon: icons.IconNfc,
      children: [
        {
          id: 'c-card',
          title: 'cards',
          type: 'item',
          url: '/app/contact/c-card',
          breadcrumbs: false,
        },
        {
          id: 'c-list',
          title: 'list',
          type: 'item',
          url: '/app/contact/c-list',
          breadcrumbs: false,
        },
      ],
    },
    {
      id: 'e-commerce',
      title: 'e-commerce',
      type: 'collapse',
      icon: icons.IconBasket,
      children: [
        {
          id: 'products',
          title: 'products',
          type: 'item',
          url: '/e-commerce/products',
        },
        {
          id: 'product-details',
          title: 'product-details',
          type: 'item',
          url: '/e-commerce/product-details/1',
          breadcrumbs: false,
        },
        {
          id: 'product-list',
          title: 'product-list',
          type: 'item',
          url: '/e-commerce/product-list',
          breadcrumbs: false,
        },
        {
          id: 'checkout',
          title: 'checkout',
          type: 'item',
          url: '/e-commerce/checkout',
        },
      ],
    },
  ],
};

export default application;
