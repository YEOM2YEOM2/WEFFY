import React, { useState, useEffect } from "react";

//img
import defaultImg from "../assets/images/defualt_image.png";

//외부라이브러리
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";
import Button from "@mui/material/Button";

//style
import styles from "../pages/setting.module.css";

const Setting = (props) => {
  const [profileImage, setProfileImage] = useState(defaultImg);
  const [nickname, setNickname] = useState("Normal");

  const handleImageChange = (e) => {
    if (e.target.files[0]) {
      setProfileImage(URL.createObjectURL(e.target.files[0]));
    }
  };

  const handleNicknameChange = (e) => {
    setNickname(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Image URL:", profileImage);
    console.log("Nickname:", nickname);
    // Here you can write code to send updated profileImage and nickname to the server
  };

  const inputFile = React.useRef(null);

  const onImageIconClick = () => {
    // trigger the click event of the hidden input file
    inputFile.current.click();
  };

  return (
    <div className={styles["container"]}>
      <div className={styles["HeaderContainer"]}>
        <h2 className={styles["Header"]} style={{ fontFamily: "Mogra" }}>
          Edit Profile
        </h2>
        <div className={styles["imgContainer"]}>
          <img
            alt="profileImg"
            src={profileImage}
            className={styles["image"]}
          />
          <input
            type="file"
            accept="image/*"
            style={{ display: "none" }}
            ref={inputFile}
            onChange={handleImageChange}
          />
          <button className={styles["imageBtn"]} onClick={onImageIconClick}>
            <AddPhotoAlternateIcon
              className={styles["imageIcon"]}
              style={{ fontSize: 50 }}
            />
          </button>
        </div>
        <div className={styles["nickNameContainer"]}>
          <input
            id="nickNameInput"
            value={nickname}
            onChange={handleNicknameChange}
            className={styles["nickNameInput"]}
          />
        </div>
        <div className={styles["buttonContainer"]}>
          <Button
            variant="contained"
            className={styles["button"]}
            style={{ marginRight: "20px", backgroundColor: "red" }}
          >
            Cancel
          </Button>
          <Button
            variant="contained"
            onClick={handleSubmit}
            className={styles["button"]}
          >
            Change
          </Button>
        </div>
      </div>
    </div>
  );
};

export default Setting;
