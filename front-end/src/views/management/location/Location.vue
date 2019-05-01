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
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleSearchLocation"
      >{{ $t('table.search') }}</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-document-add"
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

import waves from '@/directive/waves'
import { API } from '@/utils/constants'
import { getToken } from '@/utils/auth'
import { mapActions } from 'vuex'

export default {
  name: 'ComplexTable',
  components: {
    TableDistrict,
    TableWard,
    TableProvince
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
      mode: 'province',
      provinceId: '00',
      districtId: '00',
      fileList: [],
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
    handleOnSuccess(res, file, fileList) {
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
    showTableDistrict(id) {
      this.provinceId = id
      this.mode = 'district'
    },
    showTableWard(districtId) {
      this.districtId = districtId
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
</style>
