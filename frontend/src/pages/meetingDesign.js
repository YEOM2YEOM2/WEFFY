import { React, Component } from "react";
import { connect } from "react-redux";
import styles from "./meetingDesign.module.css";

// Openvidu
import axios from "axios";
import { OpenVidu } from "openvidu-browser";
import UserModel from "../models/userModel";
import OpenViduLayout from "../layout/customLayout.js";
import FileList from "../component/conference/util/fileList.js";

import Chat from "../component/conference/chat/ChatComponent.js";
import QuestionChat from "./../component/conference/chat/QuestionChat.js";
import DialogExtensionComponent from "../component/conference/dialog-extension/DialogExtension.js";
import StreamOthers from "../component/conference/stream/StreamOthers.js";
import GridStream from "../component/conference/stream/GridStream.js";
import LocalUser from "../component/conference/stream/LocalUser.js";
import BottomToolbar from "../component/conference/toolbar/BottomToolbar.js";
import Participant from "../component/conference/participant/participant.js";

// mui
import { styled } from "@mui/material/styles";
import Box from "@mui/material/Box";
import Drawer from "@mui/material/Drawer";
import MuiAppBar from "@mui/material/AppBar";
import Toolbar from "@mui/material/Toolbar";
import CssBaseline from "@mui/material/CssBaseline";
import List from "@mui/material/List";
import Typography from "@mui/material/Typography";
import Divider from "@mui/material/Divider";
import IconButton from "@mui/material/IconButton";
import ArrowBackIosIcon from "@mui/icons-material/ArrowBackIos";
import ArrowForwardIosIcon from "@mui/icons-material/ArrowForwardIos";
import SquareRoundedIcon from "@mui/icons-material/SquareRounded";
import GridViewRoundedIcon from "@mui/icons-material/GridViewRounded";
import ToggleButton from "@mui/material/ToggleButton";
import ToggleButtonGroup from "@mui/material/ToggleButtonGroup";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import Badge from "@mui/material/Badge";
import ChatIcon from "@mui/icons-material/Chat";
import ArrowLeftIcon from "@mui/icons-material/ArrowLeft";
import ArrowRightIcon from "@mui/icons-material/ArrowRight";
import Grid from "@mui/material/Grid";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import DragHandleIcon from "@mui/icons-material/DragHandle";
import QuizIcon from "@mui/icons-material/Quiz";
import MoreVertIcon from "@mui/icons-material/MoreVert";

// bootstrap
import Dropdown from "react-bootstrap/Dropdown";

// Swal
import Swal from "sweetalert2";
import { ConnectingAirportsOutlined } from "@mui/icons-material";

const drawerWidth = 320;

const Main = styled("main", { shouldForwardProp: (prop) => prop !== "open" })(
  ({ theme, open }) => ({
    flexGrow: 1,
    padding: theme.spacing(0),
    transition: theme.transitions.create("margin", {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    marginRight: -drawerWidth,
    ...(open && {
      transition: theme.transitions.create("margin", {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
      }),
      marginRight: 0,
    }),
  })
);

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== "open",
})(({ theme, open }) => ({
  transition: theme.transitions.create(["margin", "width"], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(["margin", "width"], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
    marginRight: drawerWidth,
  }),
}));

const DrawerHeader = styled("div")(({ theme }) => ({
  display: "flex",
  alignItems: "center",
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: "flex-start",
}));

var localUser = new UserModel();
const APPLICATION_SERVER_URL =
  process.env.NODE_ENV === "production" ? "" : "http://localhost:8082/";

const mapStateToProps = (state) => {
  return {
    selectedMic: state.setting.selectedMic,
    selectedCam: state.setting.selectedCam,
    accessToken: state.user.accessToken,
    identification: state.user.identification,
    activeSessionId: state.conference.activeSessionId,
    activeSessionName: state.conference.activeSessionName,
  };
};

