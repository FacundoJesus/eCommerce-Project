import { configureStore } from "@reduxjs/toolkit";
import { productReducer } from "./ProductReducer";
import { errorReducer } from "./ErrorReducer";
import { cartReducer } from "./CartReducer";
import { authReducer } from "./AuthReducer";
import { paymentMethodReducer } from "./PaymentMethodReducer";
import { adminReducer } from "./AdminReducer";
import { orderReducer } from "./OrderReducer";
import { sellerReducer } from "./SellerReducer";

const user = localStorage.getItem("auth")
    ? JSON.parse(localStorage.getItem("auth"))
    : null;

const cartItems = localStorage.getItem("cartItems")
    ? JSON.parse(localStorage.getItem("cartItems"))
    : [];

const selectUserCheckoutAddress = localStorage.getItem("CHECKOUT_ADDRESS")
    ? JSON.parse(localStorage.getItem("CHECKOUT_ADDRESS"))
    : [];

const initialState = {
    auth: { user: user, selectUserCheckoutAddress },
    carts: { cart: cartItems },
};

export const store = configureStore({
    reducer: {
        products: productReducer,
        errors: errorReducer,
        carts: cartReducer,
        auth: authReducer,
        payment: paymentMethodReducer,
        admin: adminReducer,
        order: orderReducer,
        seller: sellerReducer,
    },
    preloadedState: initialState,
});

export default store;