import { UserProfile } from 'app/berry/types/user-profile';

export interface ContactStateProps {
  contacts: UserProfile[];
  error: object | string | null;
}
