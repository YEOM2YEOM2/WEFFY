import React, { useState, useEffect, useRef } from "react";
import IconButton from "@mui/material/IconButton";
import Tooltip from "@mui/material/Tooltip";
import SendIcon from "@mui/icons-material/Send";
import CheckBoxIcon from "@mui/icons-material/CheckBox";
import CheckBoxOutlineBlankIcon from "@mui/icons-material/CheckBoxOutlineBlank";
import axios from "axios";
import { useSelector } from "react-redux";
import "./QuestionChat.css";
// assuming you're not using `useNavigate`, I've removed it

const QuestionChat = ({ user }) => {
  const accessToken = useSelector((state) => state.user.accessToken);
  const [messageList, setMessageList] = useState([]);
  const [message, setMessage] = useState("");
  const [anonymousStatus, setAnonymousStatus] = useState(true);
  const chatScroll = useRef(null);

  useEffect(() => {
    const handleSignalQuestion = (event) => {
      const data = JSON.parse(event.data);
      const newMessage = {
        connectionId: event.from.connectionId,
        nickname: data.nickname,
        message: data.message,
        timestamp: getCurTimeStamp(),
      };
      setMessageList((prevList) => [...prevList, newMessage]);
      scrollToBottom();
    };

    user.getStreamManager().stream.session.on("signal:question", handleSignalQuestion);

    return () => {
      user
        .getStreamManager()
        .stream.session.off("signal:question", handleSignalQuestion);
    };
  }, [user]);

  const getCurTimeStamp = () => {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, "0");
    const minutes = String(now.getMinutes()).padStart(2, "0");
    return `${hours}:${minutes}`;
  };

  const handleChange = (event) => {
    setMessage(event.target.value);
  };

  const handlePressKey = (event) => {
    if (event.key === "Enter") {
      sendMessage();
    }
  };

  const sendMessage = () => {
    if (typeof message === "string" && message.trim() !== "" && user) {
      const data = {
        message: `Q. ${message.trim()}`,
        // If anonymousStatus is true, use 'anonymous', otherwise use the user's nickname
        nickname: anonymousStatus ? "익명" : user.getNickname(),
        streamId: user.getStreamManager().stream.streamId,
        timestamp: getCurTimeStamp(),
      };
      console.log('질문 채팅 user',user)
      user.getStreamManager().stream.session.signal({
        data: JSON.stringify(data),
        type: "chat",
      });
      setMessage("");
    }
  };

  const scrollToBottom = () => {
    setTimeout(() => {
      if (chatScroll.current) {
        chatScroll.current.scrollTop = chatScroll.current.scrollHeight;
      }
    }, 20);
  };

  const anonymousToggle = () => {
    setAnonymousStatus((prevStatus) => !prevStatus);
  };

  // const styleChat = { display: this.props.chatDisplay };
  return (
    <div id="chatContainer">
      <div id="chatComponent">
        <div id="chatToolbar">
          <span style={{ fontFamily: "Agro", fontWeight: "400" }}>
            질문 채팅
          </span>
        </div>
        <div className="message-wrap" ref={chatScroll}>
          {/* {console.log(this.state.messageList)} */}
          {messageList.map((data, i) => (
            <div key={i} id="remoteUsers" className={"message"}>
              <div className="msg-detail">
                <div className="msg-info">
                  <p style={{ fontFamily: "Poppins", fontSize: "12px" }}>
                    {data.nickname}
                  </p>
                </div>
                <div className="content-with-timestamp">
                  <div className="msg-content">
                    <p className="text" style={{ fontFamily: "GmarketSans" }}>
                      <p className="text" style={{ fontFamily: "GmarketSans" }}>
                        {data.message}
                      </p>
                    </p>
                  </div>
                  <span className="timeStamp">{data.timestamp}</span>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div id="fileContainer">
          <Tooltip title="익명으로 질문하기" placement="top">
            <IconButton
              size="small"
              id="checkbox"
              onClick={anonymousToggle}
              style={{ padding: "10px", margin: "4px", color: "white" }}
            >
              {anonymousStatus ? (
                <CheckBoxIcon />
              ) : (
                <CheckBoxOutlineBlankIcon />
              )}
            </IconButton>
          </Tooltip>
        </div>

        <div id="messageInput">
          <input
            placeholder="메시지를 입력해주세요."
            id="chatInput"
            value={message}
            onChange={handleChange}
            onKeyPress={handlePressKey}
            style={{ fontFamily: "GmarketSans" }}
          />
          <div style={{ display: "flex" }}>
            {/* <Tooltip title="전송" placement="top"> */}
            <IconButton
              size="small"
              id="sendButton"
              onClick={sendMessage}
              style={{ padding: "10px", margin: "4px" }}
            >
              <SendIcon style={{ color: "white" }} />
            </IconButton>
          </div>
        </div>
      </div>
    </div>
  );
};

export default QuestionChat;
