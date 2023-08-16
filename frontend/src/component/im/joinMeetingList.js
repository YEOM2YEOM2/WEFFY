import React, { useState, useEffect, useRef } from "react";
import styles from "./joinMeetingList.module.css";

//mui 외부 라이브러리
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Grid from "@mui/material/Grid";
import Button from "@mui/material/Button";
//이미지
import defaultImg from "../../assets/images/defualt_image.png";

//icon
import CloseIcon from "@mui/icons-material/Close";
import { IconButton } from "@mui/material";

const recentList = [
  { hostProfile: { defaultImg }, histname: "host", url: "url" },
  { hostProfile: { defaultImg }, histname: "host2", url: "url1" },
  { hostProfile: { defaultImg }, histname: "host3", url: "url2" },
  { hostProfile: { defaultImg }, histname: "host4", url: "url3" },
  { hostProfile: { defaultImg }, histname: "host5", url: "ur5" },
  { hostProfile: { defaultImg }, histname: "host6", url: "ur4" },
  { hostProfile: { defaultImg }, histname: "host7", url: "ur2" },
];

//text를 입력하고 enter을 누르면 해당 url로 이동하는 작업수행

const JoinMeetingList = ({ handleClose, sidebarOpen }) => {
  const [text, setText] = useState("");

  const handleEnter = (event) => {
    if (event.key === "Enter") {
      window.location.href = text;
    }
  };

  //Join Button이 눌리면 해당 url로 이동
  const handleButtonClick = (url) => {
    window.location.href = url;
  };

  //pagination 을 하기 위한 것들

  return (
    <div
      className={styles["modal"]}
      onClick={handleClose}
      // style={{ left: `calc(50% + ${sidebarOpen ? drawerWidth / 2 : 0}px)` }}
    >
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <h3 className={styles["modalHeader"]} style={{ fontFamily: "Mogra" }}>
            Let's go!
          </h3>

          <IconButton onClick={handleClose}>
            <CloseIcon style={{ color: "white" }} />
          </IconButton>
        </div>

        <input
          type="text"
          className={styles["textArea"]}
          value={text} // 추가
          onChange={(e) => setText(e.target.value)} // 추가
          onKeyDown={handleEnter} // 추가
        />

        {/* <div className={styles["mettingList"]}>
          <List>
            {recentList.map((item, index) => (
              <React.Fragment key={index}>
                <ListItem
                  className={styles["meetingItem"]}
                  alignItems="flex-start"
                >
                  <ListItemAvatar
                    className={styles["private-modal-list-item-avatar"]}
                  >
                    <Avatar
                      alt={item.histname}
                      src={item.hostProfile.defaultImg}
                    />
                  </ListItemAvatar>
                  <ListItemText
                    className={styles["item-text"]}
                    primary={item.histname}
                    secondary={item.url}
                  />
                  <Button
                    variant="contained"
                    className={styles["list-item-button"]}
                    onClick={() => handleButtonClick(item.url)}
                  >
                    Join in
                  </Button>
                </ListItem>

                <Divider
                  className={styles["item-divider"]}
                  variant="inset"
                  component="li"
                />
              </React.Fragment>
            ))}
          </List>
        </div> */}

        <Grid container justifyContent="flex-end">
          {/* <Button variant="contained">Start Private Meeting</Button> */}
        </Grid>
      </div>
    </div>
  );
};

export default JoinMeetingList;
