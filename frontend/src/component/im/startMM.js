import React, { useEffect, useState, useRef } from "react";
import styles from "./privateModal.module.css";
import { useDispatch, useSelector } from "react-redux";
import {
  setParticipateName,
  setSelectedCam,
  setSelectedMic,
  toggleMicStatus,
  toggleCameraStatus,
} from "../../store/reducers/setting.js";

import { setActiveSessionId } from "../../store/reducers/conference";

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

import defaultImg from "../../assets/images/defualt_image.png";

const StartMM = ({ handleClose, groupName, channelName, channelId }) => {
  const dispatch = useDispatch();
  const [nickname, setUserNickname] = useState("default nickname");
  const [micList, setMicList] = useState([]);
  const [camList, setCamList] = useState([]);
  const OV = useRef(new OpenVidu()).current;
  const micStatus = useSelector((state) => state.micStatus);
  const cameraStatus = useSelector((state) => state.cameraStatus);

  const selectedMic = useSelector((state) => state.selectedMic);
  const selectedCam = useSelector((state) => state.selectedCam);

  useEffect(() => {
    console.log(groupName);
    console.log(channelName);
    console.log(channelId);
  }, [groupName, channelName, channelId]);

  const handleMicStatusToggle = () => {
    dispatch(toggleMicStatus());
  };

  const handleCameraStatusToggle = () => {
    dispatch(toggleCameraStatus());
  };

  useEffect(() => {
    dispatch(setActiveSessionId("SessionA"));

    OV.getDevices()
      .then((devices) => {
        console.log(devices);
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

  const handleNicknameChange = (e) => {
    const newNickname = e.target.value;
    if (!newNickname) {
      alert("Nickname can't be empty!");
      return;
    }
    setUserNickname(newNickname);
    dispatch(setParticipateName(newNickname));
  };

  const handleSelectMicrophone = (event) => {
    const newMicId = event.target.value;
    dispatch(setSelectedMic(newMicId)); // 인덱스만 전달
    console.log(`mic Id = ${newMicId}`);
  };

  const handleSelectCamera = (event) => {
    const newCamId = event.target.value;
    dispatch(setSelectedCam(newCamId)); // 인덱스만 전달
    console.log(`cam Id = ${newCamId}`);
  };

  return (
    <div className={styles["modal"]} onClick={handleClose}>
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <h3 className={styles["modalHeader"]} style={{ fontFamily: "Mogra" }}>
            start Meeting with MM
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
                  src={defaultImg}
                  sx={{ width: 200, height: 200 }}
                  className={styles["profileImg"]}
                />
              </Box>
              <Box sx={{ flexGrow: 1 }}>
                <input
                  type="text"
                  value={nickname}
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
          <Button variant="contained" className={styles["joinBtn"]}>
            Start Private Meeting
          </Button>
        </Grid>
      </div>
    </div>
  );
};

export default StartMM;
