<template>
  <section class="app-main">
    <transition v-if="!noTransition" name="fade-transform" mode="out-in">
      <keep-alive :include="cachedViews">
        <router-view :key="key" />
      </keep-alive>
    </transition>
    <keep-alive v-else :include="cachedViews">
      <router-view />
    </keep-alive>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.fullPath
    },
    noTransition() {
      const exception = ['ProfileDetail', 'ProfileHistory', 'ProfileInformation', 'AccountSetting', 'AddressManagement', 'Rating', 'Saved']
      return exception.includes(this.$route.name)
    }
  }
}
</script>

<style scoped>
.app-main {
  /*84 = navbar + tags-view = 50 +34 */
  /*min-height: calc(100vh - 84px);*/
  width: 100%;
  position: relative;
  overflow: hidden;
  background: #F5F5F5;
}
</style>

