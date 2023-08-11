import { React, Component } from 'react';
import styles from './meetingDesign.module.css';

// Openvidu
import axios from 'axios';
import { OpenVidu } from 'openvidu-browser';
import UserModel from '../models/userModel';
import OpenViduLayout from '../layout/openvidu-layout.js';

import ChatComponent from '../component/conference/chat/ChatComponent.js';
import DialogExtensionComponent from '../component/conference/dialog-extension/DialogExtension.js';
import StreamComponent from '../component/conference/stream/StreamComponent.js';
import BottomToolbar from '../component/conference/toolbar/BottomToolbar.js';

// mui
import { styled, useTheme } from '@mui/material/styles';
import Box from '@mui/material/Box';
import Drawer from '@mui/material/Drawer';
import MuiAppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import CssBaseline from '@mui/material/CssBaseline';
import List from '@mui/material/List';
import Typography from '@mui/material/Typography';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import ArrowBackIosIcon from '@mui/icons-material/ArrowBackIos';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import SquareRoundedIcon from '@mui/icons-material/SquareRounded';
import GridViewRoundedIcon from '@mui/icons-material/GridViewRounded';
import RadioButtonCheckedOutlinedIcon from '@mui/icons-material/RadioButtonCheckedOutlined';
import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import InboxIcon from '@mui/icons-material/MoveToInbox';
import MailIcon from '@mui/icons-material/Mail';
import ToggleButton from '@mui/material/ToggleButton';
import ToggleButtonGroup from '@mui/material/ToggleButtonGroup';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Badge from '@mui/material/Badge';
import ChatIcon from '@mui/icons-material/Chat';


const drawerWidth = 320;

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })(
  ({ theme, open }) => ({
    flexGrow: 1,
    padding: theme.spacing(0),
    transition: theme.transitions.create('margin', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    marginRight: -drawerWidth,
    ...(open && {
      transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
      }),
      marginRight: 0,
    }),
  }),
);

const AppBar = styled(MuiAppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
  transition: theme.transitions.create(['margin', 'width'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    width: `calc(100% - ${drawerWidth}px)`,
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
    marginRight: drawerWidth,
  }),
}));

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-start',
}));

var localUser = new UserModel();
const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'http://localhost:8080/';

class Conference extends Component {
  constructor(props) {
      super(props);
      this.hasBeenUpdated = false;
      this.layout = new OpenViduLayout();
      let sessionName = this.props.sessionName ? this.props.sessionName : 'SessionA';
      let userName = this.props.user ? this.props.user : 'WEFFY_User' + Math.floor(Math.random() * 100);
      this.remotes = [];
      this.localUserAccessAllowed = false;
      this.state = {
          mySessionId: sessionName,
          myUserName: userName,
          session: undefined,
          localUser: undefined,
          subscribers: [],
          chatDisplay: 'none',
          currentVideoDevice: undefined,

          // mui 사용을 위한 변수
          open : true,
          partChatToggle: 'part',

          // default & Grid Mode toggle 변수
          defaultMode: true,
      };

      this.joinSession = this.joinSession.bind(this);
      this.leaveSession = this.leaveSession.bind(this);
      this.onbeforeunload = this.onbeforeunload.bind(this);
    //   this.updateLayout = this.updateLayout.bind(this);
      this.camStatusChanged = this.camStatusChanged.bind(this);
      this.micStatusChanged = this.micStatusChanged.bind(this);
      this.nicknameChanged = this.nicknameChanged.bind(this);
      this.toggleFullscreen = this.toggleFullscreen.bind(this);
      this.switchCamera = this.switchCamera.bind(this);
      this.screenShare = this.screenShare.bind(this);
      this.stopScreenShare = this.stopScreenShare.bind(this);
      this.closeDialogExtension = this.closeDialogExtension.bind(this);
      this.toggleChat = this.toggleChat.bind(this);
      this.checkNotification = this.checkNotification.bind(this);
      this.checkSize = this.checkSize.bind(this);

      // mui 사용을 위한 함수
      this.handleDrawerOpen = this.handleDrawerOpen.bind(this);
      this.handleDrawerClose = this.handleDrawerClose.bind(this);
      this.handlePartChatToggle = this.handlePartChatToggle.bind(this);

      // default & Grid Mode toggle 함수
      this.handleDefaultMode = this.handleDefaultMode.bind(this);
      this.handleGridMode = this.handleGridMode.bind(this);

  }

