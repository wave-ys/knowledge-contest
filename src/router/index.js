import Vue from "vue";
import VueRouter from "vue-router";
import constants from "@/constants.js";
import store from "@/store";

Vue.use(VueRouter);

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch(err => err);
};

const originalReplace = VueRouter.prototype.replace;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalReplace.call(this, location, onResolve, onReject);
  return originalReplace.call(this, location).catch(err => err);
};

const AdminHome = resolve => require(["@/views/admin/Home.vue"], resolve);
const StudentHome = resolve => require(["@/views/student/Home.vue"], resolve);

const StudentIndex = resolve =>
  require(["@/views/student/pages/Welcome.vue"], resolve);
const Exam = resolve => require(["@/views/student/pages/Exam.vue"], resolve);
const Details = resolve =>
  require(["@/views/student/pages/Details.vue"], resolve);

const Statistics = resolve =>
  require(["@/views/admin/pages/Statistics.vue"], resolve);
const StudentList = resolve =>
  require(["@/views/admin/pages/StudentList.vue"], resolve);
// const GroupList = resolve =>
//   require(["@/views/admin/pages/GroupList.vue"], resolve);
const AdminList = resolve =>
  require(["@/views/admin/pages/AdminList.vue"], resolve);
const ChoiceList = resolve =>
  require(["@/views/admin/pages/ChoiceQuestionList.vue"], resolve);
// const JudgeList = resolve =>
//   require(["@/views/admin/pages/JudgeQuestionList.vue"], resolve);
// const ChangePassword = resolve =>
//   require(["@/views/admin/pages/ChangePassword.vue"], resolve);

export const menuList = [
  {
    path: "/admin/statistics",
    name: "statistics",
    component: Statistics,
    meta: {
      title: "数据统计",
      icon: "el-icon-s-data",
      role: [constants.ROLE_ADMIN],
      authority: [constants.AUTH_ALL, constants.AUTH_PART]
    }
  },
  {
    path: "/admin/students",
    name: "students",
    component: StudentList,
    meta: {
      title: "学生列表",
      icon: "el-icon-s-grid",
      role: [constants.ROLE_ADMIN],
      authority: [constants.AUTH_ALL, constants.AUTH_PART]
    }
  },
  // {
  //   path: "/admin/groups",
  //   name: "groups",
  //   component: GroupList,
  //   meta: {
  //     title: "班级列表",
  //     icon: "el-icon-s-grid",
  //     role: [constants.ROLE_ADMIN],
  //     authority: [constants.AUTH_ALL]
  //   }
  // },
  {
    path: "/admin/admins",
    name: "admins",
    component: AdminList,
    meta: {
      title: "管理员列表",
      icon: "el-icon-s-grid",
      role: [constants.ROLE_ADMIN],
      authority: [constants.AUTH_ALL]
    }
  },
  {
    path: "/admin/choice",
    name: "choice",
    component: ChoiceList,
    meta: {
      title: "选择题列表",
      icon: "el-icon-s-grid",
      role: [constants.ROLE_ADMIN],
      authority: [constants.AUTH_ALL]
    }
  }
  // {
  //   path: "/admin/judge",
  //   name: "judge",
  //   component: JudgeList,
  //   meta: {
  //     title: "判断题列表",
  //     icon: "el-icon-s-grid",
  //     role: [constants.ROLE_ADMIN],
  //     authority: [constants.AUTH_ALL]
  //   }
  // },
  // {
  //   path: "/admin/changePassword",
  //   name: "changePassword",
  //   component: ChangePassword,
  //   meta: {
  //     title: "修改密码",
  //     icon: "el-icon-lock",
  //     role: [constants.ROLE_ADMIN],
  //     authority: [constants.AUTH_ALL, constants.AUTH_PART]
  //   }
  // }
];

export const routes = [
  {
    path: "/admin/home",
    name: "admin-home",
    redirect: "/admin/statistics",
    component: AdminHome,
    children: menuList,
    meta: {
      role: [constants.ROLE_ADMIN],
      status: [constants.AUTH_ALL, constants.AUTH_PART]
    }
  },
  {
    path: "/student/home",
    name: "student-home",
    redirect: "student/index",
    component: StudentHome,
    meta: {
      role: [constants.ROLE_STUDENT],
      status: [
        constants.STATUS_NOT_START,
        constants.STATUS_GENERATED,
        constants.STATUS_STARTED,
        constants.STATUS_SUBMITTED
      ]
    },
    children: [
      {
        path: "/student/index",
        component: StudentIndex,
        meta: {
          role: [constants.ROLE_STUDENT],
          status: [
            constants.STATUS_NOT_START,
            constants.STATUS_GENERATED,
            constants.STATUS_STARTED,
            constants.STATUS_SUBMITTED
          ]
        }
      },
      {
        path: "/student/exam/:id",
        name: "exam",
        component: Exam,
        meta: {
          role: [constants.ROLE_STUDENT],
          status: [
            constants.STATUS_NOT_START,
            constants.STATUS_GENERATED,
            constants.STATUS_STARTED
          ]
        }
      },
      {
        path: "/student/details",
        name: "details",
        component: Details,
        meta: {
          role: [constants.ROLE_STUDENT],
          status: [constants.STATUS_SUBMITTED]
        }
      }
    ]
  }
];

const router = new VueRouter({
  routes
});

router.beforeEach(async (to, from, next) => {
  if (to.path == "/") return next();

  // if (store.state.user.id == -1) {
  //   await store.dispatch("user/getInfo");
  // }

  const role = store.state.user.role;
  const status = store.state.user.status;
  const authority = store.state.user.authority;

  if (
    to.meta.role.some(r => r == role) &&
    ((role == constants.ROLE_ADMIN &&
      to.meta.authority.some(s => s == authority)) ||
      (role == constants.ROLE_STUDENT && to.meta.status.some(s => s == status)))
  )
    return next();

  next({
    name: "login",
    query: { redirect: to.meta.redirect }
  });
});

export default router;
