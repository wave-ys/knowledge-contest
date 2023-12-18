<template>
  <div v-loading.fullscreen.lock="loading" class="container">
    <van-nav-bar title="东南大学校史校情知识竞赛" @click-right="onClickRight">
      <template #right>
        <div style="font-size: 18px">
          <i class="el-icon-s-fold"></i>
        </div>
      </template>
    </van-nav-bar>

    <van-popup
      v-model="showPopup"
      position="right"
      :style="{ height: '100%', width: '70%', maxWidth: '300px' }"
      v-loading="!this.$store.state.user.id"
    >
      <van-cell-group title="答题信息">
        <van-grid :column-num="3" :border="false" clickable>
          <van-grid-item
            v-for="value in totalQuestion"
            :key="value"
            :to="'/student/exam/' + value"
            :replace="true"
            @click="showPopup = false"
            :class="{ 'grid-item-selected': $route.params.id == value }"
          >
            <template #icon>
              <div>
                <div
                  :class="[
                    'grid-item',
                    {
                      'grid-item-not-choose': notChoosed(value)
                    }
                  ]"
                >
                  {{ value }}
                </div>
              </div>
            </template>
          </van-grid-item>
        </van-grid>
      </van-cell-group>
    </van-popup>

    <div class="context">
      <div class="card">
        <van-cell :title="`#${$route.params.id} ${currentType}（${score}分）`">
          <template #default>
            <van-count-down
              :time="remainTime"
              @finish="submit"
              v-show="!loading"
            >
              <template #default="timeData">
                <span class="block">{{
                  formatTimeNumber(timeData.hours)
                }}</span>
                <span class="colon">:</span>
                <span class="block">{{
                  formatTimeNumber(timeData.minutes)
                }}</span>
                <span class="colon">:</span>
                <span class="block">{{
                  formatTimeNumber(timeData.seconds)
                }}</span>
              </template>
            </van-count-down>
          </template>
        </van-cell>

        <div class="question">
          <h3 class="question-head">
            {{ currentQuestion.question }}
          </h3>

          <div class="question-body">
            <van-radio-group
              v-if="currentType == '选择题'"
              v-model="selectedAnswer[0]"
              @change="onCurrentAnswerChanged"
              :disabled="submitting"
            >
              <van-radio :name="0">
                {{ "A. " + currentQuestion.choiceA }}
              </van-radio>
              <van-radio :name="1">
                {{ "B. " + currentQuestion.choiceB }}
              </van-radio>
              <van-radio :name="2">
                {{ "C. " + currentQuestion.choiceC }}
              </van-radio>
              <van-radio :name="3">
                {{ "D. " + currentQuestion.choiceD }}
              </van-radio>
            </van-radio-group>

            <van-radio-group
              v-else
              v-model="selectedAnswer[0]"
              @change="onCurrentAnswerChanged"
              :disabled="submitting"
            >
              <van-radio :name="1">
                A. 正确
              </van-radio>
              <van-radio :name="0">
                B. 错误
              </van-radio>
            </van-radio-group>
          </div>
        </div>
      </div>

      <div class="button-group" style="margin-top: 1.5rem; height: 40px">
        <el-button
          class="submit-button"
          @click="onSubmit"
          :loading="submitting"
        >
          提交
        </el-button>
        <div class="page-button-group">
          <el-button
            icon="el-icon-arrow-left"
            @click="onPageChange(-1)"
            :disabled="$route.params.id == 1"
            >上一页</el-button
          >
          <el-button
            type="primary"
            @click="onPageChange(1)"
            :disabled="
              $route.params.id ==
                paper.choiceAnswerSheet.length + paper.judgeAnswerSheet.length
            "
          >
            下一页
            <i class="el-icon-arrow-right el-icon--right"></i>
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import constants from "@/constants";
import paperApi from "@/api/paper";
import { Dialog } from "vant";

