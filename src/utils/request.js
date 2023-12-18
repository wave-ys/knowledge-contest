import axios from "axios";
import router from "@/router";
import { getToken } from "@/utils/storage";
import Vue from "vue";

axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8";

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 10000
});

service.interceptors.request.use(
  config => {
    config.headers["Authorization"] = "Bearer " + getToken();
    return config;
  },
  error => {
    Promise.reject(error);
  }
);

service.interceptors.response.use(
  response => {
    return response.data;
  },
  error => {
    const { response } = error;
    if (
      response.status === 401 &&
      window.location.toString().substr(-5) !== "login"
    ) {
      router.push("/login");
      response.data = "登录信息失效，请重新登录";
    } else if (!response.data) response.data = "操作失败，请重试";
    Vue.prototype.$message.error(response.data);
    return Promise.reject(error);
  }
);

export default service;
