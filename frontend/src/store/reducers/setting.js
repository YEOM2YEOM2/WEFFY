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

// 현재 모든 디바이스들
const devices = createSlice({
  name: "devices",
  initialState: { microphones: [], cameras: [] },
  reducers: {
    setMicrophones: (state, action) => {
      state.microphones = action.payload;
    },
    setCameras: (state, action) => {
      state.cameras = action.payload;
    },
  },
});

// 미팅 시작전 선택한 디바이스들
const selectedDevicesSlice = createSlice({
  name: "selectedDevices",
  initialState: { microphone: null, camera: null },
  reducers: {
    selectMicrophone: (state, action) => {
      state.microphone = action.payload;
    },
    selectCamera: (state, action) => {
      state.camera = action.payload;
    },
  },
});

export default configureStore({
  reducer: {
    session: session.reducer,
    nickname: nickname.reducer,
    devices: devices.reducer,
    selectedDevices: selectedDevicesSlice.reducer,
    micStatus: micStatus.reducer,
    cameraStatus: cameraStatus.reducer,
  },
});

export const { setSession } = session.actions;
export const { setNickname } = nickname.actions;
export const { setMicrophones, setCameras } = devices.actions;
export const { selectMicrophone, selectCamera } = selectedDevicesSlice.actions;
export const { toggle: toggleMicStatus } = micStatus.actions;
export const { toggle: toggleCameraStatus } = cameraStatus.actions;
