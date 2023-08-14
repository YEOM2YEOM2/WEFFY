// import React, { Component } from "react";
// import styles from "./fileList.module.css";

// import axios from "axios";

// class fileList extends Component {
//   constructor(props) {
//     super(props);
//     this.state = {
//       currentIndex: 0,
//       files: ["file1.txt", "file2.txt", "file3.jpg", "file4.doc", "file5.png"],
//       //   file: null,
//     };
//     this.handleFileChange = this.handleFileChange.bind(this);
//     this.handleUploadClick = this.handleUploadClick.bind(this);
//   }

//   handleFileChange(e) {
//     if (e.target.files) {
//       this.setState({ file: e.target.files[0] });
//     }
//   }

//   handleUploadClick() {
//     const file = this.state;
//     if (!file) {
//       return;
//     }
//     fetch("https://httpbin.org/post", {
//       method: "POST",
//       body: file,
//       headers: {
//         "content-type": file.type,
//         "content-length": `${file.size}`,
//       },
//     })
//       .then((res) => res.json())
//       .then((data) => console.log(data))
//       .catch((err) => console.error(err));
//   }

//   handlePrev = () => {
//     const currentIndex = this.state.currentIndex;
//     if (currentIndex > 0) this.setState({ currentIndex: currentIndex - 4 });
//   };

//   handleNext = () => {
//     const currentIndex  = this.state.currentIndex;
//     if (currentIndex + 4 < this.state.files.length) {
//       this.setState({ currentIndex: currentIndex + 4 });
//     }

//   };

//   render() {
//     // const { file } = this.state.file;
//     return (
//       <div className={styles.modal}>
//         <div
//           className="modalHeader"
//           style={{
//             display: "flex",
//             justifyContent: "space-between",
//             width: "100%",
//             height: "30px",
//           }}
//         >
//           <h2>Files</h2>
//           <button onClick={this.props.onClose}>&times;</button>
//         </div>

//         <div>
//           <ul style={{ padding: 0 }}>
//             {this.state.files
//               .slice(this.state.currentIndex, this.state.currentIndex + 4)
//               .map((file, index) => (
//                 <li
//                   className="divider"
//                   style={{
//                     borderBottom: "1px solid gray",
//                     listStyle: "none",
//                     textAlign: "left",
//                     paddingTop: "15px",
//                   }}
//                   key={index}
//                 >
//                   {file}
//                 </li>
//               ))}
//           </ul>
//         </div>
//         <div
//           className="132"
//           style={{
//             display: "flex",
//             justifyContent: "space-between",
//             width: "100%",
//             height: "30px",
//           }}
//         >
//           <div style={{ display: "flex" }}>
//             <p>Showing 4 of 11 items</p>
//             <p>1 of 3</p>
//           </div>
//           <div>
//             <button onClick={this.handlePrev}>◂</button>
//             <button onClick={this.handleNext}>▸</button>
//           </div>
//         </div>
//         <div
//           className="upload"
//           style={{
//             border: "2px dotted gray",
//             borderRadius: "10px",
//           }}
//         >
//           {/* <input type="file" onChange={this.handleFileChange} />
//           <div>{file && `${file.name} - ${file.type}`}</div>
//           <button onClick={this.handleUploadClick}>Upload</button> */}
//         </div>
//       </div>
//     );
//   }
// }

// export default fileList;

import React, { useState } from "react";
import styles from "./fileList.module.css";
import axios from "axios";

function FileList(props) {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [files, setFiles] = useState([
    "file1.txt",
    "file2.txt",
    "file3.jpg",
    "file4.doc",
    "file5.png",
  ]);

  const handleFileChange = (e) => {
    // Logic remains the same
    setFiles(e.target.files);
  };

  const handleUploadClick = () => {
    // Logic remains the same
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
          <button onClick={handlePrev}>◂</button>
          <button onClick={handleNext}>▸</button>
        </div>
      </div>
      <div
        className="upload"
        style={{
          border: "2px dotted gray",
          borderRadius: "10px",
        }}
      >
        {/* <input type="file" onChange={handleFileChange} />
        <div>{file && `${file.name} - ${file.type}`}</div>
        <button onClick={handleUploadClick}>Upload</button> */}
      </div>
    </div>
  );
}

export default FileList;
