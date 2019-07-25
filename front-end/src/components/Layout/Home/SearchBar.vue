<template>
  <div class="search-bar">
    <el-autocomplete
      v-model="keyword"
      popper-class="custom-autocomplete"
      :fetch-suggestions="querySearch"
      placeholder="Please input"
      @select="handleSelect"
      @keyup.enter.native="handleSwitchToSearchPage"
    >
      <i
        slot="suffix"
        class="el-icon-search el-input__icon"
        @click="handleSwitchToSearchPage"
      />
      <template slot-scope="{ item }">
        <div class="value">{{ item.value }}</div>
        <span class="link">{{ item.link }}</span>
      </template>
    </el-autocomplete>
  </div>
</template>

<script>
export default {
  name: 'SearchBar',
  data() {
    return {
      links: [],
      keyword: ''
    }
  },
  mounted() {
    this.links = this.loadAll()
  },
  methods: {
    handleSwitchToSearchPage() {
      if (this.keyword !== '') {
        this.$router.push({ path: '/search?keyword=' + this.keyword })
      }
    },
    querySearch(queryString, cb) {
      var links = this.links
      var results = queryString ? links.filter(this.createFilter(queryString)) : links
      // call callback function to return suggestion objects
      cb(results)
    },
    createFilter(queryString) {
      return (link) => {
        return (link.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0)
      }
    },
    loadAll() {
      return [
        // { 'value': 'vue', 'link': 'https://github.com/vuejs/vue' },
        // { 'value': 'element', 'link': 'https://github.com/ElemeFE/element' },
        // { 'value': 'cooking', 'link': 'https://github.com/ElemeFE/cooking' },
        // { 'value': 'mint-ui', 'link': 'https://github.com/ElemeFE/mint-ui' },
        // { 'value': 'vuex', 'link': 'https://github.com/vuejs/vuex' },
        // { 'value': 'vue-router', 'link': 'https://github.com/vuejs/vue-router' },
        // { 'value': 'babel', 'link': 'https://github.com/babel/babel' }
      ]
    },
    handleSelect(item) {
      console.log(item)
    }
  }
}
</script>

<style lang="scss" scoped>
.search-bar {

  .custom-autocomplete {
    li {
      line-height: normal;
      padding: 7px;

      .value {
        text-overflow: ellipsis;
        overflow: hidden;
      }
      .link {
        font-size: 12px;
        color: #b4b4b4;
      }
    }
  }

  /deep/ .el-autocomplete {
    width: 60%;

    .el-input {
      margin-top: 5px;

      input {
        outline: 0;
        float: left;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12), 0 1px 2px rgba(0, 0, 0, 0.24);
      }

      .el-input__suffix {
        background-color: rgba(38, 50, 56, 0.8);
        height: 35px;
        width: 37px;
        cursor: pointer;
        text-align: center;
        line-height: 35px;
        color: #FFF;
        text-rendering: auto;
        text-shadow: 0 1px 1px rgba(0, 0, 0, 0.2);
        -webkit-transition: background-color .4s ease;
        transition: background-color .4s ease;
        -webkit-border-radius: 0 4px 4px 0;
        border-radius: 0 4px 4px 0;
        margin-right: -5px;
        margin-top: 1px;

        .el-input__inner {
          padding-right: 40px;
        }

        &:hover {
          background-color: #F6B402;
        }
      }
    }
  }
}
</style>
