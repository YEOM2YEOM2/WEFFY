import { createSlice, configureStore } from "@reduxjs/toolkit";

const micStatus = createSlice({
  name: "micStatus",
  initialState: false,
  reducers: {
    toggle: (state) => !state,
  },
});

const cameraStatus = createSlice({
  name: "cameraStatus",
  initialState: false,
  reducers: {
    toggle: (state) => !state,
  },
});

// 세션 정보 : 이거 있어야 하는게 맞나..?ㅎ
const session = createSlice({
  name: "session",
  initialState: null,
  reducers: {
    setSession: (state, action) => action.payload,
  },
});

// nickname : 들어갈때 바꾸고 여기 저장
const nickname = createSlice({
  name: "nickname",
  initialState: "default nickname",
  reducers: {
    setNickname: (state, action) => action.payload,
  },
});

const selectedMic = createSlice({
  name: "selectedMic",
  initialState: 0,
  reducers: {
    setSelectedMic: (state, action) => action.payload,
  },
});

const selectedCam = createSlice({
  name: "selectedCam",
  initialState: 0,
  reducers: {
    setSelectedCam: (state, action) => action.payload,
  },
});

export default configureStore({
  reducer: {
    session: session.reducer,
    nickname: nickname.reducer,
    micStatus: micStatus.reducer,
    cameraStatus: cameraStatus.reducer,
    selectedCam: selectedCam.reducer,
    selectedMic: selectedMic.reducer,
  },
});

export const { setSession } = session.actions;
export const { setNickname } = nickname.actions;
export const { toggle: toggleMicStatus } = micStatus.actions;
export const { toggle: toggleCameraStatus } = cameraStatus.actions;
export const { setSelectedCam } = selectedCam.actions;
export const { setSelectedMic } = selectedMic.actions;
