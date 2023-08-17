import React, { Component } from 'react';

import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Button from '@mui/material/Button';
import Typography from '@mui/material/Typography';
import './DialogExtension.css';

// 브라우저 화면 공유를 위한 Chrome 확장 프로그램 설치 유도 및 설치 후 브라우저 새로고침 기능 제공

export default class DialogExtensionComponent extends Component {
    constructor(props) {
        super(props);
        this.openviduExtensionUrl =
            'https://chrome.google.com/webstore/detail/openvidu-screensharing/lfcgfepafnobdloecchnfaclibenjold';

        this.state = {
            isInstalled: false,
        };
        this.goToChromePage = this.goToChromePage.bind(this);
        this.onNoClick = this.onNoClick.bind(this);
        this.refreshBrowser = this.refreshBrowser.bind(this);
    }

    componentWillReceiveProps(props) {}

    componentDidMount() {}

    onNoClick() {
        this.props.cancelClicked();
    }

    goToChromePage() {
        window.open(this.openviduExtensionUrl);
        this.setState({ isInstalled: true });
    }

    refreshBrowser() {
        window.location.reload();
    }

    render() {
        return (
            <div>
                {this.props && this.props.showDialog ? (
                    <div id="dialogExtension">
                        <Card id="card">
                            <CardContent>
                                <Typography color="textSecondary"><span style={{ fontFamily: "Mogra" }}>WEFFY </span>화면 공유 기능</Typography>
                                <Typography color="textSecondary" style={{ whiteSpace: 'pre-line' }}>
                                    화면 공유 기능을 이용하기 위해서 Chrome 확장 프로그램을 설치해야 합니다.
                                    확장 프로그램 설치 후 새로고침 버튼을 눌러주세요.
                                </Typography>
                            </CardContent>
                            <CardActions>
                                <Button size="small" onClick={this.onNoClick}>
                                    취소
                                </Button>

                                <Button size="small" onClick={this.goToChromePage}>
                                    설치
                                </Button>
                                {this.state.isInstalled ? (
                                    <Button size="small" onClick={this.refreshBrowser}>
                                        새로고침
                                    </Button>
                                ) : null}
                            </CardActions>
                        </Card>
                    </div>
                ) : null}
            </div>
        );
    }
}
