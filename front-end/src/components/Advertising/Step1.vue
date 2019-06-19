<template>
  <div class="step1">
    <h1>{{ $t('advertising.step1') }}</h1>

    <div class="select-category">
      <div class="search-form">
        <el-form>
          <el-form-item>
            <md-input
              v-model="searchKey"
              icon="search"
              :placeholder="$t('advertising.category')"
              @keyup.enter.native="loadListCategory(selectedCategoryId)"
            >{{ $t('advertising.search') }}</md-input>
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
              <el-col :id="'select-value-' + index" :md="11" :lg="11">
                <el-select v-model="postMetadata[index].valueLabel" style="width: 100%" placeholder="Select Options" @change="selectMetadata(postMetadata[index].valueLabel, item)">
                  <el-option v-for="option in item.options" :key="option.label" :label="option.label" :value="option.label" />
                </el-select>
              </el-col>
              <el-col v-if="item.type === 'color'" :id="'input-value-' + index" class="hide-toggle" :md="{span: 11}" :lg="{span: 11}">
                <el-col :md="{span: 21}" :lg="{span: 21}">
                  <el-input v-model="postMetadata[index].valueLabel" />
                </el-col>
                <el-col :md="{span: 3}" :lg="{span: 3}">
                  <el-color-picker v-if="item.type === 'color'" v-model="postMetadata[index].value" />
                </el-col>
              </el-col>
              <el-col v-else :id="'input-value-' + index" class="hide-toggle" :md="{span: 11}" :lg="{span: 11}">
                <el-input v-model="postMetadata[index].valueLabel" />
              </el-col>
              <el-button :id="'select-button-' + index" class="hide-toggle" style="margin-left: 10px" @click="toggleInput(index, $event)">Chọn</el-button>
              <el-button :id="'input-button-' + index" @click="toggleInput(index, $event)">Nhập</el-button>
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
          x.valueLabel = ''
          x.enValueLabel = ''
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
        console.log('metadata', this.postMetadata)
      }
    },
    selectMetadata(data, metaItem) {
      const index = metaItem.options.findIndex(function(element) {
        return element.label === data
      })
      const option = metaItem.options[index]
      if (option.enLabel) {
        this.postMetadata[index].enValueLabel = option.enLabel
      }
    },
    toggleInput(index) {
      const input = this.$el.querySelector('#input-value-' + index)
      const select = this.$el.querySelector('#select-value-' + index)
      const selectBtn = this.$el.querySelector('#select-button-' + index)
      const inputBtn = this.$el.querySelector('#input-button-' + index)
      input.classList.toggle('hide-toggle')
      select.classList.toggle('hide-toggle')
      selectBtn.classList.toggle('hide-toggle')
      inputBtn.classList.toggle('hide-toggle')
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

  .hide-toggle{
    display: none;
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
