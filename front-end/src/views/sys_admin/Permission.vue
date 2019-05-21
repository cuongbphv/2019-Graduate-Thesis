<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="searchKey"
        :placeholder="$t('label.search')"
        style="width: 400px;"
        class="filter-item"
        @keyup.enter.native="handleSearchPermission"
      />
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        @click="dialogVisible = true"
      >{{ $t('button.add') }}</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        @click="deleteMultiplePermission"
      >{{ $t('button.delete') }}</el-button>
    </div>

    <el-dialog :visible.sync="dialogVisible" title="Add New Permission">
      <el-form :model="permission" label-width="80px" label-position="left">
        <el-form-item label="Name">
          <el-input v-model="permission.name" placeholder="Role Name" />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible = false">{{ $t('permission.cancel') }}</el-button>
        <el-button type="primary" @click="addNewPermission">{{ $t('permission.confirm') }}</el-button>
      </div>
    </el-dialog>

    <el-table
      key="id"
      v-loading="isLoading"
      :data="permissions"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        type="selection"
        width="35"
      />
      <el-table-column :label="$t('table.header.permission')" align="center" prop="name" sortable="custom">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.name" class="edit-input" size="small" />
          </template>
          <span v-else>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="!scope.row.edit"
            type="primary"
            icon="el-icon-edit"
            circle
            @click="editPermission(scope.row)"
          />
          <el-button
            v-if="scope.row.edit"
            type="primary"
            icon="el-icon-check"
            circle
            @click="confirmEditPermission(scope.row)"
          />
          <el-button
            v-if="scope.row.edit"
            class="cancel-btn"
            size="small"
            icon="el-icon-refresh"
            type="warning"
            @click="cancelEdit(scope.row)"
          />
          <el-button
            v-if="!scope.row.edit"
            type="danger"
            icon="el-icon-delete"
            circle
            @click="deletePermission(scope.row.id)"
          />
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import permission from '../../api/permission'
import { Status } from '../../utils/constants'
export default {
  name: 'Permission',
  components: {
  },
  data() {
    return {
      isLoading: true,
      listQuery: {
        ascSort: true,
        searchKey: ''
      },
      searchKey: '',
      originalName: {},
      dialogVisible: false,
      permission: {
        name: ''
      },
      ids: []
    }
  },
  computed: {
    ...mapGetters('permission', ['permissions'])
  },
  watch: {
    searchKey: function(newVal) {
      this.listQuery.searchKey = newVal
      this.getList()
    }
  },
  created() {
    this.getList()
  },
  methods: {
    ...mapActions('permission', ['getListPermissions']),
    getList() {
      this.isLoading = true
      this.getListPermissions(this.listQuery).then(() => {
        this.isLoading = false
      })
    },
    sortChange({ prop, order }) {
      this.listQuery.ascSort = order === 'descending'
      this.getList()
    },
    handleSelectionChange(rows) {
      this.ids = []
      this.ids = rows.map(row => row.id)
    },
    handleSearchPermission() {
    },
    addNewPermission() {
      permission.createNewPermission({
        name: this.permission.name
      }).then(res => {
        if (res.status === Status.SUCCESS) {
          this.getList()
          this.$message({
            message: 'Add new permission successfully',
            type: 'success'
          })
          this.permission.name = ''
          this.dialogVisible = false
        }
      })
    },
    editPermission(row) {
      row.edit = !row.edit
      this.originalName[row.id] = row.name
    },
    confirmEditPermission(row) {
      row.edit = !row.edit
      permission.editPermissionById({
        id: row.id,
        name: row.name
      }).then(res => {
        if (res.status === Status.SUCCESS) {
          this.getList()
          this.$message({
            message: 'Update permission successfully',
            type: 'success'
          })
        }
      })
    },
    cancelEdit(row) {
      row.name = this.originalName[row.id]
      row.edit = false
      this.$message({
        message: 'Permission name has restored originally',
        type: 'info'
      })
    },
    deleteMultiplePermission() {
      if (this.ids.length > 0) {
        this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
          confirmButtonText: this.$t('button.confirm'),
          cancelButtonText: this.$t('button.cancel'),
          type: 'warning'
        }).then(() => {
          permission.deletePermissions({
            permission_ids: this.ids.join(',')
          }).then(res => {
            if (res.status === Status.SUCCESS) {
              this.getList()
              this.$message({
                message: 'Delete permission successfully',
                type: 'success'
              })
            }
          })
        }).catch(() => {})
      } else {
        this.$alert(this.$t('message.info_delete'), this.$t('label.info'), {
          confirmButtonText: 'OK'
        })
      }
    },
    deletePermission(id) {
      this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
        confirmButtonText: this.$t('button.confirm'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        permission.deletePermissions({
          permission_ids: id
        }).then(res => {
          if (res.status === Status.SUCCESS) {
            this.getList()
            this.$message({
              message: 'Delete permission successfully',
              type: 'success'
            })
          }
        })
      }).catch(() => {})
    }
  }
}
</script>

<style lang="scss" scoped>
  /deep/ .el-checkbox {
    z-index: 0;
  }
</style>
