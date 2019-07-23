<template>
  <div class="container">
    <el-col>
      <search-item v-for="ads in savedAds.content" :key="ads.id" :ads="ads" />
    </el-col>
    <el-col>
      <pagination
        v-show="savedAds.totalRecord > 0"
        :total="savedAds.totalRecord"
        :page="savedAds.pageNumber"
        :limit.sync="savedAds.pageSize"
        @pagination="handlePageChange"
      />
    </el-col>
  </div>
</template>
<script>
import { SearchItem } from '../../../components/Advertising'
import { mapActions, mapGetters } from 'vuex'
import Pagination from '@/components/Pagination'

export default {
  name: 'Saved',
  components: {
    SearchItem,
    Pagination
  },
  data() {
    return {
      profileUserId: this.$route.params.id,
      queryParam: {
        pageNumber: 0,
        pageSize: 10
      }
    }
  },
  computed: {
    ...mapGetters('advertising', ['savedAds'])
  },
  mounted() {
    this.getSavedPost()
  },
  methods: {
    ...mapActions('advertising', ['getSavedAds', 'removeSaveAds']),
    getSavedPost() {
      this.queryParam.id = this.profileUserId
      this.getSavedAds(this.queryParam).then(() => {
      })
    },
    handlePageChange(pagination) {
      this.queryParam.pageNumber = pagination.page - 1
      this.queryParam.pageSize = pagination.limit
      this.getSavedPost()
    }
  }
}
</script>
<style scoped>

</style>
