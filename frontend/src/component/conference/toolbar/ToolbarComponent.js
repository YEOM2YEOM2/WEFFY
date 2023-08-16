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
        this.toggleQuestion = this.toggleQuestion.bind(this);
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

    toggleQuestion() {
        this.props.toggleQuestion();
    }

    render() {
        const mySessionId = this.props.sessionId;
        const localUser = this.props.user;
        return (
            <AppBar className="toolbar" id="header">
                <Toolbar className="toolbar">
                    <div id="navSessionInfo">
                        <img
                            id="header_img"
                            alt="OpenVidu Logo"
                            src={logo}
                        />

                        {this.props.sessionId && <div id="titleContent">
                            <span id="session-title" style={{ fontFamily: "Kalam", }}>{ mySessionId }</span>
                        </div>}
                    </div>

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
                        {/* 카메라 스위치 버튼 
                        <IconButton color="inherit" className="navButton" onClick={this.switchCamera}>
                            <SwitchCameraIcon />
                        </IconButton> */}
                        <IconButton color="inherit" className="navButton" onClick={this.toggleFullscreen}>
                            {localUser !== undefined && this.state.fullscreen ? <FullscreenExitIcon /> : <FullscreenIcon />}
                        </IconButton>
                        <IconButton className="navButton" onClick={this.leaveSession} id="navLeaveButton" style={{ color: "white",  backgroundColor: "red" }}>
                            <ExitToAppIcon />
                        </IconButton>
                         <IconButton color="inherit" onClick={this.toggleChat} id="navChatButton">
                            {this.props.showNotification && <div id="point" className="" />}
                            <Tooltip title="채팅">
                                <QuestionAnswerIcon />
                            </Tooltip>
                        </IconButton>
                        <IconButton color="inherit" onClick={this.toggleQuestion} >
                            {this.props.showQuestion && <div id="point" />}
                            <Tooltip title="질문">
                                <QuestionAnswerIcon />
                            </Tooltip>
                        </IconButton>
                    </div>
                </Toolbar>
            </AppBar>
        );
    }
}
