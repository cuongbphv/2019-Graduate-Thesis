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
        <step1 v-if="step === 1" />
        <step2 v-if="step === 2" :location="newAdvertising.location" @submitFormLocation="saveLocationData" />
        <step3 v-if="step === 3" :images="newAdvertising.images" @submitFormImage="saveImage" />
        <step4 v-if="step === 4" />
        <step5 v-if="step === 5" />
      </transition>
    </div>

  </div>
</template>

<script>
import { Step1, Step2, Step3, Step4, Step5 } from '@/components/Advertising/index'
export default {
  name: 'CreateAdvertising',
  components: {
    Step1,
    Step2,
    Step3,
    Step4,
    Step5
  },
  data() {
    return {
      step: 1,
      newAdvertising: {
        category: {},
        location: {},
        images: [],
        description: {},
        author: {}
      },
      interval: null,
      isMoving: false,
      keepData: false
    }
  },
  created() {
  },
  methods: {
    changeStep(step) {
      this.step = step
      this.backToTop()
    },
    saveLocationData(location) {
      this.newAdvertising.location = Object.assign({}, location)
      this.step++
    },
    saveImage(imageList) {
      this.newAdvertising.images = imageList
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
    }
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

  .step-content {
    padding: 30px;
  }
}
</style>
