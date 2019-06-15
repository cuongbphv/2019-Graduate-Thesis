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
import { Stomp } from '@stomp/stompjs'
import * as SockJS from 'sockjs-client'
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
  data() {
    return {
      socketUrl: 'http://localhost:8080/socket'
    }
  },
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
    this.subscribeMessage()
  },
  methods: {
    ...mapActions('layout', ['closeSideBar']),
    handleClickOutside() {
      this.closeSideBar({ withoutAnimation: false })
    },
    subscribeMessage() {
      const ws = new SockJS(this.socketUrl)
      this.stompClient = Stomp.over(ws)
      const that = this
      this.stompClient.connect({}, function() {
        that.stompClient.subscribe('/user/5cae4c886bf46d0b5453a66f/queue/notification', (message) => {
          if (message.body) {
            const body = JSON.parse(message.body)
            switch (body.type) {
              case 'NEW_POST':
                console.log(body)
                break
              case 'MESSAGE':
              default:
              // code block
            }
          }
        })
      })
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
