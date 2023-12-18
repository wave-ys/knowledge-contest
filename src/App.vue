<template>
  <div id="app">
    <router-view v-if="this.$store.state.user.id != -1" />
    <div class="login-container" v-else>
      <el-card class="login-card" shadow="never">
        <div slot="header" style="text-align: center">
          东南大学大学生手册考试
        </div>

        <div
          style="height: 250px; display: flex; align-items: center; justifiy-content: center"
          v-loading="!failed"
        >
          <div v-if="failed" style="text-align: center; width: 100%">
            登录失败，系统中没有您的信息
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as qs from "querystring";
import authApi from "@/api/auth";
import { setToken } from "@/utils/storage";
import { isWechat } from "@/utils/device";
// import { removeToken } from "@/utils/storage";
import constants from "@/constants";

export default {
  name: "App",
  data() {
    return {
      failed: false
    };
  },
  async created() {
    const ticket = qs.parse(window.location.search.split("?")[1]).ticket;
    if (ticket) {
      try {
        const res = await authApi.login({ ticket });
        setToken(res.token);
        this.$store.commit("SET_TOKEN", res.token);
        window.location.search = "";
        return;
      } catch (e) {
        this.failed = true;
        return;
      }
    }

    try {
      await this.$store.dispatch("user/getInfo");
      const push =
        this.$store.state.user.role === constants.ROLE_STUDENT
          ? "/student/home"
          : "/admin/home";

      this.$router.push({ path: push });
    } catch (e) {
      this.login();
    }
  },
  computed: {
    hasUserInfo() {
      return this.$store.state.user.id != -1;
    }
  },
  watch: {
    hasUserInfo: function(newState) {
      if (newState == false) this.login();
    }
  },
  methods: {
    login() {
      if (isWechat) {
        window.location = `https://xgbxscwx.seu.edu.cn/cas-we-can/login?goto=${encodeURIComponent(
          "https://xgbxscwx.seu.edu.cn/manual/"
        )}`;
      } else {
        window.location = `https://newids.seu.edu.cn/authserver/login?goto=${encodeURIComponent(
          "https://xgbxscwx.seu.edu.cn/manual/"
        )}`;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  background: #2b4b6b;
  height: 100%;
  background-image: url("./assets/image/login-background.jpg");
  background-size: cover;
  background-position-x: 20%;
  background-position-y: 10%;
}

.login-card {
  max-width: 450px;
  width: 80%;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(255, 255, 255, 0.98);
}
</style>
