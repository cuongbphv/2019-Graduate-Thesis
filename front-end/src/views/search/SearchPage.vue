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
                <a href="javascript:void(0)" @click="handleLoadListCategory(categoryId || null, 'root')">{{ $t('label.category') }}</a>
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
            <el-button v-show="(displayLimit < listCategory.length)" type="default" @click="expandCate = !expandCate"> {{ expandCate?'Thu Gọn':'Thêm' }}
              <i v-if="!expandCate" class="el-icon-arrow-down" />
              <i v-else class="el-icon-arrow-up" />
            </el-button>
          </div>
          <div class="search-group">
            <h4>Tìm theo địa điểm</h4>
            <el-radio-group v-model="checkedProvince">
              <el-radio
                v-for="(loc, locIndex) in listLocation"
                v-show="(locIndex < displayLimit) || expandLoc"
                :key="loc.id"
                :label="loc.id"
              > <p>{{ loc.name }}</p>
                <el-radio-group v-if="checkedProvince === loc.id" v-model="checkedDistrict">
                  <el-radio
                    v-for="(dic, dicIndex) in loc.districts"
                    v-show="(dicIndex < displayLimit) || expandDist"
                    :key="dic.id"
                    :label="dic.id"
                  >
                    <p>{{ dic.nameWithType }}</p>
                    <el-radio-group v-if="checkedDistrict === dic.id" v-model="checkedWard">
                      <el-radio
                        v-for="(ward, wardIndex) in dic.wards"
                        v-show="(wardIndex < displayLimit) || expandWard"
                        :key="ward.id"
                        :label="ward.id"
                      >
                        <p>{{ ward.nameWithType }}</p>
                      </el-radio>
                    </el-radio-group>
                    <el-button v-if="checkedDistrict === dic.id" v-model="checkedWard" type="default" style="display: block" @click="expandWard = !expandWard"> {{ expandWard?'Thu Gọn':'Thêm' }}
                      <i v-if="!expandWard" class="el-icon-arrow-down" />
                      <i v-else class="el-icon-arrow-up" />
                    </el-button>
                  </el-radio>
                </el-radio-group>
                <el-button v-if="checkedProvince === loc.id" v-model="checkedDistrict" type="default" style="display: block" @click="expandDist = !expandDist"> {{ expandDist?'Thu Gọn':'Thêm' }}
                  <i v-if="!expandDist" class="el-icon-arrow-down" />
                  <i v-else class="el-icon-arrow-up" />
                </el-button>
              </el-radio>
            </el-radio-group>
            <el-button type="default" style="display: block" @click="expandLoc = !expandLoc"> {{ expandLoc?'Thu Gọn':'Thêm' }}
              <i v-if="!expandLoc" class="el-icon-arrow-down" />
              <i v-else class="el-icon-arrow-up" />
            </el-button>
          </div>
          <div
            v-for="(meta, index) in metadataTemplate"
            :key="meta.slug"
            class="search-group"
          >
            <h4> {{ meta.label }}</h4>
            <el-checkbox-group v-model="postMetadata[index].value">
              <el-checkbox
                v-for="metaOption in meta.options"
                :key="metaOption.label"
                :label="metaOption.label"
              > {{ metaOption.label }} </el-checkbox>
            </el-checkbox-group>
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
            <el-button type="warning" size="medium" @click="resetFilter">Đặt lại</el-button>
            <el-button type="primary" size="medium" @click="applyFilter">Áp dụng</el-button>
          </div>
        </el-col>
        <el-col class="right" :sm="18" :md="19" :lg="19">
          <el-row>
            <el-col :md="16" :lg="16">
              <h4 v-if="searchQuery.searchKey !== ''"> {{ searchResult.totalRecord }} kết quả cho từ khóa {{ searchQuery.searchKey }}</h4>
              <h4 v-else />
            </el-col>
            <el-col :md="8" :lg="8" style="text-align: right">
              <!--<el-dropdown split-button type="default" style="text-align: center;" @change="sortChange">-->
              <!--Sort-->
              <!--<el-dropdown-menu slot="dropdown">-->
              <!--<el-dropdown-item command="createdDate">By Date</el-dropdown-item>-->
              <!--<el-dropdown-item command="_score">By Matching Percent</el-dropdown-item>-->
              <!--</el-dropdown-menu>-->
              <!--</el-dropdown>-->
              <span>Sắp xếp:  </span>
              <el-select v-model="searchQuery.sortKey" placeholder="Select sort type" @change="sortChange">
                <el-option value="createdDate" label="Ngày đăng">Ngày đăng</el-option>
                <el-option value="author.ratingAverage" label="Đánh giá">Đánh giá</el-option>
                <el-option value="priceIncrease" label="Giá tăng dần">Giá tăng dần</el-option>
                <el-option value="priceDecrease" label="Giá giảm dần">Giá giảm dần</el-option>
                <el-option value="_score" label="Độ chính xác">Độ chính xác</el-option>
              </el-select>
            </el-col>
          </el-row>

          <section class="content">
            <div v-if="pushPost">
              <search-item :ads="pushPost" :hot="true" />
            </div>
            <div v-for="adsItem in searchResult.content" :key="adsItem.id">
              <search-item v-if="adsItem.id !== pushPost.id" :ads="adsItem" />
            </div>
          </section>

          <pagination
            v-show="searchResult.totalRecord > 0"
            :total="searchResult.totalRecord"
            :page="searchResult.pageNumber"
            :limit.sync="searchResult.pageSize"
            @pagination="handlePageChange"
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
  props: {
    categoryId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      searchQuery: {
        searchKey: this.$route.query.keyword || '',
        categoryId: '',
        pageNumber: 1,
        pageSize: 10,
        sortKey: 'createdDate',
        ascSort: false,
        minPrice: '0',
        maxPrice: '1000000000'
      },
      listBreadCrumb: [],
      listMetadata: [],
      metadataTemplate: [],
      postMetadata: [],
      checkedProvince: '',
      checkedDistrict: '',
      checkedWard: '',
      displayLimit: 7,
      metadataLimit: 4,
      showWard: false,
      showDist: false,
      expandCate: false,
      expandLoc: false,
      expandDist: false,
      expandWard: false,
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
    ...mapGetters('category', ['listCategory', 'metadata', 'metadatas']),
    ...mapGetters('location', ['listLocation', 'listDistrictByProvinceId', 'listWardByDistrictId']),
    ...mapGetters('advertising', ['searchResult', 'searchState', 'pushPost'])
  },
  watch: {
    listCategory: function(newVal) {
      // let allMeta = []
      // for (const key in this.listMetadata) {
      //   if (this.listMetadata.hasOwnProperty(key)) {
      //     allMeta = [...allMeta, ...this.listMetadata[key]]
      //   }
      // }

      // this.metadataTemplate = allMeta
      this.metadataTemplate = this.listMetadata

      this.postMetadata = this.listMetadata.map(obj => {
        const x = Object.assign({}, obj)
        x.value = []
        delete x.options
        return x
      })
    },
    metadata: function(newVal) {
      this.listMetadata.push(newVal)
    },
    metadatas: function(newVal) {
      this.listMetadata = Object.assign([], newVal)
    },
    checkedDistrict: function(newVal) {
      this.checkedWard = ''
      this.expandWard = false
    },
    checkedProvince: function(newVal) {
      this.checkedDistrict = ''
      this.expandDist = false
    },
    categoryId: function(newVal) {
      this.searchQuery.categoryId = newVal
    }
  },
  mounted() {
    if (this.categoryId) {
      this.getMetadatasByCategoryId(this.categoryId).then(() => {
        this.handleLoadListCategory(this.categoryId)
      })
    } else {
      if (this.listMetadata.length === 0) {
        this.handleLoadListCategory()
      }
    }
    this.handleLoadListLocation()
  },
  methods: {
    ...mapActions('category', ['getListCategory', 'getMetadataByCategoryId', 'getMetadatasByCategoryId']),
    ...mapActions('location', ['loadListLocation']),
    ...mapActions('advertising', ['fullTextSearch', 'saveSearchState', 'getPushPost']),
    handlePageChange(pagination) {
      this.searchQuery.pageNumber = pagination.page
      this.searchQuery.pageSize = pagination.limit
      this.handleFullTextSearch()
    },
    handleLoadListCategory(parentId, pos) {
      if (parentId === null) {
        this.listBreadCrumb = []
        this.listMetadata = []
        this.searchQuery.categoryId = null
      }
      if (pos === 'root') {
        this.listBreadCrumb = []
      }
      this.getListCategory({
        searchKey: this.searchKey,
        parentId: parentId
      }).then(() => {
      })
      this.resetSearchQuery()
      this.handleFullTextSearch(parentId)
    },
    handleLoadListLocation() {
      this.loadListLocation({ searchKey: '' }).then(() => {
      })
    },
    handleFullTextSearch(catId) {
      this.searchQuery.categoryId = catId || this.searchQuery.categoryId
      this.saveState()
      this.fullTextSearch(this.searchQuery)
      this.getPushPost(this.searchQuery.categoryId)
    },
    selectCategory(item) {
      this.searchQuery.pageNumber = 1
      this.getMetadatasByCategoryId(item.id).then(() => {
        this.listBreadCrumb.push(item)
        this.handleLoadListCategory(item.id)
      })
    },
    handleBreadCrumbClick(item) {
      const index = this.listBreadCrumb.findIndex(element => {
        return element.id === item.id
      })
      this.listBreadCrumb = Object.assign([], this.listBreadCrumb.slice(0, index + 1))
      // this.listMetadata = Object.assign([], this.listMetadata.slice(0, index + 1))
      this.getMetadatasByCategoryId(item.id)
      this.handleLoadListCategory(item.id)
    },
    resetSearchQuery() {
      // reset metadata
      this.postMetadata.forEach(function(element) {
        element.value = []
      })
      const defaultSearchQuery =
        {
          searchKey: this.$route.query.keyword || '',
          categoryId: '',
          locationId: '',
          pageNumber: '1',
          pageSize: '10',
          sortKey: 'createdDate',
          ascSort: false,
          minPrice: '0',
          maxPrice: '1000000000'
        }

      for (const key of Object.keys(this.searchQuery)) {
        if (defaultSearchQuery.hasOwnProperty(key)) {
          defaultSearchQuery[key] = this.searchQuery[key]
        }
      }
      this.searchQuery = Object.assign({}, defaultSearchQuery)
      console.log('search query', this.searchQuery)
    },
    applyFilter() {
      const tempMeta = this.postMetadata.filter(function(element) {
        return element.value.length !== 0
      })
      tempMeta.forEach((element) => {
        this.searchQuery[element.slug.replace(/-/g, '_')] = element.value.join(',')
      })
      this.searchQuery['locationId'] = this.checkedWard || this.checkedDistrict || this.checkedProvince || ''
      console.log('stt', this.searchState)
      this.handleFullTextSearch()
    },
    resetFilter() {
      // reset location
      this.checkedProvince = ''
      this.checkedDistrict = ''
      this.checkedWard = ''
      this.searchQuery.locationId = ''
      this.resetSearchQuery()
      this.applyFilter()
    },
    sortChange() {
      this.searchQuery.ascSort = this.searchQuery.sortKey === 'priceIncrease'
      this.handleFullTextSearch()
    },
    saveState() {
      const state = {}
      state.searchQuery = this.searchQuery
      state.listBreadCrumb = this.listBreadCrumb
      state.listMetadata = this.listMetadata
      state.metadataTemplate = this.metadataTemplate
      state.postMetadata = this.postMetadata
      state.checkedProvince = this.checkedProvince
      state.checkedDistrict = this.checkedDistrict
      state.checkedWard = this.checkedWard
      this.saveSearchState(state)
    },
    loadState() {
      if (Object.keys(this.searchState).length !== 0) {
        this.searchQuery = this.searchState.searchQuery
        this.listBreadCrumb = this.searchState.listBreadCrumb
        this.listMetadata = this.searchState.listMetadata
        this.metadataTemplate = this.searchState.metadataTemplate
        this.postMetadata = this.searchState.postMetadata
        this.checkedProvince = this.searchState.checkedProvince
        this.checkedDistrict = this.searchState.checkedDistrict
        this.checkedWard = this.searchState.checkedWard
      }
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
        /deep/ .el-radio-group {
          .el-radio {
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
