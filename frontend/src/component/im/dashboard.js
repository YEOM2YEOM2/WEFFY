import React, { useState, useEffect } from "react";
import { Route, Routes, Outlet, useLocation } from "react-router-dom";
import styles from "../pages/im.module.css";
import { useNavigate } from "react-router-dom";

//mui 라이브라리
import { styled, useTheme } from "@mui/material/styles";
import Box from "@mui/material/Box";
import MuiDrawer from "@mui/material/Drawer";
import MuiAppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import List from "@mui/material/List";
import CssBaseline from "@mui/material/CssBaseline";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import MenuIcon from "@mui/icons-material/Menu";
import ChevronLeftIcon from "@mui/icons-material/ChevronLeft";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";

//icons
import GridViewIcon from "@mui/icons-material/GridView";
import MoveToInboxIcon from "@mui/icons-material/MoveToInbox";
import SettingsIcon from "@mui/icons-material/Settings";
import LogoutIcon from "@mui/icons-material/Logout";

//image
import defaultImg from "../assets/images/defualt_image.png";
//buttons
import newMM from "../assets/images/newMM.png";
import newPrivate from "../assets/images/newPrivate.png";
import participate from "../assets/images/participate.png";

//Modal js
import StartMM from "../component/im/startMM.js";
import MMListModal from "../component/im/mmListModal";
import PrivateModal from "../component/im/privateModal.js";
import JoinMeetingModal from "../component/im/joinMeetingList.js";

const buttons = [
  { name: "PrivateModal", src: newPrivate },
  { name: "MMListModal", src: newMM },
  { name: "JoinMeetingModal", src: participate },
];

//mui icon
const drawerWidth = 240;

const icons = [
  { name: "DashBoard", src: <GridViewIcon /> },
  { name: "MyList", src: <MoveToInboxIcon /> },
  { name: "Setting", src: <SettingsIcon /> },
  { name: "Logout", src: <LogoutIcon /> },
];

const openedMixin = (theme) => ({
  width: drawerWidth,
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.enteringScreen,
  }),
  overflowX: "hidden",
  backgroundColor: "rgba(46, 46, 72, 0.7)", // Add this line
});

const closedMixin = (theme) => ({
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  overflowX: "hidden",
  width: `calc(${theme.spacing(7)} + 1px)`,
  [theme.breakpoints.up("sm")]: {
    width: `calc(${theme.spacing(8)} + 1px)`,
  },

  backgroundColor: "rgba(65, 65, 112, 0.8)", // 열렸을경우
});

const DrawerHeader = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  justifyContent: "flex-end",
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
}));

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  zIndex: theme.zIndex.drawer + 1,
  transition: theme.transitions.create(["width", "margin"], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    marginLeft: drawerWidth,
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(["width", "margin"], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

const Drawer = styled(MuiDrawer, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  width: drawerWidth,
  flexShrink: 0,
  whiteSpace: "nowrap",
  boxSizing: "border-box",
  ...(open && {
    ...openedMixin(theme),
    "& .MuiDrawer-paper": openedMixin(theme),
  }),
  ...(!open && {
    ...closedMixin(theme),
    "& .MuiDrawer-paper": closedMixin(theme),
  }),
}));

