import axios, { AxiosInstance, AxiosRequestConfig, AxiosError } from "axios";

export class Axios {
    private readonly axiosInstance: AxiosInstance;

    constructor() {
        this.axiosInstance = axios.create();

        this.axiosInstance.interceptors.request.use(
            (config: AxiosRequestConfig): AxiosRequestConfig => {
                const token = localStorage.getItem("token") ?? sessionStorage.getItem("token");
                if (token) {
                    config.headers = {
                        ...config.headers,
                        'Authorization': `Bearer ${token}`,
                    };
                } else {
                    if(process.env.NODE_ENV === "development") {
                        console.warn("No token found in localStorage or sessionStorage.");
                    }

                }
                return config;
            },
            (error: AxiosError): Promise<AxiosError> => {
                console.error("Error in request interceptor:", error);
                return Promise.reject(error);
            }
        );
    }

    public getInstance(): AxiosInstance {
        return this.axiosInstance;
    }
}
