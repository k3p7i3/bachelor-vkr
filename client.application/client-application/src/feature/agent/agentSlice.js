import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { middleware } from "../middleware";
import { 
  registerAgent, editAgent, getAgent, getAgentBrief, 
  getAgents, addAvatar, addImages, deleteImage,
  saveAgentSelection, getAgentSelection, deleteAgentSelection
} from "../../api/agentApi";


export const createAgent = createAsyncThunk('agent/createAgent', async (params, thunk) => {
  return await middleware(registerAgent, params, thunk, { });
});

export const updateAgentProfile = createAsyncThunk('agent/updateAgentProfile', async (params, thunk) => {
  return await middleware(editAgent, params, thunk, { });
});

export const fetchCurrentAgent = createAsyncThunk('agent/fetchCurrentAgent', async (params, thunk) => {
  return await middleware(getAgent, params, thunk, { }, false);
});

export const fetchAuthAgent = createAsyncThunk('agent/fetchAuthAgent', async (params, thunk) => {
  return await middleware(getAgent, params, thunk, { }, false);
});

export const fetchAgents = createAsyncThunk('agent/fetchAgents', async (params, thunk) => {
  return await middleware(getAgents, params, thunk, { }, false);
});

export const postAvatar = createAsyncThunk('agent/postAvatar', async (params, thunk) => {
  return await middleware(addAvatar, params, thunk, { });
});

export const postImages = createAsyncThunk('agent/postImages', async (params, thunk) => {
  return await middleware(addImages, params, thunk, { });
});

export const removeImage = createAsyncThunk('agent/removeImage', async (params, thunk) => {
  return await middleware(deleteImage, params, thunk, { });
});

export const fetchSelectedBriefAgent = createAsyncThunk('agent/fetchSelectedBriefAgent', async (params, thunk) => {
  return await middleware(getAgentSelection, params, thunk, { }, false);
});

export const fetchAgentForOrder = createAsyncThunk('agent/fetchAgentForOrder', async (params, thunk) => {
  return await middleware(getAgentBrief, params, thunk, { }, false);
});

export const postAgentSelection = createAsyncThunk('agent/postAgentSelection', async (params, thunk) => {
  return await middleware(saveAgentSelection, params, thunk, { }, false);
});

export const removeAgentSelection = createAsyncThunk('agent/removeAgentSelection', async (params, thunk) => {
  return await middleware(deleteAgentSelection, params, thunk, { }, false);
});


const agentSlice = createSlice({
  name: 'agent',
  initialState: {
    selectedAgent: undefined,
    authAgent: undefined,
    currentAgent: undefined,
    orderAgent: undefined,
    agents: undefined,
    status: {
      message: undefined,
      code: undefined
    },
    progress: false
  },
  reducers: {
    resetStatus: (state, action) => {
      state.status = {
        message: undefined,
        code: undefined,
      }
    }
  },
  extraReducers(builder) {
    builder
      .addCase(createAgent.fulfilled, (state, action) => {
        state.status = {
          message: "Регистрация нового посредника прошла успешно",
          code: 3,
        }
        state.progress = false;
      })
      .addCase(createAgent.pending, (state, action) => {
        state.progress = true
      })
      .addCase(createAgent.rejected, (state, action) => {
        state.status = {
          message: `Не удалось зарегистрировать посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchCurrentAgent.fulfilled, (state, action) => {
        state.currentAgent = {
          agent: action.payload,
          agentId: action.payload.profile.id
        }
        state.progress = false;
      })
      .addCase(fetchCurrentAgent.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchCurrentAgent.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить данные посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchAuthAgent.fulfilled, (state, action) => {
        state.authAgent = {
          agent: action.payload,
          agentId: action.payload.profile.id
        }
        state.progress = false;
      })
      .addCase(fetchAuthAgent.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchAuthAgent.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить данные посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchAgents.fulfilled, (state, action) => {
        state.agents = action.payload
        state.progress = false;
      })
      .addCase(fetchAgents.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchAgents.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить данные посредников ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(updateAgentProfile.fulfilled, (state, action) => {
        state.status = {
          message: "Обновление профиля посредника прошло успешно",
          code: 3,
        }
        state.authAgent.agent.profile = action.payload
        state.progress = false;
      })
      .addCase(updateAgentProfile.pending, (state, action) => {
        state.progress = true
      })
      .addCase(updateAgentProfile.rejected, (state, action) => {
        state.status = {
          message: `Не удалось обновить профиль посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(postAvatar.fulfilled, (state, action) => {
        state.status = {
          message: "Обновление изображения профиля посредника прошло успешно",
          code: 3,
        }
        state.authAgent.agent.avatar = action.payload
        state.progress = false;
      })
      .addCase(postAvatar.pending, (state, action) => {
        state.progress = true
      })
      .addCase(postAvatar.rejected, (state, action) => {
        state.status = {
          message: `Не удалось обновить изображения профиля посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(postImages.fulfilled, (state, action) => {
        state.status = {
          message: "Загрузка изображений профиля посредника прошло успешно",
          code: 3,
        }
        state.authAgent.agent.images = (state.authAgent.agent.images 
          ? [...state.authAgent.agent.images, ...action.payload]
          : action.payload)
        state.progress = false;
      })
      .addCase(postImages.pending, (state, action) => {
        state.progress = true
      })
      .addCase(postImages.rejected, (state, action) => {
        state.status = {
          message: `Не удалось загрузить изображения посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(removeImage.fulfilled, (state, action) => {
        state.status = {
          message: "Изображений успешно удалено",
          code: 3,
        }
        state.authAgent.agent.images = state.authAgent.agent.images.filter((img) => (
          img.id != action.payload
        ))
        state.progress = false;
      })
      .addCase(removeImage.pending, (state, action) => {
        state.progress = true
      })
      .addCase(removeImage.rejected, (state, action) => {
        state.status = {
          message: `Не удалось удалить изображение посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchSelectedBriefAgent.fulfilled, (state, action) => {
        if (action.payload === '') {
          state.selectedAgent = null
        } else {
          state.selectedAgent = action.payload;
        }
        state.progress = false;
      })
      .addCase(fetchSelectedBriefAgent.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchSelectedBriefAgent.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить данные посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchAgentForOrder.fulfilled, (state, action) => {
        state.orderAgent = action.payload
        state.progress = false;
      })
      .addCase(fetchAgentForOrder.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchAgentForOrder.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить данные посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(postAgentSelection.fulfilled, (state, action) => {
        if (state.selectedAgent?.agentId != action.payload?.agentId) {
          state.selectedAgent = undefined;
        }
        state.progress = false;
      })
      .addCase(postAgentSelection.pending, (state, action) => {
        state.progress = true
      })
      .addCase(postAgentSelection.rejected, (state, action) => {
        state.status = {
          message: `Не удалось сохранить посредника для заказа ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(removeAgentSelection.fulfilled, (state, action) => {
        state.selectedAgent = null;
        state.progress = false;
      })
      .addCase(removeAgentSelection.pending, (state, action) => {
        state.progress = true
      })
      .addCase(removeAgentSelection.rejected, (state, action) => {
        state.status = {
          message: `Не удалось удалить выбранного посредника ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })
  }
});

export const selectAgents = (state) => state.agent.agents;
export const selectCurrentAgent = (state) => state.agent.currentAgent;
export const selectAuthAgent = (state) => state.agent.authAgent;
export const selectAgentSelection = (state) => state.agent.selectedAgent;
export const selectOrderAgent = (state) => state.agent.orderAgent;

export default agentSlice.reducer;
export const { resetStatus } = agentSlice.actions;