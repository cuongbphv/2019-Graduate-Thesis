<template>
  <div class="gallery">

    <el-row class="row-filter">
      <span>{{ $t('advertising.search') }}</span>
      <el-input
        v-model="listQuery.searchKey"
        :placeholder="$t('label.search')"
        style="width: 300px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />

      <el-select
        v-model="listQuery.categoryId"
        :placeholder="$t('table.header.category')"
        clearable
        class="filter-item"
        @clear="listQuery.categoryId = ''"
      >
        <el-option v-for="item in listCategory" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>

      <el-select
        v-model="listQuery.status"
        :placeholder="$t('table.header.status')"
        clearable
        class="filter-item"
        @clear="listQuery.status = -1"
      >
        <el-option v-for="item in statusOptions" :key="item.status" :label="item.label" :value="item.status" />
      </el-select>

      <el-divider />
    </el-row>

    <el-timeline v-for="item in listUserClassifiedAds" :key="item.id">
      <el-timeline-item :timestamp="item.createdDate | parseTime('{y}-{m}-{d} {h}:{i}')" placement="top">
        <el-card>
          <el-row>
            <a :href="'/advertising/' + item.id">
              <el-col :xs="8" :sm="8" :md="8" :lg="8">
                <img :src="item.thumbnail.url" alt="No image">
              </el-col>
              <el-col :xs="16" :sm="16" :md="16" :lg="16">
                <h3>{{ item.title }}</h3>
                <p>{{ $t('advertising.location') }} <el-tag type="info">{{ item.addressResponse.detail ? item.addressResponse.detail + ', ' + item.addressResponse.ward.pathWithType : item.addressResponse.ward.pathWithType }}</el-tag></p>
                <p>{{ $t('advertising.category') }} <el-tag type="info">{{ item.categoryName }}</el-tag></p>
                <p>{{ $t('table.header.status') }} <el-tag :type="item.status | statusFilter">{{ mapStatus(item.status) }}</el-tag></p>
              </el-col>
            </a>
          </el-row>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <el-button
      v-if="totalPages > 1 && listQuery.pageNumber < totalPages"
      class="load-more"
      type="primary"
      @click="handleLoadMore"
    >{{ $t('button.load_more') }}</el-button>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
export default {
  name: 'History',
  data() {
    return {
      listUserClassifiedAds: [],
      profileId: this.$route.params.id,
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        searchKey: '',
        ascSort: false,
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
  watch: {
    'listQuery.searchKey': function(newVal, oldVal) {
      this.handleFilter()
    },
    'listQuery.status': function(newVal, oldVal) {
      this.handleFilter()
    },
    'listQuery.categoryId': function(newVal, oldVal) {
      this.handleFilter()
    }
  },
  created() {
    this.getList()
    this.loadListLocation({ searchKey: '' })
    this.getListCategory({ searchKey: this.searchKey, parentId: null })
  },
  methods: {
    ...mapActions('advertising', ['getPagingUserHistoryClassifiedAds']),
    ...mapActions('location', ['loadListLocation']),
    ...mapActions('category', ['getListCategory']),
    getList() {
      this.listQuery.userId = this.profileId
      this.getPagingUserHistoryClassifiedAds(this.listQuery).then(() => {
        this.listUserClassifiedAds = [...this.listPagingClassifiedAds]
      })
    },
    handleFilter() {
      this.listQuery.pageNumber = 1
      this.getList()
    },
    handleLoadMore() {
      this.listQuery.pageNumber++
      this.getPagingUserHistoryClassifiedAds(this.listQuery).then(() => {
        this.listUserClassifiedAds = [...this.listUserClassifiedAds, ...this.listPagingClassifiedAds]
      })
    },
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
    }
  }
}
</script>

<style lang="scss" scoped>
  .gallery  {
    margin-top: 20px;
    width: 100%;
    img {
      box-shadow: 0 3px 6px rgba(0,0,0,0.10), 0 3px 6px rgba(0,0,0,0.10);
      width: auto;
      height: auto;
      cursor: pointer;
      max-width: 100%;
    }
    .load-more {
      display: flex;
      justify-content: center;
      margin: 20px auto;
    }
    /deep/ .el-timeline-item__timestamp {
      font-weight: 600;
    }
    /deep/ .el-card__body {
      .el-row {
        margin-bottom: 0;
      }
      img {
        background: none;
        width: 90%;
      }
    }
    .row-filter {
      padding-left: 40px;
      span {
        padding-right: 35px;
      }
      .filter-item {
        width: 150px;
      }
    }
  }
</style>
