<template>
  <div class="register-container">
    <el-card class="register-card" shadow="never" v-loading="loading">
      <div slot="header" style="text-align: center">
        注册
        <el-button
          style="float: left; padding: 3px 0; color: black;"
          type="text"
          icon="el-icon-arrow-left"
          @click="$router.replace('login')"
        >
        </el-button>
      </div>

      <el-form
        ref="registerForm"
        label-width="0"
        :model="registerForm"
        :rules="registerRules"
      >
        <el-form-item>
          <el-input
            placeholder="学号"
            v-model="registerForm.sid"
            autocomplete="off"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            placeholder="一卡通号"
            v-model="registerForm.cardId"
            autocomplete="off"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            placeholder="姓名"
            v-model="registerForm.name"
            autocomplete="off"
          />
        </el-form-item>

        <!-- 按钮 -->
        <el-form-item>
          <el-button type="primary" style="width: 100%;" @click="onSubmit"
            >注册</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import removeToken from "@/utils/storage";
import authApi from "@/api/auth";

export default {
  data() {
    return {
      registerForm: {
        sid: "",
        cardId: "",
        name: "",
        code: "CODE"
      },
      registerRules: {
        sid: [{ required: true, trigger: "blur", message: "用户名不能为空" }],
        cardId: [
          { required: true, trigger: "blur", message: "一卡通号不能为空" }
        ],
        name: [{ required: true, trigger: "blur", message: "姓名不能为空" }]
        //code: [{ required: true, trigger: "change", message: "验证码不能为空" }]
      },
      loading: false
    };
  },
  methods: {
    async onSubmit() {
      try {
        this.loading = true;
        await authApi.register(this.registerForm);
        this.$message.success("注册成功");
        this.$router.replace("/login");
      } finally {
        this.loading = false;
      }
    },
    created() {
      removeToken();
    }
  }
};
</script>

<style lang="scss" scoped>
.register-container {
  background: #2b4b6b;
  height: 100%;
  background-image: url("../assets/image/login-background.jpg");
  background-size: cover;
  background-position-x: 20%;
  background-position-y: 10%;
}

.register-card {
  max-width: 450px;
  width: 80%;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
</style>
