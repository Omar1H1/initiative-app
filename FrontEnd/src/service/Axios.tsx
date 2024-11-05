import axios, { AxiosInstance, AxiosRequestConfig } from "axios";


export class Axios {
    private readonly axiosInstance: AxiosInstance;

    constructor() {
        this.axiosInstance = axios.create({
            baseURL: 'http://localhost:8080',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        this.axiosInstance.interceptors.request.use(
            // @ts-ignore
            (config: AxiosRequestConfig) => {
                const token = localStorage.getItem("token") ?? sessionStorage.getItem("token");
                if (token) {
                    // @ts-ignore
                    config.headers['Authorization'] = token;
                }
                return config;
            },
            (error) => {
                return Promise.reject(error);
            }
        );
    }


    public getInstance(): AxiosInstance {
        return this.axiosInstance;
    }

}