export default {
  data() {
    return {
      showPopup: false,
      paper: {
        id: null,
        choiceAnswerSheet: [],
        judgeAnswerSheet: []
      },
      remainTime: 24 * 60 * 60 * 3600 * 1000,
      selectedAnswer: [-1],
      loading: true,
      submitting: false
    };
  },
  methods: {
    notChoosed(index) {
      if (index <= constants.CHOICE_QUESTION_NUM)
        return this.paper.choiceAnswerSheet[index - 1] == -1;
      else
        return (
          this.paper.judgeAnswerSheet[
            index - constants.CHOICE_QUESTION_NUM - 1
          ] == -1
        );
    },
    async onSubmit() {
      await Dialog.confirm({
        title: "提示",
        message: "此操作将提交答卷, 是否继续？"
      });

      await Dialog.confirm({
        title: "提示",
        message: "再次确认：提交答卷后无法更改，是否继续？"
      });

      await Dialog.confirm({
        title: "提示",
        message: "最终确认：是否确定提交？"
      });

      this.submitting = true;
      this.submit();
    },
    async submit() {
      try {
        await paperApi.submit(
          this.paper.choiceAnswerSheet,
          this.paper.judgeAnswerSheet
        );
        this.$store.commit("user/SET_INFO", {
          status: constants.STATUS_SUBMITTED
        });
        this.$router.replace("/student/details");
      } catch (error) {
        this.submitting = false;
      }
    },
    formatTimeNumber(num) {
      return num < 10 ? "0" + num : num;
    },
    onPageChange(num) {
      let nextPage = parseInt(this.$route.params.id) + num;
      if (
        nextPage >= 1 &&
        nextPage <= constants.CHOICE_QUESTION_NUM + constants.JUDGE_QUESTION_NUM
      ) {
        this.$router.replace("/student/exam/" + nextPage);
      }
    },
    onCurrentAnswerChanged(name) {
      if (this.currentType === "选择题") {
        this.paper.choiceAnswerSheet[this.realCurrentIndex - 1] = name;
      } else {
        this.paper.judgeAnswerSheet[this.realCurrentIndex - 1] = name;
      }
    },
    onClickRight() {
      this.showPopup = !this.showPopup;
    },
    async getPaper() {
      this.paper = await paperApi.generatePaper();
      this.$store.commit("user/SET_INFO", {
        status: constants.STATUS_GENERATED
      });

      this.loading = false;
      if (this.userInfo.status === constants.STATUS_GENERATED) {
        this.paper.startTime = new Date();
        try {
          await paperApi.calibrateTime(this.paper.startTime);
        } catch (e) {
          this.$router.replace("/student/index");
        }
        this.$store.commit("user/SET_INFO", {
          status: constants.STATUS_STARTED
        });
      }

      this.remainTime =
        constants.TIME_LIMIT - (new Date().getTime() - this.paper.startTime);
    }
  },
  watch: {
    $route() {
      this.selectedAnswer[0] = this.currentAnswer;
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    },
    realCurrentIndex() {
      let index = this.$route.params.id;
      if (this.paper.id === null) return 0;
      if (index <= constants.CHOICE_QUESTION_NUM) return index;
      else return index - constants.CHOICE_QUESTION_NUM;
    },
    score() {
      if (this.currentType === "选择题") return constants.CHOICE_QUESTION_SCORE;
      else return constants.JUDGE_QUESTION_SCORE;
    },
    currentQuestion() {
      let index = this.$route.params.id;
      if (this.paper.id === null) return {};
      if (index <= constants.CHOICE_QUESTION_NUM)
        return this.paper.choiceQuestions[index - 1];
      else
        return this.paper.judgeQuestions[
          index - constants.CHOICE_QUESTION_NUM - 1
        ];
    },
    currentType() {
      let index = this.$route.params.id;
      if (this.paper.id === null) return "";
      if (index <= constants.CHOICE_QUESTION_NUM) return "选择题";
      else return "判断题";
    },
    currentAnswer() {
      let index = this.$route.params.id;
      if (this.paper.id === null) return -1;
      if (index <= constants.CHOICE_QUESTION_NUM)
        return this.paper.choiceAnswerSheet[index - 1];
      else
        return this.paper.judgeAnswerSheet[
          index - constants.CHOICE_QUESTION_NUM - 1
        ];
    },
    totalQuestion() {
      return constants.CHOICE_QUESTION_NUM + constants.JUDGE_QUESTION_NUM;
    }
  },
  async created() {
    this.loading = true;
    await this.getPaper();
  }
};
</script>

<style lang="scss" scoped>
.submit-button {
  position: absolute;
  left: 1rem;
}

.page-button-group {
  position: absolute;
  right: 1rem;
}

.grid-item {
  margin-bottom: 0.5rem;
  width: 3rem;
  height: 3rem;
  border: 2px solid #7299e6;
  border-radius: 15%;
  text-align: center;
  line-height: 3rem;
  color: #7299e6;

  &.grid-item-not-choose {
    color: grey;
    border-color: grey;
  }
}

.van-row {
  margin: 1rem;
}

.colon {
  display: inline-block;
  margin: 0 4px;
  color: #ee0a24;
}
.block {
  display: inline-block;
  width: 22px;
  color: #fff;
  font-size: 12px;
  text-align: center;
  background-color: #ee0a24;
  border-radius: 4px;
}

.question {
  width: 100%;
  padding: 1rem;
  margin: 0;
  box-sizing: border-box;

  .question-head {
    font-weight: 700;
    font-size: 1rem;
    text-align: center;
    font-family: Avenir, Helvetica, Arial, sans-serif;
  }

  .question-body {
    border-top: 1px solid rgba(0, 0, 0, 0.1);
    margin-top: 2rem;
    padding-top: 2rem;

    .van-radio {
      line-height: 2rem;
      min-height: 1.7rem;
    }
  }
}

.container {
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
  background-color: rgb(249, 249, 249);
}

.context {
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

.van-radio {
  text-align: left;
  margin-bottom: 0.2rem;
}
</style>
