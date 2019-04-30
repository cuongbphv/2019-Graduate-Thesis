<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.searchKey"
        :placeholder="$t('label.search')"
        style="width: 400px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >{{ $t('table.search') }}</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >{{ $t('table.add') }}</el-button>
      <el-upload
        class="upload-demo"
        :action="uploadModel.url"
        :headers="uploadModel.headers"
        :on-success="handleOnSuccess"
        :on-exceed="handleOnExceed"
        :file-list="fileList"
        :limit="1"
        accept=".json"
      >
        <el-button
          v-waves
          :loading="importLoading"
          class="filter-item"
          type="success"
          icon="el-icon-upload2"
        >{{ $t('table.import') }}</el-button>
      </el-upload>
    </div>

    <el-table
      :key="tableKey"
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
      <el-table-column :label="$t('table.header.city')" align="center" prop="2" sortable="custom">
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
      <el-table-column :label="$t('table.header.type')" align="center" prop="3" sortable="custom">
        <template slot-scope="scope">
          <el-tag>{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="info" size="mini">{{ $t('button.detail') }}</el-button>
          <el-button
            type="primary"
            size="mini"
            @click="scope.row.edit = !scope.row.edit"
          >{{ $t('button.edit') }}</el-button>
          <el-button type="danger" size="mini">{{ $t('button.delete') }}</el-button>
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

    <!--    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">-->
    <!--      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="70px" style="width: 400px; margin-left:50px;">-->
    <!--        <el-form-item :label="$t('table.type')" prop="type">-->
    <!--          <el-select v-model="temp.type" class="filter-item" placeholder="Please select">-->
    <!--            <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />-->
    <!--          </el-select>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item :label="$t('table.date')" prop="timestamp">-->
    <!--          <el-date-picker v-model="temp.timestamp" type="datetime" placeholder="Please pick a date" />-->
    <!--        </el-form-item>-->
    <!--        <el-form-item :label="$t('table.title')" prop="title">-->
    <!--          <el-input v-model="temp.title" />-->
    <!--        </el-form-item>-->
    <!--        <el-form-item :label="$t('table.status')">-->
    <!--          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">-->
    <!--            <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />-->
    <!--          </el-select>-->
    <!--        </el-form-item>-->
    <!--        <el-form-item :label="$t('table.importance')">-->
    <!--          <el-rate v-model="temp.importance" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="3" style="margin-top:8px;" />-->
    <!--        </el-form-item>-->
    <!--        <el-form-item :label="$t('table.remark')">-->
    <!--          <el-input v-model="temp.remark" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />-->
    <!--        </el-form-item>-->
    <!--      </el-form>-->
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button @click="dialogFormVisible = false">{{ $t('table.cancel') }}</el-button>-->
    <!--        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">{{ $t('table.confirm') }}</el-button>-->
    <!--      </div>-->
    <!--    </el-dialog>-->

    <!--    <el-dialog :visible.sync="dialogPvVisible" title="Reading statistics">-->
    <!--      <el-table :data="pvData" border fit highlight-current-row style="width: 100%">-->
    <!--        <el-table-column prop="key" label="Channel" />-->
    <!--        <el-table-column prop="pv" label="Pv" />-->
    <!--      </el-table>-->
    <!--      <span slot="footer" class="dialog-footer">-->
    <!--        <el-button type="primary" @click="dialogPvVisible = false">{{ $t('table.confirm') }}</el-button>-->
    <!--      </span>-->
    <!--    </el-dialog>-->

  </div>
</template>

<script>
import waves from '@/directive/waves' // Waves directive
import { parseTime } from '@/utils'
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { API } from '@/utils/constants'
import { getToken } from '../../../utils/auth'
import { mapActions, mapGetters, mapState } from 'vuex'

export default {
  name: 'ComplexTable',
  components: {
    Pagination
  },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      fileList: [],
      isVisible: false,
      tableKey: 0,
      listLoading: true,
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        searchKey: '',
        ascSort: true,
        sortKey: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: 'Edit',
        create: 'Create'
      },
      dialogPvVisible: false,
      pvData: [],
      rules: {
        type: [{ required: true, message: 'type is required', trigger: 'change' }],
        timestamp: [{ type: 'date', required: true, message: 'timestamp is required', trigger: 'change' }],
        title: [{ required: true, message: 'title is required', trigger: 'blur' }]
      },
      importLoading: false,
      uploadModel: {
        url: process.env.VUE_APP_BASE_URL + API.IMPORT_LOCATION,
        headers: {
          'Authorization': 'Bearer ' + getToken()
        }
      }
    }
  },
  computed: {
    ...mapGetters('location', ['listPagingLocation']),
    ...mapState('location', ['totalElements', 'totalPages'])
  },
  created() {
    this.getList()
  },
  methods: {
    ...mapActions('location', ['loadListPagingLocation']),
    /*
    Handle fo import database
     */
    handleOnSuccess(res, file, fileList) {
      console.log(res)
    },
    handleOnExceed(file, fileList) {
      this.$message({
        message: 'Remove uploaded file to upload new file',
        type: 'error'
      })
    },
    handleRemove(file, fileList) {
    },
    /*
    Handle Search Sort
    */
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
    /*
    Handle action with table
     */
    cancelEdit(row) {
      row.title = row.originalTitle
      row.edit = false
      this.$message({
        message: 'The title has been restored to the original value',
        type: 'warning'
      })
    },
    confirmEdit(row) {
      row.edit = false
      row.originalTitle = row.title
      this.$message({
        message: 'The title has been edited',
        type: 'success'
      })
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
          this.temp.author = 'vue-element-admin'
          // createArticle(this.temp).then(() => {
          //   this.list.unshift(this.temp)
          //   this.dialogFormVisible = false
          //   this.$notify({
          //     title: '成功',
          //     message: '创建成功',
          //     type: 'success',
          //     duration: 2000
          //   })
          // })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.temp.timestamp = new Date(this.temp.timestamp)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          tempData.timestamp = +new Date(tempData.timestamp) // change Thu Nov 30 2017 16:41:05 GMT+0800 (CST) to 1512031311464
          // updateArticle(tempData).then(() => {
          //   for (const v of this.list) {
          //     if (v.id === this.temp.id) {
          //       const index = this.list.indexOf(v)
          //       this.list.splice(index, 1, this.temp)
          //       break
          //     }
          //   }
          //   this.dialogFormVisible = false
          //   this.$notify({
          //     title: '成功',
          //     message: '更新成功',
          //     type: 'success',
          //     duration: 2000
          //   })
          // })
        }
      })
    },
    handleDelete(row) {
      this.$notify({
        title: '成功',
        message: '删除成功',
        type: 'success',
        duration: 2000
      })
      const index = this.list.indexOf(row)
      this.list.splice(index, 1)
    },
    handleFetchPv(pv) {
      // fetchPv(pv).then(response => {
      //   this.pvData = response.data.pvData
      //   this.dialogPvVisible = true
      // })
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

<style lang="scss" scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
