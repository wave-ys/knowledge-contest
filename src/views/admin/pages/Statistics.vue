<template>
  <el-container style="height: 100%;">
    <el-header>
      <el-form :inline="true">
        <el-form-item label="自动刷新">
          <el-select v-model="refreshInterval" @change="startTimer">
            <el-option label="10 秒 / 次" :value="10000"></el-option>
            <el-option label="30 秒 / 次" :value="30000"></el-option>
            <el-option label="1 分钟 / 次" :value="60000"></el-option>
            <el-option label="关闭" :value="-1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-refresh" @click="refresh"></el-button>
          <el-button @click="onExport" :loading="exporting">导出</el-button>
        </el-form-item>
      </el-form>
    </el-header>

    <el-main style="padding: 0">
      <el-table
        height="100%"
        style="width: 100%"
        v-loading="loading"
        tooltip-effect="dark"
        :data="statisticsList"
      >
        <el-table-column type="index" width="50" align="right">
        </el-table-column>
        <el-table-column
          prop="department.name"
          label="院系"
          sortable
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="提交人数 / 总人数" align="center">
          <template slot-scope="scope">
            {{ scope.row.submittedPerson }} / {{ scope.row.totalPerson }}
          </template>
        </el-table-column>
        <el-table-column
          label="平均分（不含未提交）"
          align="center"
          sortable
          :sort-by="
            row =>
              row.submittedPerson ? row.totalScore / row.submittedPerson : 0
          "
        >
          <template slot-scope="scope">
            {{
              scope.row.submittedPerson
                ? scope.row.totalScore / scope.row.submittedPerson
                : "-"
            }}
          </template>
        </el-table-column>
        <el-table-column
          label="平均分（含未提交）"
          align="center"
          sortable
          :sort-by="
            row => (row.totalPerson ? row.totalScore / row.totalPerson : 0)
          "
        >
          <template slot-scope="scope">
            {{
              scope.row.totalPerson
                ? scope.row.totalScore / scope.row.totalPerson
                : "-"
            }}
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>

<script>
import constants from "@/constants";
import departmentApi from "@/api/department";

export default {
  data() {
    return {
      constants,
      loading: false,

      statisticsList: [],
      timer: null,
      refreshInterval: -1,

      exporting: false
    };
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    }
  },
  methods: {
    async getList() {
      this.loading = true;
      this.statisticsList = await departmentApi.getStatistics();
      this.loading = false;
    },
    startTimer() {
      if (this.refreshInterval == -1) {
        this.clearTimer();
      } else
        this.timer = setInterval(() => {
          this.getList();
        }, this.refreshInterval);
    },
    clearTimer() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },
    async refresh() {
      this.clearTimer();
      await this.getList();
      this.startTimer();
    },
    async onExport() {
      this.exporting = true;
      const data = await departmentApi.exportDepartmentStatistics();
      if (!data) this.$message.error("导出失败");
      this.exporting = false;

      let url = window.URL.createObjectURL(data);
      let link = document.createElement("a");
      link.style.display = "none";
      link.href = url;
      link.setAttribute("download", "data.xlsx");
      document.body.appendChild(link);
      link.click();
    }
  },
  created() {
    this.getList();
  },
  mounted() {
    this.startTimer();
  },
  beforeDestroy() {
    this.clearTimer();
  }
};
</script>

<style lang="scss" scoped></style>
