<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.searchKey"
        :placeholder="$t('label.search')"
        style="width: 400px;"
        class="filter-item"
      />
      <!--<el-button-->
      <!--class="filter-item"-->
      <!--style="margin-left: 10px;"-->
      <!--type="danger"-->
      <!--&gt;{{ $t('button.delete') }}</el-button>-->
    </div>
    <div class="table-container">
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="listUserExceptAdmin"
        border
        fit
        highlight-current-row
        style="width: 100%;"
      >
        <el-table-column :label="$t('table.header.name')" align="center" prop="1" sortable="custom">
          <template slot-scope="scope">
            <span>{{ mapName(scope.row.firstName, scope.row.lastName) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.loginAccount')" align="center" prop="3" sortable="custom" width="200">
          <template slot-scope="scope">
            <span>{{ scope.row.phone || scope.row.email }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.createdDate')" align="center" prop="7" sortable="custom">
          <template slot-scope="scope">
            <span>{{ scope.row.createdDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.role')" class-name="status-col" prop="5" sortable="custom">
          <template slot-scope="scope">
            {{ mapRole(scope.row.roleId) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.provider')" class-name="status-col" prop="4" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="scope.row.provider | providerFilter">{{ scope.row.provider }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.status')" class-name="status-col" prop="6" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status | statusFilter">{{ mapStatus(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.actions')" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              :disabled="scope.row.provider !== 'local'"
              type="primary"
              icon="el-icon-key"
              circle
              @click="handleBeforeChangePassword(scope.row.userId)"
            />
            <el-button
              v-if="scope.row.status === 1"
              type="warning"
              icon="el-icon-lock"
              circle
              @click="handleBanUser(scope.row.userId)"
            />
            <el-button
              v-if="scope.row.status === 3"
              type="warning"
              icon="el-icon-unlock"
              circle
              @click="handleUnlockUser(scope.row.userId)"
            />
            <el-button
              v-if="scope.row.status !== 0"
              type="danger"
              icon="el-icon-delete"
              circle
              @click="handleDeleteUser(scope.row.userId)"
            />
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="totalElements > 0"
        :total="totalPage"
        :page.sync="listQuery.pageNumber"
        :limit.sync="listQuery.pageSize"
        @pagination="getList"
      />
      <el-dialog
        :visible.sync="visible"
        :title="$t('userSetting.changePassword')"
        width="30%"
        top="20vh"
        @close="handleClosePwdChange"
      >
        <el-row>
          <p>{{ $t('userSetting.newPassword') }}</p>
          <el-input
            v-model="newPassword"
            type="password"
            :placeholder="$t('userSetting.newPassword')"
          />
          <el-button style="margin-top: 1rem; float: right" type="success" @click="handleChangePassword">{{ $t('userSetting.changePassword') }}</el-button>
        </el-row>
      </el-dialog>
    </div>

  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import { mapActions, mapGetters } from 'vuex'
import i18n from '@/lang'
import { showSuccess } from '../../../utils/message'

export default {
  name: 'User',
  components: { Pagination },
  directives: { waves },
  filters: {
    providerFilter(provider) {
      const providerMap = {
        'google': 'danger',
        'facebook': 'primary',
        'local': 'info'
      }
      return providerMap[provider] || providerMap['local']
    }
  },
  data() {
    return {
      tableKey: 0,
      listLoading: true,
      listQuery: {
        searchKey: '',
        sortKey: 7,
        ascSort: true,
        pageNumber: 1,
        pageSize: 10,
        roleId: '',
        provider: '',
        status: -1
      },
      statusOptions: [
        {
          status: -1,
          label: this.$t('label.status.all')
        },
        {
          status: 0,
          label: this.$t('label.status.delete')
        },
        {
          status: 1,
          label: this.$t('label.status.active')
        },
        {
          status: 2,
          label: this.$t('label.status.pending')
        },
        {
          status: 3,
          label: this.$t('label.status.block')
        }
      ],
      visible: false,
      newPassword: '',
      selectedPwdId: ''
    }
  },
  computed: {
    ...mapGetters('sysAdminUser', ['listUserExceptAdmin', 'totalElements']),
    ...mapGetters('permission', ['roles', 'permissions', 'permissionByRoleId']),
    totalPage() {
      return Math.ceil(this.totalElements / this.listQuery.pageSize)
    }
  },
  mounted() {
    this.getListRoles().then(() => {
      this.getList()
    })
  },
  methods: {
    ...mapActions('sysAdminUser', ['getListUserExceptAdminPaging']),
    ...mapActions('user', ['changePassword', 'banUser', 'unlockUser', 'deleteUser']),
    ...mapActions('permission', ['getListRoles', 'getListPermissions']),
    getList() {
      this.listLoading = true
      this.getListUserExceptAdminPaging(this.listQuery).then(() => {
        this.listLoading = false
      })
    },
    mapName(firstName, lastName) {
      if (i18n.locale === 'vi') {
        return (lastName || '') + ' ' + firstName
      } else if (i18n.locale === 'en') {
        return firstName + ' ' + (lastName || '')
      }
    },
    mapRole(roleId) {
      return this.roles.find(role => role.id === roleId).name
    },
    mapStatus(status) {
      switch (status) {
        case 0:
          return this.$t('label.status.delete')
        case 1:
          return this.$t('label.status.active')
        case 2:
          return this.$t('label.status.pending')
        case 3:
          return this.$t('label.status.block')
      }
    },
    handleBeforeChangePassword(userId) {
      this.selectedPwdId = userId
      this.visible = true
    },
    handleChangePassword() {
      const param = {
        id: this.selectedPwdId,
        newPassword: this.newPassword
      }
      this.changePassword(param).then(() => {
        showSuccess('message.change_pass_success')
        this.selectedPwdId = ''
        this.visible = false
        this.newPassword = ''
      })
    },
    handleClosePwdChange() {
      this.selectedPwdId = ''
      this.visible = false
      this.newPassword = ''
    },
    handleBanUser(userId) {
      this.banUser(userId).then(() => {
        showSuccess('message.ban_user_success')
        this.getList()
      })
    },
    handleUnlockUser(userId) {
      this.unlockUser(userId).then(() => {
        showSuccess('message.unlock_user_success')
        this.getList()
      })
    },
    handleDeleteUser(userId) {
      this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
        confirmButtonText: this.$t('button.confirm'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        this.deleteUser(userId).then(() => {
          showSuccess('message.delete_user_success')
          this.getList()
        })
      }).catch(() => {
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  .edit-input {
    padding-right: 100px;
  }
  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 10px;
  }
  /deep/ .el-upload {
    display: block;
  }
  /deep/ .el-table, .el-checkbox__inner {
    z-index: 0;
  }
  .upload-dialog {
    z-index: 2;
  }
</style>
