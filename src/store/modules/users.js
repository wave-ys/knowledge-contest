import { getToken, setToken } from "@/utils/storage";
import authApi from "@/api/auth";
import userApi from "@/api/user";
import { getDepartmentBySid } from "@/utils/department";
import constants from "@/constants";

const user = {
  namespaced: true,
  state: {
    token: getToken(),
    name: "",
    id: -1,
    sid: "",
    cardId: "",
    role: "",
    status: "",
    department: ""
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_INFO: (state, info) => {
      state.name = info.name || state.name;
      state.id = info.id || state.id;
      state.sid = info.sid || state.sid;
      state.cardId = info.cardId || state.cardId;
      state.role = info.role || state.role;
      state.status = info.status || state.status;
      if (info.department)
        if (state.role != constants.ROLE_ADMIN)
          state.department = getDepartmentBySid(info.sid);
        else state.department = info.department;
    },
    CLEAR_INFO: state => {
      state.token = getToken();
      state.name = "";
      state.id = -1;
      state.sid = "";
      state.cardId = "";
      state.role = "";
      state.status = "";
      state.department = "";
    }
  },

  actions: {
    // 登录
    login({ commit }, userInfo) {
      userInfo.sid = userInfo.sid.trim();
      return new Promise((resolve, reject) => {
        authApi
          .login(userInfo)
          .then(res => {
            setToken(res.token);
            commit("SET_TOKEN", res.token);
            resolve();
          })
          .catch(error => {
            reject(error);
          });
      });
    },
    getInfo({ commit }) {
      return new Promise((resolve, reject) => {
        userApi
          .getInfo()
          .then(res => {
            commit("SET_INFO", res);
            resolve(res);
          })
          .catch(error => {
            reject(error);
          });
      });
    }
  }
};

export default user;
