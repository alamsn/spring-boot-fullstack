import axios from "axios";

export const instance = axios.create({
    baseURL: `${import.meta.env.VITE_API_BASE_URL}`
})
export const getCustomer = async () => {
    try {
        return await instance.get(`/customers`)
    } catch (e) {
        console.log(e)
    }
}