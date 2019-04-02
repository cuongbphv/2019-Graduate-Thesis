<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <contact-bar />
    <nav-bar />
    <about />
    <div class="main-container">
      <category />
      <h3>Tìm kiếm hàng đầu</h3>
      <home-post />
      <h3>Tin mới nhất</h3>
      <home-post />
      <home-footer />
    </div>
  </div>
</template>

<script>
import { About, Category, ContactBar, HomeFooter, HomePost, NavBar } from '../../components/Layout/Home/index'
import ResizeMixin from '../../mixins/ResizeHandler'
export default {
  name: 'HomeLayout',
  components: {
    About,
    Category,
    ContactBar,
    HomeFooter,
    HomePost,
    NavBar
  },
  mixins: [ResizeMixin],
  computed: {
    sidebar() {
      return this.$store.state.app.sidebar
    },
    device() {
      return this.$store.state.app.device
    },
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('closeSideBar', { withoutAnimation: false })
    }
  }
}
</script>

<style scoped>
  @import "../../styles/mixin.scss";
  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;
    &.mobile.openSidebar{
      position: fixed;
      top: 0;
    }
  }
  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }
</style>
