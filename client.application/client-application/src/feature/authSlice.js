import { createSlice } from '@reduxjs/toolkit';
import { Buffer } from 'buffer';

const authSlice = createSlice({
    name: 'auth', 
    initialState: {
        status: {
            message: undefined,
            code: undefined,
        },
        data: undefined,
    },
    reducers: {
        resetStatus: (state, action) => {
            state.status = {
                message: undefined,
                code: undefined,
            }
        },
        setAuthData: (state, action) => {
            const access_token = action.payload.access_token;
            const data = JSON.parse(Buffer.from(access_token.split('.')[1], 'base64').toString());
            state.data = {
                email: data.sub,
                role: data.additionalInfo?.role,
                agentId: data.additionalInfo?.agentId,
                accessToken: access_token,
            }
        },
        resetAuthData: (state, action) => {
          state.data = undefined;
        },
        setMessage: (state, action) => {
            state.status.message = action.payload;
        }
    }
})

export const { resetStatus, setAuthData, resetAuthData, setMessage } = authSlice.actions;

export const selectStatus = (state) => state.auth.status;
export const selectAuthData = (state) => state.auth.data;

export default authSlice.reducer;