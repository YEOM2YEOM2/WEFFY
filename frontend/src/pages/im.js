import React, { useState, useEffect } from "react";
import { Route, Routes, Outlet, useLocation } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";

//리덕스
import {
  setIdentification,
  setAccessToken,
  setRefreshToken,
  setCsrfToken,
  setProfileImg,
  setNickname,
} from "../store/reducers/user.js";

//CSS
import styles from "../pages/im.module.css";

//mui 라이브라리
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
import ListItem from "@mui/material/ListItem";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";

// Icons
import GridViewIcon from "@mui/icons-material/GridView";
import MoveToInboxIcon from "@mui/icons-material/MoveToInbox";
import SettingsIcon from "@mui/icons-material/Settings";
import LogoutIcon from "@mui/icons-material/Logout";

// Images
import defaultImg from "../assets/images/defualt_image.png";
import newMM from "../assets/images/newMM.png";
import newPrivate from "../assets/images/newPrivate.png";
import participate from "../assets/images/participate.png";

//Modal js
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

//옆 사이드 바의 메뉴
const icons = [
  { name: "DashBoard", src: <GridViewIcon /> },
  { name: "MyList", src: <MoveToInboxIcon /> },
  { name: "Setting", src: <SettingsIcon /> },
  { name: "Logout", src: <LogoutIcon /> },
];

//사이드 바가 열렸을 경우
const openedMixin = (theme) => ({
  width: drawerWidth,
  transition: theme.transitions.create("width", {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.enteringScreen,
  }),
  overflowX: "hidden",
  backgroundColor: "rgba(46, 46, 72, 0.7)",
});

//사이드 바가 닫혔을 경우
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

//맨 위 header가 움직이도록 구성
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

export default function Im() {
  const dispatch = useDispatch();

  const theme = useTheme();
  const [open, setOpen] = React.useState(false);

  //redux에서 사진, 닉네임 가져오기
  const profileImg =
    useSelector((state) => state.user.profileImg) || defaultImg;
  const nickname = useSelector((state) => state.user.nickname) || "WEBBY";

  const navigate = useNavigate();
  const identification = useSelector((state) => state.user.identification);

  // accesToken null일 경우 다시 /로 빽~!
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

  // 모달 상태 선언
  const [modalStatus, setModalStatus] = useState({
    startMM: false,
    newPrivate: false,
    JoinMeetingModal: false,
    MMListModal: false,
  });

  const [selectedChannelId, setSelectedChannelId] = useState(null);
  const [selectedChannel, setSelectedChannel] = useState(null);
  const [selectedGroup, setSelectedGroup] = useState(null);

  // //mm연동 미팅 시작 핸들링
  // const handleStartMeeting = (groupName, channelName, channelId) => {
  //   setSelectedGroup(groupName);
  //   setSelectedChannel(channelName);
  //   setSelectedChannelId(channelId);

  //   console.log("startMeeting 클릭 완료!", groupName, channelName, channelId);

  //   setModalStatus({
  //     ...modalStatus,
  //     startMM: true, // MmModal을 보여주고
  //     MMListModal: false, // MMListModal은 숨깁니다.
  //   });
  // };

  // useEffect(() => {
  //   console.log(selectedChannelId);
  //   console.log(selectedChannel);
  //   console.log(selectedGroup);
  // }, [selectedChannelId, selectedChannel, selectedGroup]);

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
    // window.location.href = "https://localhost:8080";
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
          <MMListModal
            handleClose={() => handleModalClose("MMListModal")}
            // handleStartMeeting={() =>
            //   handleStartMeeting(
            //     selectedGroup,
            //     selectedChannel,
            //     selectedChannelId
            //   )
            // }
          />
        )}

        {/* {modalStatus.startMM && (
          <StartMM
            handleClose={() => handleModalClose("startMM")}
            groupName={selectedGroup}
            channelName={selectedChannel}
            channelId={selectedChannelId}
          />
        )} */}
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
