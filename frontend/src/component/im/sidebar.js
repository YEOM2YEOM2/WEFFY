import * as React from "react";
import styles from "./sidebar.module.css";

//외부 라이브러리
import Drawer from "@mui/material/Drawer";
import Button from "@mui/material/Button";
import List from "@mui/material/List";
import Divider from "@mui/material/Divider";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";

//이미지
import defaultImg from "../../assets/images/defualt_image.png";
import dashboardIcon from "../../assets/images/icon_dashboard.png";
import meetingIcon from "../../assets/images/icon_mymeeting.png";
import settingIcon from "../../assets/images/icon_setting.png";
import logoutIcon from "../../assets/images/icon_logout.png";
import { useNavigate } from "react-router-dom";
import { Grid } from "@mui/material";

const icons = [
  <img src={dashboardIcon} alt="dashboard" />,
  <img src={meetingIcon} alt="meeting" />,
  <img src={settingIcon} alt="setting" />,
  <img src={logoutIcon} alt="logout" />,
];

const Sidebar = (props) => {
  const navigate = useNavigate();

  const handleDashboardClick = () => {
    console.log("Dashboard was clicked");
    // Dashboard 클릭 시 수행할 동작
    navigate("/im");
  };

  const handleMyMeetingClick = () => {
    console.log("My Meeting was clicked");
    // My Meeting 클릭 시 수행할 동작
    navigate("/im/mylist");
  };

  const handleSettingClick = () => {
    console.log("Setting was clicked");
    // Profile 클릭 시 수행할 동작
    navigate("/im/setting");
  };

  const handleLogoutClick = () => {
    console.log("Logout was clicked");
    // Profile 클릭 시 수행할 동작
  };

  return (
    <Grid container spacing={2}>
      <div className={styles.sideBarBox}>
        <Toolbar />
        <Divider />
        <Grid
          item
          xl={12} // 12칸 전체를 사용하도록 변경
          container
          direction="column" // 아이템들이 세로로 배열되게 변경
          justify="center" // 아이템들이 가로로 중앙에 위치하게 변경
          alignItems="center" // 아이템들이 세로로 중앙에 위치하게 변경
          spacing={2}
        >
          <Grid item xs={12}>
            {" "}
            <img src={defaultImg} alt="Profile" className={styles.profileImg} />
          </Grid>
          <Grid item xs={12}>
            {" "}
            <span className={styles.nickName}>Silver.jjin</span>
          </Grid>
        </Grid>
        <Grid xs={1}>
          <List>
            {["dashboard", "myList", "setting", "Logout"].map((text, index) => (
              <ListItem key={text} disablePadding className={styles.listItem}>
                <ListItemButton>
                  <ListItemIcon>{icons[index]}</ListItemIcon>
                  <ListItemText primary={text} className={styles.text} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Grid>
      </div>
    </Grid>
  );
};

export default Sidebar;
