import React, { useState } from "react";
import "./temp.module.css";
import axios from "axios";
import OpenViduSession from "openvidu-react";

const App = () => {
  const APPLICATION_SERVER_URL =
    process.env.NODE_ENV === "production" ? "" : "http://localhost:8080/";

  const [mySessionId, setMySessionId] = useState("SessionA");
  const [myUserName, setMyUserName] = useState(
    "OpenVidu_User_" + Math.floor(Math.random() * 100)
  );
  const [token, setToken] = useState(undefined);
  const [session, setSession] = useState(undefined);

  const handlerJoinSessionEvent = () => {
    console.log("Join session");
  };

  const handlerLeaveSessionEvent = () => {
    console.log("Leave session");
    setSession(undefined);
  };

  const handlerErrorEvent = () => {
    console.log("Error");
  };

  const handleChangeSessionId = (e) => {
    setMySessionId(e.target.value);
  };

  const handleChangeUserName = (e) => {
    setMyUserName(e.target.value);
  };

  const joinSession = async (event) => {
    event.preventDefault();
    if (mySessionId && myUserName) {
      const token = await getToken();
      setToken(token);
      setSession(true);
    }
  };

  const getToken = async () => {
    const sessionId = await createSession(mySessionId);
    return await createToken(sessionId);
  };

  const createSession = async (sessionId) => {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "api/sessions",
      { customSessionId: sessionId },
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    return response.data;
  };

  const createToken = async (sessionId) => {
    const response = await axios.post(
      APPLICATION_SERVER_URL + "api/sessions/" + sessionId + "/connections",
      {},
      {
        headers: { "Content-Type": "application/json" },
      }
    );
    return response.data;
  };

  return (
    <div>
      {session === undefined ? (
        <div id="join">
          <div id="join-dialog">
            <h1> Join a video session </h1>
            <form onSubmit={joinSession}>
              <p>
                <label>Participant: </label>
                <input
                  type="text"
                  id="userName"
                  value={myUserName}
                  onChange={handleChangeUserName}
                  required
                />
              </p>
              <p>
                <label> Session: </label>
                <input
                  type="text"
                  id="sessionId"
                  value={mySessionId}
                  onChange={handleChangeSessionId}
                  required
                />
              </p>
              <p>
                <input name="commit" type="submit" value="JOIN" />
              </p>
            </form>
          </div>
        </div>
      ) : (
        <div id="session">
          <OpenViduSession
            id="opv-session"
            sessionName={mySessionId}
            user={myUserName}
            token={token}
            joinSession={handlerJoinSessionEvent}
            leaveSession={handlerLeaveSessionEvent}
            error={handlerErrorEvent}
          />
        </div>
      )}
    </div>
  );
};

export default App;
