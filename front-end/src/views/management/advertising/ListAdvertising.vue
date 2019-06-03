<template>
  <div class="app-container">
    <div class="filter-container">
      <div class="filter-item">
        <span>{{ $t('advertising.search') }}</span>
        <el-input
          v-model="listQuery.searchKey"
          :placeholder="$t('label.search')"
          style="width: 300px;"
          class="filter-item"
          @keyup.enter.native="handleSearchClassifiedAds"
        />
      </div>

      <div class="filter-item">
        <span>{{ $t('advertising.category') }}</span>
        <el-select
          v-model="listQuery.categoryId"
          :placeholder="$t('table.header.category')"
          clearable
          class="filter-item"
          @clear="listQuery.categoryId = ''"
        >
          <el-option v-for="item in listCategory" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </div>

      <div class="filter-item">
        <span>{{ $t('table.header.status') }}</span>
        <el-select
          v-model="listQuery.status"
          :placeholder="$t('table.header.status')"
          clearable
          class="filter-item"
          @clear="listQuery.status = -1"
        >
          <el-option v-for="item in statusOptions" :key="item.status" :label="item.label" :value="item.status" />
        </el-select>
      </div>
    </div>

    <div class="filter-container">
      <div class="filter-item">
        <span>{{ $t('advertising.location') }}</span>
        <el-select
          v-model="listQuery.provinceId"
          :placeholder="$t('table.header.city')"
          clearable
          class="filter-item"
          @clear="listQuery.provinceId = ''"
        >
          <el-option v-for="item in listLocation" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-select
          v-if="listQuery.provinceId !== ''"
          v-model="listQuery.districtId"
          :placeholder="$t('table.header.district')"
          clearable
          class="filter-item"
          @clear="listQuery.districtId = ''"
        >
          <el-option v-for="item in listDistrictByProvinceId(listQuery.provinceId)" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <el-select
          v-if="listQuery.districtId !== ''"
          v-model="listQuery.wardId"
          :placeholder="$t('table.header.ward')"
          clearable
          class="filter-item"
          @clear="listQuery.wardId = ''"
        >
          <el-option
            v-for="item in listWardByDistrictId(listQuery.provinceId, listQuery.districtId)"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
        <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{ $t('table.search') }}</el-button>
      </div>
    </div>

    <el-table
      :key="0"
      v-loading="isLoading"
      :data="listPagingClassifiedAds"
      border
      fit
      highlight-current-row
      style="width: 100%;"
      @sort-change="sortChange"
    >
      <el-table-column :label="$t('table.header.title')" align="center" width="300" prop="1" sortable="custom">
        <template slot-scope="scope">
          <span>{{ scope.row.title }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.author')" align="center">
        <template slot-scope="scope">
          <span>{{ mapName(scope.row.firstName, scope.row.lastName) }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.category')" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.categoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.location')" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.addressResponse.detail ? scope.row.addressResponse.detail + ', ' + scope.row.addressResponse.ward.pathWithType : scope.row.addressResponse.ward.pathWithType }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.createdDate')" align="center" width="140" prop="2" sortable="custom">
        <template slot-scope="scope">
          <span>{{ scope.row.createdDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.header.status')" align="center" width="120" prop="3" sortable="custom">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ mapStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('table.actions')" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            type="info"
            icon="el-icon-info"
            circle
            @click="gotoDetail(scope.row.id)"
          />
          <el-button
            v-if="scope.row.status === 2"
            type="primary"
            icon="el-icon-check"
            circle
            @click="handleChangeStatusAds(scope.row.id, 1)"
          />
          <el-button
            v-if="scope.row.status === 2"
            type="warning"
            icon="el-icon-remove"
            circle
            @click="handleChangeStatusAds(scope.row.id, 3)"
          />
          <el-button
            v-if="scope.row.status !== 0"
            type="danger"
            icon="el-icon-delete"
            circle
            @click="handleChangeStatusAds(scope.row.id, 0)"
          />
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="totalElements > 0"
      :total="totalPages"
      :page.sync="listQuery.pageNumber"
      :limit.sync="listQuery.pageSize"
      @pagination="getList"
    />

  </div>
</template>

<script>
import waves from '@/directive/waves' // Waves directive
import Pagination from '@/components/Pagination' // Secondary package based on el-pagination
import { mapState, mapActions, mapGetters } from 'vuex'
import i18n from '@/lang'
import { showMessageAfterCallAPI } from '@/utils/message'

export default {
  name: 'ListAdvertising',
  components: {
    Pagination
  },
  directives: { waves },
  data() {
    return {
      isLoading: false,
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        searchKey: '',
        ascSort: false,
        sortKey: 2,
        provinceId: '',
        districtId: '',
        wardId: '',
        categoryId: '',
        status: -1
      },
      statusOptions: [
        {
          status: -1,
          label: this.$t('label.status.all')
        },
        {
          status: 0,
          label: this.$t('label.status.delete')
        },
        {
          status: 1,
          label: this.$t('label.status.active')
        },
        {
          status: 2,
          label: this.$t('label.status.pending')
        },
        {
          status: 3,
          label: this.$t('label.status.block')
        }
      ]
    }
  },
  computed: {
    ...mapState('advertising', ['totalPages', 'totalElements']),
    ...mapGetters('advertising', ['listPagingClassifiedAds']),
    ...mapGetters('location', ['listLocation', 'listDistrictByProvinceId', 'listWardByDistrictId']),
    ...mapGetters('category', ['listCategory'])
  },
  created() {
    this.getList()
    this.loadListLocation({ searchKey: '' })
    this.getListCategory({
      searchKey: this.searchKey,
      parentId: null
    })
  },
  methods: {
    ...mapActions('advertising', ['getPagingNewClassifiedAds', 'changeStatusAds']),
    ...mapActions('location', ['loadListLocation']),
    ...mapActions('category', ['getListCategory']),
    mapStatus(status) {
      switch (status) {
        case 0:
          return this.$t('label.status.delete')
        case 1:
          return this.$t('label.status.active')
        case 2:
          return this.$t('label.status.pending')
        case 3:
          return this.$t('label.status.block')
      }
    },
    mapName(firstName, lastName) {
      if (i18n.locale === 'vi') {
        return lastName + ' ' + firstName
      } else if (i18n.locale === 'en') {
        return firstName + ' ' + lastName
      }
    },
    handleFilter() {
      this.listQuery.pageNumber = 1
      this.getList()
    },
    getList() {
      this.isLoading = true
      this.getPagingNewClassifiedAds(this.listQuery).then(() => {
        this.isLoading = false
      })
    },
    handleSearchClassifiedAds() {
      this.getList()
    },
    sortChange({ prop, order }) {
      this.listQuery.ascSort = order === 'descending'
      this.listQuery.sortKey = parseInt(prop)
      this.getList()
    },
    gotoDetail(id) {
      this.$router.push({ path: '/advertising/' + id })
    },
    handleChangeStatusAds(id, status) {
      this.changeStatusAds({
        id: id, status: status
      }).then(res => {
        showMessageAfterCallAPI(res, 'message.update_status_ads_success')
        this.getList()
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
  .filter-item {
    display: inline-block;
    padding-right: 15px;
    span {
      padding-right: 5px;
    }
  }
</style>
