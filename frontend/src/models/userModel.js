class UserModel {
    connectionId;
    audioActive;
    videoActive;
    screenShareActive;
    nickname;
    streamManager;
    type; // 'remote' | 'local'
    mmIdentification;

    constructor() {
        this.connectionId = '';
        this.audioActive = false;
        this.videoActive = false;
        this.screenShareActive = false;
        this.nickname = '';
        this.streamManager = null;
        this.type = 'local';
        this.mmIdentification = '';
    }

    isAudioActive() {
        return this.audioActive;
    }

    isVideoActive() {
        return this.videoActive;
    }

    isScreenShareActive() {
        return this.screenShareActive;
    }

    getConnectionId() {
        return this.connectionId;
    }

    getNickname() {
        return this.nickname;
    }

    getStreamManager() {
        return this.streamManager;
    }

    getMmIdentification() {
        return this.mmIdentification;
    }

    isLocal() {
        return this.type === 'local';
    }
    isRemote() {
        return !this.isLocal();
    }
    isHost() {
        return this.role === 'host';
    }
    setAudioActive(isAudioActive) {
        this.audioActive = isAudioActive;
    }
    setVideoActive(isVideoActive) {
        this.videoActive = isVideoActive;
    }
    setScreenShareActive(isScreenShareActive) {
        this.screenShareActive = isScreenShareActive;
    }
    setStreamManager(streamManager) {
        this.streamManager = streamManager;
    }

    setConnectionId(conecctionId) {
        this.connectionId = conecctionId;
    }
    setNickname(nickname) {
        this.nickname = nickname;
    }
    setType(type) {
        if (type === 'local' |  type === 'remote') {
            this.type = type;
        }
    }
    setMmIdentification(identification) {
        this.mmIdentification = identification;
    }
}

export default UserModel;
