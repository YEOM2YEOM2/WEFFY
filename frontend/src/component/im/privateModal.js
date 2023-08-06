import React, { useState } from "react";
import styles from "./privateModal.module.css";

//mui 외부 라이브러리
import Avatar from "@mui/material/Avatar";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Paper from "@mui/material/Paper";
import { styled } from "@mui/material/styles";

//기본 프로필 이미지
import defaultImg from "../../assets/images/defualt_image.png";

//icon
import CloseIcon from "@mui/icons-material/Close";
import MicOffIcon from "@mui/icons-material/MicOff";
import MicIcon from "@mui/icons-material/Mic";
import VideocamIcon from "@mui/icons-material/Videocam";
import VideocamOffIcon from "@mui/icons-material/VideocamOff";
import { IconButton, Typography } from "@mui/material";

const Item = styled(Paper)(({ theme }) => ({
  backgroundColor: theme.palette.mode === "dark" ? "#1A2027" : "#fff",
  ...theme.typography.body2,
  padding: theme.spacing(1),
  textAlign: "center",
  color: theme.palette.text.secondary,
}));

const PrivateModal = ({ handleClose }) => {
  const [micStatus, setMicStatus] = useState(false);
  const [cameraStatus, setCameraStatus] = useState(false);
  const [nickname, setNickname] = useState("default nickname");

  //openVidu

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

        {/* <div style={{ display: "flex", justifyContent: "center" }}>
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
          <Typography className={styles["nickname"]}>{nickname}</Typography>
        </div> */}
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
                  onChange={(e) => setNickname(e.target.value)}
                  className={styles["nickname"]}
                />
              </Box>
            </Grid>
            <Grid item xs={8}>
              <Item>xs=8</Item>
              <Item>xs=8</Item>
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
