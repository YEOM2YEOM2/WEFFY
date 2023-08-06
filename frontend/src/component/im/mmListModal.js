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

//dump 값
const Group = [
  { group: "구미 5반", names: [1, 2, 3] },
  { group: "구미 1반", names: [4, 5, 6] },
  { group: "구미 2반", names: [7, 8, 9] },
  { group: "구미 4반", names: [10, 11, 12] },
];

const drawerWidth = 240;

const MMListModal = ({ handleClose, handleStartMeeting, sidebarOpen }) => {
  const [selectedGroup, setSelectedGroup] = React.useState("");
  const [selectedNames, setSelectedNames] = React.useState([]);

  const handleGroupChange = (group) => {
    setSelectedGroup(group);
    setSelectedNames([]);
  };

  const handleNamesChange = (name) => {
    setSelectedNames([name]);
  };

  const currentGroup = Group.find((group) => group.group === selectedGroup);
  const currentNames = currentGroup ? currentGroup.names : [];

  return (
    <div
      className={styles["modal"]}
      onClick={handleClose}
      //   style={{ left: `calc(50% + ${sidebarOpen ? drawerWidth / 2 : 0}px)` }}
    >
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
            <List
              className={styles["textFieldInput"]}
              // style={{ fontFamily: "GmarketSans" }}
            >
              {Group.map(({ group }) => (
                <ListItem
                  key={group}
                  button
                  onClick={() => handleGroupChange(group)}
                >
                  <ListItemText
                    primary={
                      <Typography sx={{ fontFamily: "GmarketSans" }}>
                        {group}
                      </Typography>
                    }
                  />
                  <NavigateNextIcon
                    style={{ color: "red", margin: "0 10px" }}
                  />
                </ListItem>
              ))}
            </List>
          </Grid>
          <Grid item xs={6}>
            <Typography
              variant="subtitle1"
              className={styles["selectField"]}
              style={{ fontFamily: "GmarketSans" }}
            >
              {selectedNames.length > 0
                ? selectedNames.join(", ")
                : "MatterMost Channel"}
            </Typography>
            <List className={styles["textFieldInput"]}>
              {currentNames.map((name) => (
                <ListItem
                  key={name}
                  button
                  onClick={() => handleNamesChange(name)}
                >
                  <ListItemText
                    primary={
                      <Typography sx={{ fontFamily: "GmarketSans" }}>
                        {name}
                      </Typography>
                    }
                  />
                </ListItem>
              ))}
            </List>
          </Grid>
        </Grid>
        <Grid container justifyContent="flex-end">
          <Button variant="contained" onClick={handleStartMeeting}>
            Start Private Meeting
          </Button>
        </Grid>
      </div>
    </div>
  );
};

export default MMListModal;
