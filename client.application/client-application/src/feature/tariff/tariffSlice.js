import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { middleware } from "../middleware";
import { getAgentTariffs, getTariff, createTariff, updateTariff, deleteTariff, 
  getCurrencyRates, saveCurrencyRates, calculateOrderCost, getAgentsBriefTariffs
} from "../../api/tariffApi"

export const fetchAgentTariffs = createAsyncThunk('tariff/fetchAgentTariffs', async (params, thunk) => {
  return await middleware(getAgentTariffs, params, thunk, { }, false);
});

export const fetchAgentsBriefTariffs = createAsyncThunk('tariff/fetchAgentsBriefTariffs', async (params, thunk) => {
  return await middleware(getAgentsBriefTariffs, params, thunk, { }, false);
});

export const fetchTariffsForOrder = createAsyncThunk('tariff/fetchSelectedAgentTariffs', async (params, thunk) => {
  return await middleware(getAgentTariffs, params, thunk, { }, false);
});

export const fetchTariff = createAsyncThunk('tariff/fetchTariff', async (params, thunk) => {
  return await middleware(getTariff, params, thunk, { }, false);
});

export const postTariff = createAsyncThunk('tariff/postTariff', async (params, thunk) => {
  return await middleware(createTariff, params, thunk, { });
});

export const editTariff = createAsyncThunk('tariff/editTariff', async (params, thunk) => {
  return await middleware(updateTariff, params, thunk, { });
});

export const removeTariff = createAsyncThunk('tariff/removeTariff', async (params, thunk) => {
  return await middleware(deleteTariff, params, thunk, { });
});

export const fetchCurrencyRates = createAsyncThunk('tariff/fetchCurrencyRates', async (params, thunk) => {
  return await middleware(getCurrencyRates, params, thunk, { }, false);
});

export const postCurrencyRates = createAsyncThunk('tariff/postCurrencyRates', async (params, thunk) => {
  return await middleware(saveCurrencyRates, params, thunk, { });
});

export const calcOrderCost = createAsyncThunk('tariff/calcOrderCost', async (params, thunk) => {
  return await middleware(calculateOrderCost, params, thunk, { });
});



const tariffSlice = createSlice({
  name: 'tariff',
  initialState: {
    currentTariffs: undefined,
    searchFeedTariffs: undefined,
    selectedAgentTariffs: undefined,
    briefTariffs: undefined,
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
      .addCase(fetchAgentTariffs.fulfilled, (state, action) => {
        console.log(action.payload)
        state.currentTariffs = {
          agentId: action.meta.arg.agentId,
          tariffs: action.payload
        }
        state.progress = false;
      })
      .addCase(fetchAgentTariffs.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchAgentTariffs.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить тарифы ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })


      .addCase(fetchTariffsForOrder.fulfilled, (state, action) => {
        console.log(action.payload)
        state.selectedAgentTariffs = {
          agentId: action.meta.arg.agentId,
          tariffs: action.payload
        }
        state.progress = false;
      })
      .addCase(fetchTariffsForOrder.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchTariffsForOrder.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить тарифы ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchAgentsBriefTariffs.fulfilled, (state, action) => {
        console.log(action.payload)
        state.briefTariffs = {
          page: action.meta.arg.page,
          tariffs: action.payload
        }
        state.progress = false;
      })
      .addCase(fetchAgentsBriefTariffs.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchAgentsBriefTariffs.rejected, (state, action) => {
        state.status = {
          message: `Не удалось получить тарифы ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })
  }

})

export const selectAgentTariffs = (state) => state.tariff.currentTariffs;
export const selectTariffsForOrder = (state) => state.tariff.selectedAgentTariffs;
export const selectAgentsBriefTariffs = (state) => state.tariff.briefTariffs;

export default tariffSlice.reducer;
export const { resetStatus } = tariffSlice.actions;