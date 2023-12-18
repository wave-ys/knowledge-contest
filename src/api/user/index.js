import request from "@/utils/request";

export default {
  getInfo() {
    return request({
      url: "/user",
      method: "get"
    });
  },
  getStudentPage(current, size) {
    return request({
      url: "/user/student/page",
      method: "post",
      data: {
        pageIndex: current,
        pageSize: size
      }
    });
  },
  editStudent(student) {
    return request({
      url: "user/student/edit",
      method: "put",
      data: student
    });
  },
  deleteStudent(id) {
    return request({
      url: "user/student/" + id,
      method: "delete"
    });
  },
  deleteStudents(ids) {
    return request({
      url: "user/student",
      method: "delete",
      data: ids
    });
  },
  insertStudent(student) {
    return request({
      url: "user/student/insert",
      method: "put",
      data: student
    });
  },
  changePassword(data) {
    return request({
      url: "user/password",
      method: "put",
      data
    });
  },
  queryStudent(pageIndex, pageSize, queryType, queryValue) {
    return request({
      url: "user/student/page",
      method: "post",
      data: {
        pageIndex,
        pageSize,
        queryType,
        queryValue
      }
    });
  },
  exportStudentList() {
    // return request.get(
    //   "user/student/export",
    //   {
    //     responseType: "blob"
    //   },
    //   {
    //     timeout: 0
    //   }
    // );
    return request({
      method: "get",
      url: "user/student/export",
      responseType: "blob",
      timeout: 0
    });
  },
  exportStatistics() {
    return request({
      method: "get",
      url: "user/admin/statistics/export",
      responseType: "blob",
      timeout: 0
    });
  },
  importStudentAndInsert(formData) {
    return request({
      url: "user/student/import/insert",
      data: formData,
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      timeout: 90000
    });
  },
  importStudentAndCover(formData) {
    return request({
      url: "user/student/import/cover",
      data: formData,
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      timeout: 90000
    });
  },
  getCount() {
    return request({
      url: "user/student/count",
      method: "get"
    });
  },
  deleteAll() {
    return request({
      url: "user/student/all",
      method: "delete"
    });
  },
  deleteAdmin(id) {
    return request({
      url: "user/admin/" + id,
      method: "delete"
    });
  },
  deleteAdmins(ids) {
    return request({
      url: "user/admin",
      method: "delete",
      data: ids
    });
  },
  deleteAllAdmin() {
    return request({
      url: "user/admin/all",
      method: "delete"
    });
  },
  getAdminPage(current, size) {
    return request({
      url: "/user/admin/page",
      method: "post",
      data: {
        pageIndex: current,
        pageSize: size
      }
    });
  },
  insertAdmin(data) {
    return request({
      url: "/user/admin/insert",
      method: "put",
      data
    });
  },
  editAdmin(data) {
    return request({
      url: "/user/admin",
      method: "put",
      data
    });
  },
  importAdminAndInsert(formData) {
    return request({
      url: "user/admin/import/insert",
      data: formData,
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      timeout: 90000
    });
  },
  importAdminAndCover(formData) {
    return request({
      url: "user/admin/import/cover",
      data: formData,
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      timeout: 90000
    });
  },
  getDepartmentList() {
    return request({
      url: "user/admin/department",
      method: "get"
    });
  },
  getDepartmentAdmins() {
    return request({
      url: "user/admin/",
      method: "get"
    });
  },
  getStatistics() {
    return request({
      url: "user/admin/statistics",
      method: "get",
      timeout: 0
    });
  }
};
