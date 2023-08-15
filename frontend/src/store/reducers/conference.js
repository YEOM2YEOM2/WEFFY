import { createSlice } from "@reduxjs/toolkit";

let conference = createSlice({
  name: "conference",
  initialState: {
    activeSessionId: null,
    classId: null,
  },
  reducers: {
    setActiveSessionId(state, action) {
      state.activeSessionId = action.payload;
    },
    setClassId(state, action) {
      state.classId = action.payload;
    },
  },
});

export const { setActiveSessionId, setClassId } = conference.actions;
export default conference.reducer;
