// 기본 CSS
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

// 라이브러리
import { Route, Routes } from "react-router-dom";

// 파일
import Login from "./pages/login.js";
import Signup from "./pages/signUp.js";
import Im from "./pages/im.js";
import MyList from "./pages/myList.js";
import Setting from "./pages/setting.js";
import VideoRoomComponent from "./pages/VideoRoomComponent.js";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/im" element={<Im />}>
          <Route path="mylist" element={<MyList />} />
          <Route path="setting" element={<Setting />} />
        </Route>
        <Route path="/conference" element={<VideoRoomComponent />} />
      </Routes>
    </div>
  );
}

export default App;
