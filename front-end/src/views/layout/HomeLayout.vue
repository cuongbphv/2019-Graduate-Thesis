<template>
  <div class="home-layout">
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
import { mapGetters } from 'vuex'
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
    ...mapGetters('layout', ['device'])
  },
  created() {
  },
  methods: {
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
</style>
