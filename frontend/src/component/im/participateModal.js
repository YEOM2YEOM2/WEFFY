import React from "react";
import styles from "./participateModal.module.css";

//이미지


const ParticipateModal = ({ handleClose }) => {
  const myList = ['item1', 'item2', 'item3', /* and so on... */]; // 원하는 아이템으로 변경 가능

  return (
    <div className={styles["modal"]} onClick={handleClose}>
      <div className={styles["modalBody"]} onClick={(e) => e.stopPropagation()}>
        <h2 className={styles["modalHeader"]}>미팅 참여하기</h2>
        <ul>
          {myList.slice(0, 10).map((item, index) => <li key={index}>{item}</li>)}
        </ul>
      </div>
    </div>





  );
};

export default ParticipateModal;
