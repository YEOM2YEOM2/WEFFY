import React, { Component } from 'react';
import './StreamComponent.css';
import OvVideoComponent from './OvVideo';

import MicOffIcon from '@mui/icons-material/MicOff';
import VolumeUpIcon from '@mui/icons-material/VolumeUp';
import VolumeOffIcon from '@mui/icons-material/VolumeOff';
import InfoIcon from '@mui/icons-material/Info';
import FormControl from '@mui/material/FormControl';
import Input from '@mui/material/Input';
import InputLabel from '@mui/material/InputLabel';
import IconButton from '@mui/material/IconButton';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import FormHelperText from '@mui/material/FormHelperText';
import Tooltip from '@mui/material/Tooltip';

export default class StreamComponent extends Component {
    constructor(props) {
        super(props);
        this.state = { nickname: this.props.user.getNickname(), showForm: false, mutedSound: false, isFormValid: true };
        this.handleChange = this.handleChange.bind(this);
        this.handlePressKey = this.handlePressKey.bind(this);
        this.toggleNicknameForm = this.toggleNicknameForm.bind(this);
        this.toggleSound = this.toggleSound.bind(this);
    }

    handleChange(event) {
        this.setState({ nickname: event.target.value });
        event.preventDefault();
    }

    toggleNicknameForm() {
        if (this.props.user.isLocal()) {
            this.setState({ showForm: !this.state.showForm });
        }
    }

    toggleSound() {
        this.setState({ mutedSound: !this.state.mutedSound });
    }

    handlePressKey(event) {
        if (event.key === 'Enter') {
            console.log(this.state.nickname);
            if (this.state.nickname.length >= 3 && this.state.nickname.length <= 20) {
                this.props.handleNickname(this.state.nickname);
                this.toggleNicknameForm();
                this.setState({ isFormValid: true });
            } else {
                this.setState({ isFormValid: false });
            }
        }
    }

    render() {
        return (
            <div className="OT_widget-container" style={{ width: '97%', maxHeight: '600px', borderRadius: '10px' }}>
                <div className="pointer nickname">
                    {this.state.showForm ? (
                        <FormControl id="nicknameForm" style={{  }}>
                            <IconButton color="inherit" id="closeButton" onClick={this.toggleNicknameForm} style={{postion: 'absolute', top: '-7px'}}>
                                <HighlightOffIcon />
                            </IconButton>
                            <InputLabel htmlFor="name-simple" id="label" style={{ fontFamily: 'GmarketSans' ,top: '10px', left: '-3px' }}>
                                참가자명 수정
                            </InputLabel>
                            <Input
                                id="input"
                                value={this.state.nickname}
                                onChange={this.handleChange}
                                onKeyPress={this.handlePressKey}
                                required
                                style={{ fontFamily: 'GmarketSans' }}
                            />
                            {!this.state.isFormValid && this.state.nickname.length <= 3 && (
                                <FormHelperText id="name-error-text" style={{ fontFamily: 'GmarketSans' }}>4 ~ 20자로 작성해주세요.</FormHelperText>
                            )}
                            {!this.state.isFormValid && this.state.nickname.length >= 21 && (
                                <FormHelperText id="name-error-text" style={{ fontFamily: 'GmarketSans' }}>4 ~ 20자로 작성해주세요.</FormHelperText>
                            )}
                        </FormControl>
                    ) : (
                        <div onClick={this.toggleNicknameForm}>
                            <Tooltip title="참가자명 변경" placement='right'>
                                <span id="nickname">{this.props.user.getNickname()}</span>
                                {this.props.user.isLocal() && <span id=""><InfoIcon fontSize='small' style={{ marginLeft: '3px' }} /></span>}
                            </Tooltip>
                        </div>
                    )}
                </div>

                {this.props.user !== undefined && this.props.user.getStreamManager() !== undefined ? (
                    <div className="streamComponent">
                        <OvVideoComponent user={this.props.user} mutedSound={this.state.mutedSound} />
                        <div id="statusIcons">
                            {!this.props.user.isAudioActive() ? (
                                <div id="micIcon">
                                    <MicOffIcon id="statusMic" style={{ position: 'absolute', left: "45px", top: '-4px' }}/>
                                </div>
                            ) : null}
                        </div>
                        <div>
                            {!this.props.user.isLocal() && (
                                <IconButton id="volumeButton" onClick={this.toggleSound}>
                                    {this.state.mutedSound ? <VolumeOffIcon style={{ color: "red" }} /> : <VolumeUpIcon />}
                                </IconButton>
                            )}
                        </div>
                    </div>
                ) : null}
            </div>
        );
    }
}
