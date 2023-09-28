import { css } from '@emotion/react';

export const container = css`
  width: 100%;
  padding: 0px 0px 5px;
  font-size: 15px;
  letter-spacing: -0.2px;
  flex: 1 0 100%;
`;

export const menu = css`
  display: flex;
  li {
    line-height: 2;
    flex: 1;
    text-align: center;
    button {
      font-weight: bold;
      color: #fff;
      background-color: rgba(0,0,0,0);
      border: 0;
    }
    &.active, &:hover{
      > ul {
        display: none;
        position: absolute;
        bottom: -40px;
        left: 0;
        background-color: #fff;
        height: 40px;
        justify-content: center;
        width: 100%;
        > li {
          line-height: 1.6;
          margin: 5px 20px;
          flex: initial;
          a.active {
            border-bottom: 2px solid #fff;
          }
        }
      }
      button {
        border-bottom: 2px solid #fff;
      }
    }
    > ul {
      display: none;
    }
  }
`;
export const navsub = css`

`;
