<template>
  <div class="home-container">
    <about />
    <div class="main-container">
      <category />
      <h3>Tìm kiếm hàng đầu</h3>
      <home-post />
      <h3>Tin mới nhất</h3>
      <home-post />
    </div>
  </div>
</template>

<script>
import { About, Category, HomePost } from '@/components/Layout/Home/index'
export default {
  name: 'Home',
  components: {
    About,
    Category,
    HomePost
  }
}
</script>

<style scoped>

</style>
