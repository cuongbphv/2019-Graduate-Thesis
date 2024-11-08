<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="searchKey"
        :placeholder="$t('label.search')"
        style="width: 400px;"
        class="filter-item"
        @keyup.enter.native="handleSearchLocation"
      />
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        @click="openAddLocationDialog"
      >{{ $t('button.add') }}</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
        @click="handleDeleteLocation"
      >{{ $t('button.delete') }}</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="success"
        @click="openImportDialog"
      >{{ $t('button.import') }}</el-button>

      <el-dialog class="upload-dialog" :title="$t('label.upload_modal')" :visible.sync="dialogVisible" width="80%">
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
          >{{ $t('button.choose_file') }}</el-button>
        </el-upload>
        <h3>{{ $t('label.json_example') }}</h3>
        <pre class="format-code">
            <code>
              {
                  "89": {
                  "name": "An Giang",
                  "slug": "an-giang",
                  "type": "tinh",
                  "name_with_type": "Tỉnh An Giang",
                  "code": "89",
                      "quan-huyen": {
                          "883": {
                              "name": "Long Xuyên",
                              "type": "thanh-pho",
                              "slug": "long-xuyen",
                              "name_with_type": "Thành phố Long Xuyên",
                              "path": "Long Xuyên, An Giang",
                              "path_with_type": "Thành phố Long Xuyên, Tỉnh An Giang",
                              "code": "883",
                              "parent_code": "89",
                              "xa-phuong": {
                                  "30280": {
                                  "name": "Mỹ Bình",
                                  "type": "phuong",
                                  "slug": "my-binh",
                                  "name_with_type": "Phường Mỹ Bình",
                                  "path": "Mỹ Bình, Long Xuyên, An Giang",
                                  "path_with_type": "Phường Mỹ Bình, Thành phố Long Xuyên, Tỉnh An Giang",
                                  "code": "30280",
                                  "parent_code": "883"
                              }
                          }
                      }
                  }
              }
            </code>
          </pre>
      </el-dialog>
    </div>

    <add-location-modal
      :visible="addDialogVisible"
      :add-mode="addMode"
      @closeModal="handleCloseModal"
      @reloadList="handleReloadList"
    />

    <el-breadcrumb separator="/" style="margin-bottom: 15px; font-weight: 500;">
      <el-breadcrumb-item v-if="mode === 'district' || mode === 'ward'">
        <a href="javascript:void(0)" @click="loadListBefore('province')">{{ $t('label.list_province') }}</a>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="mode === 'district' || mode === 'ward'">
        <a href="javascript:void(0)" @click="loadListBefore('district')">{{ provinceName }}</a>
      </el-breadcrumb-item>
      <el-breadcrumb-item v-if="mode === 'ward'">
        <a href="javascript:void(0)" @click="loadListBefore('ward')">{{ districtName }}</a>
      </el-breadcrumb-item>
    </el-breadcrumb>

    <table-province
      v-if="mode === 'province'"
      :paging="tempPaging.provincePaging"
      :search-key="searchKey"
      @handleLoadListDistricts="showTableDistrict"
      @keepProvincePaging="saveProvincePagingStatus"
      @keepListIdToDelete="handleKeepIdsToDelete"
    />

    <table-district
      v-else-if="mode === 'district'"
      :key="districtKey"
      :province-id="provinceId"
      :paging="tempPaging.districtPaging"
      :search-key="searchKey"
      @handleLoadListWards="showTableWard"
      @keepDistrictPaging="saveDistrictPagingStatus"
      @keepListIdToDelete="handleKeepIdsToDelete"
      @reloadList="handleReloadList"
    />

    <table-ward
      v-else-if="mode === 'ward'"
      :key="wardKey"
      :province-id="provinceId"
      :district-id="districtId"
      :paging="tempPaging.wardPaging"
      :search-key="searchKey"
      @keepListIdToDelete="handleKeepIdsToDelete"
      @reloadList="handleReloadList"
    />

  </div>
</template>

<script>
import TableDistrict from '@/components/Location/TableDistrict'
import TableWard from '@/components/Location/TableWard'
import TableProvince from '@/components/Location/TableProvince'
import AddLocationModal from '@/components/Location/AddLocationModal'

import waves from '@/directive/waves'
import { API } from '@/utils/constants'
import { getToken } from '@/utils/auth'
import { mapActions } from 'vuex'
import { Status } from '../../../utils/constants'
import { showMessageAfterCallAPI } from '../../../utils/message'

export default {
  name: 'Location',
  components: {
    TableDistrict,
    TableWard,
    TableProvince,
    AddLocationModal
  },
  directives: { waves },
  data() {
    return {
      mode: 'province',
      provinceId: '00',
      districtId: '00',
      provinceName: '',
      districtName: '',
      districtKey: 0,
      wardKey: 0,
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
          ascSort: true,
          sortKey: 2
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
    ...mapActions('location', ['loadListPagingLocation', 'deleteProvinces', 'deleteDistricts', 'deleteWards']),
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
        message: this.$t('message.location.upload_success'),
        type: 'success'
      })
    },
    handleOnExceed(file, fileList) {
      this.$message({
        message: this.$t('message.location.upload_exceed'),
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
    handleKeepIdsToDelete(ids) {
      let allIds = []
      for (const key in ids) {
        if (ids.prototype.hasOwnProperty.call(key)) {
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
            this.deleteProvinces(this.deletedIds).then((res) => {
              if (res.status === Status.SUCCESS) {
                this.loadListPagingLocation(this.tempPaging.provincePaging)
              }
              showMessageAfterCallAPI(res, 'message.location.delete_province_success')
            })
          } else if (this.mode === 'district') {
            this.deleteDistricts({
              provinceId: this.provinceId,
              ids: this.deletedIds
            }).then((res) => {
              if (res.status === Status.SUCCESS) {
                this.loadListPagingLocation(this.tempPaging.provincePaging)
                this.districtKey++
              }
              showMessageAfterCallAPI(res, 'message.location.delete_district_success')
            })
          } else if (this.mode === 'ward') {
            this.deleteWards({
              provinceId: this.provinceId,
              districtId: this.districtId,
              ids: this.deletedIds
            }).then((res) => {
              if (res.status === Status.SUCCESS) {
                this.loadListPagingLocation(this.tempPaging.provincePaging)
                this.wardKey++
              }
              showMessageAfterCallAPI(res, 'message.location.delete_ward_success')
            })
          }
        }).catch(() => {})
      } else {
        this.$alert(this.$t('message.info_delete'), this.$t('label.info'), {
          confirmButtonText: 'OK'
        })
      }
    },
    handleReloadList() {
      this.loadListPagingLocation(this.tempPaging.provincePaging).then(() => {
        if (this.mode === 'district') {
          this.districtKey++
        } else if (this.mode === 'ward') {
          this.wardKey++
        }
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
