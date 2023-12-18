import axios from "axios";
import { getToken, removeToken } from "@/utils/storage";
import store from "@/store";

import Vue from "vue";

axios.defaults.headers["Content-Type"] = "application/json;charset=utf-8";

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 20000
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
    if (response.status === 401) {
      store.commit("user/CLEAR_INFO");
      removeToken();
      return Promise.reject(error);
    } else if (!response.data) response.data = "操作失败，请重试";
    Vue.prototype.$message.error(response.data);
    return Promise.reject(error);
  }
);

export default service;
