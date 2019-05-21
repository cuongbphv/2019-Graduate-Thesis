<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddRole">{{ $t('permission.addRole') }}</el-button>

    <el-table :data="roles" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="Role Name">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column align="center" width="230" label="Operations">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" circle size="small" @click="handleEdit(scope.row)" />
          <el-button type="danger" icon="el-icon-delete" circle size="small" @click="handleDelete(scope.row)" />
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'Edit Role':'New Role'">
      <el-form :model="role" label-width="80px" label-position="left">
        <el-form-item label="Name">
          <el-input v-model="role.name" placeholder="Role Name" />
        </el-form-item>
        <el-form-item label="Desc">
          <el-input
            v-model="role.description"
            :autosize="{ minRows: 2, maxRows: 4}"
            type="textarea"
            placeholder="Role Description"
          />
        </el-form-item>
        <el-form-item label="Menus">
          <el-tree
            ref="tree"
            :data="listPermissions"
            :props="{label: 'name'}"
            node-key="id"
            show-checkbox
            class="permission-tree"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible = false">{{ $t('permission.cancel') }}</el-button>
        <el-button type="primary" @click="confirmActionRole">{{ $t('permission.confirm') }}</el-button>
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
    this.loadListPermission()
  },
  methods: {
    ...mapActions('permission', ['getListRoles', 'getListPermissions']),
    loadListPermission() {
      this.getListPermissions({
        searchKey: '',
        ascSort: true
      }).then(() => {
        this.listPermissions = [...this.permissions]
      })
    },
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
      this.$nextTick(() => {
        this.listPermissions.forEach(permission => {
          if (this.role.permissions.findIndex(rolePermission => rolePermission.id === permission.id) !== -1) {
            this.selectedPermission.push(permission)
          }
        })
        this.$refs.tree.setCheckedNodes(this.selectedPermission)
      })
    },
    handleDelete(row) {
      this.$confirm('Confirm to remove the role?', 'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      }).then(() => {
        role.deleteRole({ role_id: row.id }).then(res => {
          if (res.status === Status.SUCCESS) {
            this.getListRoles()
            this.$message({
              message: 'Delete role successfully',
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
              message: 'Update role and role permission successfully',
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
              message: 'Create role and role permission successfully',
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
