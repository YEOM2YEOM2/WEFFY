import React, { useState, useEffect } from "react";
import styles from "../pages/myList.module.css";
import { useSelector } from "react-redux";
import axios from "axios";

//mui component
import Button from "@mui/material/Button";
import Divider from "@mui/material/Divider";
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Typography from "@mui/material/Typography";

const MyList = (props) => {
  const [itemData, setItemData] = useState([]);
  const identification = useSelector((state) => state.user.identification);
  const fetchConferenceList = async () => {
    try {
      const response = await axios.get(
        `http://i9d107.p.ssafy.io:8082/conferences?identification=${identification}`
      );
      const formattedData = response.data.data.map((item) => ({
        text: item.title,
        url: item.conferenceUrl,
      }));

      setItemData(formattedData);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchConferenceList();
  }, []);

  const handleButtonClick = async (url) => {
    let lastUrl = url.split("/").pop();

    try {
      const response = await axios.patch(
        `http://i9d107.p.ssafy.io:8082/conferences/${lastUrl}/status`,
        null,
        {
          params: {
            identification: identification,
          },
        }
      );
      if (response.status === 200) {
        fetchConferenceList();
      } else {
        console.error("Error");
      }
    } catch (error) {
      console.error("Error");
    }
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
                    <Button
                      variant="contained"
                      color="error"
                      className={styles["list-item-button"]}
                      onClick={() => handleButtonClick(item.url)}
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
