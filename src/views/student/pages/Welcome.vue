<template>
  <div class="container">
    <van-nav-bar title="东南大学校史校情知识竞赛" @click-right="onClickRight">
      <template #right>
        <van-icon name="user-o" size="18" color="black" />
      </template>
    </van-nav-bar>

    <van-popup
      v-model="showPopup"
      position="right"
      :style="{ height: '100%', width: '70%', maxWidth: '300px' }"
      v-loading="!this.$store.state.user.id"
    >
      <van-cell-group title="学生信息">
        <van-cell title="姓名" :value="userInfo.name" />
        <van-cell title="学号" :value="userInfo.sid" />
        <van-cell title="一卡通号" :value="userInfo.cardId" />
        <van-cell title="院系" :value="userInfo.department" />
      </van-cell-group>

      <van-row style="padding: 1rem">
        <el-button style="width: 100%" type="danger" @click="onLogout">
          退出
        </el-button>
      </van-row>
    </van-popup>

    <div
      class="welcome"
      v-if="userInfo.status !== constants.STATUS_SUBMITTED"
      v-loading="loading"
    >
      <div class="card">
        <h3>欢迎您，{{ userInfo.name }} 同学！</h3>
        <p>
          本次竞赛共有
          {{ constants.CHOICE_QUESTION_NUM + constants.JUDGE_QUESTION_NUM }}
          小题，包含：
        </p>
        <ul>
          <li>
            选择题 {{ constants.CHOICE_QUESTION_NUM }} 题，每题
            {{ constants.CHOICE_QUESTION_SCORE }} 分。
          </li>
          <li>
            判断题 {{ constants.JUDGE_QUESTION_NUM }} 题，每题
            {{ constants.JUDGE_QUESTION_SCORE }} 分。
          </li>
        </ul>
        <p>时间限制为 {{ constants.TIME_LIMIT / 60 / 1000 }} 分钟。</p>
        <p style="color: red">答题过程中请不要中途退出。</p>

        <div>
          <el-button type="primary" @click="onStartExam">
            开始答题
          </el-button>
        </div>
      </div>
    </div>

    <div class="welcome" v-else v-loading="loading">
      <div class="card">
        <h3>欢迎您，{{ userInfo.name }} 同学！</h3>
        <p>您的成绩：</p>
        <p style="font-size: 4rem; color: red">{{ score }}</p>
        <el-button type="primary" @click="onGetDetails">
          查看详情
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { removeToken } from "@/utils/storage";
import paperApi from "@/api/paper";
import constants from "@/constants";

export default {
  data() {
    return {
      showPopup: false,
      constants,
      score: 0,
      loading: false
    };
  },
  methods: {
    onGetDetails() {
      this.$router.push("/student/details");
    },
    onClickRight() {
      this.showPopup = !this.showPopup;
    },
    onStartExam() {
      this.$router.replace("/student/exam/1");
    },
    onLogout() {
      removeToken();
      this.$router.replace("/login");
      this.$store.commit("user/CLEAR_INFO");
    }
  },
  async created() {
    this.loading = true;
    if (!this.$store.state.user.id) await this.$store.dispatch("user/getInfo");
    if (this.userInfo.status === constants.STATUS_SUBMITTED)
      this.score = await paperApi.getScore();
    this.loading = false;
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  background-color: rgb(249, 249, 249);
}

.welcome {
  text-align: center;
  flex-grow: 1;
  flex-shrink: 1;

  display: flex;
  flex-direction: column;
  justify-content: center;
}

.card {
  margin: 0 auto;
  width: 70%;
  padding: 24px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 12px #ebedf0;
}
</style>
