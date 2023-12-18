<template>
  <div style="height: 100%">
    <el-container style="height: 100%">
      <!-- 页头 -->
      <el-header height="60px">
        <div class="home-header-container">
          <h1 class="home-header-title">东南大学大学生手册考试</h1>
          <span>
            <span>欢迎您，管理员 {{ userInfo.name }} ！</span>
            <!-- <span class="exit" @click="logout()">退出</span> -->
          </span>
        </div>
      </el-header>
      <el-container>
        <!-- 导航栏 -->
        <el-aside width="200px">
          <el-menu :default-active="$route.matched[1].path" router>
            <el-menu-item
              v-for="item in menuList"
              :key="item.path"
              :index="item.path"
            >
              <template v-slot:title>
                <i :class="item.meta.icon"></i>
                <span>{{ item.meta.title }}</span>
              </template>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <!-- 内容页面 -->
        <el-main><router-view></router-view></el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { removeToken } from "@/utils/storage";
import constants from "@/constants.js";
import { menuList } from "@/router";

export default {
  data() {
    return {
      constants
    };
  },
  created() {
    if (this.$store.state.user.id == -1) this.$store.dispatch("user/getInfo");
  },
  methods: {
    logout() {
      removeToken();
      this.$store.commit("user/CLEAR_INFO");
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    },
    menuList() {
      const res = menuList.filter(
        value =>
          value.meta.role.some(s => s === this.userInfo.role) &&
          value.meta.authority.some(s => s === this.userInfo.authority)
      );
      return res;
    }
  }
};
</script>

<style lang="scss" scoped>
.el-header {
  background-color: #fff;
  width: 100%;
  padding: 0;
  z-index: 10;

  .home-header-container {
    box-shadow: 0 2px 8px #f0f1f2;
    height: 100%;
    justify-content: space-between;
    align-items: center;
    display: flex;
    padding-left: 40px;
    padding-right: 40px;

    .home-header-title {
      display: flex;
    }

    span {
      display: flex;
      justify-content: space-around;

      * {
        margin: 10px;
      }
    }
  }

  .exit {
    text-decoration: none;
    color: #1989fa;
    cursor: pointer;
  }
}

.el-aside {
  height: 100%;
  box-shadow: 0 2px 8px #f0f1f2;

  .el-menu {
    height: 100%;
  }

  .el-menu-item.is-active {
    background-color: #ffffff;
  }
}
</style>
