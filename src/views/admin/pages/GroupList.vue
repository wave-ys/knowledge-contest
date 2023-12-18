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

        <!-- <el-dropdown
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
        </el-dropdown> -->
      </el-col>
    </el-header>
    <el-main style="padding: 0">
      <el-table
        height="100%"
        v-loading="loading"
        :data="groupList"
        tooltip-effect="dark"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column
          type="selection"
          width="30"
          :selectable="row => row.authority != constants.AUTH_ALL"
        >
        </el-table-column>
        <el-table-column prop="department" label="院系" show-overflow-tooltip>
          <template slot-scope="scope">
            {{ scope.row.department || "（无）" }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="班级名"> </el-table-column>
        <el-table-column prop="adminName" label="管理员" :width="200">
          <template slot-scope="scope">
            {{ scope.row.adminName || "（无）" }}
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
              :disabled="scope.row.authority == constants.AUTH_ALL"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-main>

    <el-footer style="margin: 10px auto; height: auto">
      <el-pagination
        @size-change="getGroupList"
        @current-change="getGroupList"
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
        <el-form-item label="班级名">
          <el-input v-model="editData.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="管理员">
          <el-select
            v-model="editData.userId"
            placeholder="请选择"
            style="width: 100%"
          >
            <el-option
              v-for="item in adminList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{
                item.department
              }}</span>
            </el-option>
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
import groupApi from "@/api/group";
import userApi from "@/api/user";
import constants from "@/constants";

export default {
  data() {
    return {
      groupList: [],
      adminList: [],
      constants,
      dialogVisible: false,
      editting: false, // true 表示 inserting
      editData: {},
      loading: false,
      multipleSelection: [],

      dropdownLoading: false,
      uploadType: "",

      pageSize: 50,
      total: 0,
      currentPage: 1
    };
  },
  computed: {
    userInfo() {
      return this.$store.state.user;
    }
  },
  created() {
    this.getGroupList();
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
          await groupApi.importGroupAndInsert(formData);
        else await groupApi.importGroupAndCover(formData);
      } finally {
        this.dropdownLoading = false;
        this.getGroupList();
      }
    },
    async handleDropdown(command) {
      try {
        this.dropdownLoading = true;
        if (command == "importAndInsert") {
          this.uploadType = "insert";
          document.getElementById("uploadInput").click();
        } else {
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
      await groupApi.deleteGroups(
        this.multipleSelection.map(value => value.id)
      );
      this.getGroupList();
    },
    async deleteAll() {
      await this.$confirm("此操作将永久删除所有记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      });
      await groupApi.deleteAllGroup();
      this.getGroupList();
    },
    async getGroupList() {
      this.loading = true;
      const res = await groupApi.getGroupPage(this.currentPage, this.pageSize);
      this.adminList = await userApi.getDepartmentAdmins();
      this.groupList = res.list;
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
      await groupApi.deleteGroup(row.id);
      this.$message({
        type: "success",
        message: "删除成功!"
      });
      this.getGroupList();
    },
    async submitEdit() {
      if (this.editting) {
        if (this.editData.status == constants.STATUS_ALL)
          this.editData.department = -1;
        await groupApi.editGroup(this.editData);
        if (this.editData.status == constants.STATUS_ALL)
          this.$store.dispatch("user/getInfo");
      } else {
        this.editData.id = null;
        await groupApi.insertGroup(this.editData);
      }
      this.getGroupList();
      this.editting = false;
      this.dialogVisible = false;
    }
  }
};
</script>

<style lang="scss" scoped></style>
