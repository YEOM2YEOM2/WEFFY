import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import "./assets/fonts/font.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import registerServiceWorker from "./registerServiceWorker";
import { BrowserRouter } from "react-router-dom";

import { Provider } from "react-redux";
import { persistStore } from "redux-persist";
import { PersistGate } from "redux-persist/integration/react";
import store from "./store/index.js";

let persistor = persistStore(store);

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <BrowserRouter>
        <App />
      </BrowserRouter>
    </PersistGate>
  </Provider>
);

reportWebVitals();
registerServiceWorker();
