import { IconBrandChrome, IconHelp, IconSitemap } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';

const icons = {
  IconBrandChrome,
  IconHelp,
  IconSitemap,
};

const entity: NavItemType[] = [
  {
    id: '#entities',
    title: 'entities',
    type: 'collapse',
    icon: icons.IconSitemap,
    children: [
      {
        id: '#category',
        title: 'category',
        type: 'item',
        url: '/entities/category',
        breadcrumbs: true,
      },
      {
        id: '#form',
        title: 'form',
        type: 'item',
        url: '/entities/form',
        breadcrumbs: true,
      },
      {
        id: '#field',
        title: 'field',
        type: 'item',
        url: '/entities/field',
        breadcrumbs: true,
      },
      {
        id: '#company',
        title: 'company',
        type: 'item',
        url: '/entities/company',
        breadcrumbs: true,
      },
      {
        id: '#group',
        title: 'group',
        type: 'item',
        url: '/entities/group',
        breadcrumbs: true,
      },
      {
        id: '#file',
        title: 'file',
        type: 'item',
        url: '/entities/file',
        breadcrumbs: true,
      },
    ],
  },
];
export default entity;
