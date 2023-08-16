import React, { useState, useEffect } from "react";
import styles from "../pages/myList.module.css";
import { useSelector } from "react-redux";
import axios from "axios";

//mui component
import Button from "@mui/material/Button"; // Button imported
import Divider from "@mui/material/Divider"; // Divider imported
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import ListItemAvatar from "@mui/material/ListItemAvatar";
import Avatar from "@mui/material/Avatar";
import Typography from "@mui/material/Typography";

// const itemData = [
//   {
//     text: "nickname1",
//     url: "url1",
//   },
//   {
//     text: "nickname2",
//     url: "url2",
//   },
//   {
//     text: "nickname3",
//     url: "url3",
//   },
//   {
//     text: "nickname3",
//     url: "url3",
//   },
//   {
//     text: "nickname3",
//     url: "url3",
//   },
//   {
//     text: "nickname3",
//     url: "url3",
//   },
//   {
//     text: "nickname3",
//     url: "url3",
//   },
//   {
//     text: "nickname3",
//     url: "url3",
//   },
//   {
//     text: "nickname3",
//     url: "url3",
//   },
// ];

const MyList = (props) => {
  const [itemData, setItemData] = useState([]);
  const identification = useSelector((state) => state.user.identification);
  console.log("123" + identification);
  useEffect(() => {
    const fetchConferenceList = async () => {
      try {
        console.log("identification+" + identification);
        const response = await axios.get(
          `http://localhost:8082/conferences?identification=${identification}`
        );
        console.log(response.data);
        const formattedData = response.data.data.map((item) => ({
          text: item.title,
          url: item.conferenceUrl, // 예제에 따라 실제 필드를 사용해 주세요.
        }));
        console.log(formattedData);

        setItemData(formattedData);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };
    fetchConferenceList();
  }, [identification]);

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
                  <Typography className={styles["listItemText"]}>
                    {item.text}
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
