import React, { Component } from 'react';
import { useNavigate } from 'react-router-dom';
import './ToolbarComponent.css';

import Toolbar from '@mui/material/Toolbar';

// mui
import MicIcon from '@mui/icons-material/Mic';
import MicOffIcon from '@mui/icons-material/MicOff';
import VideocamIcon from '@mui/icons-material/Videocam';
import VideocamOffIcon from '@mui/icons-material/VideocamOff';
import FullscreenIcon from '@mui/icons-material/Fullscreen';
import FullscreenExitIcon from '@mui/icons-material/FullscreenExit';
import PictureInPictureIcon from '@mui/icons-material/PictureInPicture';
import ScreenShareIcon from '@mui/icons-material/ScreenShare';
import StopScreenShareIcon from '@mui/icons-material/StopScreenShare';
import Tooltip from '@mui/material/Tooltip';
import ExitToAppIcon from '@mui/icons-material/ExitToApp';
import IconButton from '@mui/material/IconButton';


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
                        
                          {localUser !== undefined && localUser.isScreenShareActive() ? 
                          <Tooltip title="화면 공유창 선택" placement="top"><PictureInPictureIcon /></Tooltip> : <Tooltip title="화면 공유" placement="top"><ScreenShareIcon /></Tooltip>}
                      </IconButton>

                      {localUser !== undefined &&
                          localUser.isScreenShareActive() && (
                            <Tooltip title="화면 공유 정지" placement="top">
                              <IconButton onClick={this.stopScreenShare} id="navScreenButton">
                                  <StopScreenShareIcon style={{ color: "red" }} />
                              </IconButton>
                            </Tooltip>
                          )}
                      <IconButton color="inherit" className="navButton" onClick={this.toggleFullscreen}>
                          {localUser !== undefined && this.state.fullscreen ? 
                          <Tooltip title="전체화면 종료" placement='top'><FullscreenExitIcon /></Tooltip> 
                          : <Tooltip title="전체 화면" placement='top'><FullscreenIcon /></Tooltip>}
                      </IconButton>
                      
                      <IconButton id="navChatButton" onClick={this.leaveSession}>
                        <ExitBtn /> 
                      </IconButton>
                  </div>
              </Toolbar>
        );
    }
}

function ExitBtn() {
    let navigate = useNavigate();

    const handleExit = () => {
        navigate("/im")
    }

    return (
        <Tooltip title="회의 나가기" placement="top">
            <IconButton onClick={handleExit} id="navLeaveButton" style={{ color: "white",  backgroundColor: "red", borderRadius: '5px', top: '-2px' }}>
                <ExitToAppIcon fontSize='small'/>
            </IconButton>
        </Tooltip>
    )
}