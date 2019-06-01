<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddRole">{{ $t('button.new_role') }}</el-button>

    <el-table :data="roles" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" :label="$t('place_holder.role_name')">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column align="center" width="230" :label="$t('table.actions')">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle size="small" @click="handleEdit(scope.row)" />
          <el-button type="danger" icon="el-icon-delete" circle size="small" @click="handleDelete(scope.row)" />
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'? $t('label.edit_role') : $t('button.new_role')">
      <el-form :model="role" label-width="130px" label-position="left">
        <el-form-item :label="$t('place_holder.role_name')">
          <el-input v-model="role.name" :placeholder="$t('place_holder.role_name')" />
        </el-form-item>
        <el-form-item :label="$t('place_holder.role_description')">
          <el-input
            v-model="role.description"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            :placeholder="$t('place_holder.role_description')"
          />
        </el-form-item>
        <el-form-item :label="$t('label.list_permission')">
          <el-tree
            ref="tree"
            :data="permissions"
            :props="{label: 'name'}"
            node-key="id"
            show-checkbox
            class="permission-tree"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible = false">{{ $t('button.cancel') }}</el-button>
        <el-button type="primary" @click="confirmActionRole">{{ $t('button.confirm') }}</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import role from '../../api/role'
import { Status } from '../../utils/constants'

const defaultRole = {
  key: '',
  name: '',
  description: '',
  routes: []
}

export default {
  name: 'Role',
  data() {
    return {
      role: Object.assign({}, defaultRole),
      listPermissions: [],
      rolesList: [],
      selectedPermission: [],
      dialogVisible: false,
      dialogType: 'new'
    }
  },
  computed: {
    ...mapGetters('permission', ['roles', 'permissions', 'permissionByRoleId'])
  },
  created() {
    this.getListRoles()
  },
  methods: {
    ...mapActions('permission', ['getListRoles', 'getListPermissions']),
    handleAddRole() {
      this.role = Object.assign({}, defaultRole)
      if (this.$refs.tree) {
        this.$refs.tree.setCheckedNodes([])
      }
      this.dialogType = 'new'
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.selectedPermission = []
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.role = Object.assign({}, row)
      this.getListPermissions({
        searchKey: '',
        ascSort: true
      }).then(() => {
        this.permissions.forEach(permission => {
          if (this.role.permissions.findIndex(rolePermission => rolePermission.id === permission.id) !== -1) {
            this.selectedPermission.push(permission)
          }
        })
        this.$refs.tree.setCheckedNodes(this.selectedPermission)
      })
    },
    handleDelete(row) {
      this.$confirm(this.$t('message.confirm_remove_role'), this.$t('label.dialog_type.warning'), {
        confirmButtonText: this.$t('button.confirm'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        role.deleteRole({ role_id: row.id }).then(res => {
          if (res.status === Status.SUCCESS) {
            this.getListRoles()
            this.$message({
              message: this.$t('message.delete_role_success'),
              type: 'success'
            })
          }
        })
      }).catch(() => {})
    },
    confirmActionRole() {
      const ids = this.$refs.tree.getCheckedNodes().map(permission => permission.id)
      if (this.dialogType === 'edit') {
        role.updateRole({
          id: this.role.id,
          name: this.role.name,
          description: this.role.description,
          permissionIds: ids
        }).then(res => {
          if (res.status === Status.SUCCESS) {
            this.getListRoles()
            this.$message({
              message: this.$t('message.update_role_success'),
              type: 'success'
            })
            this.dialogVisible = false
          }
        })
      } else if (this.dialogType === 'new') {
        role.createNewRole({
          name: this.role.name,
          description: this.role.description,
          permissionIds: ids
        }).then(res => {
          if (res.status === Status.SUCCESS) {
            this.getListRoles()
            this.$message({
              message: this.$t('message.create_role_success'),
              type: 'success'
            })
            this.dialogVisible = false
          }
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  .app-container {
    .roles-table {
      margin-top: 30px;
    }
    .permission-tree {
      margin-bottom: 30px;
    }
  }
  /deep/ .el-dialog {
    margin-top: 3vh !important;
  }
</style>
