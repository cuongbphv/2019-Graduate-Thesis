<template>
  <div class="step4">
    <h3>{{ $t('advertising.additional_info') }}</h3>
    <el-radio-group v-model="advertisingType" size="small">
      <el-radio label="sell" border>{{ $t('advertising.sell') }}</el-radio>
      <el-radio label="buy" border>{{ $t('advertising.buy') }}</el-radio>
    </el-radio-group>

    <el-form :model="localInfo" label-position="left" label-width="150px">
      <el-row>
        <el-col :md="24" :lg="24">
          <el-form-item :label="$t('advertising.title')">
            <el-input
              v-model="localInfo.title"
              :placeholder="$t('advertising.title_placeholder')"
              maxlength="50"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('advertising.description')">
            <el-input
              v-model="localInfo.description"
              type="textarea"
              :rows="8"
              maxlength="2000"
              show-word-limit
              :placeholder="$t('advertising.description_placeholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row v-if="advertisingType === 'sell'">
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

  </div>
</template>

<script>
import { Money } from 'v-money'
export default {
  name: 'Step4',
  components: {
    Money
  },
  prop: {
    info: {
      type: Object,
      default: () => {
        return {
          description: '',
          adsType: 'sell',
          price: 1000,
          rangePrice: 0,
          title: ''
        }
      }
    }
  },
  data() {
    return {
      advertisingType: 'sell',
      localInfo: {},
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
  created() {
    this.localInfo = Object.assign({}, this.info)
  },
  methods: {
    handleChange(value) {
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
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
