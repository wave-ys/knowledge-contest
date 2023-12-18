import request from "@/utils/request";

export default {
  getQuestionList() {
    return request({
      url: "/choice/list",
      method: "get"
    });
  },
  getQuestionPage(current, size) {
    return request({
      url: "/choice/page",
      method: "post",
      data: {
        pageIndex: current,
        pageSize: size
      }
    });
  },
  editQuestion(question) {
    return request({
      url: "/choice/edit",
      method: "put",
      data: question
    });
  },
  insertQuestion(question) {
    return request({
      url: "/choice/insert",
      method: "put",
      data: question
    });
  },
  deleteQuestion(id) {
    return request({
      url: "choice/" + id,
      method: "delete"
    });
  },
  deleteQuestions(ids) {
    return request({
      url: "choice",
      method: "delete",
      data: ids
    });
  },
  importAndInsert(formData) {
    return request({
      url: "choice/import/insert",
      data: formData,
      method: "post",
      headers: {
        "Content-Type": "multipart/form-data"
      },
      timeout: 90000
    });
  },
  importAndCover(formData) {
    return request({
      url: "choice/import/cover",
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
      url: "choice/count",
      method: "get"
    });
  },
  deleteAll() {
    return request({
      url: "choice/all",
      method: "delete"
    });
  }
};