  componentDidMount() {
      const openViduLayoutOptions = {
          maxRatio: 3 / 2, // The narrowest ratio that will be used (default 2x3)
          minRatio: 9 / 16, // The widest ratio that will be used (default 16x9)
          fixedRatio: false, // If this is true then the aspect ratio of the video is maintained and minRatio and maxRatio are ignored (default false)
          bigClass: 'OV_big', // The class to add to elements that should be sized bigger
          bigPercentage: 0.8, // The maximum percentage of space the big ones should take up
          bigFixedRatio: false, // fixedRatio for the big ones
          bigMaxRatio: 3 / 2, // The narrowest ratio to use for the big elements (default 2x3)
          bigMinRatio: 9 / 16, // The widest ratio to use for the big elements (default 16x9)
          bigFirst: true, // Whether to place the big one in the top left (true) or bottom right
          animate: true, // Whether you want to animate the transitions
      };

    //   this.layout.initLayoutContainer(document.getElementById('layout'), openViduLayoutOptions);
      window.addEventListener('beforeunload', this.onbeforeunload);
    //   window.addEventListener('resize', this.updateLayout);
      window.addEventListener('resize', this.checkSize);
      this.joinSession();
  }

  componentWillUnmount() {
      window.removeEventListener('beforeunload', this.onbeforeunload);
    //   window.removeEventListener('resize', this.updateLayout);
      window.removeEventListener('resize', this.checkSize);
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
          },
      );
  }

  async connectToSession() {
      if (this.props.token !== undefined) {
          console.log('token received: ', this.props.token);
          this.connect(this.props.token);
      } else {
          try {
              var token = await this.getToken();
              console.log(token);
              this.connect(token);
          } catch (error) {
              console.error('There was an error getting the token:', error.code, error.message);
              if(this.props.error){
                  this.props.error({ error: error.error, messgae: error.message, code: error.code, status: error.status });
              }
              alert('There was an error getting the token:', error.message);
          }
      }
  }

  connect(token) {
      this.state.session
          .connect(
              token,
              { clientData: this.state.myUserName },
          )
          .then(() => {
              this.connectWebCam();
          })
          .catch((error) => {
              if(this.props.error){
                  this.props.error({ error: error.error, messgae: error.message, code: error.code, status: error.status });
              }
              alert('There was an error connecting to the session:', error.message);
              console.log('There was an error connecting to the session:', error.code, error.message);
          });
  }

  async connectWebCam() {
      await this.OV.getUserMedia({ audioSource: undefined, videoSource: undefined });
      var devices = await this.OV.getDevices();
      var videoDevices = devices.filter(device => device.kind === 'videoinput');

      let publisher = this.OV.initPublisher(undefined, {
          // audioSource, videoSource 넘어온 설정 값으로 변경할 때 사용해야함.
          audioSource: undefined,
          videoSource: videoDevices[0].deviceId,
          publishAudio: localUser.isAudioActive(),
          publishVideo: localUser.isVideoActive(),
          resolution: '640x480',
          frameRate: 30,
          insertMode: 'APPEND',
      });

      if (this.state.session.capabilities.publish) {
          publisher.on('accessAllowed' , () => {
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
      this.sendSignalUserChanged({ isScreenShareActive: localUser.isScreenShareActive() });

      this.setState({ currentVideoDevice: videoDevices[0], localUser: localUser }, () => {
          this.state.localUser.getStreamManager().on('streamPlaying', (e) => {
            //   this.updateLayout();
              publisher.videos[0].video.parentElement.classList.remove('custom-class');
          });
      });
  }

  updateSubscribers() {
      var subscribers = this.remotes;
      this.setState(
          {
              subscribers: subscribers,
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
            //   this.updateLayout();
          },
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
          mySessionId: 'SessionA',
          myUserName: 'WEEFY_User' + Math.floor(Math.random() * 100),
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
      this.sendSignalUserChanged({ nickname: this.state.localUser.getNickname() });
  }

  deleteSubscriber(stream) {
      const remoteUsers = this.state.subscribers;
      const userStream = remoteUsers.filter((user) => user.getStreamManager().stream === stream)[0];
      let index = remoteUsers.indexOf(userStream, 0);
      if (index > -1) {
          remoteUsers.splice(index, 1);
          this.setState({
              subscribers: remoteUsers,
          });
      }
  }

  subscribeToStreamCreated() {
      this.state.session.on('streamCreated', (event) => {
          const subscriber = this.state.session.subscribe(event.stream, undefined);
          // var subscribers = this.state.subscribers;
          subscriber.on('streamPlaying', (e) => {
              this.checkSomeoneShareScreen();
              subscriber.videos[0].video.parentElement.classList.remove('custom-class');
          });
          const newUser = new UserModel();
          newUser.setStreamManager(subscriber);
          newUser.setConnectionId(event.stream.connection.connectionId);
          newUser.setType('remote');
          const nickname = event.stream.connection.data.split('%')[0];
          newUser.setNickname(JSON.parse(nickname).clientData);
          this.remotes.push(newUser);
          if(this.localUserAccessAllowed) {
              this.updateSubscribers();
          }
      });
  }

  subscribeToStreamDestroyed() {
      // On every Stream destroyed...
      this.state.session.on('streamDestroyed', (event) => {
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
      this.state.session.on('signal:userChanged', (event) => {
          let remoteUsers = this.state.subscribers;
          remoteUsers.forEach((user) => {
              if (user.getConnectionId() === event.from.connectionId) {
                  const data = JSON.parse(event.data);
                  console.log('EVENTO REMOTE: ', event.data);
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
              },
              () => this.checkSomeoneShareScreen(),
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
          type: 'userChanged',
      };
      this.state.session.signal(signalOptions);
  }

  toggleFullscreen() {
      const document = window.document;
      const fs = document.getElementById('container');
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
      try{
          const devices = await this.OV.getDevices()
          var videoDevices = devices.filter(device => device.kind === 'videoinput');

          if(videoDevices && videoDevices.length > 1) {

              var newVideoDevice = videoDevices.filter(device => device.deviceId !== this.state.currentVideoDevice.deviceId)

              if (newVideoDevice.length > 0) {
                  // Creating a new publisher with specific videoSource
                  // In mobile devices the default and first camera is the front one
                  var newPublisher = this.OV.initPublisher(undefined, {
                      audioSource: undefined,
                      videoSource: newVideoDevice[0].deviceId,
                      publishAudio: localUser.isAudioActive(),
                      publishVideo: localUser.isVideoActive(),
                      mirror: false
                  });

                  //newPublisher.once("accessAllowed", () => {
                  await this.state.session.unpublish(this.state.localUser.getStreamManager());
                  await this.state.session.publish(newPublisher)
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
      const videoSource = navigator.userAgent.indexOf('Firefox') !== -1 ? 'window' : 'screen';
      const publisher = this.OV.initPublisher(
          undefined,
          {
              videoSource: videoSource,
              publishAudio: localUser.isAudioActive(),
              publishVideo: localUser.isVideoActive(),
              mirror: true,
          },
          (error) => {
              if (error && error.name === 'SCREEN_EXTENSION_NOT_INSTALLED') {
                  this.setState({ showExtensionDialog: true });
              } else if (error && error.name === 'SCREEN_SHARING_NOT_SUPPORTED') {
                  alert('Your browser does not support screen sharing');
              } else if (error && error.name === 'SCREEN_EXTENSION_DISABLED') {
                  alert('You need to enable screen sharing extension');
              } else if (error && error.name === 'SCREEN_CAPTURE_DENIED') {
                  alert('You need to choose a window or application to share');
              }
          },
      );

      publisher.once('accessAllowed', () => {
          this.state.session.unpublish(localUser.getStreamManager());
          localUser.setStreamManager(publisher);
          this.state.session.publish(localUser.getStreamManager()).then(() => {
              localUser.setScreenShareActive(true);
              this.setState({ localUser: localUser }, () => {
                  this.sendSignalUserChanged({ isScreenShareActive: localUser.isScreenShareActive() });
              });
          });
      });
      publisher.on('streamPlaying', () => {
        //   this.updateLayout();
          publisher.videos[0].video.parentElement.classList.remove('custom-class');
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
      isScreenShared = this.state.subscribers.some((user) => user.isScreenShareActive()) || localUser.isScreenShareActive();
      const openviduLayoutOptions = {
          maxRatio: 3 / 2,
          minRatio: 9 / 16,
          fixedRatio: isScreenShared,
          bigClass: 'OV_big',
          bigPercentage: 0.8,
          bigFixedRatio: false,
          bigMaxRatio: 3 / 2,
          bigMinRatio: 9 / 16,
          bigFirst: true,
          animate: true,
      };
      this.layout.setLayoutOptions(openviduLayoutOptions);
    //   this.updateLayout();
  }

  toggleChat(property) {
      let display = property;

      if (display === undefined) {
          display = this.state.chatDisplay === 'none' ? 'block' : 'none';
      }
      if (display === 'block') {
          this.setState({ chatDisplay: display, messageReceived: false });
      } else {
          console.log('chat', display);
          this.setState({ chatDisplay: display });
      }
    //   this.updateLayout();
  }

  checkNotification(event) {
      this.setState({
          messageReceived: this.state.chatDisplay === 'none',
      });
  }
  checkSize() {
      if (document.getElementById('layout').offsetWidth <= 700 && !this.hasBeenUpdated) {
          this.toggleChat('none');
          this.hasBeenUpdated = true;
      }
      if (document.getElementById('layout').offsetWidth > 700 && this.hasBeenUpdated) {
          this.hasBeenUpdated = false;
      }
  }

  // mui 사용을 위한 함수
  handleDrawerOpen () {
    this.setState({ open: true });
  }

  handleDrawerClose () {
    this.setState({ open: false });
  }

  handlePartChatToggle (event, val) {
    this.setState({ partChatToggle: val });
  }

  handleDefaultMode () {
    this.setState({ defaultMode: true})
  }

  handleGridMode () {
    this.setState({ defaultMode: false})
  }

  render() {
      const mySessionId = this.state.mySessionId;
      const localUser = this.state.localUser;
      var chatDisplay = { display: this.state.chatDisplay };

      // Mode toggle 버튼
      let defaultMode = this.state.defaultMode;

      return (
        <div className="container" id="container">
          {/* 브라우저 화면 공유를 위한 Chrome 확장 프로그램 설치 유도 및 설치 후 브라우저 새로고침 기능 제공 */}
          <DialogExtensionComponent showDialog={this.state.showExtensionDialog} cancelClicked={this.closeDialogExtension} />

          <div id="layout" className="bounds">
          <Box sx={{ display: 'flex' }}>
            <CssBaseline />
            <AppBar position="fixed" open={this.state.open}>
              <Toolbar style={{ backgroundColor: '#374151' }}>
                <p className={styles.logo}>WEEFY</p>
                {mySessionId && <div className={ styles.sessionId }>
                    <span id="session-title">{ mySessionId }</span>
                </div>}
                <Typography variant="h6" noWrap sx={{ flexGrow: 1 }} component="div" className={styles.screenMode}>
                    <IconButton style={{ height: "64px" }} onClick={this.handleDefaultMode}>
                        <SquareRoundedIcon fontSize='medium' style={{ color: 'white' }}/>
                        { defaultMode ? <div className={styles.line}></div> : null }
                    </IconButton>
                    <IconButton style={{ height: "64px" }} onClick={this.handleGridMode}>
                        <GridViewRoundedIcon style={{ color: 'white' }}/>
                        { !defaultMode ? <div className={styles.line}></div> : null }
                    </IconButton>
                </Typography>
                <div>
                    <IconButton className={styles.record}>
                        <RadioButtonCheckedOutlinedIcon style={{ color: "red"}}/>
                    </IconButton>
                </div>
              </Toolbar>
            </AppBar>
            <Main open={this.state.open}  style={{ backgroundColor: '#10161F', color:'white'}}>
              {/* Main Component 자리 */}
              {
                defaultMode ? 
                <div>
                    {localUser !== undefined && localUser.getStreamManager() !== undefined && (
                        <div className={ styles.videoMe } id="localUser">
                            <StreamComponent user={localUser} handleNickname={this.nicknameChanged} />
                        </div>
                    )}
                </div> : <div></div>
              }
              <div className={styles.openDrawer}>
                <IconButton
                    color="inherit"
                    aria-label="open drawer"
                    edge="end"
                    onClick={this.handleDrawerOpen}
                    sx={{ ...(this.state.open && { display: 'none' }) }}
                  >
                    <ArrowBackIosIcon fontSize='small' style={{ color:'whitesmoke' }}/>
                </IconButton>
              </div>
            </Main>
            <Drawer
              sx={{
                width: drawerWidth,
                flexShrink: 0,
                '& .MuiDrawer-paper': {
                  width: drawerWidth,
                },
              }}
              variant="persistent"
              anchor="right"
              open={this.state.open}
            >
              <div className={styles.closeDrawer}>
                <IconButton onClick={this.handleDrawerClose}>
                  <ArrowForwardIosIcon fontSize='small' style={{ color:'whitesmoke', position: 'relative', right: '4px' }}/>
                </IconButton>
              </div>
              <DrawerHeader style={{ backgroundColor: '#374151' }}>
                <div>
                <ToggleButtonGroup
                  value={this.state.partChatToggle}
                  exclusive
                  onChange={this.handlePartChatToggle}
                  aria-label="text alignment"
                >
                  <ToggleButton value="part" aria-label="left aligned">
                    <AccountCircleIcon />
                  </ToggleButton>
                  <ToggleButton value="chat" aria-label="centered">
                    <Badge badgeContent={4} color="secondary">
                      <ChatIcon color="action"/>
                    </Badge>
                  </ToggleButton>
                </ToggleButtonGroup>
                </div>
              </DrawerHeader>
              <Divider />
              <List style={{ backgroundColor: '#17202E' }}>
                {['Inbox', 'Starred', 'Send email', 'Drafts'].map((text, index) => (
                  <ListItem key={text} disablePadding>
                    <ListItemButton>
                      <ListItemIcon>
                        {index % 2 === 0 ? <InboxIcon /> : <MailIcon />}
                      </ListItemIcon>
                      <ListItemText primary={text} />
                    </ListItemButton>
                  </ListItem>
                ))}
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
      const sessionId = await this.createSession(this.state.mySessionId);
      return await this.createToken(sessionId);
  }

  async createSession(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
          headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The sessionId
  }

  async createToken(sessionId) {
      const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
          headers: { 'Content-Type': 'application/json', },
      });
      return response.data; // The token
  }
}

export default Conference;