<template>
  <el-container style="height: 100%;">
    <input
      id="uploadInput"
      type="file"
      accept=".xlsx,.xls"
      v-show="false"
      @change="upload"
    />
    <el-header>
      <el-col :span="12">
        <el-button
          type="primary"
          :disabled="this.loading"
          @click="handleInsert"
        >
          添加
        </el-button>

        <el-dropdown
          @command="handleDeleteCmd"
          style="margin-left: 12px;"
          trigger="click"
        >
          <el-button type="danger">
            批量删除<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              :disabled="this.loading || multipleSelection.length == 0"
              command="selected"
              >删除选中项</el-dropdown-item
            >
            <el-dropdown-item command="all" :disabled="this.loading"
              >删除全部</el-dropdown-item
            >
          </el-dropdown-menu>
        </el-dropdown>

        <el-dropdown
          style="margin-left: 12px;"
          @command="handleDropdown"
          v-loading="dropdownLoading"
          trigger="click"
        >
          <el-button>
            导入<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="importAndInsert">
              导入并添加
            </el-dropdown-item>
            <el-dropdown-item command="importAndCover">
              导入并覆盖
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </el-col>
    </el-header>
    <el-main style="padding: 0">
      <el-table
        height="100%"
        v-loading="loading"
        :data="questionList"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column type="selection" width="30"> </el-table-column>
        <el-table-column prop="question" label="问题" show-overflow-tooltip>
        </el-table-column>
        <el-table-column label="答案">
          <template slot-scope="scope">
            {{ scope.row.answer == 0 ? "错误" : "正确" }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="175">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
              >编辑</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="handleDelete(scope.$index, scope.row)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-footer style="margin: 10px auto; height: auto">
      <el-pagination
        @size-change="getQuestionList"
        @current-change="getQuestionList"
        :current-page.sync="currentPage"
        :page-sizes="[50, 100, 150, 200]"
        :page-size.sync="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-footer>

    <el-dialog title="添加/编辑" :visible.sync="dialogVisible">
      <el-form :model="editData">
        <el-form-item label="问题">
          <el-input v-model="editData.question" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="答案">
          <el-select v-model="editData.answer" placeholder="请选择答案">
            <el-option label="错误" :value="0"></el-option>
            <el-option label="正确" :value="1"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button
          @click="
            dialogVisible = false;
            editting = false;
          "
          >取 消</el-button
        >
        <el-button type="primary" @click="submitEdit()">确 定</el-button>
      </div>
    </el-dialog>
  </el-container>
</template>

<script>
import judgeApi from "@/api/judgeQuestion";
import constants from "@/constants";

export default {
  data() {
    return {
      questionList: [],
      constants,
      dialogVisible: false,
      editting: false, // true 表示 inserting
      editData: {},
      loading: false,
      multipleSelection: [],

      pageSize: 50,
      total: 0,
      currentPage: 1,

      dropdownLoading: false,
      uploadType: ""
    };
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    }
  },
  created() {
    this.getQuestionList();
  },
  methods: {
    handleDeleteCmd(command) {
      if (command == "selected") this.deleteSelectedRow();
      else this.deleteAll();
    },
    async upload() {
      this.dropdownLoading = true;
      this.loading = true;
      const formData = new window.FormData();
      formData.append(
        "file",
        document.querySelector("input[type=file]").files[0]
      );
      try {
        if (this.uploadType == "insert")
          await judgeApi.importAndInsert(formData);
        else await judgeApi.importAndCover(formData);
      } finally {
        this.dropdownLoading = false;
        this.getQuestionList();
      }
    },
    async handleDropdown(command) {
      try {
        this.dropdownLoading = true;
        if (command == "importAndInsert") {
          this.uploadType = "insert";
          document.getElementById("uploadInput").click();
        } else {
          await this.$confirm(
            "此操作将永久删除所有记录后再添加, 是否继续?",
            "提示",
            {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }
          );

          this.uploadType = "cover";
          document.getElementById("uploadInput").click();
        }
      } finally {
        this.dropdownLoading = false;
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async deleteSelectedRow() {
      await this.$confirm("此操作将永久删除选中的记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      });
      await judgeApi.deleteQuestions(
        this.multipleSelection.map(value => value.id)
      );
      this.getQuestionList();
    },
    async deleteAll() {
      await this.$confirm("此操作将永久删除所有记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      });
      await judgeApi.deleteAll();
      this.getQuestionList();
    },
    async getQuestionList() {
      this.loading = true;
      const res = await judgeApi.getQuestionPage(
        this.currentPage,
        this.pageSize
      );
      this.questionList = res.list;
      this.total = res.total;
      this.loading = false;
    },
    handleInsert() {
      this.editData = {};
      this.editting = false;
      this.dialogVisible = true;
    },
    handleEdit(index, row) {
      this.editData = { ...row };
      this.editting = true;
      this.dialogVisible = true;
    },
    async handleDelete(index, row) {
      await this.$confirm("此操作将永久删除该记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      });
      await judgeApi.deleteQuestion(row.id);
      this.$message({
        type: "success",
        message: "删除成功!"
      });
      this.getQuestionList();
    },
    async submitEdit() {
      if (this.editting) {
        await judgeApi.editQuestion(this.editData);
      } else {
        this.editData.id = null;
        await judgeApi.insertQuestion(this.editData);
      }
      this.getQuestionList();
      this.editting = false;
      this.dialogVisible = false;
    }
  }
};
</script>

<style lang="scss" scoped></style>
