import request from "@/utils/request";

export default {
  getGroupList() {
    return request({
      url: "/group",
      method: "get"
    });
  },
  deleteGroup(id) {
    return request({
      url: "group/" + id,
      method: "delete"
    });
  },
  deleteGroups(ids) {
    return request({
      url: "group",
      method: "delete",
      data: ids
    });
  },
  deleteAllGroup() {
    return request({
      url: "group/all",
      method: "delete"
    });
  },
  getGroupPage(current, size) {
    return request({
      url: "/group/page",
      method: "post",
      data: {
        pageIndex: current,
        pageSize: size
      }
    });
  },
  insertGroup(data) {
    return request({
      url: "/group/insert",
      method: "put",
      data
    });
  },
  editGroup(data) {
    return request({
      url: "/group/edit",
      method: "put",
      data
    });
  },
  importGroupAndInsert(formData) {
    return request({
      url: "group/import/insert",
      data: formData,
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      timeout: 90000
    });
  },
  importGroupAndCover(formData) {
    return request({
      url: "group/import/cover",
      data: formData,
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      timeout: 90000
    });
  }
};
