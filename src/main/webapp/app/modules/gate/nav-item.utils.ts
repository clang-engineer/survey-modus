import { NavItemType } from 'app/berry/types';
import { IForm } from 'app/shared/model/form.model';
import { IconHome, IconClipboardText } from '@tabler/icons';
import { ICompany } from 'app/shared/model/company.model';

const NavItemWrapper = (item: NavItemType) => {
  return {
    id: Math.random().toString(36).substring(7),
    type: 'group',
    children: [item],
  };
};

const CreateCompanyNavItems = (company: ICompany) => {
  const menuItems: NavItemType[] = [];

  const formList: IForm[] = company.forms;
  formList
    .filter(f => f.activated)
    .forEach(form => {
      menuItems.push(
        NavItemWrapper({
          id: `${form.id}`,
          title: form.title,
          icon: IconClipboardText,
          type: 'item',
          url: `/gate/companies/${company.id}/forms/${form.id}`,
        })
      );
    });

  menuItems.push(
    NavItemWrapper({
      id: 'companies',
      title: 'Home',
      icon: IconHome,
      type: 'item',
      url: `/gate/companies`,
    })
  );

  return menuItems;
};

export { NavItemWrapper, CreateCompanyNavItems };
