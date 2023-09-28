import {Color} from "../../components/StyleUtils";

function Footer() {
  return <footer
    style={{
      display: "flex",
      height: "60px",
      color: Color.brownishGrey,
      alignItems: "center",
      justifyContent: "end",
      paddingRight: "20px",
      fontSize: "14px",
      border: "1px solid #d7dae6"
  }}>
    ©2022 인공지능산업융합사업단. ALL RIGHTS RESERVED
  </footer>;
}

export default Footer;
