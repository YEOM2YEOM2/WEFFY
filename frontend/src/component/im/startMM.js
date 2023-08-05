import React, { useState } from "react";
import styles from "./startMM.module.css";

//mui 외부 라이브러리
import Avatar from "@mui/material/Avatar";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";

//이미지
import defaultImg from "../../assets/images/defualt_image.png";
//icon
import CloseIcon from "@mui/icons-material/Close";
import MicOffIcon from "@mui/icons-material/MicOff";
import MicIcon from "@mui/icons-material/Mic";
import VideocamIcon from "@mui/icons-material/Videocam";
import VideocamOffIcon from "@mui/icons-material/VideocamOff";
import { IconButton } from "@mui/material";

const StartMM = ({ handleClose, sidebarOpen }) => {
  const [micStatus, setMicStatus] = useState(false);
  const [cameraStatus, setCameraStatus] = useState(false);
  const [nickname, setNickname] = useState("default nickname");

  return (
    <div className={styles["modal"]} onClick={handleClose}>
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <h3 className={styles["modalHeader"]} style={{ fontFamily: "Mogra" }}>
            start MM Meeting
          </h3>
          <IconButton>
            <CloseIcon onClick={handleClose} style={{ color: "white" }} />
          </IconButton>
        </div>

        <div style={{ display: "flex", justifyContent: "center" }}>
          <Avatar
            alt="profileImg"
            src={defaultImg}
            sx={{ width: 200, height: 200 }}
          />
        </div>
        <div className={styles["settingContainer"]}>
          <div style={{ display: "flex", justifyContent: "flex-start" }}>
            <IconButton onClick={() => setMicStatus(!micStatus)}>
              <Box borderRadius={3} className={styles["btnBox"]}>
                {micStatus === true ? (
                  <MicIcon style={{ color: "white", fontSize: 40 }} />
                ) : (
                  <MicOffIcon style={{ color: "#FF0E2B", fontSize: 40 }} />
                )}
              </Box>
            </IconButton>

            <IconButton onClick={() => setCameraStatus(!cameraStatus)}>
              <Box borderRadius={3} className={styles["btnBox"]}>
                {cameraStatus === true ? (
                  <VideocamIcon style={{ color: "white", fontSize: 40 }} />
                ) : (
                  <VideocamOffIcon style={{ color: "#FF0E2B", fontSize: 40 }} />
                )}
              </Box>
            </IconButton>
          </div>
          <input
            type="text"
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
            className={styles["textArea"]}
          />
        </div>
        <Grid container justifyContent="flex-end">
          <Button variant="contained">Start MM Meeting</Button>
        </Grid>
      </div>
    </div>
  );
};

export default StartMM;
