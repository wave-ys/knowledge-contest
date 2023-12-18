import request from "@/utils/request";

export default {
  // login(data) {
  //   return request({
  //     url: "/auth/login",
  //     method: "post",
  //     data
  //   });
  // }
  login(data) {
    return request({
      url: "/auth/login",
      method: "post",
      data
    });
  }
};
