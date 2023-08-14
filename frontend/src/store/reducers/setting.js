import { createSlice } from "@reduxjs/toolkit";

const setting = createSlice({
  name: "setting",
  initialState: {
    micStatus: false,
    cameraStatus: false,
    participateName: "WEFFY",
    selectedMic: 1,
    selectedCam: 1,
  },
  reducers: {
    toggleMicStatus(state) {
      state.micStatus = !state.micStatus;
    },
    toggleCameraStatus(state) {
      state.cameraStatus = !state.cameraStatus;
    },
    setParticipateName(state, action) {
      state.participateName = action.payload;
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
  setParticipateName,
  setSelectedMic,
  setSelectedCam,
} = setting.actions;

export default setting.reducer;
