import React, { useState, useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";

import { setNickname, setProfileImg } from "../store/reducers/user";

//img
import defaultImg from "../assets/images/defualt_image.png";

//외부라이브러리
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";
import Button from "@mui/material/Button";

//style
import styles from "../pages/setting.module.css";

const Setting = (props) => {
  const dispatch = useDispatch();

  const profileImg =
    useSelector((state) => state.user.profileImg) || defaultImg;
  const nickname = useSelector((state) => state.user.nickname) || "";

  const [newProfileImage, setNewProfileImage] = useState(profileImg);
  const [newNickName, setNewNickname] = useState(nickname);

  const handleImageChange = (e) => {
    if (e.target.files[0]) {
      setNewProfileImage(URL.createObjectURL(e.target.files[0]));
    }
  };

  //닉네임 변경 로직 => redux에는 반영X
  const handleNicknameChange = (e) => {
    setNewNickname(e.target.value);
  };

  const accessToken = useSelector((state) => state.user.accessToken);
  //reduxt값 수정 + db값 수정
  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    const blob = new Blob([JSON.stringify(newProfileImage)], {
      type: "application/json",
    });

    formData.append("profileImg", blob);
    formData.append("nickName", newNickName);

    axios({
      method: "patch",
      url: "http://i9d107.p.ssafy.io:8081/api/v1/users/me",
      headers: {
        accept: "application/json",
        "Content-Type": "multipart/form-data",
        Authorization: `Bearer ${accessToken}`,
        "X-CSRF-TOKEN": "c138db3c-b48a-418f-b12a-28ead92c9c5b",
      },
      data: formData,
    })
      .then(() => {
        dispatch(setProfileImg(newProfileImage));
        dispatch(setNickname(newNickName));
      })
      .catch(() => {
        console.log("프로필 불러오기");
      });
  };

  const inputFile = React.useRef(null);

  const onImageIconClick = () => {
    inputFile.current.click();
  };

  const handleCancel = () => {
    setNewProfileImage(profileImg);
    setNewNickname(nickname);
  };

  useEffect(() => {
    setNewProfileImage(profileImg);
    setNewNickname(nickname);
  }, [profileImg, nickname]);

  return (
    <div className={styles["container"]}>
      <div className={styles["HeaderContainer"]}>
        <h2 className={styles["Header"]} style={{ fontFamily: "Mogra" }}>
          Edit Profile
        </h2>
        <div className={styles["imgContainer"]}>
          <img
            alt="profileImg"
            src={newProfileImage}
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
            value={newNickName}
            onChange={handleNicknameChange}
            className={styles["nickNameInput"]}
          />
        </div>
        <div className={styles["buttonContainer"]}>
          <Button
            variant="contained"
            className={styles["button"]}
            style={{ marginRight: "20px", backgroundColor: "red" }}
            onClick={handleCancel}
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
