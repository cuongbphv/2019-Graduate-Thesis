<template>
  <div class="step4">
    <h3>{{ $t('advertising.additional_info') }}</h3>
    <el-radio-group v-model="advertisingType" size="small">
      <el-radio :label="1" border>{{ $t('advertising.sell') }}</el-radio>
      <el-radio :label="2" border>{{ $t('advertising.buy') }}</el-radio>
    </el-radio-group>

    <el-form ref="additionalInfo" :model="localInfo" :rules="advertisingRules" label-position="left" label-width="100px">
      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.title')" prop="title">
            <el-input
              v-model="localInfo.title"
              name="title"
              :placeholder="$t('advertising.title_placeholder')"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('advertising.description')" prop="description">
            <el-input
              v-model="localInfo.description"
              name="description"
              type="textarea"
              :rows="8"
              maxlength="2000"
              show-word-limit
              :placeholder="$t('advertising.description_placeholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="advertisingType === 1">
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.price')">
            <money
              v-model="localInfo.price"
              v-bind="money"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-else>
        <el-form-item :label="$t('advertising.price')">
          <el-col :md="4" :lg="4">
            <money
              v-model="localInfo.price"
              v-bind="money"
            />
          </el-col>
          <el-col :md="16" :lg="16">
            <el-slider
              v-model="localInfo.rangePrice"
              range
            />
          </el-col>
          <el-col :md="4" :lg="4">
            <money
              v-model="localInfo.maxPrice"
              v-bind="money"
            />
          </el-col>
        </el-form-item>
      </el-row>
    </el-form>

    <el-row class="center-padding-top">
      <el-button type="primary" @click="changeStep('back')">{{ $t('button.previous') }}</el-button>
      <el-button type="success" @click="changeStep('next')">{{ $t('button.continue') }}</el-button>
    </el-row>

  </div>
</template>

<script>
import { Money } from 'v-money'
export default {
  name: 'Step4',
  components: {
    Money
  },
  props: {
    info: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      advertisingType: 1,
      localInfo: {
        description: '',
        adsType: 1,
        price: 1000,
        rangePrice: [5, 50],
        title: ''
      },
      money: {
        decimal: '.',
        thousands: ',',
        prefix: '',
        suffix: '',
        precision: 0,
        masked: false
      },
      advertisingRules: {
        title: [{ required: true, message: this.$t('validator.required'), trigger: 'blur' }],
        description: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
          if (value.length < 20) {
            callback(new Error(this.$t('validator.description.max_length', { 'length': 20 })))
          }
          callback()
        } }]
      }
    }
  },
  watch: {
    'localInfo.rangePrice': function(newVal) {
      this.localInfo.rangePrice = newVal
      if (this.localInfo.adsType === 2) {
        this.localInfo.price = this.localInfo.rangePrice[0] * 100000
        this.localInfo.maxPrice = this.localInfo.rangePrice[1] * 100000
      }
    },
    advertisingType: function(newVal) {
      this.localInfo.adsType = newVal
    }
  },
  created() {
    if (Object.keys(this.info).length > 0) {
      this.localInfo = Object.assign({}, this.info)
    }
    if (this.localInfo.adsType === 2) {
      this.advertisingType = this.localInfo.adsType
      this.localInfo.price = this.localInfo.rangePrice[0] * 100000
      this.localInfo.maxPrice = this.localInfo.rangePrice[1] * 100000
    }
  },
  methods: {
    changeStep(action) {
      if (action === 'next') {
        this.$refs.additionalInfo.validate((valid) => {
          if (valid) {
            this.$emit('submitFormAdditionalInfo', this.localInfo)
            this.$emit('changeStep', action)
          } else {
            if (action === 'next') {
              this.$message({
                message: 'Please fill in additional info for classified advertising.',
                type: 'error'
              })
            }
          }
        })
      } else {
        this.$emit('changeStep', action)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.step4 {
  margin: 0 20px;
  text-align: center;

  h3 {
    text-align: center;
    font-weight: bold;
  }

  /deep/ .el-form {
    margin: 20px 0;
    padding: 0 40px;

    .el-slider {
      padding: 0 30px;
    }

    /*.el-form-item__content {*/
    /*  margin-left: 0 !important;*/
    /*}*/
  }
}
</style>
