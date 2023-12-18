import { getToken } from "@/utils/storage";
// import authApi from "@/api/auth";
import userApi from "@/api/user";

const user = {
  namespaced: true,
  state: {
    token: getToken(),
    name: "",
    id: -1,
    studentId: "",
    cardId: "",
    role: "",
    status: "",
    authority: "",
    group: "（无）",
    department: "（无）"
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_INFO: (state, info) => {
      state.name = info.name || state.name;
      state.id = info.id || state.id;
      state.studentId = info.studentId || state.studentId;
      state.cardId = info.cardId || state.cardId;
      state.role = info.role || state.role;
      state.status = info.status || state.status;
      state.authority = info.authority || state.authority;
      state.group = info.group || state.group;
      state.department = info.department || state.department;
    },
    CLEAR_INFO: state => {
      state.token = getToken();
      state.name = "";
      state.id = -1;
      state.studentId = "";
      state.cardId = "";
      state.role = "";
      state.status = "";
      state.authority = "";
      state.group = "（无）";
      state.department = "（无）";
    }
  },

  actions: {
    // 登录
    // async login({ commit }) {
    //   const ticket = getQueryString("ticket");
    //   if (ticket) {
    //     const res =
    //   }

    //   // return new Promise((resolve, reject) => {
    //   //   authApi
    //   //     .login(userInfo)
    //   //     .then(res => {
    //   //       setToken(res.token);
    //   //       commit("SET_TOKEN", res.token);
    //   //       resolve();
    //   //     })
    //   //     .catch(error => {
    //   //       reject(error);
    //   //     });
    //   // });
    // },
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
