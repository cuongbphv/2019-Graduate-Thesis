<template>
  <div class="step1">
    <h1>Lựa chọn danh mục tin đăng</h1>

    <div class="select-category">
      <div class="search-form">
        <el-form>
          <el-form-item>
            <md-input
              v-model="searchKey"
              icon="search"
              placeholder="Nhập khu vực tìm kiếm"
              @keyup.enter.native="loadListCategory(selectedCategoryId)"
            >Tìm kiếm</md-input>
          </el-form-item>
        </el-form>
      </div>

      <div class="category-result">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item v-if="listBreadCrumb.length > 0">
            <a href="javascript:void(0)" @click="loadListCategory(null)">{{ $t('label.category') }}</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item
            v-for="item in listBreadCrumb"
            :key="item.id"
          >
            <a href="javascript:void(0)" @click="handleBreadCrumbClick(item)">{{ item.name }}</a>
          </el-breadcrumb-item>
        </el-breadcrumb>

        <ul>
          <li v-for="item in categories" :key="item.id" @click="selectCategory(item)">{{ item.name }}</li>
        </ul>

        <el-form v-if="showMetadata" label-position="left" label-width="150px">
          <el-row
            v-for="(item, index) in metadataTemplate"
            :key="item.slug"
          >
            <el-form-item :label="item.label">
              <el-col :md="11" :lg="11">
                <el-select v-model="postMetadata[index].value" style="width: 100%" placeholder="Select Options">
                  <el-option v-for="option in item.options" :key="option.value" :label="option.label" :value="option.value" />
                </el-select>
              </el-col>
              <el-col :md="{span: 11, offset: 2}" :lg="{span: 11, offset: 2}">
                <el-input v-if="item.type === 'text'" v-model="postMetadata[index].value" />
                <el-color-picker v-if="item.type === 'color'" v-model="postMetadata[index].value" />
              </el-col>
            </el-form-item>
          </el-row>
        </el-form>
      </div>
    </div>

    <el-row class="center-padding-top">
      <el-button type="success" @click="changeStep('next')">{{ $t('button.continue') }}</el-button>
    </el-row>
  </div>
</template>

<script>
import MdInput from '@/components/MDinput'
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'Step1',
  components: {
    MdInput
  },
  props: {
    category: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      searchKey: '',
      categories: [],
      listBreadCrumb: [],
      selectedCategoryId: null,
      listMetadata: [],
      postMetadata: [],
      metadataTemplate: [],
      showMetadata: false
    }
  },
  computed: {
    ...mapGetters('category', ['listCategory', 'metadata'])
  },
  watch: {
    listCategory: function(newVal) {
      this.showMetadata = newVal.length === 0
      if (this.showMetadata) {
        let allMeta = []
        for (const key in this.listMetadata) {
          if (this.listMetadata.hasOwnProperty(key)) {
            allMeta = [...allMeta, ...this.listMetadata[key]]
          }
        }

        this.metadataTemplate = allMeta

        this.postMetadata = allMeta.map(obj => {
          const x = Object.assign({}, obj)
          x.value = ''
          delete x.options
          return x
        })
      } else {
        this.metadataTemplate = []
        this.postMetadata = []
      }
      this.categories = Object.assign([], newVal)
    },
    metadata: function(newVal) {
      this.listMetadata.push(newVal)
    }
  },
  created() {
    if (Object.keys(this.category).length > 0) {
      this.showMetadata = true
      this.selectedCategoryId = this.category.breadCrumb[this.category.breadCrumb.length - 1].id
      this.listBreadCrumb = this.category.breadCrumb
      this.listMetadata = this.category.listMetadata
      let allMeta = []
      for (const key in this.listMetadata) {
        if (this.listMetadata.hasOwnProperty(key)) {
          allMeta = [...allMeta, ...this.listMetadata[key]]
        }
      }
      this.metadataTemplate = allMeta
      this.postMetadata = this.category.postMetadata
    } else {
      this.loadListCategory(this.selectedCategoryId)
    }
  },
  methods: {
    ...mapActions('category', ['getListCategory', 'getMetadataByCategoryId']),
    loadListCategory(parentId) {
      if (parentId === null) {
        this.listBreadCrumb = []
        this.listMetadata = []
      }
      this.getListCategory({
        searchKey: this.searchKey,
        parentId: parentId
      }).then(() => {
      })
    },
    selectCategory(item) {
      this.getMetadataByCategoryId(item.id)
      this.selectedCategoryId = item.id
      this.listBreadCrumb.push(item)
      this.loadListCategory(item.id)
    },
    handleBreadCrumbClick(item) {
      const index = this.listBreadCrumb.findIndex(element => {
        return element.id === item.id
      })
      this.listBreadCrumb = Object.assign([], this.listBreadCrumb.slice(0, index + 1))
      this.listMetadata = Object.assign([], this.listMetadata.slice(0, index + 1))
      this.loadListCategory(item.id)
    },
    changeStep(action) {
      if (action === 'next') {
        if (this.selectedCategoryId === null) {
          this.$message({
            message: 'Please select category for classified advertising.',
            type: 'error'
          })
          return
        }
        this.$emit('changeStep', action)
        this.$emit('submitFormCategory', {
          breadCrumb: this.listBreadCrumb,
          listMetadata: this.listMetadata,
          postMetadata: this.postMetadata
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.step1 {
  margin: 0 20px;
  text-align: center;

  h3 {
    text-align: center;
    font-weight: bold;
  }

  .search-form {
    height: 100px;
    width: 50%;
    margin: 0 auto;
  }

  .category-result {
    width: 70%;
    margin: 0 auto;

    .el-breadcrumb {
      padding: 10px 40px;
      font-weight: 500;
    }

    ul {
      list-style: none;
      padding: 0;
    }

    li {
      background: #f5f7fa;
      color: #606266;
      padding: 10px;
      margin: 10px auto;
      position: relative;
      font-size: 1.2em;

      &:hover {
        background: #dee0e5;
        color: #409EFF;
        cursor: pointer;
      }

      &.selected {
        background: #dee0e5;
        color: #409EFF;
      }
    }
  }
}
</style>
