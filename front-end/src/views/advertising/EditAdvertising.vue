<template>
  <div class="create-advertising">
    <div class="step-progress">
      <el-steps :active="step" simple>
        <el-step :title="$t('advertising.category')" icon="el-icon-menu" />
        <el-step :title="$t('advertising.location')" icon="el-icon-location" />
        <el-step :title="$t('advertising.image')" icon="el-icon-picture-outline" />
        <el-step :title="$t('advertising.info')" icon="el-icon-info" />
        <el-step :title="$t('advertising.preview')" icon="el-icon-edit-outline" />
        <el-step :title="$t('advertising.submit')" icon="el-icon-success" />
      </el-steps>
    </div>

    <div class="step-content">
      <transition name="fade-transform" mode="out-in">
        <step1
          v-if="step === 1"
          :category="editAdvertising.category"
          @changeStep="handleChangeStep"
          @submitFormCategory="saveCategory"
        />
        <step2
          v-else-if="step === 2"
          :location="editAdvertising.location"
          @changeStep="handleChangeStep"
          @submitFormLocation="saveLocation"
        />
        <step3
          v-else-if="step === 3"
          :images="editAdvertising.images"
          @changeStep="handleChangeStep"
          @submitFormImage="saveImage"
        />
        <step4
          v-else-if="step === 4"
          :info="editAdvertising.additionalInfo"
          @changeStep="handleChangeStep"
          @submitFormAdditionalInfo="saveAdditionalInfo"
        />
        <step5
          v-else-if="step === 5"
          :preview-data="editAdvertising"
          @changeStep="handleChangeStep"
          @submitNewAdvertising="saveEditAdvertising"
        />
        <step6
          v-else-if="step === 6"
          :classified-ads-id="classifiedAdsId"
        />
      </transition>
    </div>
  </div>
</template>

<script>
import { Step1, Step2, Step3, Step4, Step5, Step6 } from '@/components/Advertising/index'
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'EditAdvertising',
  components: {
    Step1,
    Step2,
    Step3,
    Step4,
    Step5,
    Step6
  },
  data() {
    return {
      adsId: this.$route.params.id,
      step: 0,
      editAdvertising: {
        category: {},
        location: {},
        images: [],
        additionalInfo: {}
      },
      interval: null,
      isMoving: false,
      classifiedAdsId: ''
    }
  },
  computed: {
    ...mapGetters('advertising', ['classifiedAds'])
  },
  watch: {
    classifiedAds: function(newVal) {
      this.editAdvertising.location = newVal.address
      this.editAdvertising.images = newVal.detail.images
      this.editAdvertising.additionalInfo = newVal.detail.additionalInfo
      this.editAdvertising.category.breadCrumb = newVal.breadcrumbs
      this.editAdvertising.category.postMetadata = newVal.detail.metadata
      this.step++
    }
  },
  mounted() {
    this.handleGetAds(this.adsId)
  },
  beforeDestroy() {
  },
  methods: {
    ...mapActions('advertising', ['addNewAdvertising', 'updateAdvertising', 'getClassifiedAdsDetail']),
    handleChangeStep(action) {
      if (action === 'back') {
        this.step--
      } else if (action === 'next') {
        this.step++
      }
      this.backToTop()
    },
    handleGetAds(id) {
      this.getClassifiedAdsDetail(id)
    },
    saveCategory(category) {
      this.editAdvertising.category = Object.assign({}, category)
    },
    saveLocation(location) {
      this.editAdvertising.location = Object.assign({}, location)
    },
    saveImage(imageList) {
      this.editAdvertising.images = imageList
    },
    saveAdditionalInfo(info) {
      this.editAdvertising.additionalInfo = Object.assign({}, info)
    },
    saveEditAdvertising() {
      this.editAdvertising.id = this.adsId
      this.updateAdvertising(this.editAdvertising).then((id) => {
        this.classifiedAdsId = id
        this.step++
        this.backToTop()
      })
    },
    backToTop() {
      const start = window.pageYOffset
      let i = 0
      this.isMoving = true
      this.interval = setInterval(() => {
        const next = Math.floor(this.easeInOutQuad(10 * i, start, -start, 500))
        if (next <= 0) {
          window.scrollTo(0, 0)
          clearInterval(this.interval)
          this.isMoving = false
        } else {
          window.scrollTo(0, next)
        }
        i++
      }, 16.7)
    },
    easeInOutQuad(t, b, c, d) {
      if ((t /= d / 2) < 1) return c / 2 * t * t + b
      return -c / 2 * (--t * (t - 2) - 1) + b
    }
  }
}
</script>

<style lang="scss" scoped>
.create-advertising {
  margin: 10px 15px !important;
  border: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);
  background: #FFF;

  .step-progress {
    /deep/ .el-steps--simple {
      border-bottom: 1px solid #d8dce5;
      box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);
      border-radius: 0 !important;
      padding: 1rem 2rem;

      .el-step__title {
        font-size: 15px;
        line-height: 25px;
      }

      .el-step__icon {
        z-index: 0;
      }
    }
  }

  .step-content {
    padding: 30px;
  }

  @media screen and (min-width: 991px) and (max-width: 1000px) {
    .step-progress {
      /deep/ .el-steps {
        .el-step.is-simple:not(:last-of-type) .el-step__title {
          max-width: 100% !important;
        }
      }
    }
  }

  @media screen and (min-width: 761px) and (max-width: 990px) {
    .step-progress {

      /deep/ .el-steps--simple {
        padding: 0.75rem 1rem;
      }

      /deep/ .el-steps {
        .el-step.is-simple:not(:last-of-type) .el-step__title {
          max-width: 100% !important;
        }
      }
    }
  }

  @media screen and (max-width: 760px) {
    .step-progress {
      display: none;
    }
  }

  @media screen and (max-width: 375px) {
    .step-content {
      padding: 20px 10px;
    }
  }
}
</style>
