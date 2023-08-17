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


  // const handleImageChange = (e) => {
  //   const file = e.target.files[0];
  //   if (file) {
  //     setNewProfileImage(URL.createObjectURL(file));
  //     setSelectedFile(file); // Store the actual file
  //   }
  // };

  const accessToken = useSelector((state) => state.user.accessToken);
  const csrfToken = useSelector((state) => state.user.csrfToken);
  //reduxt값 수정 + db값 수정
  const handleSubmit = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    const blob = new Blob([JSON.stringify(newProfileImage)], {
      type: 'application/json'
    })

    formData.append("profileImg", blob);
    formData.append("nickName", newNickName);


    for (var pair of formData.entries()) {
        console.log(pair[0]+ ', ' + pair[1]); 
    }

    //서버와 통신하기
    axios({
      method: "patch",
      url: "http://i9d107.p.ssafy.io:8081/api/v1/users/me",
      headers: {
        accept: "application/json",
        "Content-Type": "multipart/form-data",
        Authorization: `Bearer ${accessToken}`,
        "X-CSRF-TOKEN": 'c138db3c-b48a-418f-b12a-28ead92c9c5b',
      },
      data: formData,
    })
      .then((res) => {
        console.log(res.data);
        dispatch(setProfileImg(newProfileImage));
        dispatch(setNickname(newNickName));
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const inputFile = React.useRef(null);

  const onImageIconClick = () => {
    // trigger the click event of the hidden input file
    inputFile.current.click();
  };

  const handleCancel = () => {
    setNewProfileImage(profileImg);
    setNewNickname(nickname);
  };

  //변경이 되면 같이 뿅 바뀐다
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
