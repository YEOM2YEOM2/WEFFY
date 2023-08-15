import React, { useState, useEffect } from "react";
import styles from "./fileList.module.css";
import axios from "axios";
import { Button } from "@mui/material";

//mui 버튼
import ArrowCircleLeftIcon from "@mui/icons-material/ArrowCircleLeft";
import ArrowCircleRightIcon from "@mui/icons-material/ArrowCircleRight";

function FileList(props) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [files, setFiles] = useState([]);

  const [uploadFile, setUploadFile] = useState(null);
  const [isHovered, setIsHovered] = useState(false);

  const conferenceId = "conferenceId";
  const type = "type?";

  const handleFileChange = (e) => {
    // Logic remains the same
    setUploadFile(e.target.files[0]);
  };

  const handleUploadClick = async () => {
    if (!uploadFile) return;

    const formData = new FormData();
    formData.append("uploadFile", uploadFile);

    try {
      // 업로드 파일 여기에 API맞춰서 하면 됩니당
      const response = await axios.post(
        `http://localhost:8081/${conferenceId}`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
          params: { type },
        }
      );

      if (response.data.success) {
        alert("File uploaded successfully");
      } else {
        alert("Failed to upload file");
      }
    } catch (error) {
      console.error("There was an error while uploading files:", error);
    }
  };

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

  const labelStyles = {
    padding: "10px 15px",
    backgroundColor: isHovered ? "#45a049" : "white",
    color: "skyblue",
    cursor: "pointer",
    borderRadius: "20px",
  };

  // 들어오자마자 백에 요청해서 db에서 file들 가져오는 코드입니다용
  // useEffect(() => {
  //   async function fetchFiles() {
  //     try {
  //       const url = "http://localhost:8081/api/v1/files";

  //       const requestData = {
  //         conferenceId: conferenceId,
  //         start: "시작시간",
  //         end: "종료시간",
  //       };

  //       const response = await axios.post(url, { data: requestData });
  //       if (
  //         response.data &&
  //         response.data.data &&
  //         Array.isArray(response.data.data.getFileDto)
  //       ) {
  //         setFiles(response.data.data.getFileDto.map((file) => file.fileName));
  //       }
  //     } catch (error) {
  //       console.error("Error : " + error);
  //     }
  //   }

  //   fetchFiles();
  // }, []);

  return (
    <div className={styles.modal}>
      <div
        className="modalHeader"
        style={{
          display: "flex",
          justifyContent: "space-between",
          width: "100%",
          height: "30px",
        }}
      >
        <h2>Files</h2>
        <button onClick={props.onClose}>&times;</button>
      </div>

      <div>
        <ul style={{ padding: 0 }}>
          {files.slice(currentIndex, currentIndex + 4).map((file, index) => (
            <li
              className="divider"
              style={{
                borderBottom: "1px solid gray",
                listStyle: "none",
                textAlign: "left",
                paddingTop: "15px",
              }}
              key={index}
            >
              {file}
            </li>
          ))}
        </ul>
      </div>
      <div
        className="132"
        style={{
          display: "flex",
          justifyContent: "space-between",
          width: "100%",
          height: "30px",
        }}
      >
        <div style={{ display: "flex" }}>
          <p>Showing 4 of 11 items</p>
          <p>1 of 3</p>
        </div>
        <div>
          <Button onClick={handlePrev} size="small">
            <ArrowCircleLeftIcon />
          </Button>
          <Button onClick={handleNext} size="small">
            <ArrowCircleRightIcon />
          </Button>
          {/* <button onClick={handlePrev}>◂</button>

          <button onClick={handleNext}>▸</button> */}
        </div>
      </div>
      <div
        className="upload"
        style={{
          border: "2px dotted gray",
          borderRadius: "10px",
        }}
      >
        <input
          type="file"
          className="hidden-input"
          id="fileInput"
          multiple
          onChange={handleFileChange}
          style={{ display: "none" }}
        />
        <label
          htmlFor="fileInput"
          className="custom-file-label"
          style={labelStyles}
          onMouseEnter={() => setIsHovered(true)}
          onMouseLeave={() => setIsHovered(false)}
        >
          Choose files
        </label>
        {uploadFile && (
          <div>
            <p>{uploadFile.name}</p>
            <p>{uploadFile.size} bytes</p>
            <button onClick={handleUploadClick}>Upload</button>
          </div>
        )}
      </div>
    </div>
  );
}

export default FileList;
