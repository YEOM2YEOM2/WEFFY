import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import "./assets/fonts/font.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import registerServiceWorker from "./registerServiceWorker";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "react-query";
import { Provider } from "react-redux";
import setting from "./store/setting.js";

const queryClient = new QueryClient();

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <Provider store={setting}>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </Provider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
registerServiceWorker();
