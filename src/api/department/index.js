import request from "@/utils/request";

export default {
  get() {
    return request({
      url: "/department",
      method: "get"
    });
  },
  getStatistics() {
    return request({
      url: "/department/statistics",
      method: "get"
    });
  },
  exportDepartmentStatistics() {
    return request.get("department/export", {
      responseType: "blob"
    });
  }
};
