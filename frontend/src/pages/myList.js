import React, { useState, useEffect } from "react";
import styles from "../pages/myList.module.css";

//mui
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import Box from "@mui/material/Box";

//img
import defaultImg from "../assets/images/defualt_image.png";

const itemData = [
  {
    id: 1,
    img: { defaultImg },
    text: "nickname1",
    url: "url1",
  },
  {
    id: 2,
    img: { defaultImg },
    text: "nickname2",
    url: "url2",
  },
  {
    id: 3,
    img: { defaultImg },
    text: "nickname3",
    url: "url3",
  },
];

const MyList = (props) => {
  // 모달 상태 선언

  return (
    <div className={styles["container"]}>
      <h2 className={styles["header"]}>My Meeting List</h2>
      <List className={styles["list"]}>
        {itemData.map((item) => (
          <ListItem key={item.id} className={styles["listItem"]}>
            <ListItemAvatar>
              <Avatar src={item.img} />
            </ListItemAvatar>
            <ListItemText primary={item.text} className={styles["itemText"]} />
            <Box className={styles["buttonBox"]}>
              <Button
                variant="contained"
                onClick={() => window.open(item.url, "_blank")}
                color="error"
                style={{ marginRight: "10px" }}
                className={styles["deleteBtn"]}
              >
                Deleted
              </Button>
              <Button
                variant="contained"
                onClick={() => window.open(item.url, "_blank")}
              >
                Join
              </Button>
            </Box>
          </ListItem>
        ))}
      </List>
    </div>
  );
};

export default MyList;
