<template>
  <div :class="classObj" class="app-wrapper">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside" />
    <side-bar class="sidebar-container" />
    <div class="main-container">
      <nav-bar />
      <tags-view />
      <app-main />
    </div>
  </div>
</template>

<script>
import NavBar from '@/components/Layout/NavBar/NavBar'
import SideBar from '@/components/Layout/SideBar/SideBar'
import AppMain from '@/components/Layout/AppMain/AppMain'
import TagsView from '@/components/Layout/TagsView/TagsView'
import ResizeMixin from '@/mixins/ResizeHandler'
import { mapGetters, mapActions } from 'vuex'

export default {
  name: 'Layout',
  components: {
    NavBar,
    SideBar,
    AppMain,
    TagsView
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
  methods: {
    ...mapActions('layout', ['closeSideBar']),
    handleClickOutside() {
      this.closeSideBar({ withoutAnimation: false })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  @import "~@/styles/mixin.scss";
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
