import React, { Component } from 'react';
import styles from './GridStream.module.css';
import GridVideo from './GridVideo.js'

import MicOffIcon from '@mui/icons-material/MicOff';
import VolumeUpIcon from '@mui/icons-material/VolumeUp';
import VolumeOffIcon from '@mui/icons-material/VolumeOff';
import IconButton from '@mui/material/IconButton';

export default class StreamComponent extends Component {
    constructor(props) {
        super(props);
        this.state = { nickname: this.props.user.getNickname(), showForm: false, mutedSound: false, isFormValid: true };
        this.handleChange = this.handleChange.bind(this);
        this.toggleSound = this.toggleSound.bind(this);
    }

    handleChange(event) {
        this.setState({ nickname: event.target.value });
        event.preventDefault();
    }

    toggleSound() {
        this.setState({ mutedSound: !this.state.mutedSound });
    }

    render() {
        return (
            <div className={styles.container}>
                <div className="nickname">
                  <span id="nickname">{this.props.user.getNickname()}</span>
                </div>

                {this.props.user !== undefined && this.props.user.getStreamManager() !== undefined ? (
                    <div className={styles.videoContainer}>
                        <GridVideo user={this.props.user} mutedSound={this.state.mutedSound}/>
                        <div id="statusIcons">
                            {!this.props.user.isAudioActive() ? (
                                <div id="micIcon" style={{ position: 'absolute', left: "40px", top: '-8px' }}>
                                    <MicOffIcon id="statusMic"/>
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
