<template>
  <div>
    <van-nav-bar
      title="东南大学校史校情知识竞赛"
      @click-right="onClickRight"
      @click-left="$router.replace('/student/index')"
      :fixed="true"
    >
      <template #left>
        <van-icon name="arrow-left" size="18" color="black" />
      </template>
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
      <van-row style="padding: 1rem">
        <el-button style="width: 100%" type="danger" @click="onLogout">
          退出
        </el-button>
      </van-row>
      <van-cell-group title="答题信息">
        <van-grid :column-num="3" :border="false" clickable>
          <van-grid-item
            v-for="value in totalQuestion"
            :key="value"
            @click="gridClicked(value)"
          >
            <div>
              <div
                :class="[
                  'grid-item',
                  {
                    'grid-item-not-choose': notChoosed(value),
                    'grid-item-correct': isCorrect(value)
                  }
                ]"
              >
                {{ value }}
              </div>
              <div
                :class="[
                  'grid-item-circle',
                  {
                    'grid-item-circle-not-choose': notChoosed(value),
                    'grid-item-circle-correct': isCorrect(value)
                  }
                ]"
              >
                <van-icon
                  style="top: 0.1rem;"
                  size="0.8rem"
                  :name="isCorrect(value) ? 'success' : 'cross'"
                  color="white"
                />
              </div>
            </div>
          </van-grid-item>
        </van-grid>
      </van-cell-group>
    </van-popup>

    <div :id="'anchor' + 0" style="height: 46px"></div>
    <van-notice-bar
      :scrollable="false"
      :text="`您的成绩：${completePaper.score} / 100`"
      v-loading="loading"
    />
    <div class="details" v-loading="loading">
      <h2 class="details-header">答题详情</h2>
      <div
        :class="[
          'card',
          item.answer === completePaper.choiceAnswerSheet[index]
            ? 'correct'
            : 'wrong'
        ]"
        v-for="(item, index) in completePaper.choiceQuestions"
        :key="item.id"
      >
        <van-cell
          :title="
            `#${index + 1} 选择题（${constants.CHOICE_QUESTION_SCORE}分）`
          "
        >
        </van-cell>
        <div class="question">
          <h3 class="question-head">{{ item.question }}</h3>
          <div class="question-body">
            <van-radio-group :value="completePaper.choiceAnswerSheet[index]">
              <van-radio
                :name="0"
                :checked-color="item.answer === 0 ? '#07c160' : 'red'"
              >
                <span>{{ "A. " + item.choiceA }}</span>
              </van-radio>
              <van-radio
                :name="1"
                :checked-color="item.answer === 1 ? '#07c160' : 'red'"
              >
                {{ "B. " + item.choiceB }}
              </van-radio>
              <van-radio
                :name="2"
                :checked-color="item.answer === 2 ? '#07c160' : 'red'"
              >
                {{ "C. " + item.choiceC }}
              </van-radio>
              <van-radio
                :name="3"
                :checked-color="item.answer === 3 ? '#07c160' : 'red'"
              >
                {{ "D. " + item.choiceD }}
              </van-radio>
            </van-radio-group>
            本题答案：{{ String.fromCharCode(item.answer + 65) }}
          </div>
          <div :id="'anchor' + (index + 1)"></div>
        </div>
      </div>

      <div
        :class="[
          'card',
          item.answer === completePaper.judgeAnswerSheet[index]
            ? 'correct'
            : 'wrong'
        ]"
        v-for="(item, index) in completePaper.judgeQuestions"
        :key="item.id"
      >
        <van-cell
          :title="
            `#${index + constants.CHOICE_QUESTION_NUM + 1} 判断题（${
              constants.JUDGE_QUESTION_SCORE
            }分）`
          "
        >
        </van-cell>
        <div class="question">
          <h3 class="question-head">{{ item.question }}</h3>
          <div class="question-body">
            <van-radio-group :value="completePaper.judgeAnswerSheet[index]">
              <van-radio
                :name="1"
                :checked-color="item.answer === 1 ? '#07c160' : 'red'"
              >
                正确
              </van-radio>
              <van-radio
                :name="0"
                :checked-color="item.answer === 0 ? '#07c160' : 'red'"
              >
                错误
              </van-radio>
            </van-radio-group>
            本题答案：{{ item.answer ? "正确" : "错误" }}
          </div>
          <div
            :id="'anchor' + (index + constants.CHOICE_QUESTION_NUM + 1)"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import constants from "@/constants";
