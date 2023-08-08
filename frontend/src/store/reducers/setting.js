import { createSlice } from "@reduxjs/toolkit";

const setting = createSlice({
  name: "setting",
  initialState: {
    micStatus: false,
    cameraStatus: false,
    session: null,
    nickname: "default nickname",
    selectedMic: 0,
    selectedCam: 0,
  },
  reducers: {
    toggleMicStatus(state) {
      state.micStatus = !state.micStatus;
    },
    toggleCameraStatus(state) {
      state.cameraStatus = !state.cameraStatus;
    },
    setSession(state, action) {
      state.session = action.payload;
    },
    setNickname(state, action) {
      state.nickname = action.payload;
    },
    setSelectedMic(state, action) {
      state.selectedMic = action.payload;
    },
    setSelectedCam(state, action) {
      state.selectedCam = action.payload;
    },
  },
});

export const {
  toggleMicStatus,
  toggleCameraStatus,
  setSession,
  setNickname,
  setSelectedMic,
  setSelectedCam,
} = setting.actions;

export default setting.reducer;
