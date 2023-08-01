import React from "react";
import styles from "./participateModal.module.css";

//miu
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemText from "@mui/material/ListItemText";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";

//이미지
import defaultImg from "../../assets/images/defualt_image.png";

//불러서 받을 데이타ㅓ
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

const ParticipateModal = ({ handleClose }) => {
  return (
    <div className={styles["modal"]} onClick={handleClose}>
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <h2 className={styles["modalHeader"]}>미팅 참여하기</h2>
        <input type="text" className={styles["textInput"]} />
        <List
          sx={{ width: "100%", maxWidth: 360 }}
          className={styles["listItem"]}
        >
          {itemData.map((item) => (
            <ListItem
              key={item.id}
              sx={{
                my: 2,
                border: "1px solid #ddd",
                borderRadius: 2,
                backgroundColor: "#f5f5f5",
                "&:hover": {
                  backgroundColor: "#e0e0e0",
                },
              }}
            >
              <ListItemAvatar>
                <Avatar src={item.img} />
              </ListItemAvatar>
              <ListItemText primary={item.text} />
              <Button
                variant="outlined"
                onClick={() => window.open(item.url, "_blank")}
              >
                Join
              </Button>
            </ListItem>
          ))}
        </List>
      </div>
    </div>
  );
};

export default ParticipateModal;
