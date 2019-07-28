<template>
  <div class="table-category">
    <div class="filter-container">
      <el-button
        class="filter-item"
        type="primary"
        @click="handleOpenCreateModal"
      >{{ $t('button.add') }}</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        @click="handleDeleteCategory"
      >{{ $t('button.delete') }}</el-button>
    </div>
    <metadata-modal
      :visible="metadataDialogVisible"
      :category-id="metadataDialogCategoryId"
      :mode="metadataMode"
      @updateMetadataSuccess="handleUpdateMetadataSuccess"
      @updateFilterSuccess="handleUpdateFilterSuccess"
      @closeModal="handleCloseModal"
    />
    <create-category-modal
      :visible="categoryDialogVisible"
      :parent-id="categoryDialogParentId"
      @closeModal="handleCloseCreateCategoryModal"
      @createCategorySuccess="handleCreateCategorySuccess"
    />
    <category-detail-modal
      :visible="categoryUpdateDialogVisible"
      :category-id="categoryDialogId"
      @closeModal="handleCloseUpdateCategoryModal"
      @createCategorySuccess="handleUpdateCategorySuccess"
    />
    <el-breadcrumb separator="/" style="margin-bottom: 15px; font-weight: 500;">
      <el-breadcrumb-item>
        <a href="javascript:void(0)" @click="getList()">{{ $t('label.category') }}</a>
      </el-breadcrumb-item>
      <el-breadcrumb-item
        v-for="item in listBreadCrumb"
        :key="item.id"
      >
        <a href="javascript:void(0)" @click="handleBreadCrumbClick(item)">{{ item.name }}</a>
      </el-breadcrumb-item>
    </el-breadcrumb>
    <el-table
      :key="0"
      ref="categoryTable"
      v-loading="listLoading"
      :data="listCategory"
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
      <el-table-column :label="$t('table.header.name')" align="center" prop="1" sortable="custom" min-width="200px">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.name" class="edit-input" size="small" />
          </template>
          <span v-else>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.metadata')" align="center" min-width="100px">
        <template slot-scope="scope">
          <template>
            <el-button
              type="primary"
              icon="el-icon-edit"
              circle
              @click="handleOpenMetadataModal(scope.row)"
            />
          </template>
        </template>
      </el-table-column>
      <!--<el-table-column :label="$t('table.header.filter')" align="center" min-width="100px">-->
      <!--<template slot-scope="scope">-->
      <!--<template>-->
      <!--<el-button-->
      <!--type="primary"-->
      <!--icon="el-icon-edit"-->
      <!--circle-->
      <!--@click="handleOpenFilterModal(scope.row)"-->
      <!--/>-->
      <!--</template>-->
      <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column :label="$t('table.actions')" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="info"
            icon="el-icon-info"
            circle
            @click="getList(scope.row)"
          />
          <el-button
            type="primary"
            icon="el-icon-edit"
            circle
            @click="handleOpenUpdateModal(scope.row)"
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
      @pagination="loadListWithSelected"
    />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import MetadataModal from './MetadataModal'