class Conference extends Component {
  constructor(props) {
    super(props);
    this.hasBeenUpdated = false;
    this.layout = new OpenViduLayout();
    let userName = this.props.user
      ? this.props.user
      : "WEFFY_User" + Math.floor(Math.random() * 100);
    this.remotes = [];
    this.localUserAccessAllowed = false;
    this.state = {
      mySessionId: this.props.activeSessionId,
      myUserName: userName,
      session: undefined,
      localUser: undefined,
      subscribers: [],
      subscribers8: [],
      subIdx4: 0,
      subIdx8: 0,
      chatDisplay: "none",
      QuestionDisplay: "none",
      currentVideoDevice: undefined,

      // mui 사용을 위한 변수
      open: true,
      partChatToggle: "participant",

      // default & Grid Mode toggle 변수
      defaultMode: true,

      // default Mode localuser
      singleMode: false,

      //file 모달창
      isFileListVisible: false,
    };

    this.joinSession = this.joinSession.bind(this);
    this.leaveSession = this.leaveSession.bind(this);
    this.onbeforeunload = this.onbeforeunload.bind(this);
    this.camStatusChanged = this.camStatusChanged.bind(this);
    this.micStatusChanged = this.micStatusChanged.bind(this);
    this.nicknameChanged = this.nicknameChanged.bind(this);
    this.toggleFullscreen = this.toggleFullscreen.bind(this);
    this.switchCamera = this.switchCamera.bind(this);
    this.screenShare = this.screenShare.bind(this);
    this.stopScreenShare = this.stopScreenShare.bind(this);
    this.closeDialogExtension = this.closeDialogExtension.bind(this);
    this.toggleChat = this.toggleChat.bind(this);
    this.toggleQuestion = this.toggleQuestion.bind(this);
    this.checkNotification = this.checkNotification.bind(this);
    this.checkQuestionNotification = this.checkQuestionNotification.bind(this);
    this.checkSize = this.checkSize.bind(this);

    // mui 사용을 위한 함수
    this.handleDrawerOpen = this.handleDrawerOpen.bind(this);
    this.handleDrawerClose = this.handleDrawerClose.bind(this);
    this.handlePartChatToggle = this.handlePartChatToggle.bind(this);

    // default & Grid Mode toggle 함수
    this.handleDefaultMode = this.handleDefaultMode.bind(this);
    this.handleGridMode = this.handleGridMode.bind(this);
    this.increaseSubIdx4 = this.increaseSubIdx4.bind(this);
    this.decreaseSubIdx4 = this.decreaseSubIdx4.bind(this);
    this.increaseSubIdx8 = this.increaseSubIdx8.bind(this);
    this.decreaseSubIdx8 = this.decreaseSubIdx8.bind(this);

    // single Mode
    this.toggleSingleMode = this.toggleSingleMode.bind(this);
  }

  showFileList = () => {
    this.setState({ isFileListVisible: true });
  };

  hideFileList = () => {
    this.setState({ isFileListVisible: false });
  };

  componentDidMount() {
    window.addEventListener("beforeunload", this.onbeforeunload);
    window.addEventListener("resize", this.checkSize);
    this.joinSession();
  }

  componentWillUnmount() {
    window.removeEventListener("beforeunload", this.onbeforeunload);
    window.removeEventListener("resize", this.checkSize);
    this.leaveSession();
  }

  onbeforeunload(event) {
    this.leaveSession();
  }

  joinSession() {
    this.OV = new OpenVidu();

    this.setState(
      {
        session: this.OV.initSession(),
      },
      async () => {
        this.subscribeToStreamCreated();
        await this.connectToSession();
      }
    );
  }

  async connectToSession() {
    if (this.props.token !== undefined) {
      console.log("token received: ", this.props.token);
      this.connect(this.props.token);
    } else {
      try {
        var token = await this.getToken();
        console.log(token);
        this.connect(token);
      } catch (error) {
        console.error(
          "There was an error getting the token:",
          error.code,
          error.message
        );
        if (this.props.error) {
          this.props.error({
            error: error.error,
            messgae: error.message,
            code: error.code,
            status: error.status,
          });
        }
        Swal.fire({
          icon: "error",
          html: `<div style="font-family:GmarketSans">유효하지 않은 사용자입니다.<br>로그인 후 이용해주세요.</div>`,
          confirmButtonText: "OK",
          confirmButtonColor: "#2672B9",
        });
      }
    }
  }

  connect(token) {
    this.state.session
      .connect(token, { clientData: this.state.myUserName })
      .then(() => {
        this.connectWebCam();
      })
      .catch((error) => {
        if (this.props.error) {
          this.props.error({
            error: error.error,
            messgae: error.message,
            code: error.code,
            status: error.status,
          });
        }
        Swal.fire({
          icon: "error",
          html: `<div style="font-family:GmarketSans">회의 연결이 원활하지 않습니다.<br>잠시 후 다시 시도해주세요.</div>`,
          confirmButtonText: "OK",
          confirmButtonColor: "#2672B9",
        });
      });
  }

