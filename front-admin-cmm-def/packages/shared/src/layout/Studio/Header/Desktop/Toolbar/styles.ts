import { css } from '@emotion/react';

export const container = css`
  position: relative;
  display: flex;
  padding: 5px 15px 0px;
  background-color: #160909;
  color: #fff;
  justify-content: space-between;
  font-size: 14px;
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

export const sideon = css`
  display: flex;
  flex: 1;
  > p {
    margin-right: 10px;
    em.on{
      display: inline-block;
      width: 12px;
      height: 12px;
      background-color: #2DC11C;
      border-radius: 10px;
      margin-left: 4px;
    }
  }
`;
export const portal = css`
  display: flex;
  flex: 0 0 60%;
  justify-content: center;
  > li{
    margin: 0 10px;
  }
  > li a{
    color: #ccc;
    font-size: 14px;
  }
`;

export const usetext = css`
  margin-left: 15px;
`;

export const utility = css`
  display: flex;
  flex: 1;
  justify-content: end;
  > li{
    margin: 0 10px;
  }
  > li a{
    color: #fff;
  }
`;

export const space = css`
  flex: 1;
`;
export const item = css`
  flex: 1;
`;
