<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.searchKey"
        :placeholder="$t('label.search')"
        style="width: 300px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select v-model="listQuery.roleId" :placeholder="$t('table.header.role')" clearable class="filter-item">
        <el-option v-for="item in roles" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-select v-model="listQuery.status" :placeholder="$t('table.header.status')" clearable class="filter-item">
        <el-option v-for="item in statusOptions" :key="item.status" :label="item.label" :value="item.status" />
      </el-select>
      <el-select v-model="listQuery.provider" :placeholder="$t('table.header.provider')" clearable class="filter-item">
        <el-option v-for="item in providerOptions" :key="item.provider" :label="item.label" :value="item.provider" />
      </el-select>
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      <!--      <el-checkbox v-model="showReviewer" class="filter-item" style="margin-left:15px;" @change="tableKey=tableKey+1">{{ $t('table.reviewer') }}</el-checkbox>-->
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="listUser"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column :label="$t('table.header.name')" align="center" prop="1" sortable="custom">
        <template slot-scope="scope">
          <span>{{ mapName(scope.row.firstName, scope.row.lastName) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.loginAccount')" align="center" prop="3" sortable="custom" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.phone }}</span>
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
        <template>
          <el-button
            type="primary"
            icon="el-icon-edit"
            circle
          />
          <el-button
            type="danger"
            icon="el-icon-delete"
            circle
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

  </div>
</template>

<script>
import waves from '@/directive/waves' // Waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapGetters, mapActions } from 'vuex'
import i18n from '@/lang'

export default {
  name: 'ComplexTable',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        3: 'warning',
        2: 'info',
        1: 'primary',
        0: 'danger'
      }
      return statusMap[status] || statusMap[2]
    },
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
      providerOptions: [
        {
          provider: 'local',
          label: 'BeeMarket'
        },
        {
          provider: 'google',
          label: 'Google'
        },
        {
          provider: 'facebook',
          label: 'Facebook'
        }
      ],
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
      showReviewer: false
    }
  },
  computed: {
    ...mapGetters('sysAdminUser', ['listUser', 'totalElements']),
    ...mapGetters('permission', ['roles']),
    totalPage() {
      return Math.ceil(this.totalElements / this.listQuery.pageSize)
    }
  },
  created() {
    this.getListRoles()
    this.getList()
  },
  methods: {
    ...mapActions('sysAdminUser', ['getListUserPaging']),
    ...mapActions('permission', ['getListRoles']),
    getList() {
      this.listLoading = true
      this.getListUserPaging(this.listQuery).then(() => {
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    mapStatus(status) {
      switch (status) {
        case 0:
          return this.$t('label.status.delete')
        case 1:
          return this.$t('label.status.active')
      }
    },
    mapName(firstName, lastName) {
      if (i18n.locale === 'vi') {
        return lastName + ' ' + firstName
      } else if (i18n.locale === 'en') {
        return firstName + ' ' + lastName
      }
    },
    mapRole(roleId) {
      return this.roles.find(role => role.id === roleId).name
    },
    handleFilter() {
      this.listQuery.pageNumber = 1
      this.listQuery.status = this.listQuery.status === null ? -1 : this.listQuery.status
      this.listQuery.provider = this.listQuery.provider === null ? '' : this.listQuery.provider
      this.listQuery.roleId = this.listQuery.roleId === null ? '' : this.listQuery.roleId
      this.getList()
    },
    sortChange({ prop, order }) {
      this.listQuery.ascSort = order === 'ascending'
      if (prop === '1') {
        if (i18n.locale === 'vi') {
          this.listQuery.sortKey = 2
        } else if (i18n.locale === 'en') {
          this.listQuery.sortKey = 1
        }
      } else {
        this.listQuery.sortKey = parseInt(prop)
      }
      this.handleFilter()
    },
    formatJson(filterVal, jsonData) {
      return jsonData.map(v => filterVal.map(j => {
        if (j === 'timestamp') {
          return parseTime(v[j])
        } else {
          return v[j]
        }
      }))
    }
  }
}
</script>
