<template>
  <div class="classified-ads-detail-container">
    <header>
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/home' }">{{ $t('navbar.home') }}</el-breadcrumb-item>
        <el-breadcrumb-item
          v-for="item in classifiedAds.breadcrumbs"
          :key="item.id"
          :to="{ path: '/category/' + item.id }"
        >{{ item.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </header>
    <main>
      <article>
        <el-row>
          <el-col class="carousel-left" :xs="0" :sm="8" :md="6">
            <div>
              <ul class="el-upload-list el-upload-list--picture-card">
                <li
                  v-for="(item, index) in images"
                  :key="item.uid"
                  class="el-upload-list__item side-image"
                  :class="{'is-active': index === imageIndex}"
                  @click="handleChangeCarousel(index)"
                >
                  <el-image :src="item.url" class="el-upload-list__item-thumbnail side-image">
                    <div slot="error" class="image-slot">
                      <i class="el-icon-picture-outline" />
                    </div>
                  </el-image>
                </li>
              </ul>
            </div>
          </el-col>

          <el-col class="carousel-right" :xs="24" :sm="16" :md="18">
            <el-carousel ref="carousel" :interval="5000" arrow="always" @change="handleChangeCarousel">
              <el-carousel-item v-for="item in images" :key="item.uid">
                <img :src="item.url" alt="No image">
              </el-carousel-item>
            </el-carousel>
          </el-col>
        </el-row>
        <div class="basic-info">
          <h2>{{ additionalInfo.title }}  <i v-if="author.userId === userId" style="cursor: pointer" class="el-icon-edit" @click="handleEditAds" /> </h2>
          <el-row>
            <div class="price">{{ additionalInfo.price | currency('VNĐ', 0, {symbolOnLeft: false, spaceBetweenAmountAndSymbol: true}) }}</div>
            <!--<span>-</span>-->
            <div v-if="additionalInfo.maxPrice" class="price">{{ additionalInfo.maxPrice | currency('VNĐ', 0, {symbolOnLeft: false, spaceBetweenAmountAndSymbol: true}) }}</div>
            <div class="action">
              <el-select v-if="author.userId === userId" v-model="tradingStatus" :disabled="tradingStatus === 0" clearable placeholder="Select" @change="handleTradingSelection">
                <el-option
                  v-for="item in tradingStatusOption"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
              <el-badge v-if="author.userId !== userId" :value="saves.length" :max="10" class="item">
                <el-button v-if="saves.includes(userId)" size="mini" type="success" @click="handleRemoveSave">{{ $t('button.remove_save_ads') }}</el-button>
                <el-button v-else size="mini" type="success" @click="handleSave">{{ $t('button.save_ads') }}</el-button>
              </el-badge>
              <el-badge v-if="author.userId !== userId" :value="reportModel.number" :max="10" class="item">
                <el-button size="mini" type="warning" @click="reportDialogShow = true">{{ $t('button.report') }}</el-button>
              </el-badge>
            </div>
          </el-row>
        </div>
        <el-divider />
        <div class="author-info">
          <el-row>
            <el-col :xs="6" :sm="4" :md="3" style="display: table; height: 100px; text-align: center;">
              <div style="display: table-cell; vertical-align: middle;">
                <img :src="author.avatarUrl" alt="No logo">
              </div>
            </el-col>
            <el-col :xs="18" :sm="20" :md="21">
              <a :href="'/profile/' + author.userId">
                <h3 v-if="i18n.locale === 'vi'">{{ author.lastName + ' ' + author.firstName }}</h3>
                <h3 v-if="i18n.locale === 'en'">{{ author.firstName + ' ' + author.lastName }}</h3>
              </a>
              <p>Ngày tham gia: {{ author.createdDate | formatDate(author.createdDate) }}</p>
              <div class="author-action">
                <el-row>
                  <el-button type="success" icon="el-icon-phone" circle />
                  <el-button type="info" icon="el-icon-message" circle @click="handleOpenChatModal" />
                  <el-button type="primary" icon="el-icon-bell" circle />
                  <el-button type="warning" icon="el-icon-warning" circle />
                </el-row>
                <chat-popup :recipient="author" :visible="chatPopupVisible" @closeChatModal="handleCloseChatModal" />
              </div>
            </el-col>
          </el-row>
        </div>
        <el-divider />
        <div class="description">
          <h3>{{ $t('advertising.desc_info') }}</h3>
          <div style="white-space: pre-line;">{{ additionalInfo.description }}</div>
        </div>
        <el-divider />
        <div class="metadata">
          <el-row>
            <h3>{{ $t('advertising.tech_info') }}</h3>
            <el-col v-for="item in metadata" :key="item.id" style="margin-bottom: 20px;" :md="12">
              <template v-if="item.value || item.valueLabel || item.enValueLabel">
                <span>{{ item.label }}</span> :
                <span style="font-weight: 600;">{{ item.valueLabel || item.enValueLabel }} </span>
                <div v-if="item.value && item.value.startsWith('#')" :style="{'background': item.value, 'width': '20px', 'height': '10px', 'display': 'inline-block'}" />
              </template>
            </el-col>
          </el-row>
        </div>
      </article>
      <aside>
        <div v-for="adsItem in topCategoryPost" :key="adsItem.id">
          <search-item v-if="adsItem.id !== classifiedAdsId" :ads="adsItem" :mode="'topPost'" />
        </div>
      </aside>
    </main>

    <el-dialog :visible.sync="reportDialogShow" :title="$t('label.report_modal')">
      <el-form label-width="130px" label-position="left">
        <el-form-item :label="$t('label.reason')">
          <el-select
            v-model="reportModel.reason"
            :placeholder="$t('place_holder.select_reason')"
            style="width: 100%"
          >
            <el-option
              v-for="item in reasons"
              :key="item.id"
              :label="$t(item.key)"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.description')">
          <el-input
            v-model="reportModel.description"
            type="textarea"
            :rows="3"
            :placeholder="$t('label.description')"
          />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="reportDialogShow = false">{{ $t('button.cancel') }}</el-button>
        <el-button type="primary" @click="createReport">{{ $t('button.report') }}</el-button>
      </div>
    </el-dialog>

    <!--    <footer>footer</footer>-->
  </div>
</template>

<script>
import { mapActions, mapState, mapGetters } from 'vuex'
import i18n from '@/lang'
import SearchItem from '@/components/Advertising/SearchItem'
import { Status } from '@/utils/constants'
import ChatPopup from '../chat/ChatPopup'
import { showSuccess } from '../../utils/message'

export default {
  name: 'ClassifiedAdsDetail',
  components: {
    SearchItem,
    ChatPopup
  },
  data() {
    return {
      classifiedAdsId: '',
      images: [],
      additionalInfo: {},
      author: {},
      address: {},
      metadata: [],
      i18n: i18n,
      imageIndex: 0,
      chatPopupVisible: false,
      searchQuery: {
        categoryId: '',
        pageNumber: 1,
        pageSize: 5,
        sortKey: 'createdDate',
        ascSort: false,
        minPrice: '0',
        maxPrice: '10000000000'
      },
      reportModel: {
        reason: 0,
        description: '',
        number: 0
      },
      reasons: [
        {
          id: 0,
          key: 'label.reasons.cheat'
        },
        {
          id: 1,
          key: 'label.reasons.duplicate'
        },
        {
          id: 2,
          key: 'label.reasons.sold_out'
        },
        {
          id: 3,
          key: 'label.reasons.not_contact'
        },
        {
          id: 4,
          key: 'label.reasons.not_true'
        },
        {
          id: 5,
          key: 'label.reasons.other'
        }
      ],
      tradingStatusOption: [
        {
          value: 0,
          label: 'Chờ xử lý'
        },
        {
          value: 1,
          label: 'Đang bán'
        }, {
          value: 2,
          label: 'Đã bán'
        }, {
          value: 3,
          label: 'Ẩn tin'
        }],
      tradingStatus: '',
      saves: [],
      reportDialogShow: false
    }
  },
  // metaInfo() {
  //   return {
  //     title: 'hello'
  //   }
  // },
  computed: {
    ...mapState('advertising', ['classifiedAds']),
    ...mapGetters('advertising', ['topCategoryPost']),
    ...mapGetters('profile', ['userId'])
  },
  mounted() {
    this.classifiedAdsId = this.$route.params.id
    this.getClassifiedAdsDetail(this.classifiedAdsId).then(() => {
      this.images = [...this.classifiedAds.detail.images]
      this.metadata = [...this.classifiedAds.detail.metadata]
      this.additionalInfo = Object.assign({}, this.classifiedAds.detail.additionalInfo)
      this.author = Object.assign({}, this.classifiedAds.author)
      this.address = Object.assign({}, this.classifiedAds.address)
      this.tradingStatus = this.classifiedAds.detail.tradeStatus
      this.saves = Object.assign([], this.classifiedAds.detail.saves)
      this.handleGetTopCategoryPost()
      this.getNumberOfReportAds()
      document.title = this.additionalInfo.title
    })
  },
  created() {
  },
  methods: {
    ...mapActions('advertising', ['getClassifiedAdsDetail', 'getTopCategoryPost',
      'changeTradingStatusAds', 'saveAds', 'removeSaveAds']),
    ...mapActions('report', ['createNewReport', 'getNumberOfReport']),
    handleChangeCarousel(index) {
      this.imageIndex = index
      this.$refs.carousel.activeIndex = index
    },
    handleGetTopCategoryPost() {
      this.searchQuery.categoryId = this.classifiedAds.breadcrumbs[0].id
      this.getTopCategoryPost(this.searchQuery).then(() => {
      })
    },
    handleCloseChatModal() {
      this.chatPopupVisible = false
    },
    handleOpenChatModal() {
      this.chatPopupVisible = true
    },
    handleTradingSelection() {
      this.changeTradingStatusAds({ id: this.classifiedAdsId, status: this.tradingStatus })
        .then(() => {
          showSuccess('message.change_trading_status_success')
        })
    },
    handleSave() {
      this.saveAds(this.classifiedAdsId).then((res) => {
        this.saves = Object.assign([], res.data)
        showSuccess('message.save_ads_success')
      })
    },
    handleRemoveSave() {
      this.removeSaveAds(this.classifiedAdsId).then((res) => {
        this.saves = Object.assign([], res.data)
        showSuccess('message.remove_save_ads_success')
      })
    },
    handleEditAds() {
      this.$router.push({ path: '/advertising/edit/' + this.classifiedAdsId })
    },
    createReport() {
      this.createNewReport({
        classifiedAdsId: this.classifiedAdsId,
        reason: this.reportModel.reason,
        description: this.reportModel.description
      }).then(res => {
        if (res.status === Status.SUCCESS) {
          this.$message({
            message: this.$t('message.report_success'),
            type: 'success'
          })
          this.getNumberOfReportAds()
        } else {
          this.$message({
            message: this.$t('errors.' + res.status),
            type: 'error'
          })
        }
        this.reportDialogShow = false
      })
    },
    getNumberOfReportAds() {
      this.getNumberOfReport(this.classifiedAdsId).then(res => {
        if (res.status === Status.SUCCESS) {
          this.reportModel.number = res.data
        } else {
          this.reportModel.number = 0
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.classified-ads-detail-container {
  margin: 10px 15px;
  padding: 10px 20px;
  background-color: #fff;
  box-shadow: 0 3px 6px rgba(0,0,0,0.10), 0 3px 6px rgba(0,0,0,0.10);
  main {
    min-height: 50rem;
    margin: 0;
    padding: 0;
    display: flex;
    flex-flow: row;
  }
  main > article {
    margin: 4px;
    padding: 5px;
    background: rgba(255, 255, 255, 0.9);
    flex: 3 1 60%;
    order: 2;
    .side-image{
      max-width:144px;
      max-height:144px;
      width: auto;
      height: auto;
    }
    .carousel-left {
      text-align: center;
      height: 400px;
      overflow-y: auto;

      .el-upload-list__item {
        border-radius: 0;
        border: none;
        cursor: pointer;
        &.is-active {
          border: 2px solid #42b1fa !important;
        }
      }
    }
    .carousel-right {
      /deep/ .el-carousel {
        height: 400px;
        .el-carousel__container {
          height: 100% !important;
        }
        .el-carousel__arrow {
          z-index: 3;
        }
        .el-carousel__item {
          text-align: center;
        }
      }
      img {
        max-width:800px;
        max-height:400px;
        width: auto;
        height: auto;
      }
    }

    .basic-info {
      margin-top: 20px;
      padding-left: 20px;
      h2 {
        font-size: 20px;
        font-weight: bold;
      }
      .price {
        display: inline-block;
        font-size: 18px;
        font-weight: bold;
        color: red;
        text-align: left;
      }
      span {
        color: red;
      }
      .action {
        display: inline;
        float: right;
        .el-badge {
          margin-right: 20px;
        }
      }
    }

    .author-info {
      img {
        width: 70px;
        height: 70px;
        border-radius: 50%;
      }
    }

    .description {
      padding-left: 20px;
    }

    .metadata {
      padding-left: 20px;
    }
  }

  /*main > nav {*/
  /*  margin: 0.25rem;*/
  /*  padding: 0.312rem;*/
  /*  border: 0.187rem solid rgba(0, 0, 0, 0.6);*/
  /*  border-radius: 0.625rem;*/
  /*  background: rgba(255, 255, 255, 0.9);*/
  /*  flex: 1 6 20%;*/
  /*  order: 1;*/
  /*}*/

  main > aside {
    margin: 0.25rem;
    padding: 0.312rem;
    background: rgba(255, 255, 255, 0.9);
    flex: 1 6 20%;
    order: 3;
  }

  header {
    display: block;
    margin: 0.25rem;
    padding: 0.312rem;
    min-height: 2rem;
    background: rgba(255, 255, 255, 0.9);
  }

  footer {
    display: block;
    margin: 0.25rem;
    padding: 0.312rem;
    min-height: 6.25rem;
    border: 0.187rem solid rgba(0, 0, 0, 0.6);
    border-radius: 0.625rem;
    background: rgba(255, 255, 255, 0.9);
  }

  @media all and (max-width: 640px) {
    main {
      flex-direction: column;
    }

    main > article,
    main > nav,
    main > aside {
      order: 0;
    }

    main > nav,
    main > aside,
    header,
    footer {
      min-height: 3.125rem;
      max-height: 3.125rem;
    }
  }

  @media all and (max-width: 767px) {
    .basic-info, .description, .metadata {
      padding-left: 0 !important;;
    }
  }
}
</style>
