import React, { useState } from "react";
import SideBar from "../component/im/sidebar.js";
import styles from "./im.module.css";

import MmModal from "../component/im/mmModal.js";
import PrivateModal from "../component/im/privateModal.js";
import ParticipateModal from "../component/im/participateModal.js";

//image
import newMM from "../assets/images/newMM.png";
import newPrivate from "../assets/images/newPrivate.png";
import participate from "../assets/images/participate.png";

const icons = [
  <img src={newMM} alt="newMM" />,
  <img src={newPrivate} alt="newPrivate" />,
  <img src={participate} alt="participate" />,
];

const Im = (props) => {
  let [buttonStatus, setStatue] = useState(true);

  // 모달 상태 선언
  const [modalStatus, setModalStatus] = useState({
    newMM: false,
    newPrivate: false,
    participate: false,
  });

  // 모달 핸들러
  const handleOpen = (modal) => () => {
    setModalStatus((prevState) => ({ ...prevState, [modal]: true }));
  };

  const handleClose = (modal) => () => {
    setModalStatus((prevState) => ({ ...prevState, [modal]: false }));
  };

  return (
    <div className={styles["container"]}>
      <span className={styles["logoText"]}>
        <span>WEFFY</span>
      </span>
      <div className={styles["sidebar"]}>
        <SideBar />
      </div>
      <div className={styles["functionsBtn"]}>
        {buttonStatus &&
          ["newMM", "newPrivate", "participate"].map((modal, index) => (
            <div
              className={styles["button"]}
              key={index}
              onClick={handleOpen(modal)}
            >
              {icons[index]}

              {/* 모달 */}
              {modal === "newMM" && (
                <MmModal
                  show={modalStatus[modal]}
                  handleClose={() => handleClose(modal)}
                />
              )}
              {modal === "newPrivate" && (
                <PrivateModal
                  show={modalStatus[modal]}
                  handleClose={() => handleClose(modal)}
                />
              )}
              {modal === "participate" && (
                <ParticipateModal
                  show={modalStatus[modal]}
                  handleClose={() => handleClose(modal)}
                />
              )}
            </div>
          ))}
      </div>
    </div>
  );
};

export default Im;
