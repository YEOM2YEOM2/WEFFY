import React, { useState, useEffect } from "react";
import { Outlet, useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

import {
  setIdentification,
  setAccessToken,
  setRefreshToken,
  setCsrfToken,
  setProfileImg,
  setNickname,
} from "../store/reducers/user.js";

import styles from "../pages/im.module.css";

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
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";

import GridViewIcon from "@mui/icons-material/GridView";
import MoveToInboxIcon from "@mui/icons-material/MoveToInbox";
import SettingsIcon from "@mui/icons-material/Settings";
import LogoutIcon from "@mui/icons-material/Logout";

import defaultImg from "../assets/images/defualt_image.png";
import newMM from "../assets/images/newMM.png";
import newPrivate from "../assets/images/newPrivate.png";
import participate from "../assets/images/participate.png";

import MMListModal from "../component/im/mmListModal";
import PrivateModal from "../component/im/privateModal.js";
import JoinMeetingModal from "../component/im/joinMeetingList.js";

const buttons = [
  { name: "PrivateModal", src: newPrivate },
  { name: "MMListModal", src: newMM },
  { name: "JoinMeetingModal", src: participate },
];

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
  backgroundColor: "rgba(46, 46, 72, 0.7)",
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

  backgroundColor: "rgba(65, 65, 112, 0.8)",
});

const DrawerHeader = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  justifyContent: "flex-end",
  padding: theme.spacing(0, 1),
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

export default function Im() {
  const dispatch = useDispatch();

  const theme = useTheme();
  const [open, setOpen] = React.useState(false);

  const profileImg =
    useSelector((state) => state.user.profileImg) || defaultImg;
  const nickname = useSelector((state) => state.user.nickname) || "WEBBY";

  const navigate = useNavigate();
  const identification = useSelector((state) => state.user.identification);

  useEffect(() => {
    if (!identification) {
      navigate("/");
    }
  }, [identification, navigate]);

  const handleDrawerOpen = () => {
    setOpen(true);
  };

  const handleDrawerClose = () => {
    setOpen(false);
  };

  const location = useLocation();

  const isImPage = location.pathname === "/im";

  useEffect(() => {
    setModalStatus({
      startMM: false,
      newPrivate: false,
      JoinMeetingModal: false,
      MMListModal: false,
    });
  }, [location]);

  const [modalStatus, setModalStatus] = useState({
    startMM: false,
    newPrivate: false,
    JoinMeetingModal: false,
    MMListModal: false,
  });

  const handleModalOpen = (modalName) => {
    setModalStatus({
      ...modalStatus,
      [modalName]: true,
    });
  };

  const handleModalClose = (modalName) => {
    setModalStatus({
      ...modalStatus,
      [modalName]: false,
    });
  };

  const isModalOpen = Object.values(modalStatus).some(
    (status) => status === true
  );

  const handleDashboardClick = () => {
    navigate("/im");
  };

  const handleMyMeetingClick = () => {
    navigate("/im/mylist");
  };

  const handleSettingClick = () => {
    navigate("/im/setting");
  };

  const handleLogoutClick = () => {
    console.log("Logout was clicked");
    // Profile 클릭 시 수행할 동작
    dispatch(setIdentification(null));
    dispatch(setAccessToken(null));
    dispatch(setRefreshToken(null));
    dispatch(setCsrfToken(null));
    dispatch(setProfileImg(null));
    dispatch(setNickname(null));

    navigate("/");
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
        minHeight="300px"
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
            {theme.direction === "rtl" ? null : (
              <ChevronLeftIcon style={{ color: "white" }} />
            )}
          </IconButton>
        </DrawerHeader>
        <div className={styles["profileContainer"]}>
          <img
            src={profileImg}
            alt="Profile"
            className={open ? styles["profileImg"] : styles["profileImgClosed"]}
          />
          {open && <h7 className={styles["nickname"]}>{nickname}</h7>}
        </div>
        <Divider />

        <List className={styles["sidebar"]}>
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
        }}
        className={styles["container"]}
      >
        <Outlet />

        {isImPage && !isModalOpen && (
          <div className={styles["buttonContainer"]}>
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
          <MMListModal handleClose={() => handleModalClose("MMListModal")} />
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
            sidebarOpen={open}
          />
        )}
      </Box>
    </Box>
  );
}
