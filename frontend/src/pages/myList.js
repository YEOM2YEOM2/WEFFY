import React, { useState, useEffect } from "react";
import styles from "../pages/myList.module.css";
import { useSelector } from "react-redux";
import axios from "axios";

//mui component
import Button from "@mui/material/Button"; // Button imported
import Divider from "@mui/material/Divider"; // Divider imported
import List from "@mui/material/List";
import ListItem from "@mui/material/ListItem";
import Typography from "@mui/material/Typography";

const MyList = (props) => {
  const [itemData, setItemData] = useState([]);
  const identification = useSelector((state) => state.user.identification);
  console.log("123" + identification);
  const fetchConferenceList = async () => {
    try {
      console.log("identification+" + identification);
      const response = await axios.get(
        `http://localhost:8082/conferences?identification=${identification}`
      );
      console.log(response.data);
      const formattedData = response.data.data.map((item) => ({
        text: item.title,
        url: item.conferenceUrl,
      }));
      console.log(formattedData);

      setItemData(formattedData);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  useEffect(() => {
    fetchConferenceList();
  }, []);

  const handleButtonClick = async (url) => {
    console.log(url);
    let lastUrl = url.split("/").pop();

    console.log(lastUrl);
    // do something
    try {
      const response = await axios.patch(
        `http://localhost:8082/conferences/${lastUrl}/status`,
        null,
        {
          params: {
            identification: identification,
          },
        }
      );
      if (response.status === 200) {
        console.log("비활성화 완료");
        fetchConferenceList();
      } else {
        console.error("Error:" + response.data);
      }
    } catch (error) {
      console.error("Error" + error);
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
                    {/* New div */}
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
