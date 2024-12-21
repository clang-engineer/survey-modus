import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';

const icons = {
  IconBrandChrome,
  IconHelp,
  IconSitemap,
};

const entity: NavItemType[] = [
  {
    id: 'entities',
    title: 'entities',
    type: 'collapse',
    icon: icons.IconSitemap,
    children: [
      {
        id: 'group',
        title: 'group',
        type: 'item',
        url: '/group',
        breadcrumbs: true,
      },
      {
        id: 'company',
        title: 'company',
        type: 'item',
        url: '/company',
        breadcrumbs: true,
      },
      {
        id: 'form',
        title: 'form',
        type: 'item',
        url: '/form',
        breadcrumbs: true,
      },
      {
        id: 'category',
        title: 'category',
        type: 'item',
        url: '/category',
        breadcrumbs: true,
      },
      {
        id: 'field',
        title: 'field',
        type: 'item',
        url: '/field',
        breadcrumbs: true,
      },
      {
        id: 'company-form',
        title: 'company-form',
        type: 'item',
        url: '/company-form',
        breadcrumbs: true,
      },
    ],
  },
];
export default entity;
