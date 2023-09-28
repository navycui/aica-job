import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(relativeTime);
export type { Dayjs } from 'dayjs';
export default dayjs;
