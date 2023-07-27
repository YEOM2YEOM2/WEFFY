import React, { useState } from "react";
import SideBar from "../component/im/sidebar.js";
import styles from "./im.module.css";

import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import ListItemIcon from "@mui/material/ListItemIcon";

//image
import newMM from "../assets/images/newMM.png";
import newPrivate from "../assets/images/newPrivate.png";
import participate from "../assets/images/participate.png";
import { Grid } from "@mui/material";

const icons = [
  <img src={newMM} alt="newMM" />,
  <img src={newPrivate} alt="newPrivate" />,
  <img src={participate} alt="participate" />,
];

const Im = (props) => {
  let [buttonStatus, setStatue] = useState(true);
  return (
    <div className={styles["container"]}>
      <span className={styles["logoText"]}>
        <span>WEFFY</span>
      </span>
      <div className={styles["sidebar"]}>
        <SideBar />
      </div>
      <div className={styles["functionsBtn"]}>
        {buttonStatus &&
          icons.map((icon, index) => (
            <div className={styles["button"]} key={index}>
              {icon}
            </div>
          ))}
      </div>
    </div>
  );
};

export default Im;
