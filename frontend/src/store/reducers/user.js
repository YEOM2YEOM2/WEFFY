import { createSlice } from "@reduxjs/toolkit";

let user = createSlice({
    name : 'user',
    initialState : {
        identification : null,
        accessToken: null,
        refreshToken: null,
        csrfToken: null,
        nickname: null,
        profileImg: null,
        id : null
    },
    reducers: {
        setIdentification(state, action) {
            state.identification = action.payload
        },
        setAccessToken(state, action) {
            state.accessToken = action.payload
        },
        setRefreshToken(state, action) {
            state.refreshToken = action.payload
        },
        setCsrfToken(state, action) {
            state.csrfToken = action.payload
        },
        setProfileImg(state, action) {
            state.profileImg = action.payload
        },
        setNickname(state, action) {
            state.nickname = action.payload
        },
        setId(state, action) {
            state.id = action.payload
        }
    }
})

export const { setIdentification, setAccessToken, setRefreshToken, setCsrfToken, setProfileImg, setNickname, setId } = user.actions
export default user.reducer;