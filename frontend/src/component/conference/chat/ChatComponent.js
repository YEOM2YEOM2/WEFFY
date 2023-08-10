import React, { Component } from "react";
import IconButton from "@mui/material/IconButton";
import CancelOutlinedIcon from "@mui/icons-material/CancelOutlined";
import ForwardToInboxOutlinedIcon from "@mui/icons-material/ForwardToInboxOutlined";
import AttachFileIcon from "@mui/icons-material/AttachFile";
import { connect } from "react-redux";

import "./ChatComponent.css";
import Tooltip from "@mui/material/Tooltip";
import { yellow } from "@mui/material/colors";
import SendIcon from "@mui/icons-material/Send";

import axios from "axios";

const mapStateToProps = (state) => {
  return {
    accessToken: state.user.accessToken,
  };
};

// // If you need to dispatch actions to Redux, use this
// const mapDispatchToProps = (dispatch) => {
//   return {
//     // For example:
//     // setIdentification: (id) => dispatch(setIdentification(id))
//   };
// };

class ChatComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      messageList: [],
      message: "",
      // authorization: "",
    };
    this.chatScroll = React.createRef();

    this.handleChange = this.handleChange.bind(this);
    this.handlePressKey = this.handlePressKey.bind(this);
    this.close = this.close.bind(this);
    this.sendMessage = this.sendMessage.bind(this);
    this.addFile = this.addFile.bind(this);
  }

  componentDidMount() {
    this.props.user
      .getStreamManager()
      .stream.session.on("signal:chat", (event) => {
        const data = JSON.parse(event.data);
        let messageList = this.state.messageList;
        messageList.push({
          connectionId: event.from.connectionId,
          nickname: data.nickname,
          message: data.message,
        });
        const document = window.document;
        setTimeout(() => {
          const userImg = document.getElementById(
            "userImg-" + (this.state.messageList.length - 1)
          );
          const video = document.getElementById("video-" + data.streamId);
          const avatar = userImg.getContext("2d");
          avatar.drawImage(video, 200, 120, 285, 285, 0, 0, 60, 60);
          this.props.messageReceived();
        }, 50);
        this.setState({ messageList: messageList });
        this.scrollToBottom();
      });
  }

  handleChange(event) {
    this.setState({ message: event.target.value });
  }

  handlePressKey(event) {
    if (event.key === "Enter") {
      this.sendMessage();
    }
  }

  sendMessage() {
    console.log(this.state.message);
    if (typeof this.state.message === "string") {
      if (this.props.user && this.state.message) {
        let message = this.state.message.replace(/ +(?= )/g, "");
        if (message !== "" && message !== " ") {
          const data = {
            message: message,
            nickname: this.props.user.getNickname(),
            streamId: this.props.user.getStreamManager().stream.streamId,
          };
          this.props.user.getStreamManager().stream.session.signal({
            data: JSON.stringify(data),
            type: "chat",
          });
        }
      }
    } else if (
      typeof this.state.message === "object" &&
      this.state.message.url &&
      this.state.message.fileName
    ) {
      if (this.props.user) {
        const data = {
          message: {
            url: this.state.message.url,
            fileName: this.state.message.fileName,
          },
          nickname: this.props.user.getNickname(),
          streamId: this.props.user.getStreamManager().stream.streamId,
        };
        this.props.user.getStreamManager().stream.session.signal({
          data: JSON.stringify(data),
          type: "chat",
        });
      }
    }
  }

  sendMessage() {
    console.log(this.state.message);

    // Check if there's a file to upload
    if (this.state.fileData) {
      axios({
        method: "post",
        url: "http://i9d107.p.ssafy.io:8081/api/v1/files",
        headers: {
          accept: "application/json",
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${this.props.accessToken}`,
        },
        data: this.state.fileData,
      })
        .then((res) => {
          const fileInfo = {
            url: res.data.data,
            fileName: "이름없는 파일",
          };

          this.setState(
            {
              message: fileInfo,
              fileData: null, // Reset the fileData once it's sent
            },
            () => {
              this.sendMessage(); // This will handle the file info as a message now
            }
          );
        })
        .catch((err) => {
          console.log(err);
        });
    }
    // Check if it's a text message
    else if (
      typeof this.state.message === "string" &&
      this.props.user &&
      this.state.message
    ) {
      let message = this.state.message.replace(/ +(?= )/g, "");

      if (message !== "" && message !== " ") {
        const data = {
          message: message,
          nickname: this.props.user.getNickname(),
          streamId: this.props.user.getStreamManager().stream.streamId,
        };

        this.props.user.getStreamManager().stream.session.signal({
          data: JSON.stringify(data),
          type: "chat",
        });

        this.setState({ message: "" });
      }
    }
    // Check if it's a file info message
    else if (
      typeof this.state.message === "object" &&
      this.state.message.url &&
      this.state.message.fileName &&
      this.props.user
    ) {
      const data = {
        message: {
          url: this.state.message.url,
          fileName: this.state.message.fileName,
        },
        nickname: this.props.user.getNickname(),
        streamId: this.props.user.getStreamManager().stream.streamId,
      };

      this.props.user.getStreamManager().stream.session.signal({
        data: JSON.stringify(data),
        type: "chat",
      });

      this.setState({ message: "" });
    }
  }

  addFile(event) {
    const file = event.target.files[0];
    if (file) {
      // console.log(file);

      // const accessToken = useSelector((state) => state.user.accessToken);
      // console.log(this.props.accessToken);

      const formData = new FormData();
      formData.append("file", file);

      this.setState({ fileData: formData });
      axios({
        method: "post",
        url: "http://i9d107.p.ssafy.io:8081/api/v1/files",
        headers: {
          accept: "application/json",
          "Content-Type": "multipart/form-data",
          Authorization: `Bearer ${this.props.accessToken}`,
        },
        data: formData,
      })
        .then((res) => {
          // console.log(res.data.data);

          const fileInfo = {
            url: res.data.data,
            fileName: "이름없는 파일",
          };

          this.setState({ message: fileInfo }, () => {
            this.sendMessage();
          });
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }

  scrollToBottom() {
    setTimeout(() => {
      try {
        this.chatScroll.current.scrollTop =
          this.chatScroll.current.scrollHeight;
      } catch (err) {}
    }, 20);
  }

  close() {
    this.props.close(undefined);
  }

  render() {
    const styleChat = { display: this.props.chatDisplay };
    return (
      <div id="chatContainer">
        <div id="chatComponent" style={styleChat}>
          <div id="chatToolbar">
            <span style={{ fontFamily: "Agro", fontWeight: "400" }}>
              일반 채팅
            </span>
            <IconButton id="closeButton" onClick={this.close}>
              <CancelOutlinedIcon style={{ color: "red" }} />
            </IconButton>
          </div>
          <div className="message-wrap" ref={this.chatScroll}>
            {console.log(this.state.messageList)}
            {this.state.messageList.map((data, i) => (
              <div
                key={i}
                id="remoteUsers"
                className={
                  "message" +
                  (data.connectionId !== this.props.user.getConnectionId()
                    ? " left"
                    : " right")
                }
              >
                <canvas
                  id={"userImg-" + i}
                  width="60"
                  height="60"
                  className="user-img"
                />
                <div className="msg-detail">
                  <div className="msg-info">
                    <p style={{ fontFamily: "Poppins", fontSize: "12px" }}>
                      {" "}
                      {data.nickname}
                    </p>
                  </div>
                  <div className="msg-content">
                    <span className="triangle" />

                    <p className="text" style={{ fontFamily: "GmarketSans" }}>
                      {data.message && typeof data.message === "object" ? (
                        <a
                          href={data.message.url || "#"}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          {data.message.fileName || "이름 없는 파일"}
                        </a>
                      ) : (
                        data.message || ""
                      )}
                    </p>
                  </div>
                </div>
              </div>
            ))}
          </div>

          <div id="fileContainer">
            <Tooltip title="파일 첨부" placement="top">
              <IconButton
                size="small"
                id="fileButton"
                onClick={() => document.getElementById("fileInput").click()}
                style={{ padding: "10px", margin: "4px" }}
              >
                <input
                  type="file"
                  id="fileInput"
                  style={{ display: "none" }}
                  onChange={this.addFile}
                />
                <AttachFileIcon />
              </IconButton>
            </Tooltip>
          </div>

          <div id="messageInput">
            <input
              placeholder="메시지를 입력해주세요."
              id="chatInput"
              value={this.state.message}
              onChange={this.handleChange}
              onKeyPress={this.handlePressKey}
              style={{ fontFamily: "GmarketSans" }}
            />
            <div style={{ display: "flex" }}>
              {/* <Tooltip title="전송" placement="top"> */}
              <IconButton
                size="small"
                id="sendButton"
                onClick={this.sendMessage}
                style={{ padding: "10px", margin: "4px" }}
              >
                <SendIcon style={{ color: "white" }} />
              </IconButton>
              {/* </Tooltip> */}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default connect(mapStateToProps)(ChatComponent);
