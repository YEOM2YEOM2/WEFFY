import React, { useEffect, useState, useRef } from "react";
import styles from "./privateModal.module.css";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import {
  setParticipateName,
  setSelectedCam,
  setSelectedMic,
  toggleMicStatus,
  toggleCameraStatus,
} from "../../store/reducers/setting.js";

import { OpenVidu } from "openvidu-browser";

import Avatar from "@mui/material/Avatar";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Stack from "@mui/material/Stack";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import MenuItem from "@mui/material/MenuItem";
import CloseIcon from "@mui/icons-material/Close";
import MicOffIcon from "@mui/icons-material/MicOff";
import MicIcon from "@mui/icons-material/Mic";
import VideocamIcon from "@mui/icons-material/Videocam";
import VideocamOffIcon from "@mui/icons-material/VideocamOff";
import { IconButton } from "@mui/material";

// store conference
import {
  setActiveSessionId,
  setActiveSessionName,
} from "../../store/reducers/conference.js";

const PrivateModal = ({ handleClose }) => {
  const micStatus = useSelector((state) => state.setting.micStatus);
  const cameraStatus = useSelector((state) => state.setting.cameraStatus);
  const selectedMic = useSelector((state) => state.setting.selectedMic);
  const selectedCam = useSelector((state) => state.setting.selectedCam);

  //redux에서 값 가져오기
  const profileImg = useSelector((state) => state.user.profileImg);
  const nickname = useSelector((state) => state.user.nickname) || "";
  const userId = useSelector((state) => state.user.id);

  const [localNickname, setLocalNickname] = useState(nickname);

  useEffect(() => {
    setLocalNickname(nickname);
  }, [nickname]);

  const dispatch = useDispatch();
  const [micList, setMicList] = useState([]);
  const [camList, setCamList] = useState([]);
  const OV = useRef(new OpenVidu()).current;

  const handleMicStatusToggle = () => {
    dispatch(toggleMicStatus());
  };

  const handleCameraStatusToggle = () => {
    dispatch(toggleCameraStatus());
  };

  useEffect(() => {
    dispatch(setActiveSessionId(userId));

    OV.getDevices()
      .then((devices) => {
        const filteredMicList = devices.filter(
          (device) => device.kind === "audioinput"
        );
        const filteredCamList = devices.filter(
          (device) => device.kind === "videoinput"
        );

        //가져온 값으로 list변경
        setMicList(filteredMicList);
        setCamList(filteredCamList);

        if (filteredMicList.length > 0) {
          dispatch(setSelectedMic(0));
        }
        if (filteredCamList.length > 0) {
          dispatch(setSelectedCam(0));
        }
      })
      .catch((error) => console.error(error));
  }, []);

  useEffect(() => {
  }, [micStatus]);

  useEffect(() => {
  }, [cameraStatus]);

  const handleSelectMicrophone = (event) => {
    const newMicId = parseInt(event.target.value, 10);

    dispatch(setSelectedMic(newMicId)); // 인덱스만 전달
  };

  const handleNicknameChange = (e) => {
    const newNickname = e.target.value;
    if (!newNickname) {
      alert("닉네임을 입력해 주세요!");
      return;
    }
    setLocalNickname(newNickname);
  };

  const navigate = useNavigate();

  const startPrivateMeeting = () => {
    dispatch(setParticipateName(localNickname));
    dispatch(setActiveSessionName(`${userId}의 개인룸`));

    navigate(`/meeting/${userId} `);
  };

  const handleSelectCamera = (event) => {
    const newCamId = parseInt(event.target.value, 10);
    dispatch(setSelectedCam(newCamId)); // 인덱스만 전달
  };

  return (
    <div className={styles["modal"]} onClick={handleClose}>
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <h3 className={styles["modalHeader"]} style={{ fontFamily: "Mogra" }}>
            start Meeting
          </h3>
          <IconButton onClick={handleClose}>
            <CloseIcon style={{ color: "white" }} />
          </IconButton>
        </div>

        <Box sx={{ flexGrow: 1 }}>
          <Grid container spacing={2} columns={14}>
            <Grid item xs={6} className={styles["leftBox"]}>
              <Box className={styles["profileImgBox"]}>
                <Avatar
                  alt="profileImg"
                  src={profileImg}
                  sx={{ width: 200, height: 200 }}
                  className={styles["profileImg"]}
                />
              </Box>
              <Box sx={{ flexGrow: 1 }}>
                <input
                  type="text"
                  defaultValue={nickname}
                  onChange={handleNicknameChange}
                  className={styles["nickname"]}
                />
              </Box>
            </Grid>

            <Grid item xs={8} className={styles["selectContainer"]}>
              <Stack spacing={10}>
                <Grid>
                  <FormControl className={styles["selectBox"]}>
                    <Select
                      value={selectedMic !== null ? selectedMic : ""}
                      onChange={handleSelectMicrophone}
                      displayEmpty
                      inputProps={{ "aria-label": "Without label" }}
                    >
                      {micList.map((mic, index) => (
                        <MenuItem key={index} value={index}>
                          {mic.label || `Microphone ${index + 1}`}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <IconButton
                    onClick={handleMicStatusToggle}
                    className={styles["btnBox"]}
                  >
                    {micStatus ? (
                      <MicIcon />
                    ) : (
                      <MicOffIcon style={{ color: "red" }} />
                    )}
                  </IconButton>
                </Grid>
                <Grid>
                  <FormControl className={styles["selectBox"]}>
                    <Select
                      value={selectedCam !== null ? selectedCam : ""}
                      onChange={handleSelectCamera}
                      displayEmpty
                      inputProps={{ "aria-label": "Without label" }}
                    >
                      {camList.map((cam, index) => (
                        <MenuItem key={index} value={index}>
                          {cam.label}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <IconButton
                    onClick={handleCameraStatusToggle}
                    className={styles["btnBox"]}
                  >
                    {cameraStatus ? (
                      <VideocamIcon />
                    ) : (
                      <VideocamOffIcon style={{ color: "red" }} />
                    )}
                  </IconButton>
                </Grid>
              </Stack>
            </Grid>
          </Grid>
        </Box>

        <Grid container justifyContent="flex-end">
          <Button
            variant="contained"
            className={styles["joinBtn"]}
            onClick={startPrivateMeeting}
          >
            Start Private Meeting
          </Button>

          {/* <Link to={`conferece/${sessionB}`}>Start Private Meeting</Link> */}
        </Grid>
      </div>
    </div>
  );
};

export default PrivateModal;