export default function ClippedDrawer() {
  const theme = useTheme();
  const [open, setOpen] = React.useState(true);

  const navigate = useNavigate();

  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
  };

  const location = useLocation();

  const isImPage = location.pathname === "/im";

  // 모달 상태 선언
  const [modalStatus, setModalStatus] = useState({
    startMM: false,
    newPrivate: false,
    JoinMeetingModal: false,
    MMListModal: false,
  });

  //mm연동 미팅 시작 핸들링
  const handleStartMeeting = () => {
    setModalStatus({
      ...modalStatus,
      startMM: true, // MmModal을 보여주고
      MMListModal: false, // MMListModal은 숨깁니다.
    });
  };

  const handleModalOpen = (modalName) => {
    //modalName에 맞는 modal true로 변경
    setModalStatus({
      ...modalStatus,
      [modalName]: true,
    });
  };

  const handleModalClose = (modalName) => {
    //modalName에 맞는 modal false 변경
    setModalStatus({
      ...modalStatus,
      [modalName]: false,
    });
  };

  const isModalOpen = Object.values(modalStatus).some(
    (status) => status === true
  );

  //옆 사이드바 아이콘을 클릭 했을 경우 url을 옮겨줄 hanndler
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
    navigate("/im/logout");
  };

  const handlers = [
    handleDashboardClick,
    handleMyMeetingClick,
    handleSettingClick,
    handleLogoutClick,
  ];

  return (
    <Box sx={{ display: "flex" }}>
      <CssBaseline />
      <AppBar
        position="fixed"
        open={open}
        style={{ backgroundColor: "rgba(46, 46, 72, 0.8)" }}
      >
        <Toolbar>
          <IconButton
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            edge="start"
            sx={{
              marginRight: 5,
              ...(open && { display: "none" }),
            }}
          >
            <MenuIcon />
          </IconButton>
          <Typography
            variant="h4"
            noWrap
            component="div"
            style={{ fontFamily: "Mogra" }}
          >
            WEFFY
          </Typography>
        </Toolbar>
      </AppBar>
      <Drawer variant="permanent" open={open}>
        <DrawerHeader>
          <IconButton onClick={handleDrawerClose}>
            {theme.direction === "rtl" ? (
              <ChevronRightIcon />
            ) : (
              <ChevronLeftIcon />
            )}
          </IconButton>
        </DrawerHeader>
        <div className={styles["profileContainer"]}>
          <img
            src={defaultImg}
            alt="Profile"
            className={open ? styles.profileImg : styles.profileImgClosed}
          />
        </div>
        <Divider />

        <List>
          {icons.map((icon, index) => (
            <ListItem key={icon.name} disablePadding sx={{ display: "block" }}>
              <ListItemButton
                sx={{
                  minHeight: 48,
                  justifyContent: open ? "flex-start" : "center", // Change this line
                  px: 2.5,
                }}
                onClick={handlers[index]} // Add this line
              >
                <ListItemIcon
                  sx={{
                    minWidth: 0,
                    mr: open ? 3 : "auto",
                    justifyContent: "center",
                  }}
                  style={{ color: "white" }}
                >
                  {icon.src}
                </ListItemIcon>
                <ListItemText
                  primary={icon.name}
                  sx={{ opacity: open ? 1 : 0 }}
                  style={{ color: "white" }}
                />
              </ListItemButton>
            </ListItem>
          ))}
        </List>

        <Divider />
        {/* ... */}
      </Drawer>
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          p: 3,
          width: open ? `calc(100vw - ${drawerWidth}px)` : "100vw", // Adjust the width dynamically
        }}
        className={styles["container"]}
      >
        {isImPage && !isModalOpen && (
          <div
            className={styles["buttonContainer"]}
            style={{ left: `calc(50% + ${open ? drawerWidth / 2 : 0}px)` }}
          >
            {buttons.map((button, index) => (
              <div key={index} onClick={() => handleModalOpen(button.name)}>
                <img
                  src={button.src}
                  alt={button.name}
                  className={styles["button"]}
                />
              </div>
            ))}
          </div>
        )}

        {modalStatus.MMListModal && (
          <MMListModal handleClose={() => handleModalClose("MMListModal")} handleStartMeeting={handleStartMeeting} />
        )}

        {modalStatus.startMM && (
          <StartMM handleClose={() => handleModalClose("startMM")} />
        )}
        {modalStatus.PrivateModal && (
          <PrivateModal
            show={modalStatus.PrivateModal}
            handleClose={() => handleModalClose("PrivateModal")}
            sidebarOpen={open} // Add this line
          />
        )}
        {modalStatus.JoinMeetingModal && (
          <JoinMeetingModal
            show={modalStatus.ParticipateModal}
            handleClose={() => handleModalClose("JoinMeetingModal")}
            sidebarOpen={open} // Add this line
          />
        )}
      </Box>
    </Box>
  );
}
