import React, { useEffect, useState, useRef } from "react";
import styles from "./privateModal.module.css";
import { useDispatch, useSelector } from "react-redux";
import {
  setSession,
  setNickname,
  setMicrophones,
  setCameras,
  selectMicrophone,
  selectCamera,
  toggleMicStatus,
  toggleCameraStatus,
} from "../../store/setting.js";

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

const PrivateModal = ({ handleClose }) => {
  const dispatch = useDispatch();
  const [nickname, setUserNickname] = useState("default nickname");
  const [mics, setMics] = useState([]);
  const [cams, setCams] = useState([]);
  const OV = useRef(new OpenVidu()).current;
  const micStatus = useSelector((state) => state.micStatus);
  const cameraStatus = useSelector((state) => state.cameraStatus);

  const handleMicStatusToggle = () => {
    dispatch(toggleMicStatus());
  };

  const handleCameraStatusToggle = () => {
    dispatch(toggleCameraStatus());
  };

  useEffect(() => {
    dispatch(setSession("SessionA"));

    OV.getDevices()
      .then((devices) => {
        console.log(devices);
        const mics = devices.filter((device) => device.kind === "audioinput");
        const cams = devices.filter((device) => device.kind === "videoinput");
        dispatch(setMicrophones(mics));
        dispatch(setCameras(cams));
        if (mics.length > 0) {
          setMics(mics);
          dispatch(selectMicrophone(mics[0]));
        }
        if (cams.length > 0) {
          setCameras(cams);
          dispatch(selectCamera(cams[0]));
        }
      })
      .catch((error) => console.error(error));
  }, []);

  const handleNicknameChange = (e) => {
    const newNickname = e.target.value;
    if (!newNickname) {
      console.error("닉네임을 비워둘 수는 없습니다.");
      return;
    }
    setUserNickname(newNickname);
    dispatch(setNickname(newNickname));
  };

  const handleSelectMicrophone = (event) => {
    const selectedMic = mics.find((mic) => mic.deviceId === event.target.value);
    dispatch(selectMicrophone(selectedMic));
  };

  const handleSelectCamera = (event) => {
    const selectedCamera = cams.find(
      (cam) => cam.deviceId === event.target.value
    );
    dispatch(selectCamera(selectedCamera));
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
                  src={defaultImg}
                  sx={{ width: 200, height: 200 }}
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
                      value={mics.length > 0 ? mics[0].deviceId : ""}
                      onChange={handleSelectMicrophone}
                      displayEmpty
                      inputProps={{ "aria-label": "Without label" }}
                    >
                      {mics.map((mic, index) => (
                        <MenuItem key={index} value={mic.deviceId}>
                          {mic.label || `Microphone ${index + 1}`}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <IconButton onClick={handleMicStatusToggle}>
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
                      value={cams.length > 0 ? cams[0].deviceId : ""}
                      onChange={handleSelectCamera}
                      displayEmpty
                      inputProps={{ "aria-label": "Without label" }}
                    >
                      {cams.map((cam, index) => (
                        <MenuItem key={index} value={cam.deviceId}>
                          {cam.label}
                        </MenuItem>
                      ))}
                    </Select>
                  </FormControl>
                  <IconButton onClick={handleCameraStatusToggle}>
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

export default PrivateModal;
