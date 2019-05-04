<template>
  <div class="table-province">
    <el-table
      :key="0"
      ref="provinceTable"
      v-loading="listLoading"
      :data="listPagingLocation"
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
      <el-table-column :label="$t('table.header.id')" align="center" prop="1" sortable="custom">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.city')" align="center" prop="2" sortable="custom" min-width="200px">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.name" class="edit-input" size="small" />
          </template>
          <span v-else>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.type')" align="center" prop="3" sortable="custom" min-width="100px">
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
            @click="handleLoadListDistricts(scope.row)"
          />
          <el-button
            v-if="!scope.row.edit"
            type="primary"
            icon="el-icon-edit"
            circle
            @click="scope.row.edit = !scope.row.edit"
          />
          <el-button
            v-if="scope.row.edit"
            type="primary"
            icon="el-icon-check"
            circle
            @click="scope.row.edit = !scope.row.edit"
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
            @click="deleteProvince(scope.row.id)"
          />
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="totalPages > 0"
      :total="totalElements"
      :page.sync="listQuery.pageNumber"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapActions, mapGetters, mapState } from 'vuex'
export default {
  name: 'TableDistrict',
  components: {
    Pagination
  },
  props: {
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
      listLoading: true,
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        ascSort: true,
        sortKey: 1,
        searchKey: ''
      },
      provinceIds: {}
    }
  },
  computed: {
    ...mapGetters('location', ['listPagingLocation']),
    ...mapState('location', ['totalElements', 'totalPages'])
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
    this.getList()
  },
  methods: {
    ...mapActions('location', ['loadListPagingLocation', 'deleteProvinces']),
    getList() {
      this.listLoading = true
      this.loadListWithSelected()
    },
    loadListWithSelected() {
      this.loadListPagingLocation(this.listQuery).then(() => {
        const selected = this.provinceIds[this.listQuery.pageNumber]
        if (selected) {
          this.listPagingLocation.forEach((province, index) => {
            if (selected.includes(province.id)) {
              this.$refs.provinceTable.toggleRowSelection(this.listPagingLocation[index])
            }
          })
        }
        setTimeout(() => {
          this.listLoading = false
        }, 500)
      })
    },
    deleteProvince(id) {
      this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
        confirmButtonText: this.$t('button.confirm'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        this.deleteProvinces(id).then(() => {
          this.loadListWithSelected()
        })
      }).catch(() => {})
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    sortChange({ prop, order }) {
      this.listQuery.ascSort = order === 'descending'
      this.listQuery.sortKey = parseInt(prop)
      this.getList()
    },
    handleLoadListDistricts(row) {
      this.$emit('handleLoadListDistricts', { id: row.id, name: row.name })
      this.$emit('keepProvincePaging', this.listQuery)
    },
    handleSelectionChange(rows) {
      if (rows.length > 0) {
        // eslint-disable-next-line
        let ids = {}
        ids[this.listQuery.pageNumber] = rows.map(province => province.id)
        this.provinceIds = Object.assign({}, this.provinceIds, ids)
        this.$emit('keepListIdToDelete', this.provinceIds)
      }
    }
  }
}
</script>
