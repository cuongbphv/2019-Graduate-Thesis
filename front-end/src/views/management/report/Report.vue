<template>
  <div class="app-container">
    <div v-if="mode === 'general'">
      <el-table
        :key="0"
        v-loading="isLoading"
        :data="listReports"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @sort-change="sortChange"
      >
        <el-table-column :label="$t('table.header.title')" align="center" prop="1">
          <template slot-scope="scope">
            <span>{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.author')" align="center">
          <template slot-scope="scope">
            <span>{{ mapName(scope.row.firstName, scope.row.lastName) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.lastReported')" align="center" prop="2" sortable="custom">
          <template slot-scope="scope">
            <span>{{ scope.row.createdDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.actions')" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              type="info"
              icon="el-icon-info"
              circle
              @click="goToDetail(scope.row.id)"
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

    <div v-else>
      <h3>{{ $t('table.header.title') }} : {{ reportDetail.title }}</h3>
      <h3>{{ $t('table.header.author') }} : {{ mapName(reportDetail.firstName, reportDetail.lastName) }}</h3>
      <h3>{{ $t('table.header.createdDate') }} : {{ reportDetail.createdDateAds | parseTime('{y}-{m}-{d} {h}:{i}') }}</h3>
      <div class="filter-container">
        <el-button
          class="filter-item"
          type="info"
          @click="mode = 'general'"
        >{{ $t('button.back') }}</el-button>
        <a
          class="el-button filter-item el-button--primary el-button--medium"
          :href="'/advertising/' + reportDetail.classifiedAdsId"
          target="_blank"
        >{{ $t('button.detail') }}</a>
        <el-button
          class="filter-item"
          type="warning"
          @click="changeStatusOfReport(reportDetail, 3)"
        >{{ $t('button.block') }}</el-button>
        <el-button
          class="filter-item"
          type="danger"
          @click="changeStatusOfReport(reportDetail, 0)"
        >{{ $t('button.delete') }}</el-button>
        <el-button
          class="filter-item"
          type="default"
          @click="changeStatusOfReport(reportDetail, -1)"
        >{{ $t('button.deactive') }}</el-button>
      </div>
      <el-table
        :key="1"
        :data="reportDetail.listReportDetail"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @sort-change="sortChange"
      >
        <el-table-column :label="$t('table.header.userReport')" align="center">
          <template slot-scope="scope">
            <span>{{ mapName(scope.row.firstName, scope.row.lastName) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.reason')" align="center">
          <template slot-scope="scope">
            <span>{{ mapReason(scope.row.reason) }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.description')" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.description }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('table.header.reportedDate')" align="center" prop="2">
          <template slot-scope="scope">
            <span>{{ scope.row.createdDate | parseTime('{y}-{m}-{d} {h}:{i}') }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>

  </div>
</template>

<script>
import waves from '@/directive/waves'
import { mapActions, mapGetters, mapState } from 'vuex'
import i18n from '@/lang'
import Pagination from '@/components/Pagination'
import { Status } from '../../../utils/constants' // Secondary package based on el-pagination

export default {
  name: 'Report',
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
        ascSort: true,
        sortKey: 1
      },
      mode: 'general',
      reportDetail: {}
    }
  },
  computed: {
    ...mapState('report', ['totalPages', 'totalElements']),
    ...mapGetters('report', ['listReports', 'getDetailReport'])
  },
  created() {
    this.getList()
  },
  methods: {
    ...mapActions('report', ['getPagingReport', 'changeStatusReport']),
    sortChange() {
      this.getList()
    },
    getList() {
      this.isLoading = true
      this.getPagingReport(this.listQuery).then(() => {
        this.isLoading = false
      })
    },
    mapName(firstName, lastName) {
      if (i18n.locale === 'vi') {
        return lastName + ' ' + firstName
      } else if (i18n.locale === 'en') {
        return firstName + ' ' + lastName
      }
    },
    mapReason(reason) {
      switch (reason) {
        case '0':
          return this.$t('label.reasons.cheat')
        case '1':
          return this.$t('label.reasons.duplicate')
        case '2':
          return this.$t('label.reasons.sold_out')
        case '3':
          return this.$t('label.reasons.not_contact')
        case '4':
          return this.$t('label.reasons.not_true')
        case '5':
          return this.$t('label.reasons.other')
      }
    },
    goToDetail(id) {
      this.mode = 'detail'
      this.reportDetail = Object.assign({}, this.getDetailReport(id))
    },
    changeStatusOfReport(reportDetail, status) {
      this.changeStatusReport({
        id: reportDetail.id,
        classifiedAdsId: reportDetail.classifiedAdsId,
        status: status
      }).then(res => {
        if (res.status === Status.SUCCESS) {
          this.$message({
            message: this.$t('message.action_report_success'),
            type: 'success'
          })
          this.getList()
          this.mode = 'general'
        } else {
          this.$message({
            message: this.$t('errors.' + res.status),
            type: 'error'
          })
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
</style>
