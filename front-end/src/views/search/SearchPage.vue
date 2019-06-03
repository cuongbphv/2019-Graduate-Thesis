<template>
  <div class="search-container">
    <main>
      <el-row>
        <el-col class="left" :sm="6" :md="5" :lg="5">
          <h3><i class="el-icon-s-operation" />&nbsp; Bộ lọc tìm kiếm</h3>
          <div class="search-group">
            <h4 v-if="listBreadCrumb.length === 0">Tìm theo danh mục</h4>
            <el-breadcrumb class="breadcrumb" separator="/">
              <el-breadcrumb-item v-if="listBreadCrumb.length > 0">
                <a href="javascript:void(0)" @click="handleLoadListCategory(null)">{{ $t('label.category') }}</a>
              </el-breadcrumb-item>
              <el-breadcrumb-item
                v-for="item in listBreadCrumb"
                :key="item.id"
              >
                <a href="javascript:void(0)" @click="handleBreadCrumbClick(item)">{{ item.name }}</a>
              </el-breadcrumb-item>
            </el-breadcrumb>
            <!--<el-checkbox-group v-model="checkedCategory" :min="0" :max="1">-->
            <!--<el-checkbox-->
            <!--v-for="(cate, cateIndex) in listCategory"-->
            <!--v-show="(cateIndex < displayLimit) || expandCate"-->
            <!--:key="cate.id"-->
            <!--:label="cate.name"-->
            <!--/>-->
            <!--</el-checkbox-group>-->
            <p
              v-for="(cate, cateIndex) in listCategory"
              v-show="(cateIndex < displayLimit) || expandCate"
              :key="cate.id"
              style="margin-top: 5px; margin-bottom: 5px"
              @click="selectCategory(cate)"
            > <a>{{ cate.name }}</a> </p>
            <el-button type="default" @click="expandCate = !expandCate"> {{ expandCate?'Thu Gọn':'Thêm' }}
              <i v-if="!expandCate" class="el-icon-arrow-down" />
              <i v-else class="el-icon-arrow-up" />
            </el-button>
          </div>
          <div class="search-group">
            <h4>Tìm theo địa điểm</h4>
            <el-checkbox-group v-model="checkedLocation">
              <el-checkbox
                v-for="(loc, locIndex) in listLocation"
                v-show="(locIndex < displayLimit) || expandLoc"
                :key="loc.id"
                :label="loc.name"
              />
            </el-checkbox-group>
            <el-button type="default" @click="expandLoc = !expandLoc"> {{ expandLoc?'Thu Gọn':'Thêm' }} <i class="el-icon-arrow-down" /></el-button>
          </div>
          <div
            v-for="meta in metadataTemplate"
            :key="meta.slug"
            class="search-group"
          >
            <h4> {{ meta.label }}</h4>
            <el-checkbox-group v-model="checkList">
              <el-checkbox
                v-for="metaOption in meta.options"
                :key="metaOption.value"
                :label="metaOption.label"
              />
            </el-checkbox-group>
            <el-button type="default">Thêm <i class="el-icon-arrow-down" /></el-button>
          </div>
          <div class="search-group">
            <h4>Tìm theo giá</h4>
            <el-row style="padding-bottom: 15px">
              <el-col :md="10" :lg="10">
                <money
                  v-model="searchQuery.minPrice"
                  v-bind="money"
                />
              </el-col>
              <el-col :md="4" :lg="4" class="text-center" style="margin-top: 10px">
                -
              </el-col>
              <el-col :md="10" :lg="10">
                <money
                  v-model="searchQuery.maxPrice"
                  v-bind="money"
                />
              </el-col>
            </el-row>
          </div>
          <div class="button-group">
            <el-button type="warning" size="medium">Đặt lại</el-button>
            <el-button type="primary" size="medium">Tìm kiếm</el-button>
          </div>
        </el-col>
        <el-col class="right" :sm="18" :md="19" :lg="19">
          <el-row>
            <el-col :md="16" :lg="16">
              <h4>20 result for keyword "adidas"</h4>
            </el-col>
            <el-col :md="8" :lg="8" style="text-align: right">
              <el-dropdown split-button type="default" style="text-align: center;">
                Sort
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>Action 1</el-dropdown-item>
                  <el-dropdown-item>Action 2</el-dropdown-item>
                  <el-dropdown-item>Action 3</el-dropdown-item>
                  <el-dropdown-item>Action 4</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-col>
          </el-row>

          <section class="content">
            <div v-for="adsItem in searchResult.content" :key="adsItem.id">
              <search-item ads="adsItem" />
            </div>
          </section>

          <pagination
            v-show="searchResult.totalRecord > 0"
            :total="searchResult.totalRecord"
            :page.sync="searchResult.pageNumber - 1"
            :limit.sync="searchResult.pageSize"
            @pagination="handleSearch"
          />
        </el-col>
      </el-row>
    </main>
  </div>
