<template>
  <div class="step6">
    <div class="el-alert--success">
      <h3><b>{{ $t('advertising.created_success') }}</b></h3>
      <h4>{{ $t('advertising.goto') }} <router-link :to="'/advertising/' + classifiedAdsId">{{ $t('advertising.post') }}</router-link> {{ $t('advertising.now') }}</h4>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Step6',
  components: {
  },
  props: {
    classifiedAdsId: {
      type: String,
      default: '1'
    }
  }
}
</script>

<style lang="scss" scoped>
  .step6 {
    text-align: center;
    height: 300px;
    font-size: 18px;

    h3 {
      text-align: center;
      font-weight: bold;
    }

    .el-alert--success {
      margin-top: 10px;
      padding: 100px 20px;
      border-radius: 10px;
      background-color: #F0F9EB;
      color: #B0DF98;
      a {
        color: #409EFF;
      }
    }
  }
</style>
