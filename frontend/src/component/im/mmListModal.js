import React from "react";
import IconButton from "@mui/material/IconButton";
import CloseIcon from "@mui/icons-material/Close";
import styles from "./mmListModal.module.css";
import Grid from "@mui/material/Grid";
import Button from "@mui/material/Button";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import Typography from "@mui/material/Typography";

import NavigateNextIcon from "@mui/icons-material/NavigateNext";

import { useState, useEffect, useMemo } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import {
  toggleMicStatus,
  toggleCameraStatus,
} from "../../store/reducers/setting.js";

// store conference
import {
  setActiveSessionId,
  setActiveSessionName,
} from "../../store/reducers/conference";

const MMListModal = ({ handleClose }) => {
  const [groupData, setGroupData] = useState([]);

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const accessToken = useSelector((state) => state.user.accessToken);

  const inintMMList = () => {
    axios({
      method: "get",
      url: "http://i9d107.p.ssafy.io:8081/api/v1/mattermost",
      headers: {
        accept: "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    })
      .then((res) => {
        const tempGroupData = [];
        console.log("mattermost 채널 조회 성공");

        res.data.data.map((val) => {
          let temp = { name: "", channels: [] };
          temp.name = val.name;
          val.channels.map((value) => {
            let tmp = { name: "", identification: "", admin: false };
            tmp.name = value.name;
            tmp.identification = value.identification;
            tmp.admin = value.admin;
            temp.channels.push(tmp);
          });
          tempGroupData.push(temp);
        });
        setGroupData(tempGroupData);
      })
      .catch((err) => {
        console.log("mattermost 채널 조회 실패");
      });
  };

  useEffect(() => {
    toggleCameraStatus(false);
    toggleMicStatus(false);
    inintMMList();
  }, []);

  const [selectedGroup, setSelectedGroup] = React.useState("");
  const [selectedChannel, setSelectedChannel] = React.useState("");
  const [selectedChannelId, setSelectedChannelId] = React.useState("");

  //큰 그룹을 선택했을때 보여주기 위한 코드
  const handleGroupChange = (groupName) => {
    setSelectedGroup(groupName);
    setSelectedChannel([]);
  };

  const handleChannelChange = (channel) => {
    setSelectedChannel(channel.name);
    setSelectedChannelId(channel.identification);
  };

  const currentGroup = groupData.find((group) => group.name === selectedGroup);
  const currentChannel = useMemo(() => {
    return currentGroup ? currentGroup.channels.map((channel) => channel) : [];
  }, [currentGroup]);

  useEffect(() => {}, [selectedGroup, selectedChannel, selectedChannelId]);

  const startMeeting = async () => {
    try {
      console.log("selectedChannelId+ " + selectedChannelId);
      const formData = new FormData();
      formData.append("channelId", selectedChannelId);

      const response = await axios.post(
        "http://i9d107.p.ssafy.io:8081/api/v1/mattermost/header",
        formData,
        {
          headers: {
            accept: "application/json",
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
      console.log(response.data);
      const { status, data } = response;

      if (status === 200) {
        console.error("미팅 시작 성공");
      } else {
        console.error("카메라 연결 실패");
        throw new Error(data.data);
      }
    } catch (error) {
      console.error("미팅 시작 실패");
    }

    dispatch(setActiveSessionId(selectedChannelId));
    dispatch(
      setActiveSessionName(`${selectedGroup} ${selectedChannel}의 미팅룸`)
    );

    navigate(`/meeting/${selectedChannelId}`);
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
        <Grid container justifyContent="space-between" spacing={2}>
          <Grid item xs={6}>
            <Typography
              variant="subtitle1"
              className={styles["selectField"]}
              style={{ fontFamily: "GmarketSans" }}
            >
              {selectedGroup || "MatterMost Group"}
            </Typography>
            <List className={styles["textFieldInput"]}>
              <List className={styles["textFieldInput"]}>
                {groupData.length > 0 &&
                  groupData.map((group, idx) => (
                    <ListItem
                      key={idx}
                      button
                      onClick={() => handleGroupChange(group.name)}
                    >
                      <ListItemText
                        primary={
                          <Typography sx={{ fontFamily: "GmarketSans" }}>
                            {group.name}
                          </Typography>
                        }
                      />
                      <NavigateNextIcon
                        style={{ color: "red", margin: "0 10px" }}
                      />
                    </ListItem>
                  ))}
              </List>
            </List>
          </Grid>
          <Grid item xs={6}>
            <Typography
              variant="subtitle1"
              className={styles["selectField"]}
              style={{ fontFamily: "GmarketSans" }}
            >
              {selectedChannel ? selectedChannel : "MatterMost Channel"}
            </Typography>
            <List className={styles["textFieldInput"]}>
              {currentChannel.map((channel) => (
                <ListItem
                  key={channel.name}
                  button
                  onClick={() => handleChannelChange(channel)}
                >
                  <ListItemText
                    primary={
                      <Typography sx={{ fontFamily: "GmarketSans" }}>
                        {channel.name}
                      </Typography>
                    }
                  />
                </ListItem>
              ))}
            </List>
          </Grid>
        </Grid>
        <Grid container justifyContent="flex-end">
          <Button variant="contained" onClick={startMeeting}>
            Start Private Meeting
          </Button>
        </Grid>
      </div>
    </div>
  );
};

export default MMListModal;
