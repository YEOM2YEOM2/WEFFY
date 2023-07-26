// 기본 CSS
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";

// 라이브러리
import { Route, Routes, Outlet } from "react-router-dom";

// 파일
import Login from "./pages/login.js";
import Im from "./pages/im.js";

function App() {
  return (
    <div className="App">
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/im" element={<Im />} />
      </Routes>
    </div>
  );
}

export default App;
