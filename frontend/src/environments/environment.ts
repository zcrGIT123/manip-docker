export const environment = {
    production: true,
    baseUrl: (window as any)['env']['backendBaseUrl']  || "http://localhost:8080/"
};