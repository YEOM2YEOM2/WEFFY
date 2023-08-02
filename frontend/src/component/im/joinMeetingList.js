import React, { useState } from "react";
import styles from "./joinMeetingList.module.css";

//mui 외부 라이브러리
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import Divider from '@mui/material/Divider';
import ListItemText from '@mui/material/ListItemText';
import ListItemAvatar from '@mui/material/ListItemAvatar';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import Grid from "@mui/material/Grid";
import Button from "@mui/material/Button";
//이미지
import defaultImg from "../../assets/images/defualt_image.png";
import offMic from "../../assets/images/mic.png";
import offCamera from "../../assets/images/camara.png";

//icon
import CloseIcon from "@mui/icons-material/Close";
import MicOffIcon from "@mui/icons-material/MicOff";
import MicIcon from "@mui/icons-material/Mic";
import VideocamIcon from "@mui/icons-material/Videocam";
import VideocamOffIcon from "@mui/icons-material/VideocamOff";
import { IconButton } from "@mui/material";

const drawerWidth = 240;

const recentList = [
  { hostProfile: { defaultImg }, histname: "host", url: "url" },
  { hostProfile: { defaultImg }, histname: "host2", url: "url1" },
  { hostProfile: { defaultImg }, histname: "host3", url: "url2" },
  { hostProfile: { defaultImg }, histname: "host4", url: "url3" },

]

//text를 입력하고 enter을 누르면 해당 url로 이동하는 작업수행


const JoinMeetingList = ({ handleClose, sidebarOpen }) => {

  const [text, setText] = useState('');
  const handleEnter = (event) => {
    if (event.key === 'Enter') {
      window.location.href = text;
    }
  };

  //Join Button이 눌리면 해당 url로 이동
  const handleButtonClick = (url) => {
    window.location.href = url;
  };


  return (
    <div
      className={styles["modal"]}
      onClick={handleClose}
      style={{ left: `calc(50% + ${sidebarOpen ? drawerWidth / 2 : 0}px)` }}
    >
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <h3 className={styles["modalHeader"]} style={{ fontFamily: "Mogra" }}>
            Let's go!
          </h3>

          <IconButton>
            <CloseIcon onClick={handleClose} style={{ color: "white" }} />
          </IconButton>
        </div>

        <input
          type="text"
          className={styles["textArea"]}
          value={text} // 추가
          onChange={e => setText(e.target.value)} // 추가
          onKeyDown={handleEnter} // 추가
        />

        <List className={styles["private-modal-list"]}>
          {recentList.map((item, index) => (
            <React.Fragment key={index}>
              <ListItem className={styles["private-modal-list-item"]} alignItems="flex-start">

                <ListItemAvatar className={styles["private-modal-list-item-avatar"]}>
                  <Avatar alt={item.histname} src={item.hostProfile.defaultImg} />
                </ListItemAvatar>
                <ListItemText className={styles["private-modal-list-item-text"]}
                  primary={item.histname}
                  secondary={item.url}
                />
                <Button variant="contained" className={styles["list-item-button"]} onClick={handleButtonClick}>Join in</Button>
              </ListItem>

              <Divider className={styles["private-modal-divider"]} variant="inset" component="li" />
            </React.Fragment>
          ))}
        </List>


        <Grid container justifyContent="flex-end">
          <Button variant="contained">Start Private Meeting</Button>
        </Grid>
      </div>
    </div>
  );
};

export default JoinMeetingList;
