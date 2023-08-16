import { createSlice } from "@reduxjs/toolkit";

let conference = createSlice({
  name: "conference",
  initialState: {
    activeSessionId: null,
    // classId: null,
    sessionName: null,
  },
  reducers: {
    setActiveSessionId(state, action) {
      state.activeSessionId = action.payload;
    },
    // setClassId(state, action) {
    //   state.classId = action.payload;
    // },
    setSessionName(state, action) {
      state.sessionName = action.payload;
    },
  },
});

export const { setActiveSessionId, setSessionName } = conference.actions;
export default conference.reducer;
