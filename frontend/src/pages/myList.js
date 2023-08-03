import React, { useState, useEffect } from "react";
import styles from "../pages/myList.module.css";
import Button from "@mui/material/Button"; // Button imported
import Divider from "@mui/material/Divider"; // Divider imported
import Grid from "@mui/material/Grid"; // Grid imported
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import defaultImg from "../assets/images/defualt_image.png";

const itemData = [
  {
    id: 1,
    img: defaultImg,
    text: "nickname1",
    url: "url1",
  },
  {
    id: 2,
    img: defaultImg,
    text: "nickname2",
    url: "url2",
  },
  {
    id: 3,
    img: defaultImg,
    text: "nickname3",
    url: "url3",
  },
];

const MyList = (props) => {
  const handleButtonClick = () => {
    // do something
  };

  return (
    <div className={styles["container"]}>
      <div className={styles["HeaderContainer"]}>
        <h2 className={styles["Header"]} style={{ fontFamily: "Mogra" }}>
          Your Meeting List
        </h2>
        <List className={styles["private-modal-list"]}>
          {itemData.map((item, index) => (
            <React.Fragment key={index}>
              <ListItem
                className={styles["private-modal-list-item"]}
                alignItems="flex-start"
              >
                <ListItemAvatar
                  className={styles["private-modal-list-item-avatar"]}
                >
                  <Avatar alt={item.text} src={item.img} />
                </ListItemAvatar>
                <ListItemText
                  className={styles["private-modal-list-item-text"]}
                  primary={item.text}
                  secondary={item.url}
                />
                <Button
                  variant="contained"
                  color="error"
                  className={styles["list-item-button"]}
                  onClick={handleButtonClick}
                >
                  비활성화
                </Button>
              </ListItem>

              <Divider
                className={styles["private-modal-divider"]}
                variant="inset"
                component="li"
              />
            </React.Fragment>
          ))}
        </List>
      </div>
    </div>
  );
};

export default MyList;
