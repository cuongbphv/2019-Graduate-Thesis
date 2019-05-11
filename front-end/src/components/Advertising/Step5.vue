<template>
  <div class="step5">
    <h3>Xem lại tin đăng</h3>

    <el-row class="center-padding-top">
      <el-button type="primary" @click="changeStep(step - 1)">Prev</el-button>
      <el-button type="success" @click="changeStep(step + 1)">Continue</el-button>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Step5',
  components: {}
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
  }
</style>
