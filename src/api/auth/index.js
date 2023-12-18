import request from "@/utils/request";

export default {
  login(data) {
    return request({
      url: "/auth/login",
      method: "post",
      data
    });
  },
  register(data) {
    return request({
      url: "/auth/register",
      method: "post",
      data
    });
  }
};
