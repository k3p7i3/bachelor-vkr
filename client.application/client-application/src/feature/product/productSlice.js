import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { middleware } from "../middleware";
import { 
  getCart, 
  addProductToCart, 
  editCountForCartProduct, 
  deleteFromCart,
  getProducts
} from "../../api/productApi";

export const getUserCart = createAsyncThunk('product/getCart', async (params, thunk) => {
  return await middleware(getCart, params, thunk, { });
});

export const addCartProduct = createAsyncThunk('product/addProductToCart', async (params, thunk) => {
  return await middleware(addProductToCart, params, thunk, { });
});

export const editCount = createAsyncThunk('product/editCountForCartProduct', async (params, thunk) => {
  return await middleware(editCountForCartProduct, params, thunk, { });
});

export const removeFromCart = createAsyncThunk('product/deleteFromCart', async (params, thunk) => {
  return await middleware(deleteFromCart, params, thunk, { });
});

export const fetchProductsForOrder = createAsyncThunk('product/fetchProductsForOrder', async (params, thunk) => {
  return await middleware(getProducts, params, thunk, { });
});

export const fetchProductsForOrders = createAsyncThunk('product/fetchProductsForOrders', async (params, thunk) => {
  return await middleware(getProducts, params, thunk, { });
});


const productSlice = createSlice({
  name: 'product',
  initialState: {
    cart: undefined,
    favorites: undefined,
    orderProducts: undefined,
    orderListProducts: undefined,
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
    setOrderProducts: (state, action) => {
      state.orderProducts = {
        orderId: action.payload.orderId,
        products: action.payload.products
      }
    }
  },
  extraReducers(builder) {
    builder
      .addCase(getUserCart.fulfilled, (state, action) => {
        state.cart = action.payload
        state.progress = false;
      })
      .addCase(getUserCart.pending, (state, action) => {
        state.progress = true
      })
      .addCase(getUserCart.rejected, (state, action) => {
        state.status = {
          message: `Не удалось загрузить товары корзины ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(addCartProduct.fulfilled, (state, action) => {
        state.status = {
          message: "Регистрация пользователя прошла успешно",
          code: 3,
        }
        state.progress = false;
      })
      .addCase(addCartProduct.pending, (state, action) => {
        state.progress = true
      })
      .addCase(addCartProduct.rejected, (state, action) => {
        state.status = {
          message: `Не удалось добавить товар ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(editCount.fulfilled, (state, action) => {
        state.cart.products = state.cart.products.map((cartProduct) => {
          if (action.payload.id == cartProduct.id) {
            cartProduct.count = action.payload.count
          };
          return cartProduct;
        })
        state.progress = false;
      })
      .addCase(editCount.pending, (state, action) => {
        state.progress = true
      })
      .addCase(editCount.rejected, (state, action) => {
        state.status = {
          message: `Не удалось обновить количество товара ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(removeFromCart.fulfilled, (state, action) => {
        state.cart = {
          userId: state.cart.userId,
          products: state.cart.products.filter((cartProduct) => (
            cartProduct.id !== action.payload
          ))
        }
        state.progress = false;
      })
      .addCase(removeFromCart.pending, (state, action) => {
        state.progress = true
      })
      .addCase(removeFromCart.rejected, (state, action) => {
        state.status = {
          message: `Не удалось удалить товар из корзины ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchProductsForOrder.fulfilled, (state, action) => {
        state.orderProducts = {
          orderId: action.meta.arg.orderId,
          products: action.payload
        }
        state.progress = false;
      })
      .addCase(fetchProductsForOrder.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchProductsForOrder.rejected, (state, action) => {
        state.status = {
          message: `Не удалось загрузить товары ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })

      .addCase(fetchProductsForOrders.fulfilled, (state, action) => {
        state.orderListProducts = action.payload
        state.progress = false;
      })
      .addCase(fetchProductsForOrders.pending, (state, action) => {
        state.progress = true
      })
      .addCase(fetchProductsForOrders.rejected, (state, action) => {
        state.status = {
          message: `Не удалось загрузить товары ${action.payload.message}`,
          code: action.payload.code,
        }
        state.progress = false;
      })
  }
});

export const selectCart = (state) => state.product.cart;
export const selectOrderProducts = (state) => state.product.orderProducts;
export const selectOrderListProducts = (state) => state.product.orderListProducts;

export const { resetStatus, setOrderProducts } = productSlice.actions;
export default productSlice.reducer;
