<template>
  <div class="step5">
    <h3>Xem lại tin đăng</h3>

    <el-form :model="localData" label-position="left" label-width="120px">
      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.image')">
            <el-upload
              class="upload-form"
              drag
              action=""
              :file-list="localData.images"
              list-type="picture-card"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.title')">
            <el-input
              v-model="localData.additionalInfo.title"
              :placeholder="$t('validator.missing_data')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.province')">
            <el-input
              v-model="localData.location.province.name"
              name="title"
              :placeholder="$t('validator.missing_data')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.district')" prop="title">
            <el-input
              v-model="localData.location.district.name"
              :placeholder="$t('validator.missing_data')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.ward')" prop="title">
            <el-input
              v-model="localData.location.ward.name"
              :placeholder="$t('validator.missing_data')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.ads_type')" prop="title">
            <el-input
              :value="adsType"
              :placeholder="$t('validator.missing_data')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="localData.additionalInfo.adsType === 1">
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.price')">
            <money
              v-model="localData.additionalInfo.price"
              v-bind="money"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

      <div v-else>
        <el-row>
          <el-col :md="24" :lg="24">
            <el-form-item :label="$t('advertising.min_price')">
              <money
                v-model="localData.additionalInfo.price"
                v-bind="money"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :md="24" :lg="24">
            <el-form-item :label="$t('advertising.max_price')">
              <money
                v-model="localData.additionalInfo.maxPrice"
                v-bind="money"
                disabled
              />
            </el-form-item>
          </el-col>
        </el-row>
      </div>

      <!--      <el-row>-->
      <!--        <el-col :md="24" :lg="24">-->
      <!--          <el-form-item :label="$t('advertising.title')" prop="title">-->
      <!--            <el-input-->
      <!--              v-model="localInfo.title"-->
      <!--              name="title"-->
      <!--              :placeholder="$t('advertising.title_placeholder')"-->
      <!--              maxlength="50"-->
      <!--              show-word-limit-->
      <!--            />-->
      <!--          </el-form-item>-->
      <!--        </el-col>-->
      <!--      </el-row>-->

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('advertising.description')">
            <el-input
              v-model="localData.additionalInfo.description"
              type="textarea"
              :rows="8"
              :placeholder="$t('validator.missing_data')"
              disabled
            />
          </el-form-item>
        </el-col>
      </el-row>

    </el-form>

    <el-row class="center-padding-top">
      <el-button type="primary" @click="changeStep('back')">{{ $t('button.previous') }}</el-button>
      <el-button type="success" @click="changeStep('next')">{{ $t('button.post') }}</el-button>
    </el-row>
  </div>
</template>

<script>
import { Money } from 'v-money'
export default {
  name: 'Step5',
  components: {
    Money
  },
  props: {
    previewData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      localData: {
        category: {},
        location: {
          province: {},
          district: {},
          ward: {}
        },
        images: [],
        additionalInfo: {},
        author: {}
      },
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
    adsType() {
      if (this.localData.additionalInfo.adsType === 2) return this.$t('advertising.buy')
      else if (this.localData.additionalInfo.adsType === 1) return this.$t('advertising.sell')
      return false
    }
  },
  created() {
    if (Object.keys(this.previewData).length > 0) {
      this.localData = Object.assign({}, this.previewData)
      console.info(this.localData)
    }
  },
  methods: {
    changeStep(action) {
      if (action === 'next') {
        this.$emit('submitNewAdvertising', this.localData)
      }
      this.$emit('changeStep', action)
    }
  }
}
</script>

<style lang="scss" scoped>
  .step5 {
    margin: 0 20px;
    text-align: center;

    h3 {
      text-align: center;
      font-weight: bold;
    }

    .upload-form {
      text-align: left;
      /deep/ .el-upload {
        display: none;

        .el-upload-list__item-actions {
          display: none;
        }
      }
    }
  }
</style>
