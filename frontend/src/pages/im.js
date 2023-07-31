import React, { useState, useEffect } from "react";

import SideBar from "../component/im/sidebar.js";
import styles from "./im.module.css";

//외부 라이브러리
import { Route, Routes, Outlet, useLocation } from "react-router-dom";
import MyList from "../pages/myList.js";

//Model js
import MmModal from "../component/im/mmModal.js";
import PrivateModal from "../component/im/privateModal.js";
import ParticipateModal from "../component/im/participateModal.js";

//image
import newMM from "../assets/images/newMM.png";
import newPrivate from "../assets/images/newPrivate.png";
import participate from "../assets/images/participate.png";

const icons = [
  { name: "PrivateModal", src: newPrivate },
  { name: "MmModal", src: newMM },
  { name: "ParticipateModal", src: participate },
];

const Im = (props) => {
  //현재 위치 확인
  const location = useLocation();

  const isImPage = location.pathname === "/im";

  // 모달 상태 선언
  const [modalStatus, setModalStatus] = useState({
    MmModal: false,
    newPrivate: false,
    participate: false,
  });

  const handleModalOpen = (modalName) => {
    //modalName에 맞는 modal true로 변경
    setModalStatus({
      ...modalStatus,
      [modalName]: true,
    });
  };

  const handleModalClose = (modalName) => {
    //modalName에 맞는 modal false 변경
    setModalStatus({
      ...modalStatus,
      [modalName]: false,
    });
  };

  const isModalOpen = Object.values(modalStatus).some(
    (status) => status === true
  );

  return (
    <div className={styles["container"]}>
      <span className={styles["logoText"]}>
        <span>WEFFY</span>
      </span>
      <div className={styles["sidebar"]}>
        <SideBar />
      </div>
      <Outlet />
      {isImPage && !isModalOpen && (
        <div className={styles["functionsBtn"]}>
          {icons.map((icon, index) => (
            <div
              className={styles["button"]}
              key={index}
              onClick={() => handleModalOpen(icon.name)}
            >
              <img src={icon.src} alt={icon.name} />
            </div>
          ))}
        </div>
      )}
      {modalStatus.MmModal && (
        <MmModal handleClose={() => handleModalClose("MmModal")} />
      )}
      {modalStatus.PrivateModal && (
        <PrivateModal
          show={modalStatus.PrivateModal}
          handleClose={() => handleModalClose("PrivateModal")}
        />
      )}
      {modalStatus.ParticipateModal && (
        <ParticipateModal
          show={modalStatus.ParticipateModal}
          handleClose={() => handleModalClose("ParticipateModal")}
        />
      )}
    </div>
  );
};

export default Im;
