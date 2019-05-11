<template>
  <div class="step1">
    <h1>Lựa chọn danh mục tin đăng</h1>

    <el-row class="center-padding-top">
      <el-button type="success" @click="changeStep(step + 1)">Continue</el-button>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Step1'
}
</script>

<style lang="scss" scoped>
.step2 {
  margin: 0 20px;
  text-align: center;

  h3 {
    text-align: center;
    font-weight: bold;
  }
}
</style>
