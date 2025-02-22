import React from 'react';

// third-party
import { FormattedMessage } from 'react-intl';

// project import

// assets
import { IconChartArcs, IconClipboardList, IconChartInfographic } from '@tabler/icons';
import { NavItemType } from 'app/berry/types';
import { useAppSelector } from 'app/config/store';

const icons = {
  widget: IconChartArcs,
  statistics: IconChartArcs,
  data: IconClipboardList,
  chart: IconChartInfographic,
};

// ==============================|| MENU ITEMS - API ||============================== //

export const Menu = () => {
  const { menu } = useAppSelector(state => state.menu);

  const SubChildrenLis = (subChildrenLis: NavItemType[]) => {
    return subChildrenLis?.map((subList: NavItemType) => {
      return {
        ...subList,
        title: subList.title,
        // @ts-ignore
        icon: icons[subList.icon],
      };
    });
  };

  const menuItem = (subList: NavItemType) => {
    let list: NavItemType = {
      ...subList,
      title: subList.title,
      // @ts-ignore
      icon: icons[subList.icon],
    };

    if (subList.type === 'collapse') {
      list.children = SubChildrenLis(subList.children!);
    }
    return list;
  };

  const withoutMenu = menu?.children?.filter((item: NavItemType) => item.id !== 'no-menu');

  const ChildrenList: NavItemType[] | undefined = withoutMenu?.map((subList: NavItemType) => menuItem(subList));

  const menuList: NavItemType = {
    ...menu,
    title: menu.title,
    // @ts-ignore
    icon: icons[menu.icon],
    children: ChildrenList,
  };

  return menuList;
};
