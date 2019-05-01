<template>
  <div class="table-province">
    <el-table
      :key="0"
      v-loading="listLoading"
      :data="listPagingLocation"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column :label="$t('table.header.id')" align="center" prop="1" sortable="custom">
        <template slot-scope="scope">
          <span>{{ scope.row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.city')" align="center" prop="2" sortable="custom" min-width="200px">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.name" class="edit-input" size="small" />
            <el-button
              class="cancel-btn"
              size="small"
              icon="el-icon-refresh"
              type="warning"
              @click="cancelEdit(scope.row)"
            />
          </template>
          <span v-else>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.type')" align="center" prop="3" sortable="custom" min-width="100px">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="info"
            icon="el-icon-info"
            circle
            @click="handleLoadListDistricts(scope.row.id)"
          />
          <el-button
            type="primary"
            icon="el-icon-edit"
            circle
            @click="scope.row.edit = !scope.row.edit"
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
      }
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
    ...mapActions('location', ['loadListPagingLocation']),
    getList() {
      this.listLoading = true
      this.loadListPagingLocation(this.listQuery).then(() => {
        setTimeout(() => {
          this.listLoading = false
        }, 1000)
      })
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
    handleLoadListDistricts(provinceId) {
      this.$emit('handleLoadListDistricts', provinceId)
      this.$emit('keepProvincePaging', this.listQuery)
    }
  }
}
</script>
