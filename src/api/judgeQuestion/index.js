import request from "@/utils/request";

export default {
  getQuestionList() {
    return request({
      url: "/judge/list",
      method: "get"
    });
  },
  getQuestionPage(current, size) {
    return request({
      url: "/judge/page",
      method: "post",
      data: {
        pageIndex: current,
        pageSize: size
      }
    });
  },
  editQuestion(question) {
    return request({
      url: "/judge/edit",
      method: "put",
      data: question
    });
  },
  insertQuestion(question) {
    console.log(question);
    return request({
      url: "/judge/insert",
      method: "put",
      data: question
    });
  },
  deleteQuestion(id) {
    return request({
      url: "judge/" + id,
      method: "delete"
    });
  },
  deleteQuestions(ids) {
    return request({
      url: "judge",
      method: "delete",
      data: ids
    });
  },
  importAndInsert(formData) {
    return request({
      url: "judge/import/insert",
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
      url: "judge/import/cover",
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
      url: "judge/count",
      method: "get"
    });
  },
  deleteAll() {
    return request({
      url: "judge/all",
      method: "delete"
    });
  }
};
