<template>
  <search-page :category-id="$route.params.id" />
</template>
<script>
import SearchPage from '../search/SearchPage'
export default {
  name: 'Category',
  components: {
    SearchPage
  }
}
</script>
<style lang="scss" scoped>
</style>
