import { css } from '@emotion/react';

export const container = css`
  position: relative;
  display: flex;
  justify-content: space-between;
  background-color: #160909;
  border-bottom: 1px solid #707070;
`;

export const sidemenu = css`
  display: flex;
  flex: 1;
  > button {
    margin-right: 20px;
  }
  h1 {
    a {
      color: #fff;
    }
  }
`;
export const nav = css``;

export const searchbox = css`
  flex: 1;
`;

export const search = css`
  position: absolute;
  top: 0;
  right: 0;
  padding: 25px;
  width: 80px;
  height: 80px;
  background-color: #1ccdcc;
`;
