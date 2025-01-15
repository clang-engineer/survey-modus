import { library } from '@fortawesome/fontawesome-svg-core';

import { faCogs as fasCogs } from '@fortawesome/free-solid-svg-icons/faCogs';
import { faBan as fasBan } from '@fortawesome/free-solid-svg-icons/faBan';
import { faAsterisk as fasAsterisk } from '@fortawesome/free-solid-svg-icons/faAsterisk';
import { faArrowLeft as fasArrowLeft } from '@fortawesome/free-solid-svg-icons/faArrowLeft';
import { faBell as fasBell } from '@fortawesome/free-solid-svg-icons/faBell';
import { faBook as fasBook } from '@fortawesome/free-solid-svg-icons/faBook';
import { faCloud as fasCloud } from '@fortawesome/free-solid-svg-icons/faCloud';
import { faDatabase as fasDatabase } from '@fortawesome/free-solid-svg-icons/faDatabase';
import { faEye as fasEye } from '@fortawesome/free-solid-svg-icons/faEye';
import { faFlag as fasFlag } from '@fortawesome/free-solid-svg-icons/faFlag';
import { faHeart as fasHeart } from '@fortawesome/free-solid-svg-icons/faHeart';
import { faHome as fasHome } from '@fortawesome/free-solid-svg-icons/faHome';
import { faList as fasList } from '@fortawesome/free-solid-svg-icons/faList';
import { faLock as fasLock } from '@fortawesome/free-solid-svg-icons/faLock';
import { faPencilAlt as fasPencilAlt } from '@fortawesome/free-solid-svg-icons/faPencilAlt';
import { faPlus as fasPlus } from '@fortawesome/free-solid-svg-icons/faPlus';
import { faSave as fasSave } from '@fortawesome/free-solid-svg-icons/faSave';
import { faSearch as fasSearch } from '@fortawesome/free-solid-svg-icons/faSearch';
import { faSort as fasSort } from '@fortawesome/free-solid-svg-icons/faSort';
import { faSync as fasSync } from '@fortawesome/free-solid-svg-icons/faSync';
import { faRoad as fasRoad } from '@fortawesome/free-solid-svg-icons/faRoad';
import { faSignInAlt as fasSignInAlt } from '@fortawesome/free-solid-svg-icons/faSignInAlt';
import { faSignOutAlt as fasSignOutAlt } from '@fortawesome/free-solid-svg-icons/faSignOutAlt';
import { faTachometerAlt as fasTachometerAlt } from '@fortawesome/free-solid-svg-icons/faTachometerAlt';
import { faTasks as fasTasks } from '@fortawesome/free-solid-svg-icons/faTasks';
import { faThList as fasThList } from '@fortawesome/free-solid-svg-icons/faThList';
import { faTimesCircle as fasTimesCircle } from '@fortawesome/free-solid-svg-icons/faTimesCircle';
import { faTrash as fasTrash } from '@fortawesome/free-solid-svg-icons/faTrash';
import { faUser as fasUser } from '@fortawesome/free-solid-svg-icons/faUser';
import { faUserPlus as fasUserPlus } from '@fortawesome/free-solid-svg-icons/faUserPlus';
import { faUsers as fasUsers } from '@fortawesome/free-solid-svg-icons/faUsers';
import { faUsersCog as fasUsersCog } from '@fortawesome/free-solid-svg-icons/faUsersCog';
import { faWrench as fasWrench } from '@fortawesome/free-solid-svg-icons/faWrench';
import { faCheck as fasCheck } from '@fortawesome/free-solid-svg-icons/faCheck';
import { faAlignLeft as fasAlignLeft } from '@fortawesome/free-solid-svg-icons/faAlignLeft';
import { faUndo as fasUndo } from '@fortawesome/free-solid-svg-icons/faUndo';
import { faCog as fasCog } from '@fortawesome/free-solid-svg-icons/faCog';

// Regular icons
import { faFolderOpen as farFolderOpen } from '@fortawesome/free-regular-svg-icons/faFolderOpen';
import { faFolder as farFolder } from '@fortawesome/free-regular-svg-icons/faFolder';
import { faCheckSquare as farCheckSquare } from '@fortawesome/free-regular-svg-icons/faCheckSquare';

export const loadIcons = () => {
  // Solid icons
  library.add(
    fasArrowLeft,
    fasAsterisk,
    fasBan,
    fasBell,
    fasBook,
    fasCloud,
    fasCogs,
    fasDatabase,
    fasEye,
    fasFlag,
    fasHeart,
    fasHome,
    fasList,
    fasLock,
    fasPencilAlt,
    fasPlus,
    fasRoad,
    fasSave,
    fasSignInAlt,
    fasSignOutAlt,
    fasSearch,
    fasSort,
    fasSync,
    fasTachometerAlt,
    fasTasks,
    fasThList,
    fasTimesCircle,
    fasTrash,
    fasUser,
    fasUserPlus,
    fasUsers,
    fasUsersCog,
    fasWrench,
    fasCheck,
    fasAlignLeft,
    fasUndo,
    fasCog
  );

  // Regular icons
  library.add(farFolder, farFolderOpen, farCheckSquare);
};