  async connectWebCam() {
    await this.OV.getUserMedia({
      audioSource: undefined,
      videoSource: undefined,
    });
    var devices = await this.OV.getDevices();
    var videoDevices = devices.filter((device) => device.kind === "videoinput");

    let publisher = this.OV.initPublisher(undefined, {
      // audioSource, videoSource 넘어온 설정 값으로 변경할 때 사용해야함.
      audioSource: undefined,
      videoSource: videoDevices[0].deviceId,
      publishAudio: localUser.isAudioActive(),
      publishVideo: localUser.isVideoActive(),
      resolution: "640x480",
      frameRate: 30,
      insertMode: "APPEND",
    });

    if (this.state.session.capabilities.publish) {
      publisher.on("accessAllowed", () => {
        this.state.session.publish(publisher).then(() => {
          this.updateSubscribers();
          this.localUserAccessAllowed = true;
          if (this.props.joinSession) {
            this.props.joinSession();
          }
        });
      });
    }
    localUser.setNickname(this.state.myUserName);
    localUser.setConnectionId(this.state.session.connection.connectionId);
    localUser.setScreenShareActive(false);
    localUser.setStreamManager(publisher);
    this.subscribeToUserChanged();
    this.subscribeToStreamDestroyed();
    this.sendSignalUserChanged({
      isScreenShareActive: localUser.isScreenShareActive(),
    });

    this.setState(
      { currentVideoDevice: videoDevices[0], localUser: localUser },
      () => {
        this.state.localUser.getStreamManager().on("streamPlaying", (e) => {
          publisher.videos[0].video.parentElement.classList.remove(
            "custom-class"
          );
        });
      }
    );
  }

  updateSubscribers() {
    var subscribers = this.remotes;
    this.setState(
      {
        subscribers: subscribers,
        subscribers8: [this.state.localUser, ...subscribers],
      },
      () => {
        if (this.state.localUser) {
          this.sendSignalUserChanged({
            isAudioActive: this.state.localUser.isAudioActive(),
            isVideoActive: this.state.localUser.isVideoActive(),
            nickname: this.state.localUser.getNickname(),
            isScreenShareActive: this.state.localUser.isScreenShareActive(),
          });
        }
      }
    );
  }

  leaveSession() {
    const mySession = this.state.session;

    if (mySession) {
      mySession.disconnect();
    }

    // Empty all properties...
    this.OV = null;
    this.setState({
      session: undefined,
      subscribers: [],
      subscribers8: [],
      mySessionId: "SessionA",
      myUserName: "WEEFY_User" + Math.floor(Math.random() * 100),
      localUser: undefined,
    });
    if (this.props.leaveSession) {
      this.props.leaveSession();
    }
  }
  camStatusChanged() {
    localUser.setVideoActive(!localUser.isVideoActive());
    localUser.getStreamManager().publishVideo(localUser.isVideoActive());
    this.sendSignalUserChanged({ isVideoActive: localUser.isVideoActive() });
    this.setState({ localUser: localUser });
  }

  micStatusChanged() {
    localUser.setAudioActive(!localUser.isAudioActive());
    localUser.getStreamManager().publishAudio(localUser.isAudioActive());
    this.sendSignalUserChanged({ isAudioActive: localUser.isAudioActive() });
    this.setState({ localUser: localUser });
  }

  nicknameChanged(nickname) {
    let localUser = this.state.localUser;
    localUser.setNickname(nickname);
    this.setState({ localUser: localUser });
    this.sendSignalUserChanged({
      nickname: this.state.localUser.getNickname(),
    });
  }

  deleteSubscriber(stream) {
    const remoteUsers = this.state.subscribers;
    const userStream = remoteUsers.filter(
      (user) => user.getStreamManager().stream === stream
    )[0];
    let index = remoteUsers.indexOf(userStream, 0);
    if (index > -1) {
      remoteUsers.splice(index, 1);
      this.setState({
        subscribers: remoteUsers,
        subscribers8: [this.state.localUser, ...remoteUsers],
      });
    }
  }

