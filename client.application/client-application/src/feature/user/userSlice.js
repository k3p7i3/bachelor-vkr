import { createAsyncThunk, createSlice } from '@reduxjs/toolkit'
import { getUser, postUser, editUser, editPassword } from '../../api/userApi';
import { middleware } from '../middleware';

export const registerUser = createAsyncThunk('user/registerUser', async (params, thunk) => {
  return await middleware(postUser, params, thunk, { }, false);
});

// export const fetchUser = createAsyncThunk('user/fetchUser', async (params, thunk) => {
//   return await middleware(getUser, params, thunk, { });
// });

export const fetchAuthUser = createAsyncThunk('user/fetchAuthUser', async (params, thunk) => {
  return await middleware(getUser, params, thunk, { });
});

export const editUserInfo = createAsyncThunk('user/editUserInfo', async (params, thunk) => {
  return await middleware(editUser, params, thunk, { });
});

export const changePassword = createAsyncThunk('user/changePassword', async (params, thunk) => {
  return await middleware(editPassword, params, thunk, { });
});

const userSlice = createSlice({
  name: 'user',
  initialState: {
    auth: undefined,
    status: {
      message: undefined,
      code: undefined,
    },
    progress: false,
  },
  reducers: {
    resetStatus: (state, action) => {
      state.status = {
        message: undefined,
        code: undefined,
      }
    },
    resetAuth: (state, action) => {
      state.auth = undefined;
    }
  },
  extraReducers(builder) {
    builder
      .addCase(registerUser.fulfilled, (state, action) => {
        state.status = {
          message: "Регистрация пользователя прошла успешно",
          code: 3,
        }
        state.progress = false;
      })
      .addCase(registerUser.pending, (state, action) => {
        state.progress = true
      })
      .addCase(registerUser.rejected, (state, action) => {
        state.status = {
          message: `Не удалось зарегистрировать пользователя ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchAuthUser.fulfilled, (state, action) => {
        state.auth = action.payload;
        state.progress = false;
      })
      .addCase(fetchAuthUser.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchAuthUser.rejected, (state, action) => {
        state.status = {
          message:  `Не удалось загрузить данные пользователя ${action.payload.message}`,
          code: action.payload.code
        }
        state.progress = false;
      })

      .addCase(editUserInfo.fulfilled, (state, action) => {
        state.auth = action.payload;
        state.progress = false;
      })
      .addCase(editUserInfo.pending, (state, action) => {
        state.progress = true
      })
      .addCase(editUserInfo.rejected, (state, action) => {
        state.status = {
          message:  `Не удалось изменить данные пользователя ${action.payload.message}`,
          code: action.payload.code
        }
        state.progress = false;
      })


      .addCase(changePassword.fulfilled, (state, action) => {
        state.status = {
          message: "Пароль изменен",
          code: 3
        }
        state.progress = false;
      })
      .addCase(changePassword.pending, (state, action) => {
        state.progress = true
      })
      .addCase(changePassword.rejected, (state, action) => {
        state.status = {
          message:  `Не удалось изменить пароль ${action.payload.message}`,
          code: action.payload.code
        }
        state.progress = false;
      })
  }
})

export const { resetStatus, resetAuth } = userSlice.actions;

export const selectUserStatus = (state) => state.user.status;
export const selectUserProgress = (state) => state.user.progress;
export const selectAuthUser = (state) => state.user.auth;
export default userSlice.reducer;