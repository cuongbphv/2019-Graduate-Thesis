<template>
  <div class="table-district">
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
      <el-table-column :label="$t('table.header.district')" align="center" prop="name" sortable="custom">
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
            type="info"
            icon="el-icon-info"
            circle
            @click="handleLoadListWards(scope.row)"
          />
          <el-button
            v-if="!scope.row.edit"
            type="primary"
            icon="el-icon-edit"
            circle
            @click="editDistrict(scope.row)"
          />
          <el-button
            v-if="scope.row.edit"
            type="primary"
            icon="el-icon-check"
            circle
            @click="confirmUpdateDistrict(scope.row)"
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
            @click="deleteDistrict(scope.row.id)"
          />
        </template>
      </el-table-column>
    </el-table>
    <pagination
      v-show="listDistricts.length > 0"
      :total="listDistricts.length"
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
      listDistricts: [],
      preloadList: [],
      tableData: [],
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        searchKey: '',
        ascSort: false,
        sortKey: 'name'
      },
      originalDistrict: {},
      districtIds: {}
    }
  },
  computed: {
    ...mapGetters('location', ['listDistrictByProvinceId'])
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
    this.preloadList = [...this.listDistrictByProvinceId(this.provinceId)]
    this.getList()
  },
  methods: {
    ...mapActions('location', ['updateDistrict', 'deleteDistricts']),
    handleLoadListWards(row) {
      this.$emit('handleLoadListWards', { id: row.id, name: row.name })
      this.$emit('keepDistrictPaging', this.listQuery)
    },
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
      this.listDistricts = this.preloadList.filter(district => {
        return district.name.toLowerCase().includes(this.searchKey.toLowerCase())
      })
      const start = (this.listQuery.pageNumber - 1) * this.listQuery.pageSize
      this.listDistricts.sort(compareExpression(this.listQuery.sortKey, this.listQuery.ascSort))
      this.tableData = this.listDistricts.slice(start, start + this.listQuery.pageSize)
    },
    editDistrict(row) {
      row.edit = !row.edit
      this.originalDistrict[row.id] = {
        name: row.name,
        type: row.type
      }
    },
    cancelEdit(row) {
      row.name = this.originalDistrict[row.id].name
      row.type = this.originalDistrict[row.id].type
      row.edit = false
      this.$message({
        message: this.$t('message.location.district_restore'),
        type: 'info'
      })
    },
    confirmUpdateDistrict(row) {
      row.edit = !row.edit
      this.updateDistrict({
        id: row.id,
        provinceId: this.provinceId,
        name: row.name,
        type: row.type
      }).then(res => {
        if (res.status === Status.SUCCESS) {
          this.$message({
            message: this.$t('message.location.update_district_success'),
            type: 'success'
          })
        }
      })
    },
    deleteDistrict(id) {
      this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
        confirmButtonText: this.$t('button.confirm'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        this.deleteDistricts({
          provinceId: this.provinceId,
          ids: id
        }).then((res) => {
          showMessageAfterCallAPI(res, 'message.location.delete_district_success')
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
