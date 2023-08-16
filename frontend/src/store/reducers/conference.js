import { createSlice } from "@reduxjs/toolkit";

let conference = createSlice({
  name: "conference",
  initialState: {
    activeSessionId: null,
    classId: null,
    conferenceName: null,
  },
  reducers: {
    setActiveSessionId(state, action) {
      state.activeSessionId = action.payload;
    },
    setClassId(state, action) {
      state.classId = action.payload;
    },
    setConferenceName(state, action) {
      state.conferenceName = action.payload;
    },
  },
});

export const { setActiveSessionId, setClassId, setConferenceName } =
  conference.actions;
export default conference.reducer;
