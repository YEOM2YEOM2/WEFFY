import React, { Component } from 'react';
import './ToolbarComponent.css';

import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';

import MicIcon from '@mui/icons-material/Mic';
import MicOffIcon from '@mui/icons-material/MicOff';
import VideocamIcon from '@mui/icons-material/Videocam';
import VideocamOffIcon from '@mui/icons-material/VideocamOff';
import FullscreenIcon from '@mui/icons-material/Fullscreen';
import FullscreenExitIcon from '@mui/icons-material/FullscreenExit';
import SwitchCameraIcon from '@mui/icons-material/SwitchCamera';
import PictureInPictureIcon from '@mui/icons-material/PictureInPicture';
import ScreenShareIcon from '@mui/icons-material/ScreenShare';
import StopScreenShareIcon from '@mui/icons-material/StopScreenShare';
import Tooltip from '@mui/material/Tooltip';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import QuestionAnswerIcon from '@mui/icons-material/QuestionAnswer';

import IconButton from '@mui/material/IconButton';

const logo = require('./../../../assets/images/WEFFY_logo.png');

export default class ToolbarComponent extends Component {
    constructor(props) {
        super(props);
        this.state = { fullscreen: false };
        this.camStatusChanged = this.camStatusChanged.bind(this);
        this.micStatusChanged = this.micStatusChanged.bind(this);
        this.screenShare = this.screenShare.bind(this);
        this.stopScreenShare = this.stopScreenShare.bind(this);
        this.toggleFullscreen = this.toggleFullscreen.bind(this);
        this.switchCamera = this.switchCamera.bind(this);
        this.leaveSession = this.leaveSession.bind(this);
        this.toggleChat = this.toggleChat.bind(this);
    }


    micStatusChanged() {
        this.props.micStatusChanged();
    }

    camStatusChanged() {
        this.props.camStatusChanged();
    }

    screenShare() {
        this.props.screenShare();
    }

    stopScreenShare() {
        this.props.stopScreenShare();
    }

    toggleFullscreen() {
        this.setState({ fullscreen: !this.state.fullscreen });
        this.props.toggleFullscreen();
    }

    switchCamera() {
        this.props.switchCamera();
    }

    leaveSession() {
        this.props.leaveSession();
    }

    toggleChat() {
        this.props.toggleChat();
    }

    render() {
        const mySessionId = this.props.sessionId;
        const localUser = this.props.user;
        return (
              <Toolbar className="toolbar" 
              style={{ position: 'fixed', bottom: '0', left: '0', width: '100%', 
                      color: 'white', backgroundColor: '#374151',zIndex: '999999' }}>
                  <div className="buttonsContent">
                      <IconButton color="inherit" className="navButton" id="navMicButton" onClick={this.micStatusChanged}>
                          {localUser !== undefined && localUser.isAudioActive() ? <MicIcon /> : <MicOffIcon style={{ color: "red" }} />}
                      </IconButton>

                      <IconButton color="inherit" className="navButton" id="navCamButton" onClick={this.camStatusChanged}>
                          {localUser !== undefined && localUser.isVideoActive() ? (
                              <VideocamIcon />
                          ) : (
                              <VideocamOffIcon style={{ color: "red" }} />
                          )}
                      </IconButton>

                      <IconButton color="inherit" className="navButton" onClick={this.screenShare}>
                          {localUser !== undefined && localUser.isScreenShareActive() ? <PictureInPictureIcon /> : <ScreenShareIcon />}
                      </IconButton>

                      {localUser !== undefined &&
                          localUser.isScreenShareActive() && (
                              <IconButton onClick={this.stopScreenShare} id="navScreenButton">
                                  <StopScreenShareIcon style={{ color: "red" }} />
                              </IconButton>
                          )}
                      <IconButton color="inherit" className="navButton" onClick={this.toggleFullscreen}>
                          {localUser !== undefined && this.state.fullscreen ? <FullscreenExitIcon /> : <FullscreenIcon />}
                      </IconButton>
                      
                        <IconButton id="navChatButton">
                        <IconButton onClick={this.leaveSession} id="navLeaveButton" style={{ color: "white",  backgroundColor: "red" }}>
                            <ExitToAppIcon />
                        </IconButton>
                      </IconButton>
                  </div>
              </Toolbar>
        );
    }
}
