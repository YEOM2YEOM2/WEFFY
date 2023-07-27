import React, { useState } from "react";
import SideBar from "../component/im/sidebar.js";
import styles from "./im.module.css";
import newMM from "../assets/images/newMM.png";
import newPrivate from "../assets/images/newPrivate.png";
import participate from "../assets/images/participate.png";
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';

const Im = (props) => {

  const [showPrivate, setShowPrivate] = useState(false);
  const [showMM, setShowMM] = useState(false);
  const [showParticipate, setShowParticipate] = useState(false);

  const handleClosePrivate = () => setShowPrivate(false);
  const handleShowPrivate = () => setShowPrivate(true);

  const handleCloseMM = () => setShowMM(false);
  const handleShowMM = () => setShowMM(true);

  const handleCloseParticipate = () => setShowParticipate(false);
  const handleShowParticipate = () => setShowParticipate(true);

  const handleNewPrivateClick = () => {
    console.log("New Private clicked!");
    handleShowPrivate();
  };

  const handleNewMMClick = () => {
    console.log("New MM clicked!");
    handleShowMM();
  };

  const handleParticipateClick = () => {
    console.log("Participate clicked!");
    handleShowParticipate();
  };

  return (
    <div className={styles["container"]}>
      <span className={styles["text"]}>
        <span>WEFFY</span>
      </span>
      <div className={styles["sidebar"]}>
        <SideBar />
      </div>
      <div className={styles["iconContainer"]}>
        <div>
          <img
            src={newPrivate}
            alt="newPrivate logo"
            className={styles["icon"]}
            onClick={handleNewPrivateClick}
          />
          <p>이연지 미팅</p>
        </div>
        <div>
          <img
            src={newMM}
            alt="newMM logo"
            className={styles["icon"]}
            onClick={handleNewMMClick}
          />
          <p>정예진 미팅</p>
        </div>
        <div>
          <img
            src={participate}
            alt="participate logo"
            className={styles["icon"]}
            onClick={handleParticipateClick}
          />
          <p>방진성 미팅</p>
        </div>
      </div>

      <Modal show={showPrivate} onHide={handleClosePrivate}>
        <Modal.Header closeButton>
          <Modal.Title>Private Modal</Modal.Title>
        </Modal.Header>
        <Modal.Body>Private modal content...</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClosePrivate}>
            Close
          </Button>
          <Button variant="primary" onClick={handleClosePrivate}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showMM} onHide={handleCloseMM}>
        <Modal.Header closeButton>
          <Modal.Title>MM Modal</Modal.Title>
        </Modal.Header>
        <Modal.Body>MM modal content...</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseMM}>
            Close
          </Button>
          <Button variant="primary" onClick={handleCloseMM}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showParticipate} onHide={handleCloseParticipate}>
        <Modal.Header closeButton>
          <Modal.Title>Participate Modal</Modal.Title>
        </Modal.Header>
        <Modal.Body>Participate modal content...</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseParticipate}>
            Close
          </Button>
          <Button variant="primary" onClick={handleCloseParticipate}>
            Save Changes
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default Im;
