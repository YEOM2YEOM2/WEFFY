import React, { Component } from 'react';
import { connect } from 'react-redux'; 
import './StreamComponent.css';

const mapStateToProps = state => {
    return {
        imageURL: state.user.profileImg
    }
}

class OvVideoComponent extends Component {
    constructor(props) {
        super(props);
        this.videoRef = React.createRef();
    }

    componentDidMount() {
        if (this.props && this.props.user.streamManager && !!this.videoRef) {
            console.log('PROPS: ', this.props);
            this.props.user.getStreamManager().addVideoElement(this.videoRef.current);
        }

        if (this.props && this.props.user.streamManager.session && this.props.user && !!this.videoRef) {
            this.props.user.streamManager.session.on('signal:userChanged', (event) => {
                const data = JSON.parse(event.data);
                if (data.isScreenShareActive !== undefined) {
                    this.props.user.getStreamManager().addVideoElement(this.videoRef.current);
                }
            });
        }
    }

    componentDidUpdate(props) {
        if (props && !!this.videoRef) {
            console.log(this.props.user)
            this.props.user.getStreamManager().addVideoElement(this.videoRef.current);
        }
    }

    render() {
        const imageURL = this.props.imageURL
        console.log(imageURL)

        return (
            // <div className="video-container" style={{ backgroundImage: `url(${imageURL})` }}>
                <video
                    autoPlay={true}
                    id={'video-' + this.props.user.getStreamManager().stream.streamId}
                    ref={this.videoRef}
                    muted={this.props.mutedSound}
                />
            // </div>
        );
    }
}

export default connect(mapStateToProps)(OvVideoComponent);