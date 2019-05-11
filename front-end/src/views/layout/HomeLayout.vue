<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device === 'mobile' && sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <div class="sticky-nav">
      <contact-bar />
      <nav-bar />
    </div>
    <app-main />
    <home-footer />

    <el-tooltip placement="top" :content="$t('label.backToTop')">
      <back-to-top
        :visibility-height="300"
        :back-position="0"
        transition-name="fade"
      />
    </el-tooltip>
  </div>
</template>

<script>
import { ContactBar, NavBar, HomeFooter } from '@/components/Layout/Home/index'
import AppMain from '@/components/Layout/AppMain/AppMain'
import ResizeMixin from '@/mixins/ResizeHandler'
import BackToTop from '@/components/BackToTop'
import { mapGetters, mapActions } from 'vuex'
export default {
  name: 'HomeLayout',
  components: {
    ContactBar,
    NavBar,
    AppMain,
    HomeFooter,
    BackToTop
  },
  mixins: [ResizeMixin],
  computed: {
    ...mapGetters('layout', ['sidebar', 'device']),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    }
  },
  created() {
    if (this.$route.query.token) {
      console.log(this.$route.query.token)
    }
  },
  methods: {
    ...mapActions('layout', ['closeSideBar']),
    handleClickOutside() {
      this.closeSideBar({ withoutAnimation: false })
    }
  }
}
</script>

<style scoped>
  @import "../../styles/mixin.scss";
  .sticky-nav {
    position: sticky;
    top: 0;
    z-index: 5;
  }
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
