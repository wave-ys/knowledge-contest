<template>
  <div class="login-container">
    <el-card class="login-card" shadow="never" v-loading="loading">
      <div slot="header" style="text-align: center">
        东南大学校史校情知识竞赛
      </div>

      <el-form
        ref="loginForm"
        label-width="0"
        :model="loginForm"
        :rules="loginRules"
      >
        <!-- 用户名 -->
        <el-form-item>
          <el-input
            prefix-icon="el-icon-user"
            placeholder="账号"
            v-model="loginForm.sid"
          />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item>
          <el-input
            prefix-icon="el-icon-lock"
            placeholder="密码"
            v-model="loginForm.password"
            type="password"
            autocomplete="off"
          />
        </el-form-item>

        <!-- 按钮 -->
        <el-form-item style="position: relative; width: 100%">
          <el-button type="primary" style="width: 45%;" @click="onSubmit">
            登录
          </el-button>
          <el-button
            style="position: absolute; width: 45%; right: 0"
            @click="onRegister"
          >
            注册
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import constants from "@/constants";
import { removeToken } from "@/utils/storage";

export default {
  data() {
    return {
      loginForm: {
        sid: "",
        password: "",
        code: "CODE"
      },
      loginRules: {
        sid: [{ required: true, trigger: "blur", message: "用户名不能为空" }],
        password: [{ required: true, trigger: "blur", message: "密码不能为空" }]
        //code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
      },
      loading: false
    };
  },
  methods: {
    async onSubmit() {
      this.$refs.loginForm.validate(async valid => {
        if (!valid) return;
        this.loading = true;

        try {
          await this.$store.dispatch("user/login", this.loginForm);
          if (this.$store.state.user.id == -1)
            await this.$store.dispatch("user/getInfo");
          const push =
            this.$store.state.user.role === constants.ROLE_STUDENT
              ? "/student/home"
              : "/admin/home";

          this.$router.push({ path: this.redirect || push });
        } finally {
          this.loading = false;
        }
      });
    },
    onRegister() {
      this.$router.push("/register");
    }
  },
  created() {
    this.$store.commit("user/CLEAR_INFO");
    removeToken();
  }
};
</script>

<style lang="scss" scoped>
.login-container {
  background: #2b4b6b;
  height: 100%;
  background-image: url("../assets/image/login-background.jpg");
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
