<template>
  <div class="app-container">
    <table-category
      :search-key="searchKey"
      :paging="tempPaging.provincePaging"
    />
  </div>
</template>
<script>
import TableCategory from '@/components/Category/CategoryTable'

import waves from '@/directive/waves'
import { API } from '@/utils/constants'
import { getToken } from '@/utils/auth'
import { mapActions } from 'vuex'

export default {
  name: 'Category',
  components: {
    TableCategory
  },
  directives: { waves },
  data() {
    return {
      mode: 'province',
      provinceId: '00',
      districtId: '00',
      provinceName: '',
      districtName: '',
      addMode: {},
      fileList: [],
      dialogVisible: false,
      addDialogVisible: false,
      searchKey: '',
      tempPaging: {
        provincePaging: {
          pageNumber: 1,
          pageSize: 10,
          searchKey: '',
          ascSort: false,
          sortKey: 0
        },
        districtPaging: {
          pageNumber: 1,
          pageSize: 10,
          searchKey: '',
          ascSort: false,
          sortKey: 'name'
        },
        wardPaging: {
          pageNumber: 1,
          pageSize: 10,
          searchKey: '',
          ascSort: false,
          sortKey: 'name'
        }
      },
      importLoading: false,
      uploadModel: {
        url: process.env.VUE_APP_BASE_URL + API.IMPORT_LOCATION,
        headers: {
          'Authorization': 'Bearer ' + getToken()
        }
      },
      deletedIds: {}
    }
  },
  methods: {
    ...mapActions('location', ['loadListPagingLocation', 'deleteProvinces']),
    // Handle fo import database
    openImportDialog() {
      this.dialogVisible = true
    },
    handleCloseModal() {
      this.addDialogVisible = false
    },
    openAddLocationDialog() {
      this.addDialogVisible = true
      if (this.mode === 'province') {
        this.addMode = {
          mode: 'province'
        }
      } else if (this.mode === 'district') {
        this.addMode = {
          mode: 'district',
          id: this.provinceId,
          name: this.provinceName
        }
      } else if (this.mode === 'ward') {
        this.addMode = {
          mode: 'ward',
          proId: this.provinceId,
          proName: this.provinceName,
          id: this.districtId,
          name: this.districtName
        }
      }
    },
    handleOnSuccess(res, file, fileList) {
      this.dialogVisible = false
      this.loadListPagingLocation(this.tempPaging.provincePaging)
      this.fileList = []
      this.$message({
        message: 'Upload location data successfully',
        type: 'success'
      })
    },
    handleOnExceed(file, fileList) {
      this.$message({
        message: 'Remove uploaded file to upload new file',
        type: 'error'
      })
    },
    handleRemove(file, fileList) {
    },
    // Handle action with table
    handleSearchLocation() {
    },
    loadListBefore(mode) {
      this.mode = mode
      this.deletedIds = {}
    },
    showTableDistrict({ id, name }) {
      this.provinceId = id
      this.provinceName = name
      this.mode = 'district'
      this.deletedIds = {}
    },
    showTableWard({ id, name }) {
      this.districtId = id
      this.districtName = name
      this.mode = 'ward'
      this.deletedIds = {}
    },
    saveProvincePagingStatus(paging) {
      this.tempPaging.provincePaging = Object.assign({}, paging)
    },
    saveDistrictPagingStatus(paging) {
      this.tempPaging.districtPaging = Object.assign({}, paging)
    },
    handleDeleteProvince(ids) {
      let allIds = []
      for (const key in ids) {
        if (ids.hasOwnProperty(key)) {
          allIds = [...allIds, ...ids[key]]
        }
      }
      this.deletedIds = allIds
    },
    handleDeleteLocation() {
      if (this.deletedIds.length > 0) {
        this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
          confirmButtonText: this.$t('button.confirm'),
          cancelButtonText: this.$t('button.cancel'),
          type: 'warning'
        }).then(() => {
          if (this.mode === 'province') {
            this.deleteProvinces(this.deletedIds).then(() => {
              this.loadListPagingLocation(this.tempPaging.provincePaging)
            })
          }
        }).catch(() => {
        })
      } else {
        this.$alert(this.$t('message.info_delete'), this.$t('label.info'), {
          confirmButtonText: 'OK'
        })
      }
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
  .format-code {
    margin-bottom: 10px;
    text-align: left;
    font-size: 14px;
    line-height: 20px;
    overflow-y: auto;
    background: #eef1f6;
    code {
      padding: 0;
      line-height: 25px;
      display: inline;
      background: none;
    }
  }
</style>
