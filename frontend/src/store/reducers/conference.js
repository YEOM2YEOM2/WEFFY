import { createSlice } from "@reduxjs/toolkit";

let conference = createSlice({
  name: "conference",
  initialState: {
    activeSessionId: null,
    activeSessionName: null,
  },
  reducers: {
    setActiveSessionId(state, action) {
      state.activeSessionId = action.payload;
    },

    setActiveSessionName(state, action) {
      state.activeSessionName = action.payload;
    },
  },
});

export const { setActiveSessionId, setActiveSessionName } = conference.actions;
export default conference.reducer;
