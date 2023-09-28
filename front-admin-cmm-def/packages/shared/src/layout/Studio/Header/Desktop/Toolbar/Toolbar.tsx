/* eslint-disable jsx-a11y/alt-text */
import Typography from '@mui/material/Typography';
import { Fragment } from 'react';
import { NavLink } from 'react-router-dom';
import { AuthenticationType } from 'shared/authentication';
import useSWR from 'swr';

function Toolbar() {
  const { data } = useSWR<AuthenticationType>('authentication');
  return (
    <section className="admin_header">
      <div className="top_logo">
        <NavLink to="/BusInformationMgt/BusInformationMgt/BusInformationMgt">
          <img src="/images/logo01.png" />
        </NavLink>
        <Typography
          variant="h5"
          gutterBottom
          component="div"
          className="top_title"
        >
          사용자지원포털
        </Typography>
      </div>

      <ul className="login_info">
        {data?.accessToken ? (
          <Fragment>
            <li>
              <NavLink to={'/#'}> 홍길동님 (anvycui)</NavLink>
            </li>
            <li>
              <NavLink to={'/signout'}>로그아웃</NavLink>
            </li>
          </Fragment>
        ) : (
          <Fragment>
            <li>
              <NavLink to={'/signin'}>로그인</NavLink>
            </li>
          </Fragment>
        )}
      </ul>
    </section>
  );
}
export default Toolbar;
