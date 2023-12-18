<template>
  <div class="account-container">
    <el-form
      label-width="80px"
      :model="passwordForm"
      ref="passwordForm"
      :rules="rules"
      v-loading="loading"
    >
      <el-form-item label="原密码" prop="oldPassword">
        <el-input
          type="password"
          v-model="passwordForm.oldPassword"
          autocomplete="off"
        />
      </el-form-item>

      <el-form-item label="新密码" prop="newPassword">
        <el-input
          type="password"
          v-model="passwordForm.newPassword"
          autocomplete="off"
        />
      </el-form-item>

      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input
          type="password"
          v-model="passwordForm.confirmPassword"
          autocomplete="off"
        />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="submit()">保存</el-button>
        <el-button @click="reset()">清空</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import userApi from "@/api/user";

export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (this.passwordForm.confirmPassword !== "") {
        this.$refs.passwordForm.validateField("confirmPassword");
      }
      callback();
    };
    var validatePass2 = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error("两次输入需要保持一致"));
      } else {
        callback();
      }
    };
    return {
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      loading: false,
      rules: {
        oldPassword: [
          { required: true, message: "请输入原密码", trigger: "blur" }
        ],
        newPassword: [
          { validator: validatePass, trigger: "blur" },
          { required: true, message: "请输入新密码", trigger: "blur" },
          {
            min: 6,
            max: 18,
            message: "长度应在 6 到 18 个字符之间",
            trigger: "blur"
          }
        ],
        confirmPassword: [
          { required: true, message: "请再输入一次新密码", trigger: "blur" },
          { validator: validatePass2, triggered: "blur" }
        ]
      }
    };
  },
  methods: {
    submit() {
      this.$refs.passwordForm.validate(async valid => {
        if (valid) {
          this.loading = true;
          try {
            await userApi.changePassword(this.passwordForm);
            this.$message.success("修改密码成功");
          } finally {
            this.loading = false;
            this.reset();
          }
        }
      });
    },
    reset() {
      this.$refs.passwordForm.resetFields();
    }
  }
};
</script>

<style scoped>
.account-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.el-form {
  width: 500px;
  height: 300px;
}
</style>
