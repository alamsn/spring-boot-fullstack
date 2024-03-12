import axios from "axios";
import {errorNotification} from "./notfication.js";

export const instance = axios.create({
    baseURL: `${import.meta.env.VITE_API_BASE_URL}`
})
export const getCustomer = async () => {
    try {
        return await instance.get(`/api/v1/customers`)
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        return await instance.post('/api/v1/customers', customer);
    } catch (e) {
        throw e;

    }
}

export const deleteCustomer = async (id) => {
    try {
        return await instance.delete(`/api/v1/customers/${id}`, id);
    } catch (e) {
        throw e;

    }
}

export const updateCustomer = async (id, customerUpdateRequest) => {
    try {
        return await instance.put(`/api/v1/customers/${id}`, customerUpdateRequest);
    } catch (e) {
        throw e;
    }
}