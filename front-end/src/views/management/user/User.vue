<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.searchKey"
        :placeholder="$t('label.search')"
        style="width: 400px;"
        class="filter-item"
        @keyup.enter.native="handleSearchUser"
      />
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="danger"
      >{{ $t('button.delete') }}</el-button>
    </div>

  </div>
</template>

<script>
import waves from '@/directive/waves'

export default {
  name: 'User',
  components: {
  },
  directives: { waves },
  data() {
    return {
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        searchKey: '',
        ascSort: true,
        sortKey: 1
      }
    }
  },
  methods: {
    handleSearchUser() {
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
