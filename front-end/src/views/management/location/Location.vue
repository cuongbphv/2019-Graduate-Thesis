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
        <h3>JSON example data</h3>
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

    <add-location-modal :visible="addDialogVisible" :add-mode="addMode" />

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
    />

    <table-district
      v-else-if="mode === 'district'"
      :province-id="provinceId"
      :paging="tempPaging.districtPaging"
      :search-key="searchKey"
      @handleLoadListWards="showTableWard"
      @keepDistrictPaging="saveDistrictPagingStatus"
    />

    <table-ward
      v-else-if="mode === 'ward'"
      :province-id="provinceId"
      :district-id="districtId"
      :paging="tempPaging.wardPaging"
      :search-key="searchKey"
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

export default {
  name: 'ComplexTable',
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
          sortKey: 1
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
      }
    }
  },
  methods: {
    ...mapActions('location', ['loadListPagingLocation']),
    // Handle fo import database
    openImportDialog() {
      this.dialogVisible = true
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
          mode: 'district',
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
    },
    showTableDistrict({ id, name }) {
      this.provinceId = id
      this.provinceName = name
      this.mode = 'district'
    },
    showTableWard({ id, name }) {
      this.districtId = id
      this.districtName = name
      this.mode = 'ward'
    },
    saveProvincePagingStatus(paging) {
      this.tempPaging.provincePaging = Object.assign({}, paging)
    },
    saveDistrictPagingStatus(paging) {
      this.tempPaging.districtPaging = Object.assign({}, paging)
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