</template>

<script>
import { Money } from 'v-money'
import { SearchItem } from '@/components/Advertising'
import Pagination from '@/components/Pagination'
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'SearchPage',
  components: {
    SearchItem,
    Money,
    Pagination
  },
  data() {
    return {
      searchQuery: {
        searchKey: 'mio',
        categoryId: '5cd2ee51e3e36a044830e7e7',
        pageNumber: '1',
        pageSize: '10',
        sortKey: 'createdDate',
        ascSort: false,
        minPrice: '0',
        maxPrice: '1000000000000',
        mau_sac: '#484556,#abcxyz'
        // metadata: [
        //   {
        //     label: 'hihi',
        //     slug: 'color',
        //     selectionType: 'single',
        //     type: 'color',
        //     value: 'value'
        //   },
        //   {
        //     label: 'huhu',
        //     slug: 'kolor',
        //     selectionType: 'single',
        //     type: 'text',
        //     value: 'zalue'
        //   }
        // ]
      },
      listBreadCrumb: [],
      listMetadata: [],
      metadataTemplate: [],
      checkedCategory: {},
      checkedLocation: [],
      displayLimit: 7,
      expandCate: false,
      expandLoc: false,
      checkList: ['Option A'],
      money: {
        decimal: '.',
        thousands: ',',
        prefix: '',
        suffix: '',
        precision: 0,
        masked: false
      }
    }
  },
  computed: {
    ...mapGetters('category', ['listCategory', 'metadata']),
    ...mapGetters('location', ['listLocation', 'listDistrictByProvinceId', 'listWardByDistrictId']),
    ...mapGetters('advertising', ['searchResult'])
  },
  watch: {
    listCategory: function(newVal) {
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
    },
    metadata: function(newVal) {
      console.log('before', this.listMetadata)
      this.listMetadata.push(newVal)
      console.log('after', this.listMetadata)
    }
  },
  created() {
    this.handleLoadListCategory()
    this.handleLoadListLocation()
    this.fullTextSearch(this.searchQuery)
  },
  methods: {
    ...mapActions('category', ['getListCategory', 'getMetadataByCategoryId']),
    ...mapActions('location', ['loadListLocation']),
    ...mapActions('advertising', ['fullTextSearch']),
    handleSearch() {
    },
    handleLoadListCategory(parentId) {
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
    handleLoadListLocation() {
      this.loadListLocation({ searchKey: '' }).then(() => {
      })
    },
    selectCategory(item) {
      this.getMetadataByCategoryId(item.id).then(() => {
        this.listBreadCrumb.push(item)
        this.handleLoadListCategory(item.id)
      })
    },
    handleBreadCrumbClick(item) {
      const index = this.listBreadCrumb.findIndex(element => {
        return element.id === item.id
      })
      this.listBreadCrumb = Object.assign([], this.listBreadCrumb.slice(0, index + 1))
      this.listMetadata = Object.assign([], this.listMetadata.slice(0, index + 1))
      this.handleLoadListCategory(item.id)
    }
  }
}
</script>

<style lang="scss" scoped>
  .search-container {
    margin: 10px 15px;
    padding: 0 !important;
    background-color: #fff;
    box-shadow: 0 3px 6px rgba(0,0,0,0.10), 0 3px 6px rgba(0,0,0,0.10);
    main {
      padding: 30px;
      h3 {
        font-weight: 600;
        margin-bottom: 10px;
      }
      .left {
        .search-group {
          border-bottom: 1px solid rgba(0,0,0,.09);
          margin: 10px 0;
          padding: 5px 0 5px 23px;
          h4 {
            margin-bottom: 5px;
          }
          a {
            padding-left: 25px;
            color: #606266;
            &:hover {
              color: #409EFF;
            }
          }
          /deep/ .el-button--default {
            border: none;
            background: none;
            span {
              padding-left: 5px;
            }
          }
        }
        .button-group {
          text-align: center;
          .el-button {
            margin-bottom: 5px;
            margin-left: 0;
          }
        }
        /deep/ .el-checkbox-group {
          .el-checkbox {
            display: flex;
            padding: 5px 0;
          }
        }
      }

      .right {
        padding-left: 3rem;

        h4 {
          margin-top: 15px;
          padding-left: 1em;
          font-weight: 600;
        }

        .content {
          margin: 15px auto;
        }
      }
    }
    .breadcrumb {
      a {
        padding-left: 0 !important;
      }
    }
  }
</style>