  subscribeToStreamCreated() {
    this.state.session.on("streamCreated", (event) => {
      const subscriber = this.state.session.subscribe(event.stream, undefined);
      // var subscribers = this.state.subscribers;
      subscriber.on("streamPlaying", (e) => {
        this.checkSomeoneShareScreen();
        subscriber.videos[0].video.parentElement.classList.remove(
          "custom-class"
        );
      });
      const newUser = new UserModel();
      newUser.setStreamManager(subscriber);
      newUser.setConnectionId(event.stream.connection.connectionId);
      newUser.setType("remote");
      const nickname = event.stream.connection.data.split("%")[0];
      newUser.setNickname(JSON.parse(nickname).clientData);
      this.remotes.push(newUser);
      if (this.localUserAccessAllowed) {
        this.updateSubscribers();
      }
    });
  }

  subscribeToStreamDestroyed() {
    // On every Stream destroyed...
    this.state.session.on("streamDestroyed", (event) => {
      // Remove the stream from 'subscribers' array
      this.deleteSubscriber(event.stream);
      setTimeout(() => {
        this.checkSomeoneShareScreen();
      }, 20);
      event.preventDefault();
      //   this.updateLayout();
    });
  }

  subscribeToUserChanged() {
    this.state.session.on("signal:userChanged", (event) => {
      let remoteUsers = this.state.subscribers;
      remoteUsers.forEach((user) => {
        if (user.getConnectionId() === event.from.connectionId) {
          const data = JSON.parse(event.data);
          console.log("EVENTO REMOTE: ", event.data);
          if (data.isAudioActive !== undefined) {
            user.setAudioActive(data.isAudioActive);
          }
          if (data.isVideoActive !== undefined) {
            user.setVideoActive(data.isVideoActive);
          }
          if (data.nickname !== undefined) {
            user.setNickname(data.nickname);
          }
          if (data.isScreenShareActive !== undefined) {
            user.setScreenShareActive(data.isScreenShareActive);
          }
        }
      });
      this.setState(
        {
          subscribers: remoteUsers,
          subscribers8: [this.state.localUser, ...remoteUsers],
        },
        () => this.checkSomeoneShareScreen()
      );
    });
  }

  updateLayout() {
    setTimeout(() => {
      this.layout.updateLayout();
    }, 20);
  }

  sendSignalUserChanged(data) {
    const signalOptions = {
      data: JSON.stringify(data),
      type: "userChanged",
    };
    this.state.session.signal(signalOptions);
  }

