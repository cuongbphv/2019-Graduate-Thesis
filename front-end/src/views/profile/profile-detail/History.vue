<template>
  <div class="gallery">

    <el-row style="text-align: right">
      <el-dropdown split-button type="default" style="text-align: center;">
        Sort
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>Action 1</el-dropdown-item>
          <el-dropdown-item>Action 2</el-dropdown-item>
          <el-dropdown-item>Action 3</el-dropdown-item>
          <el-dropdown-item>Action 4</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </el-row>

    <el-timeline v-for="item in listUserClassifiedAds" :key="item.id">
      <el-timeline-item :timestamp="item.createdDate | parseTime('{y}-{m}-{d} {h}:{i}')" placement="top">
        <el-card>
          <el-row>
            <el-col :xs="8" :sm="8" :md="8" :lg="8">
              <img :src="item.thumbnail.url" alt="No image">
            </el-col>
            <el-col :xs="16" :sm="16" :md="16" :lg="16">
              <h3>{{ item.title }}</h3>
              <p>Danh mục <el-tag type="info">{{ item.categoryName }}</el-tag></p>
              <p>Trạng thái <el-tag :type="item.status | statusFilter">{{ mapStatus(item.status) }}</el-tag></p>
            </el-col>
          </el-row>
        </el-card>
      </el-timeline-item>
    </el-timeline>

    <el-button
      v-if="totalPages > 1 && listQuery.pageNumber < totalPages"
      class="load-more"
      type="primary"
      @click="handleLoadMore"
    >Load More</el-button>
  </div>
</template>

<script>
import { mapState, mapActions, mapGetters } from 'vuex'
export default {
  name: 'History',
  data() {
    return {
      listUserClassifiedAds: [],
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
      }
    }
  },
  computed: {
    ...mapState('advertising', ['totalPages', 'totalElements']),
    ...mapGetters('advertising', ['listPagingClassifiedAds'])
  },
  created() {
    this.getList()
  },
  methods: {
    ...mapActions('advertising', ['getPagingUserHistoryClassifiedAds']),
    getList() {
      this.getPagingUserHistoryClassifiedAds(this.listQuery).then(() => {
        this.listUserClassifiedAds = [...this.listUserClassifiedAds, ...this.listPagingClassifiedAds]
      })
    },
    handleLoadMore() {
      this.listQuery.pageNumber++
      this.getList()
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
  }
</style>
