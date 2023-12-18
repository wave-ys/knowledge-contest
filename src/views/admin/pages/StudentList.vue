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
      <el-row :gutter="12">
        <el-col :span="10">
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
              导入&导出<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="importAndInsert">
                导入并添加
              </el-dropdown-item>
              <el-dropdown-item command="importAndCover">
                导入并覆盖
              </el-dropdown-item>
              <el-dropdown-item command="export">
                导出
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-col>

        <el-col :span="4">
          <el-select
            placeholder="查询类别"
            v-model="queryForm.type"
            @change="queryForm.value = ''"
          >
            <el-option label="学号" value="sid"></el-option>
            <el-option label="一卡通号" value="cardId"></el-option>
            <el-option label="姓名" value="name"></el-option>
            <el-option label="院系" value="department"></el-option>
            <el-option label="状态" value="status"></el-option>
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-select
            v-if="queryForm.type == 'status'"
            v-model="queryForm.value"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option label="已提交" :value="constants.STATUS_SUBMITTED">
            </el-option>
            <el-option label="未提交" :value="constants.STATUS_NOT_SUBMITTED">
            </el-option>
          </el-select>
          <el-select
            v-else-if="queryForm.type == 'department'"
            v-model="queryForm.value"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in constants.DEPARTMENT"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
            </el-option>
          </el-select>
          <el-input
            v-else
            placeholder="值"
            v-model="queryForm.value"
            style="width: 100%"
          ></el-input>
        </el-col>
        <el-col :span="4">
          <el-button @click="handleQuery" :disabled="this.loading">
            查询
          </el-button>
          <el-button @click="handleClearQuery" :disabled="this.loading">
            清空
          </el-button>
        </el-col>
      </el-row>
    </el-header>
    <el-main style="padding: 0">
      <el-table
        height="100%"
        v-loading="loading"
        :data="studentList"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column type="selection" width="30"> </el-table-column>
        <el-table-column prop="sid" label="学号"> </el-table-column>
        <el-table-column prop="cardId" label="一卡通号"> </el-table-column>
        <el-table-column label="院系" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ getDepartmentById(scope.row.department) }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="姓名"> </el-table-column>
        <el-table-column label="状态">
          <template slot-scope="scope">
            <el-tag :type="getTagType(scope.row.status)" disable-transitions>
              {{
                scope.row.status === constants.STATUS_SUBMITTED
                  ? "已提交"
                  : "未提交"
              }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="score" label="分数">
          <template slot-scope="scope">
            {{ scope.row.score == -1 ? "无" : scope.row.score }}
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
        @size-change="getStudentList"
        @current-change="getStudentList"
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
        <el-form-item label="学号">
          <el-input v-model="editData.sid" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="一卡通号">
          <el-input v-model="editData.cardId" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="editData.name" autocomplete="off"></el-input>
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
import userApi from "@/api/user";
import constants from "@/constants";
import { getDepartmentById } from "@/utils/department";

export default {
  data() {
    return {
      studentList: [],
      constants,
      dialogVisible: false,
      editting: false, // true 表示 inserting
      editData: {},
      loading: false,
      multipleSelection: [],

      pageSize: 50,
      total: 0,
      currentPage: 1,

      queryForm: {
        type: "",
        value: ""
      },

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
    this.getStudentList();
  },
  methods: {
    handleDeleteCmd(command) {
      if (command == "selected") this.deleteSelectedRow();
      else this.deleteAll();
    },
    async deleteAll() {
      await this.$confirm("此操作将永久删除所有记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      });
      await userApi.deleteAll();
      this.getStudentList();
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
          await userApi.importStudentAndInsert(formData);
        else await userApi.importStudentAndCover(formData);
      } finally {
        this.dropdownLoading = false;
        this.getStudentList();
      }
    },
    async handleDropdown(command) {
      try {
        this.dropdownLoading = true;
        if (command == "export") {
          const data = await userApi.exportStudentList();
          if (!data) this.$message.error("导出失败");

          let url = window.URL.createObjectURL(data);
          let link = document.createElement("a");
          link.style.display = "none";
          link.href = url;
          link.setAttribute("download", "data.xlsx");
          document.body.appendChild(link);
          link.click();
        } else if (command == "importAndInsert") {
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
    handleQuery() {
      this.getStudentList();
    },
    handleClearQuery() {
      this.queryForm.type = "";
      this.queryForm.value = "";
      this.getStudentList();
    },
    getDepartmentById,
    handleSelectionChange(val) {
      this.multipleSelection = val;
      console.log(val);
    },
    async deleteSelectedRow() {
      await this.$confirm("此操作将永久删除选中的记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      });
      await userApi.deleteStudents(
        this.multipleSelection.map(value => value.id)
      );
      this.getStudentList();
    },
    async getStudentList() {
      this.loading = true;
      const res = await userApi.queryStudent(
        this.currentPage,
        this.pageSize,
        this.queryForm.type,
        this.queryForm.value
      );
      this.studentList = res.list;
      this.total = res.total;
      this.loading = false;
    },
    getTagType(status) {
      switch (status) {
        case constants.STATUS_NOT_START:
        case constants.STATUS_GENERATED:
        case constants.STATUS_STARTED:
          return "info";
        case constants.STATUS_SUBMITTED:
          return "success";
      }
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
      await userApi.deleteStudent(row.id);
      this.$message({
        type: "success",
        message: "删除成功!"
      });
      this.getStudentList();
    },
    async submitEdit() {
      if (this.editting) {
        await userApi.editStudent(this.editData);
      } else {
        this.editData.id = null;
        await userApi.insertStudent(this.editData);
      }
      this.getStudentList();
      this.editting = false;
      this.dialogVisible = false;
    }
  }
};
</script>

<style lang="scss"></style>
