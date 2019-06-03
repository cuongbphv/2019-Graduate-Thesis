<template>
  <div class="table-ward">
    <el-table
      :key="provinceId"
      v-loading="isLoading"
      :data="tableData"
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
      <el-table-column :label="$t('table.header.id')" align="center" prop="id" sortable="custom">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.ward')" align="center" prop="name" sortable="custom">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.name" class="edit-input" size="small" />
          </template>
          <span v-else>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.type')" align="center" prop="type" sortable="custom">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.type" class="edit-input" size="small" />
          </template>
          <el-tag v-else>{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            v-if="!scope.row.edit"
            type="primary"
            icon="el-icon-edit"
            circle
            @click="editWard(scope.row)"
          />
          <el-button
            v-if="scope.row.edit"
            type="primary"
            icon="el-icon-check"
            circle
            @click="confirmUpdateWard(scope.row)"
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
            @click="deleteWard(scope.row.id)"
          />
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="listWards.length > 0"
      :total="listWards.length"
      :page.sync="listQuery.pageNumber"
      :limit.sync="listQuery.pageSize"
      @pagination="handlePagination"
    />
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import Pagination from '@/components/Pagination'
import { compareExpression } from '@/utils'
import { Status } from '@/utils/constants'
import { showMessageAfterCallAPI } from '@/utils/message'
export default {
  name: 'TableDistrict',
  components: {
    Pagination
  },
  props: {
    provinceId: {
      type: String,
      default: '00'
    },
    districtId: {
      type: String,
      default: '00'
    },
    paging: {
      type: Object,
      default: undefined
    },
    searchKey: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      isLoading: false,
      listWards: [],
      preloadList: [],
      tableData: [],
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        searchKey: '',
        ascSort: false,
        sortKey: 'name'
      },
      originalWard: {},
      wardIds: {}
    }
  },
  computed: {
    ...mapGetters('location', ['listWardByDistrictId'])
  },
  watch: {
    searchKey: function(newVal) {
      this.listQuery.searchKey = newVal
      this.getList()
    }
  },
  created() {
    this.listQuery = {
      pageNumber: this.paging.pageNumber,
      pageSize: this.paging.pageSize,
      ascSort: this.paging.ascSort,
      sortKey: this.paging.sortKey,
      searchKey: this.searchKey
    }
    this.preloadList = [...this.listWardByDistrictId(this.provinceId, this.districtId)]
    this.getList()
  },
  methods: {
    ...mapActions('location', ['updateWard', 'deleteWards']),
    handlePagination({ page, limit }) {
      this.listQuery.pageNumber = page
      this.listQuery.pageSize = limit
      this.getList()
    },
    sortChange({ prop, order }) {
      this.listQuery.ascSort = order === 'descending'
      this.listQuery.sortKey = prop
      this.getList()
    },
    getList() {
      this.listWards = this.preloadList.filter(ward => {
        return ward.name.toLowerCase().includes(this.searchKey.toLowerCase())
      })
      const start = (this.listQuery.pageNumber - 1) * this.listQuery.pageSize
      this.listWards.sort(compareExpression(this.listQuery.sortKey, this.listQuery.ascSort))
      this.tableData = this.listWards.slice(start, start + this.listQuery.pageSize)
    },
    editWard(row) {
      row.edit = !row.edit
      this.originalWard[row.id] = {
        name: row.name,
        type: row.type
      }
    },
    cancelEdit(row) {
      row.name = this.originalWard[row.id].name
      row.type = this.originalWard[row.id].type
      row.edit = false
      this.$message({
        message: this.$t('message.location.ward_restore'),
        type: 'info'
      })
    },
    confirmUpdateWard(row) {
      row.edit = !row.edit
      this.updateWard({
        id: row.id,
        provinceId: this.provinceId,
        districtId: this.districtId,
        name: row.name,
        type: row.type
      }).then(res => {
        if (res.status === Status.SUCCESS) {
          this.$message({
            message: this.$t('message.location.update_ward_success'),
            type: 'success'
          })
        } else {
          this.$message({
            message: this.$t('errors.location.' + res.status),
            type: 'error'
          })
        }
      })
    },
    deleteWard(id) {
      this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
        confirmButtonText: this.$t('button.confirm'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        this.deleteWards({
          provinceId: this.provinceId,
          districtId: this.districtId,
          ids: id
        }).then((res) => {
          showMessageAfterCallAPI(res, 'message.location.delete_ward_success')
          this.$emit('reloadList')
        })
      }).catch(() => {})
    },
    handleSelectionChange(rows) {
      if (rows.length > 0) {
        // eslint-disable-next-line
        let ids = {}
        ids[this.listQuery.pageNumber] = rows.map(district => district.id)
        this.districtIds = Object.assign({}, this.districtIds, ids)
        this.$emit('keepListIdToDelete', this.districtIds)
      }
    }
  }
}
</script>
