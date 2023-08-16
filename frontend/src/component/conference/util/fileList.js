import React, { useState, useEffect } from "react";
import styles from "./fileList.module.css";
import axios from "axios";
import { Button, accordionActionsClasses } from "@mui/material";
import { useSelector } from "react-redux";

//mui 버튼
import ArrowCircleLeftIcon from "@mui/icons-material/ArrowCircleLeft";
import ArrowCircleRightIcon from "@mui/icons-material/ArrowCircleRight";
import CloseIcon from "@mui/icons-material/Close";
import AttachFileIcon from "@mui/icons-material/AttachFile";
import IconButton from "@mui/material/IconButton";
import SimCardDownloadIcon from "@mui/icons-material/SimCardDownload";

function FileList(props) {
  const accessToken = useSelector((state) => state.user.accessToken);
  const activeSessionId = useSelector(
    (state) => state.conference.activeSessionId
  );
  const [currentIndex, setCurrentIndex] = useState(0);

  const [files, setFiles] = useState([]);

  let currentPage = Math.ceil(currentIndex / 4) + 1;
  let totalPages = Math.ceil(files.length / 4);

  let displayText = `${currentPage}/${totalPages}`;
  console.log(files);

  const handlePrev = () => {
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - 4);
    }
  };

  const handleNext = () => {
    if (currentIndex + 4 < files.length) {
      setCurrentIndex(currentIndex + 4);
    }
  };

  const downloadFileClick = (title, key) => {
    axios({
      method: "get",
      url: `http://i9d107.p.ssafy.io:8081/api/v1/files/download?objectKey=${key}&title=${title}`,
      headers: {
        accept: "application/json",
        "Content-Type": "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    })
      .then((res) => {})
      .catch((err) => {
        console.log(err.response);
      });
  };

  const initFileList = () => {
    axios({
      method: "get",
      url: `http://i9d107.p.ssafy.io:8081/api/v1/files?conferenceId=${activeSessionId}`,
      headers: {
        accept: "application/json",
        Authorization: `Bearer ${accessToken}`,
      },
    })
      .then((res) => {
        console.log(res);
        const tempFiles = [];
        res.map((val) => {
          let temp = { fileName: "", fileUrl: "" };
          temp.fileName = val.fileName;
          temp.fileUrl = val.fileUrl;
          tempFiles.push(temp);
        });
        setFiles(tempFiles);
      })
      .catch((err) => {
        console.log(err);
      });
  };

  useEffect(() => {
    initFileList();
  }, []);

  return (
    <div className={styles.modal}>
      <div
        className="modalHeader"
        style={{
          display: "flex",
          justifyContent: "space-between",
          width: "100%",
          height: "30px",
          fontFamily: "GmarketSans",
        }}
      >
        <p className={styles["title"]}>파일 목록</p>
        <IconButton>
          <CloseIcon
            onClick={props.onClose}
            style={{
              color: "gray",
              position: "relative",
              bottom: "5px",
              left: "9px",
            }}
          />
        </IconButton>
      </div>
      <div>
        {files.slice(currentIndex, currentIndex + 4).map((file, index) => (
          <div className={styles["downloadContainer"]}>
            <IconButton className="downloadText">
              <SimCardDownloadIcon
                style={{
                  color: "blueviolet",
                }}
                onClick={downloadFileClick(file.fileName, file.fileUrl)}
              />
              {file.fileName}
            </IconButton>
            <div className={styles.divider} />
          </div>
        ))}
      </div>
      <div className={styles["fileController"]}>
        <div className={styles["arrowContainer"]}>
          <IconButton
            onClick={handlePrev}
            size="small"
            className={styles["button"]}
          >
            <ArrowCircleLeftIcon />
          </IconButton>
          {displayText}
          <IconButton
            onClick={handleNext}
            size="small"
            styles={{ width: "5px" }}
            className={styles["button"]}
          >
            <ArrowCircleRightIcon />
          </IconButton>
        </div>
      </div>
    </div>
  );
}

export default FileList;
