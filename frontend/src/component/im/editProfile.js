import styles from "./editProfile.module.css";

//img
import defaultImg from "../../assets/images/defualt_image.png";

//외부라이브러리
import Divider from "@mui/material/Divider";
import TextField from "@mui/material/TextField";
import Button from "@mui/material/Button";

const EditProfile = (props) => {
  return (
    <div children={styles["container"]}>
      <div className={styles["title"]}>Edit Profile</div>
      <Divider className={styles["bar"]} style={{ backgroundColor: "white" }} />
      <div className={styles["imgContainer"]}>
        <img alt="profileImg" src={defaultImg} className={styles["image"]} />
      </div>
      <div className={styles["nickNameContainer"]}>
        <TextField
          id="outlined-basic"
          label="user nicknaem"
          variant="outlined"
          className={styles["nickeNameInput"]}
        />
      </div>
      <div className={styles["buttonContainer"]}>
        <Button variant="outlined" className={styles["button"]} style={{ marginRight: "20px" }}>
          Cancle
        </Button>
        <Button variant="contained" className={styles["button"]}>
          Change
        </Button>
      </div>
    </div>
  );
};

export default EditProfile;
