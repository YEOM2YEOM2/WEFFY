import React, { useState, useEffect } from "react";
import styles from "./joinMeetingList.module.css";
import axios from "axios";
import { useSelector } from "react-redux";

//mui 외부 라이브러리
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Divider from "@mui/material/Divider";
import ListItemText from "@mui/material/ListItemText";
import Grid from "@mui/material/Grid";
import Button from "@mui/material/Button";
import { styled } from "@mui/material/styles";

//icon
import CloseIcon from "@mui/icons-material/Close";
import { IconButton } from "@mui/material";

const JoinMeetingList = ({ handleClose }) => {
  const [text, setText] = useState("");
  const [recentList, setRecentList] = useState([]);
  const identification = useSelector((state) => state.user.identification);

  const handleEnter = (event) => {
    if (event.key === "Enter") {
      window.location.href = text;
    }
  };
  const CustomListItemText = styled(ListItemText)({
    "& .MuiListItemText-primary": {
      fontFamily: "GmarketSans",
    },
  });

  const recentListHandler = () => {
    axios({
      method: "get",
      url: `http://localhost:8082/conferences/visited?identification=${identification}`,
    })
      .then((res) => {
        const tempMyList = [];
        console.log("최근 미팅 목록 조회 성공");

        res.data.data.map((val) => {
          let temp = { url: "", title: "" };
          temp.url = val.conferenceUrl;
          temp.title = val.title;
          tempMyList.push(temp);
        });

        setRecentList(tempMyList);
      })
      .catch((err) => {
        console.log("최근 미팅 목록 조회 실패");
      });
  };

  useEffect(() => {
    recentListHandler();
  }, []);

  const handleButtonClick = (url) => {
    window.location.href = url;
  };

  return (
    <div className={styles["modal"]} onClick={handleClose}>
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
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={handleEnter}
          placeholder="참여할 화상 회의 링크를 입력해주세요."
        />

        <div className={styles["mettingList"]}>
          <List>
            {recentList.map((item, index) => (
              <React.Fragment key={index}>
                <ListItem className={styles["meetingItem"]}>
                  <CustomListItemText
                    className={styles["item-text"]}
                    primary={item.title}
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
        </div>

        <Grid container justifyContent="flex-end"></Grid>
      </div>
    </div>
  );
};

export default JoinMeetingList;
