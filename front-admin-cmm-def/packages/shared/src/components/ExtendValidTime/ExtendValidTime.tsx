import {useEffect, useRef, useState} from 'react';
import useSWR from 'swr';
import {AuthenticationType} from '../../authentication';
import dayjs from '../../libs/dayjs';
import Portal from '../Portal';
import * as styles from './styles';
import {ModalComponents} from "../../components/ModalComponents";

type PropsType = {
  onRefresh: () => void;
  onExpired: () => void;
};

function ExtendValidTime(
  {
    onRefresh = () => {
    },
    onExpired = () => {
    },
  }: PropsType) {
  const [countdown, setCountdown] = useState(1e10);
  const [open, setOpen] = useState(false)
  const [refresh, setRefresh] = useState(true)
  const {data} = useSWR<AuthenticationType>('authentication');

  const timer = useRef<any>(null);

  //* 로그인 연장
  const handleClick = () => {
    onRefresh();
  };

  const handleUpdate = () => {
    const diff = dayjs(data?.refreshTokenExpiresAt).diff(+new Date(), 's');
    setCountdown(diff);
  };

  const format = (seconds: number) => {
    const minute = Math.floor(seconds / 60);
    const second = seconds - minute * 60;
    return `${minute}분 ${second}초`;
  };

  //* authentication 데이터 변경시
  const syncAuthentication = () => {
    clearInterval(timer.current);
    if (data?.accessTokenExpiresAt) {
      timer.current = setInterval(handleUpdate, 1000);
      handleUpdate();
    }
    return () => {
      clearInterval(timer.current);
    };
  };
  useEffect(syncAuthentication, [data]);

  const syncCountdown = () => {
    //* 시간 초과시 로그아웃
    if (countdown <= 0) {
      clearInterval(timer.current);
      onExpired();
    }

    if (!open && refresh && countdown < 60 * 5) {
      setOpen(true)
    }
  };
  useEffect(syncCountdown, [countdown]);

  const init = () => {
  };
  useEffect(init, []);

  //* 5분 미만인 경우 표시
  return countdown < 60 * 5 ? (
    <ModalComponents
      isDist open={open} confirmLabel={'연장'}
      content={`남은 시간: ${format(countdown)}`}
      onConfirm={handleClick}
      onClose={() => {
        setOpen(false)
        setRefresh(false)
      }}
    />
    // <Portal>
    //   <div css={styles.container}>
    //     <div>남은 시간: {format(countdown)}</div>
    //     <button type="button" onClick={handleClick}>
    //       연장
    //     </button>
    //   </div>
    // </Portal>
  ) : null;
}

export default ExtendValidTime;
