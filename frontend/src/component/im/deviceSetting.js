import React from "react";
import styles from "../im/deviceSetting.module.css";
import { FormControl, InputLabel, Select, MenuItem, Box } from "@mui/material";

//mui
import Button from "@mui/material/Button";
import Grid from "@mui/material/Grid";

//icon
import MicIcon from "@mui/icons-material/Mic";
import VideocamIcon from "@mui/icons-material/Videocam";

const DeviceSetting = () => {
  const [selectedCamera, setSelectedCamera] = React.useState("");
  const [selectedMic, setSelectedMic] = React.useState("");
  const [selectedSpeaker, setSelectedSpeaker] = React.useState("");

  const cameras = ["cam1", "cam2"];
  const mics = ["mic1", "mic2"];
  const speakers = ["speaker1", "speaker2"];

  const handleCameraChange = (event) => {
    setSelectedCamera(event.target.value);
  };

  const handleMicChange = (event) => {
    setSelectedMic(event.target.value);
  };

  const handleSpeakerChange = (event) => {
    setSelectedSpeaker(event.target.value);
  };

  return (
    <div className={styles["container"]}>
      <Grid
        container
        direction="column"
        alignItems="center"
        justifyContent="center"
        spacing={2}
      >
        <Grid item xs={12} className={styles["deviceBox"]}>
          <h5>Video</h5>
          <FormControl className={styles["buttonContainer"]}>
            <InputLabel id="camera-label">
              <VideocamIcon /> Camera
            </InputLabel>
            <Select
              labelId="camera-label"
              id="camera-select"
              value={selectedCamera}
              onChange={handleCameraChange}
              className={styles["buttonContainer"]}
              style={{ backgroundColor: "white" }}
            >
              {cameras.map((camera, index) => (
                <MenuItem key={index} value={camera}>
                  <VideocamIcon /> {camera}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12} className={styles["deviceBox"]}>
          <h5>Audio</h5>
          <FormControl className={styles["buttonContainer"]}>
            <InputLabel id="mic-label">
              <MicIcon /> Microphone
            </InputLabel>
            <Select
              labelId="mic-label"
              id="mic-select"
              value={selectedMic}
              onChange={handleMicChange}
              className={styles["buttonContainer"]}
              style={{ backgroundColor: "white" }}
            >
              {mics.map((mic, index) => (
                <MenuItem key={index} value={mic}>
                  <MicIcon /> {mic}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12} className={styles["deviceBox"]}>
          <h5>Speaker</h5>
          <Grid container spacing={2} className={styles["buttonContainer"]}>
            <Grid item xs={12} sm={6}>
              <FormControl fullWidth>
                <InputLabel id="speaker-label">
                  <MicIcon /> Speaker
                </InputLabel>
                <Select
                  labelId="speaker-label"
                  id="speaker-select"
                  value={selectedSpeaker}
                  onChange={handleSpeakerChange}
                  className={styles["buttonContainer"]}
                  style={{ backgroundColor: "white" }}
                >
                  {speakers.map((speaker, index) => (
                    <MenuItem key={index} value={speaker}>
                      <MicIcon /> {speaker}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} sm={6}>
              <Button variant="contained" fullWidth className={styles.button}>
                Test
              </Button>
            </Grid>
          </Grid>
        </Grid>
      </Grid>
    </div>
  );
};

export default DeviceSetting;