  toggleFullscreen() {
    const document = window.document;
    const fs = document.getElementById("container");
    if (
      !document.fullscreenElement &&
      !document.mozFullScreenElement &&
      !document.webkitFullscreenElement &&
      !document.msFullscreenElement
    ) {
      if (fs.requestFullscreen) {
        fs.requestFullscreen();
      } else if (fs.msRequestFullscreen) {
        fs.msRequestFullscreen();
      } else if (fs.mozRequestFullScreen) {
        fs.mozRequestFullScreen();
      } else if (fs.webkitRequestFullscreen) {
        fs.webkitRequestFullscreen();
      }
    } else {
      if (document.exitFullscreen) {
        document.exitFullscreen();
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen();
      } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen();
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen();
      }
    }
  }

  async switchCamera() {
    try {
      const devices = await this.OV.getDevices();
      var videoDevices = devices.filter(
        (device) => device.kind === "videoinput"
      );

      if (videoDevices && videoDevices.length > 1) {
        var newVideoDevice = videoDevices.filter(
          (device) => device.deviceId !== this.state.currentVideoDevice.deviceId
        );

        if (newVideoDevice.length > 0) {
          // Creating a new publisher with specific videoSource
          // In mobile devices the default and first camera is the front one
          var newPublisher = this.OV.initPublisher(undefined, {
            audioSource: undefined,
            videoSource: newVideoDevice[0].deviceId,
            publishAudio: localUser.isAudioActive(),
            publishVideo: localUser.isVideoActive(),
            mirror: true,
          });

          //newPublisher.once("accessAllowed", () => {
          await this.state.session.unpublish(
            this.state.localUser.getStreamManager()
          );
          await this.state.session.publish(newPublisher);
          this.state.localUser.setStreamManager(newPublisher);
          this.setState({
            currentVideoDevice: newVideoDevice,
            localUser: localUser,
          });
        }
      }
    } catch (e) {
      console.error(e);
    }
  }

  screenShare() {
    const videoSource =
      navigator.userAgent.indexOf("Firefox") !== -1 ? "window" : "screen";
    const publisher = this.OV.initPublisher(
      undefined,
      {
        videoSource: videoSource,
        publishAudio: localUser.isAudioActive(),
        publishVideo: localUser.isVideoActive(),
        mirror: false,
      },
      (error) => {
        if (error && error.name === "SCREEN_EXTENSION_NOT_INSTALLED") {
          this.setState({ showExtensionDialog: true });
        } else if (error && error.name === "SCREEN_SHARING_NOT_SUPPORTED") {
          Swal.fire({
            icon: "error",
            html: `<div style="font-family:GmarketSans">사용하시는 브라우저에서는 화면 공유 기능을 제공하지 않습니다.<br>Chrome을 이용해주세요.</div>`,
            confirmButtonText: "OK",
            confirmButtonColor: "#2672B9",
          });
        } else if (error && error.name === "SCREEN_EXTENSION_DISABLED") {
          Swal.fire({
            icon: "info",
            html: `<div style="font-family:GmarketSans">화면 공유 기능 확장 프로그램 설치가 필요합니다.</div>`,
            confirmButtonText: "OK",
            confirmButtonColor: "#2672B9",
          });
        } else if (error && error.name === "SCREEN_CAPTURE_DENIED") {
          Swal.fire({
            icon: "question",
            html: `<div style="font-family:GmarketSans">공유할 페이지나 어플리케이션을 선택해 주세요.</div>`,
            confirmButtonText: "OK",
            confirmButtonColor: "#2672B9",
          });
        }
      }
    );

    publisher.once("accessAllowed", () => {
      this.state.session.unpublish(localUser.getStreamManager());
      localUser.setStreamManager(publisher);
      this.state.session.publish(localUser.getStreamManager()).then(() => {
        localUser.setScreenShareActive(true);
        this.setState({ localUser: localUser }, () => {
          this.sendSignalUserChanged({
            isScreenShareActive: localUser.isScreenShareActive(),
          });
          axios({
            method: "post",
            url: `http://localhost:8082/stream/${publisher.session.sessionId}/${publisher.stream.streamId}`,
            headers: {
              accept: "application/json",
              "Content-Type": "application/json",
            }
          })
          .then((res) => {
            console.log("화면공유 aixos결과!!!!",res)
          })
          .catch((err) => {
            console.log("화면공유 axios 에러!!!",err)
          })
        });
      });
    });
    console.log("화면공유",publisher)
    publisher.on("streamPlaying", () => {
      publisher.videos[0].video.parentElement.classList.remove("custom-class");
    });
  }

  closeDialogExtension() {
    this.setState({ showExtensionDialog: false });
  }

  stopScreenShare() {
    this.state.session.unpublish(localUser.getStreamManager());
    this.connectWebCam();
  }

  checkSomeoneShareScreen() {
    let isScreenShared;
    // return true if at least one passes the test
    isScreenShared =
      this.state.subscribers.some((user) => user.isScreenShareActive()) ||
      localUser.isScreenShareActive();
    const openviduLayoutOptions = {
      maxRatio: 3 / 2,
      minRatio: 9 / 16,
      fixedRatio: isScreenShared,
      bigClass: "OV_big",
      bigPercentage: 0.8,
      bigFixedRatio: false,
      bigMaxRatio: 3 / 2,
      bigMinRatio: 9 / 16,
      bigFirst: true,
      animate: true,
    };
    this.layout.setLayoutOptions(openviduLayoutOptions);
  }

  toggleChat(property) {
    let display = property;

    if (display === undefined) {
      display = this.state.chatDisplay === "none" ? "block" : "none";
    }
    if (display === "block") {
      this.setState({ chatDisplay: display, messageReceived: false });
    } else {
      this.setState({ chatDisplay: display });
    }
  }

  toggleQuestion(property) {
    let display = property;

    if (display === undefined) {
      display = this.state.QuestionDisplay === "none" ? "block" : "none";
    }
    if (display === "block") {
      this.setState({ QuestionDisplay: display, QuestionReceived: false });
    } else {
      this.setState({ QuestionDisplay: display });
    }
  }

  checkNotification(event) {
    this.setState({
      messageReceived: this.state.chatDisplay === "none",
    });
  }

  checkQuestionNotification(event) {
    this.setState({
      QuestionReceived: this.state.QuestionDisplay === "none",
    });
  }

  checkSize() {
    if (
      document.getElementById("layout").offsetWidth <= 700 &&
      !this.hasBeenUpdated
    ) {
      this.toggleChat("none");
      this.toggleQuestion("none");
      this.hasBeenUpdated = true;
    }
    if (
      document.getElementById("layout").offsetWidth > 700 &&
      this.hasBeenUpdated
    ) {
      this.hasBeenUpdated = false;
    }
  }

  // mui 사용을 위한 함수
  handleDrawerOpen() {
    this.setState({ open: true });
  }

  handleDrawerClose() {
    this.setState({ open: false });
  }

  handlePartChatToggle(event, val) {
    this.setState({ partChatToggle: val });
  }

  handleDefaultMode() {
    this.setState({ defaultMode: true });
  }

  handleGridMode() {
    this.setState({ defaultMode: false });
  }

  toggleSingleMode() {
    const { singleMode } = this.state;
    this.setState({ singleMode: !singleMode });
  }

  increaseSubIdx4() {
    const { subIdx4, subscribers } = this.state;
    const maxIndex = subscribers.length - 4;

    if (subIdx4 < maxIndex) {
      this.setState({ subIdx4: subIdx4 + 1 });
    }
  }

  decreaseSubIdx4() {
    const { subIdx4 } = this.state;

    if (subIdx4 > 0) {
      this.setState({ subIdx4: subIdx4 - 1 });
    }
  }

  increaseSubIdx8() {
    const { subIdx8, subscribers8 } = this.state;
    const maxIdx = subscribers8.length - 8;

    if (subIdx8 < maxIdx) {
      this.setState({ subIdx8: subIdx8 + 8 });
    }
  }

  decreaseSubIdx8() {
    const { subIdx8 } = this.state;

    if (subIdx8 >= 8) {
      this.setState({ subIdx8: subIdx8 - 8 });
    }
  }

  render() {
    const mySessionId = this.state.mySessionId;
    const localUser = this.state.localUser;
    let chatDisplay = { display: this.state.chatDisplay };
    let QuestionDisplay = { display: this.state.QuestionDisplay };

    // Mode toggle 버튼
    let defaultMode = this.state.defaultMode;

    return (
      <div className="container" id="container">
        {/* 브라우저 화면 공유를 위한 Chrome 확장 프로그램 설치 유도 및 설치 후 브라우저 새로고침 기능 제공 */}
        <DialogExtensionComponent
          showDialog={this.state.showExtensionDialog}
          cancelClicked={this.closeDialogExtension}
        />

        <div id="layout" className="bounds">
          <Box sx={{ display: "flex" }}>
            <CssBaseline />
            <AppBar position="fixed" open={this.state.open}>
              <Toolbar style={{ backgroundColor: "#374151" }}>
                <p className={styles.logo}>WEEFY</p>
                {mySessionId && (
                  <div className={styles.sessionId}>
                    <span style={{ fontFamily: "Agro", fontWeight: "100" }}>
                      {this.props.activeSessionName}
                    </span>
                  </div>
                )}
                <Typography
                  variant="h6"
                  noWrap
                  sx={{ flexGrow: 1 }}
                  component="div"
                  className={styles.screenMode}
                >
                  <IconButton
                    style={{ height: "64px" }}
                    onClick={this.handleDefaultMode}
                  >
                    <SquareRoundedIcon
                      fontSize="medium"
                      style={{ color: "white" }}
                    />
                    {defaultMode ? <div className={styles.line}></div> : null}
                  </IconButton>
                  <IconButton
                    style={{ height: "64px" }}
                    onClick={this.handleGridMode}
                  >
                    <GridViewRoundedIcon style={{ color: "white" }} />
                    {!defaultMode ? <div className={styles.line}></div> : null}
                  </IconButton>
                </Typography>
              </Toolbar>
            </AppBar>
            <Main
              open={this.state.open}
              style={{
                backgroundColor: "#10161F",
                color: "white",
                paddingBottom: "45px",
              }}
            >
              {defaultMode ? (
                // default Mode
                <div>
                  {/* 다른 사용자 */}
                  {this.state.singleMode ? null : (
                    <div
                      style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        width: "100%",
                      }}
                    >
                      {this.state.subscribers.length > 0 ? (
                        <ArrowLeftIcon
                          onClick={this.decreaseSubIdx4}
                          style={{ cursor: "pointer" }}
                        />
                      ) : null}
                      {this.state.subscribers
                        .slice(this.state.subIdx4, this.state.subIdx4 + 4)
                        .map((sub, i) => (
                          <div
                            key={i}
                            id="remoteUsers"
                            style={{ paddingRight: "3px", paddingLeft: "3px" }}
                            className="OT_root"
                          >
                            <StreamOthers
                              user={sub}
                              streamId={sub.streamManager.stream.streamId}
                              style={{ height: "180px" }}
                            />
                          </div>
                        ))}
                      {this.state.subscribers.length > 0 ? (
                        <ArrowRightIcon
                          onClick={this.increaseSubIdx4}
                          style={{ cursor: "pointer", zIndex: "99999" }}
                        />
                      ) : null}
                    </div>
                  )}
                  <div>
                    <DragHandleIcon
                      onClick={this.toggleSingleMode}
                      style={{
                        position: "relative",
                        bottom: "7px",
                        cursor: "pointer",
                      }}
                    />
                  </div>
                  {/* 나 */}
                  {localUser !== undefined &&
                    localUser.getStreamManager() !== undefined && (
                      <div
                        className={styles.videoMe}
                        id="localUser"
                        style={{ display: "flex", justifyContent: "center" }}
                      >
                        <LocalUser
                          user={localUser}
                          handleNickname={this.nicknameChanged}
                          style={{ width: "95%" }}
                        />
                      </div>
                    )}
                </div>
              ) : (
                // Grid Mode
                <div style={{ margin: "auto 0px auto 5px" }}>
                  <div
                    style={{
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                      height: "600px",
                    }}
                  >
                    <Grid container>
                      {this.state.subscribers8
                        .slice(this.state.subIdx8, this.state.subIdx8 + 8)
                        .map((sub, i) => (
                          <Grid key={i} item xs={3}>
                            <div
                              id="remoteUsers"
                              style={{
                                padding: "2px",
                                boxSizing: "border-box",
                              }}
                              className="OT_root"
                            >
                              <GridStream
                                user={sub}
                                streamId={sub.streamManager.stream.streamId}
                              />
                            </div>
                          </Grid>
                        ))}
                    </Grid>
                  </div>
                  <div>
                    <ArrowLeftIcon
                      onClick={this.decreaseSubIdx8}
                      style={{ cursor: "pointer" }}
                    />
                    <MoreHorizIcon fontSize="large" />
                    <ArrowRightIcon
                      onClick={this.increaseSubIdx8}
                      style={{ cursor: "pointer" }}
                    />
                  </div>
                </div>
              )}
              <div className={styles.openDrawer}>
                <IconButton
                  color="inherit"
                  aria-label="open drawer"
                  edge="end"
                  onClick={this.handleDrawerOpen}
                  sx={{ ...(this.state.open && { display: "none" }) }}
                >
                  <ArrowBackIosIcon
                    fontSize="small"
                    style={{ color: "whitesmoke" }}
                  />
                </IconButton>
              </div>
            </Main>
            <Drawer
              sx={{
                width: drawerWidth,
                flexShrink: 0,
                "& .MuiDrawer-paper": {
                  width: drawerWidth,
                  background: "rgb(16, 22, 31)",
                },
              }}
              variant="persistent"
              anchor="right"
              open={this.state.open}
            >
              <div className={styles.closeDrawer}>
                <IconButton onClick={this.handleDrawerClose}>
                  <ArrowForwardIosIcon
                    fontSize="small"
                    style={{
                      color: "whitesmoke",
                      position: "relative",
                      right: "4px",
                    }}
                  />
                </IconButton>
              </div>
              <DrawerHeader style={{ backgroundColor: "#374151" }}>
                <div>
                  <ToggleButtonGroup
                    value={this.state.partChatToggle}
                    exclusive
                    onChange={this.handlePartChatToggle}
                    aria-label="text alignment"
                    style={{
                      background: "white",
                      borderRadius: "15px",
                      width: "288px",
                      display: "flex",
                      justifyContent: "center",
                      zIndex: "99999",
                    }}
                  >
                    <ToggleButton
                      value="participant"
                      aria-label="left aligned"
                      style={{ width: "99px" }}
                      onClick={() => {
                        this.toggleQuestion("none");
                        this.toggleChat("none");
                      }}
                    >
                      <AccountCircleIcon />
                    </ToggleButton>
                    <ToggleButton
                      value="generalChat"
                      aria-label="centered"
                      style={{ width: "96px" }}
                      onClick={() => {
                        this.toggleQuestion("none");
                        this.toggleChat("block");
                      }}
                    >
                      <Badge badgeContent={0} color="primary">
                        <ChatIcon />
                      </Badge>
                    </ToggleButton>
                    <ToggleButton
                      value="questionChat"
                      aria-label="centered"
                      style={{ width: "99px" }}
                      onClick={() => {
                        this.toggleQuestion("block");
                        this.toggleChat("none");
                      }}
                    >
                      <Badge badgeContent={0} color="primary">
                        <QuizIcon />
                      </Badge>
                    </ToggleButton>
                  </ToggleButtonGroup>
                </div>
                <div>
                  <Dropdown>
                    <Dropdown.Toggle
                      id="dropdown-basic"
                      style={{
                        backgroundColor: "transparent",
                        border: "0",
                        position: "relative",
                        right: "10px",
                      }}
                    >
                      <MoreVertIcon
                        style={{ color: "white", cursor: "pointer" }}
                      />
                    </Dropdown.Toggle>

                    <Dropdown.Menu style={{ marginTop: "6px" }}>
                      <Dropdown.Item onClick={this.showFileList}>
                        파일 목록
                      </Dropdown.Item>
                      <Dropdown.Item href="#/action-2">스트리밍</Dropdown.Item>
                    </Dropdown.Menu>
                  </Dropdown>
                  {this.state.isFileListVisible && (
                    <FileList onClose={this.hideFileList} />
                  )}
                </div>
              </DrawerHeader>
              <Divider />
              <List
                style={{
                  backgroundColor: "#17202E",
                  height: "calc(100% - 93.5px)",
                }}
              >
                {this.state.partChatToggle === "participant" ? (
                  <Participant
                    user={localUser}
                    participants={this.state.subscribers}
                    handleNickname={this.nicknameChanged}
                  />
                ) : null}
                {localUser !== undefined &&
                localUser.getStreamManager() !== undefined ? (
                  <div
                    className="OT_root OT_publisher custom-class"
                    style={chatDisplay}
                  >
                    <Chat
                      user={localUser}
                      chatDisplay={this.state.chatDisplay}
                      close={this.toggleChat}
                      messageReceived={this.checkNotification}
                    />
                  </div>
                ) : null}
                {localUser !== undefined &&
                localUser.getStreamManager() !== undefined ? (
                  <div
                    className="OT_root OT_publisher custom-class"
                    style={QuestionDisplay}
                  >
                    <QuestionChat
                      user={localUser}
                      QuestionDisplay={this.state.QuestionDisplay}
                      close={this.toggleQuestion}
                      questionReceived={this.checkQuestionNotification}
                    />
                  </div>
                ) : null}
              </List>
              <Divider />
            </Drawer>
          </Box>
        </div>
        <BottomToolbar
          sessionId={mySessionId}
          user={localUser}
          showNotification={this.state.messageReceived}
          camStatusChanged={this.camStatusChanged}
          micStatusChanged={this.micStatusChanged}
          screenShare={this.screenShare}
          stopScreenShare={this.stopScreenShare}
          toggleFullscreen={this.toggleFullscreen}
          switchCamera={this.switchCamera}
          leaveSession={this.leaveSession}
          toggleChat={this.toggleChat}
        />
      </div>
    );
  }

  async getToken() {
    //classId
    const { activeSessionId } = this.props;
    const { accessToken } = this.props;
    const { identification } = this.props;
    const { activeSessionName } = this.props;
    console.log("activeSessionId", activeSessionId);
    console.log("accessToken", accessToken);
    console.log("identification", identification);
    console.log("activeSessionName", activeSessionName);
    await this.createSession(
      identification,
      activeSessionId,
      activeSessionName
    );
    return await this.createToken(identification, activeSessionId, accessToken);
  }

  async createSession(identification, activeSessionId, activeSessionName) {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "conferences",
      {
        identification: identification,
        classId: activeSessionId,
        title: activeSessionName,
        active: true,
      },
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    return response.data; // The sessionId
  }

  async createToken(identification, activeSessionId, accessToken) {
    const response = await axios.post(
      APPLICATION_SERVER_URL +
        "conferences/connection/" +
        activeSessionId +
        "/" +
        identification,
      {},
      {
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    return response.data; // The token
  }
}
export default connect(mapStateToProps)(Conference);
