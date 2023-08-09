import { combineReducers } from "redux";
import { configureStore } from "@reduxjs/toolkit";
import { persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";
import thunk from "redux-thunk";
import user from "./reducers/user.js";
import setting from "./reducers/setting.js";
import conference from "./reducers/conference.js";

const persistConfig = {
  key: "WEFFY",
  storage,
  whitelist: ["user", "setting", "conference"],
};

const rootReducer = combineReducers({
  user,
  setting,
  conference,
});

const persistedReducer = persistReducer(persistConfig, rootReducer);

const store = configureStore({
  reducer: persistedReducer,
  devTools: process.env.NODE_ENV !== "production",
  middleware: [thunk],
});

export default store;
