<template>
  <div class="create-advertising">
    <el-steps :active="1" simple>
      <el-step title="Location" icon="el-icon-edit" />
      <el-step title="Category" icon="el-icon-upload" />
      <el-step title="Description" icon="el-icon-upload" />
      <el-step title="Image" icon="el-icon-picture" />
      <el-step title="Preview" icon="el-icon-picture" />
      <el-step title="Submit" icon="el-icon-picture" />
    </el-steps>
  </div>
</template>

<script>
export default {
  name: 'CreateAdvertising',
  created() {
    console.info('create ad page')
  }
}
</script>

<style lang="scss" scoped>

</style>
