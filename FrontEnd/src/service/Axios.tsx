import axios, { AxiosInstance, AxiosRequestConfig, AxiosError } from "axios";

export class Axios {
    private readonly axiosInstance: AxiosInstance;

    /**
     * Constructs an instance of `Axios` and sets up an interceptor to inject the user's
     * authentication token into the `Authorization` header of requests.
     *
     * The interceptor will check for a token in both `localStorage` and `sessionStorage`.
     * If a token is found, it will be added to the headers of the request. Otherwise,
     * a warning will be logged to the console.
     *
     * The interceptor will also catch any errors that occur in the request and log them
     * to the console.
     */
    constructor() {
        this.axiosInstance = axios.create({
            baseURL: 'http://localhost:8080',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        this.axiosInstance.interceptors.request.use(
            (config: AxiosRequestConfig): AxiosRequestConfig => {
                const token = localStorage.getItem("token") ?? sessionStorage.getItem("token");
                if (token) {
                    config.headers = {
                        ...config.headers, // Preserve any existing headers
                        'Authorization': `Bearer ${token}`,
                    };
                    console.log("Authorization header set:", config.headers['Authorization']);
                } else {
                    console.warn("No token found in localStorage or sessionStorage.");
                }
                return config;
            },
            (error: AxiosError): Promise<AxiosError> => {
                console.error("Error in request interceptor:", error);
                return Promise.reject(error);
            }
        );
    }

    /**
     * Gets the underlying instance of `AxiosInstance`.
     *
     * @returns The underlying instance of `AxiosInstance`.
     */
    public getInstance(): AxiosInstance {
        return this.axiosInstance;
    }
}
