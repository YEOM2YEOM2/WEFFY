import React from "react";
import Button from "react-bootstrap/Button";
import styles from "./mmModal.module.css";
import Grid from '@mui/material/Grid';

//이미지
import defaultImg from "../../assets/images/defualt_image.png";
import offMic from "../../assets/images/mic.png";
import offCamera from "../../assets/images/camara.png"

const MmModal = ({ handleClose }) => {
  return (
    <div className={styles["modal"]} onClick={handleClose}>
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <h2 className={styles["modalHeader"]}>Start Prive Meeting</h2>
        <div className={styles["profileContainer"]}>
          <div className={styles["profile"]}>
            <img src={defaultImg} alt="profileImg" className={styles["profileImg"]} />
          </div>
          <Grid container justifyContent="flex-start" className={styles["controlContainer"]}>
            <div className={styles["iconBox"]}>
              <img src={offMic} alt="mic" className={styles["mic"]} />
            </div>
            <div className={styles["iconBox"]}>
              <img src={offCamera} alt="camera" className={styles["camera"]} />
            </div>
          </Grid>

          <input type="text" className={styles["textInput"]} />
        </div>
        <Grid container justifyContent="flex-end">
          <Grid item>
            <Button variant="contained" onClick={handleClose} className={styles["closeBtn"]}>Close</Button>
          </Grid>
          <Grid item>
            <Button variant="contained" className={styles["startBtn"]}>New Meeting with MM</Button>
          </Grid>
        </Grid>
      </div>
    </div>
  );
};

export default MmModal;
