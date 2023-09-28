import { css } from '@emotion/react';

export const container = css`
  position: relative;
  display: flex;
  padding: 8px 15px;
  background-color: rgba(0, 0, 0, 0.3);
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
export const detal_tab = css`
  background-color: rgba(0, 0, 0, 0.3);
  .MuiBox-root{
    padding: 0;
  }
  .MuiTabs-root{
    max-width: 1260px;
    margin: 0 auto;
  }
  .MuiTabs-flexContainer{
    > button{
      padding: 11px 32px;
      font-size: 18px;
      border-radius: 10px 10px 0 0;
      color: #707070;
      background-color: #E0E0E0;
      border-right: 1px solid #000;
    }
    .Mui-selected {
      color: #1976d2;
      background-color: #fff;
    }
  }
  @media (min-width: 320px) and (max-width: 820px) {
    .MuiTabs-flexContainer{
      padding: 0 15px;
      > button{
        flex: 1;
        font-size: 16px;
      }
    }
  }
`;
export const containerFactor = css`
  position: relative;
  display: flex;
  padding: 17px 40px;
  background-color: #160909;
  color: #fff;
  // border-bottom: 1px solid #707070;
  justify-content: space-between;
  
  font-size: 14px;
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

export const sub_cont01 = css`
  position: relative;
  display: block;
  color: #fff;
  .benner{
    text-align: center;
    background-color: #1F2437;
    min-height: 460px;
    width: 100%;
  }
  .txtbox{
    margin: 0 auto;
    max-width: 1080px;
    width: 100%;
    .tit{
      font-size: 48px;
      letter-spacing: -4px;
      font-weight: 800;
      margin-bottom: 23px;
    }
    p {
      line-height: 1.8;
    }
  }
  .search_btn{
    position: absolute;
    right: 0;
    border-radius: 30px;
    width: 140px;
    height: 60px;
    background-color: #4063EC;
    font-size: 18px;
  }
  @media (min-width: 320px) and (max-width: 820px) {
    .txtbox{
      .tit{
        font-size: 28px;
        margin-bottom: 19px;
      }
      p {
        font-size: 14px;
        line-height: 2;
      }
    }
    .search_btn{
      width: 80px;
      height: 50px;
      font-size: 16px;
    }
    .benner{
      min-height: 420px;
    }
  }
`;

export const sub_cont02 = css`
  min-height: 840px;
  background-color: #fff;
  color: #333;
  .MuiTypography-h5 {
    font-size: 24px; 
    font-weight: 800;
  }
  .md_btn{
    color: #333;
    border: 1px solid #333;
    width: 220px;
    height: 55px;
    border-radius: 0;
    margin-top: 10px;
  }
  .data{
    margin-left: 10px;
    font-size: 16px;
    > em {
      font-style: normal;
      font-weight: bold;
      color: #4063EC;
    }
  }
  .MuiSelect-select{
    padding: 8px 40px 8px 20px;
    margin-right: 10px;
  }
  @media (min-width: 320px) and (max-width: 820px) {
    .MuiTypography-h5 {
      font-size: 22px; 
    }
    .MuiSelect-select{
      font-size: 14px;
    }
  }
`;