import CreateCategoryModal from './CreateCategoryModal'
import CategoryDetailModal from './CategoryDetailModal'
import { mapActions, mapGetters, mapState } from 'vuex'
export default {
  name: 'TableCategory',
  components: {
    CategoryDetailModal,
    CreateCategoryModal,
    MetadataModal,
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
    },
    parentId: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      listBreadCrumb: [],
      listLoading: false,
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        ascSort: true,
        sortKey: 0,
        searchKey: ''
      },
      categoryIds: {},
      metadataDialogVisible: false,
      metadataDialogCategoryId: '',
      metadataMode: 'metadata',
      categoryDialogVisible: false,
      categoryUpdateDialogVisible: false,
      categoryDialogParentId: null,
      categoryDialogId: ''
    }
  },
  computed: {
    ...mapGetters('category', ['listCategory']),
    ...mapState('category', ['totalElements', 'totalPages'])
  },
  watch: {
    searchKey: function(newVal) {
      this.listQuery.searchKey = newVal
      this.loadListWithSelected()
    }
  },
  created() {
    this.listQuery = {
      pageNumber: this.paging.pageNumber,
      pageSize: this.paging.pageSize,
      ascSort: this.paging.ascSort,
      sortKey: this.paging.sortKey,
      searchKey: this.searchKey,
      parentId: this.parentId
    }
    this.getList()
  },
  methods: {
    ...mapActions('category', ['loadListPagingCategory', 'deleteCategoryByIds']),
    getList(parentElement) {
      // reset table state
      this.categoryIds = {}
      this.listQuery.pageNumber = 1
      // check if sub category click
      if (parentElement) {
        this.listBreadCrumb.push(parentElement)
        this.listQuery.parentId = parentElement.id
      } else {
        this.listQuery.parentId = null
        this.listBreadCrumb = []
      }
      this.loadListWithSelected()
    },
    loadListWithSelected() {
      this.listLoading = true
      this.loadListPagingCategory(this.listQuery).then(() => {
        const selected = this.categoryIds[this.listQuery.pageNumber]
        if (selected) {
          this.listCategory.forEach((cat, index) => {
            if (selected.includes(cat.id)) {
              this.$refs.categoryTable.toggleRowSelection(this.listCategory[index])
            }
          })
        }
        this.listLoading = false
      })
    },
    loadListSubCategory(row) {
      this.listLoading = true
      this.listQuery.parentId = row.id
      this.listQuery.pageNumber = 1
    },
    handleBreadCrumbClick(row) {
      const index = this.listBreadCrumb.findIndex(element => {
        return element.id === row.id
      })
      this.listBreadCrumb = Object.assign([], this.listBreadCrumb.slice(0, index + 1))

      // reset table state
      this.categoryIds = {}
      this.listQuery.pageNumber = 1
      this.listQuery.parentId = row.id
      this.loadListWithSelected()
    },
    // deleteCategory(id) {
    //   this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
    //     confirmButtonText: this.$t('button.confirm'),
    //     cancelButtonText: this.$t('button.cancel'),
    //     type: 'warning'
    //   }).then(() => {
    //     this.deleteProvinces(id).then(() => {
    //       this.loadListWithSelected()
    //     })
    //   }).catch(() => {})
    // },
    handleDeleteCategory() {
      let allIds = []
      for (const key in this.categoryIds) {
        if (this.categoryIds.hasOwnProperty(key)) {
          allIds = [...allIds, ...this.categoryIds[key]]
        }
      }
      if (allIds.length > 0) {
        this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
          confirmButtonText: this.$t('button.confirm'),
          cancelButtonText: this.$t('button.cancel'),
          type: 'warning'
        }).then(() => {
          this.deleteCategoryByIds(allIds).then(() => {
            this.categoryIds = {}
            this.loadListWithSelected()
          })
        }).catch(() => {})
      } else {
        this.$alert(this.$t('message.info_delete'), this.$t('label.info'), {
          confirmButtonText: 'OK'
        })
      }
    },
    sortChange({ prop, order }) {
      this.listQuery.ascSort = order === 'descending'
      this.listQuery.sortKey = parseInt(prop)
      this.loadListWithSelected()
    },
    handleOpenMetadataModal(row) {
      this.metadataDialogVisible = true
      this.metadataDialogCategoryId = row.id
      this.metadataMode = 'metadata'
    },
    handleOpenFilterModal(row) {
      this.metadataDialogVisible = true
      this.metadataDialogCategoryId = row.id
      this.metadataMode = 'filter'
    },
    handleOpenCreateModal() {
      this.categoryDialogParentId = this.listQuery.parentId
      this.categoryDialogVisible = true
    },
    handleOpenUpdateModal(row) {
      this.categoryDialogId = row.id
      this.categoryUpdateDialogVisible = true
    },
    handleUpdateMetadataSuccess(data) {
      const index = this.listCategory.findIndex(item => {
        return item.id === this.metadataDialogCategoryId
      })
      this.listCategory[index].metadata = data
    },
    handleUpdateFilterSuccess(data) {
      const index = this.listCategory.findIndex(item => {
        return item.id === this.metadataDialogCategoryId
      })
      this.listCategory[index].filter = data
    },
    handleCloseModal() {
      this.metadataDialogVisible = false
      this.metadataDialogCategoryId = ''
    },
    handleCloseCreateCategoryModal() {
      this.categoryDialogVisible = false
    },
    handleCloseUpdateCategoryModal() {
      this.categoryUpdateDialogVisible = false
    },
    handleCreateCategorySuccess() {
      this.categoryDialogVisible = false
      this.loadListWithSelected()
    },
    handleUpdateCategorySuccess() {
      this.categoryUpdateDialogVisible = false
      this.loadListWithSelected()
    },
    handleSelectionChange(rows) {
      if (rows.length > 0) {
        // eslint-disable-next-line
        let ids = {}
        ids[this.listQuery.pageNumber] = rows.map(ct => ct.id)
        this.categoryIds = Object.assign({}, this.categoryIds, ids)
      }
    }
  }
}
</script>
