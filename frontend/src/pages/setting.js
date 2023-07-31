import React, { useState, useEffect } from "react";
//img
import defaultImg from "../assets/images/defualt_image.png";

//style
import styles from "../pages/setting.module.css";

//외부 라이브러리
import Grid from "@mui/material/Grid";
import BadgeIcon from "@mui/icons-material/Badge";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import SettingsIcon from "@mui/icons-material/Settings";

//내 파일
import EditProfile from "../component/im/editProfile.js";

const Setting = (props) => {
  const [status, setStatus] = useState(true);

  return (
    <div className={styles["container"]}>
      <Grid container style={{ height: "100%" }}>
        <Grid item xs={3} className={styles["bar"]}>
          <h2 style={{ color: "white", textAlign: "left", marginLeft: "10px" }}>
            Setting
          </h2>
          <List>
            {["Profile", "Device Setting"].map((text, index) => (
              <ListItem key={text} disablePadding>
                <ListItemButton>
                  <ListItemIcon
                    className={styles["menuIcon"]}
                    style={{ color: "white" }}
                  >
                    {index === 0 ? <BadgeIcon /> : <SettingsIcon />}
                  </ListItemIcon>
                  <ListItemText
                    primary={<span className={styles["menuIcon"]}>{text}</span>}
                  />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Grid>
        <Grid item xs={9} className={styles["content"]}>
          {status === true ? <EditProfile /> : null}
        </Grid>
      </Grid>
    </div>
  );
};

export default Setting;