import { removeToken } from "@/utils/storage";
import paperApi from "@/api/paper";
import canstants from "@/constants";

export default {
  data() {
    return {
      showPopup: false,
      completePaper: {},
      constants: canstants,
      loading: true
    };
  },
  methods: {
    gridClicked(index) {
      document.querySelector("#anchor" + (index - 1)).scrollIntoView(true);
      this.showPopup = false;
    },
    onClickRight() {
      this.showPopup = !this.showPopup;
    },
    onLogout() {
      removeToken();
      this.$router.push("/login");
      this.$store.commit("user/CLEAR_INFO");
    },
    notChoosed(index) {
      if (!this.completePaper.id) return true;
      if (index <= constants.CHOICE_QUESTION_NUM) {
        return this.completePaper.choiceAnswerSheet[index - 1] == -1;
      } else {
        return (
          this.completePaper.judgeAnswerSheet[
            index - constants.CHOICE_QUESTION_NUM - 1
          ] == -1
        );
      }
    },
    isCorrect(index) {
      if (!this.completePaper.id) return false;
      if (index <= constants.CHOICE_QUESTION_NUM)
        return (
          this.completePaper.choiceAnswerSheet[index - 1] ==
          this.completePaper.choiceQuestions[index - 1].answer
        );
      else
        return (
          this.completePaper.judgeAnswerSheet[
            index - constants.CHOICE_QUESTION_NUM - 1
          ] ==
          this.completePaper.judgeQuestions[
            index - constants.CHOICE_QUESTION_NUM - 1
          ].answer
        );
    }
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    },
    totalQuestion() {
      return constants.CHOICE_QUESTION_NUM + constants.JUDGE_QUESTION_NUM;
    }
  },
  async created() {
    this.loading = true;
    this.completePaper = await paperApi.getCompletePaper();
    this.loading = false;
  }
};
</script>

<style lang="scss" scoped>
.details {
  margin: 0 1rem;

  .card {
    margin-bottom: 24px;
    padding: 1rem;
    background-color: #fff;
    border-radius: 1rem;

    .question {
      width: 100%;
      padding: 1rem;
      margin: 0;
      box-sizing: border-box;
      text-align: center;

      .question-head {
        font-weight: 700;
        font-size: 1rem;
        font-family: Avenir, Helvetica, Arial, sans-serif;
      }

      .question-body {
        border-top: 1px solid rgba(0, 0, 0, 0.1);
        margin-top: 2rem;
        padding-top: 2rem;

        .van-radio {
          line-height: 2rem;
          min-height: 1.7rem;
          text-align: left;
          margin-bottom: 0.2rem;
        }
      }
    }
  }

  .correct {
    border: #07c160 2px solid;
    box-shadow: 0 0.6rem 1rem #07c16148;
  }

  .wrong {
    border: #ff0000 2px solid;
    box-shadow: 0 0.6rem 1rem #ff000048;
  }
}

.grid-item {
  margin-bottom: 0.5rem;
  width: 3rem;
  height: 3rem;
  border: 2px solid #ea5141;
  color: #ea5141;
  border-radius: 15%;
  text-align: center;
  line-height: 3rem;
  border: 2px solid #7299e6;
  color: #7299e6;

  &.grid-item-not-choose {
    color: grey;
    border-color: grey;
  }
}

.grid-item-circle {
  height: 1.2rem;
  width: 1.2rem;
  border-radius: 50%;
  margin: 0 auto;
  position: relative;
  top: -1.2rem;
  background-color: #e65244;
  text-align: center;

  &.grid-item-circle-correct {
    background-color: #27ce6e;
  }
}
</style>
