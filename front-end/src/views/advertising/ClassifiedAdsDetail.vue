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
                  class="el-upload-list__item"
                  :class="{'is-active': index === imageIndex}"
                  @click="handleChangeCarousel(index)"
                >
                  <el-image :src="item.url" class="el-upload-list__item-thumbnail">
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
          <h2>{{ additionalInfo.title }}</h2>
          <el-row>
            <div class="price">{{ additionalInfo.price | currency('VNĐ', 0, {symbolOnLeft: false, spaceBetweenAmountAndSymbol: true}) }}</div>
            <span>-</span>
            <div v-if="additionalInfo.maxPrice" class="price">{{ additionalInfo.maxPrice | currency('VNĐ', 0, {symbolOnLeft: false, spaceBetweenAmountAndSymbol: true}) }}</div>
            <div class="action">
              <el-badge :value="200" :max="99" class="item">
                <el-button size="mini" type="success">Save</el-button>
              </el-badge>
              <el-badge :value="100" :max="10" class="item">
                <el-button size="mini" type="warning">Report</el-button>
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
                  <el-button type="info" icon="el-icon-message" circle />
                  <el-button type="primary" icon="el-icon-bell" circle />
                  <el-button type="warning" icon="el-icon-warning" circle />
                </el-row>
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
                <div v-if="item.value.startsWith('#')" :style="{'background': item.value, 'width': '20px', 'height': '10px', 'display': 'inline-block'}" />
              </template>
            </el-col>
          </el-row>
        </div>
      </article>
      <aside>aside</aside>
    </main>
    <!--    <footer>footer</footer>-->
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import i18n from '@/lang'

export default {
  name: 'ClassifiedAdsDetail',
  data() {
    return {
      classifiedAdsId: '',
      images: [],
      additionalInfo: {},
      author: {},
      address: {},
      metadata: [],
      i18n: i18n,
      imageIndex: 0
    }
  },
  computed: {
    ...mapState('advertising', ['classifiedAds'])
  },
  mounted() {
    this.classifiedAdsId = this.$route.params.id
    this.getClassifiedAdsDetail(this.classifiedAdsId).then(() => {
      this.images = [...this.classifiedAds.detail.images]
      this.metadata = [...this.classifiedAds.detail.metadata]
      this.additionalInfo = Object.assign({}, this.classifiedAds.detail.additionalInfo)
      this.author = Object.assign({}, this.classifiedAds.author)
      this.address = Object.assign({}, this.classifiedAds.address)
    })
  },
  created() {
  },
  methods: {
    ...mapActions('advertising', ['getClassifiedAdsDetail']),
    handleChangeCarousel(index) {
      this.imageIndex = index
      this.$refs.carousel.activeIndex = index
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
      }
      img {
        width: 100%;
        height: 400px;
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
