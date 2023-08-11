import React, { useState, useEffect } from "react";
import styles from "../pages/myList.module.css";

//mui component
import Button from "@mui/material/Button"; // Button imported
import Divider from "@mui/material/Divider"; // Divider imported
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";

//image
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
  {
    id: 3,
    img: defaultImg,
    text: "nickname3",
    url: "url3",
  },
  {
    id: 3,
    img: defaultImg,
    text: "nickname3",
    url: "url3",
  },
  {
    id: 3,
    img: defaultImg,
    text: "nickname3",
    url: "url3",
  },
  {
    id: 3,
    img: defaultImg,
    text: "nickname3",
    url: "url3",
  },
  {
    id: 3,
    img: defaultImg,
    text: "nickname3",
    url: "url3",
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
        <div className={styles["meeting-list"]}>
          <List>
            {itemData.map((item, index) => (
              <React.Fragment key={index}>
                <ListItem
                  className={styles["listItem"]}
                  alignItems="flex-start"
                >
                  <ListItemAvatar
                    className={styles["private-modal-list-item-avatar"]}
                  >
                    <Avatar alt={item.text} src={item.img} />
                  </ListItemAvatar>
                  <Typography className={styles["listItemText"]}>
                    {" "}
                    {/* Updated class */}
                    {item.text}
                    <br />
                    {item.url}
                  </Typography>
                  <div className={styles["buttonContainer"]}>
                    {" "}
                    {/* New div */}
                    <Button
                      variant="contained"
                      color="error"
                      className={styles["list-item-button"]}
                      onClick={handleButtonClick}
                    >
                      비활성화
                    </Button>
                  </div>
                </ListItem>

                <Divider variant="inset" component="li" />
              </React.Fragment>
            ))}
          </List>
        </div>
      </div>
    </div>
  );
};

export default MyList;
