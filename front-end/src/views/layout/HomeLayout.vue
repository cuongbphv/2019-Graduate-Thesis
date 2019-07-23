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
    ...mapGetters('layout', ['device']),
    ...mapGetters('profile', ['userId'])
  },
  mounted() {
    console.log('userId', this.userId)
    this.subscribeMessage()
  },
  methods: {
    ...mapActions('layout', ['closeSideBar']),
    ...mapActions('chat', ['getAllConversationByUserId']),
    ...mapActions('notification', ['getNotifications']),
    handleClickOutside() {
      this.closeSideBar({ withoutAnimation: false })
    },
    subscribeMessage() {
      const ws = new SockJS(this.socketUrl)
      this.stompClient = Stomp.over(ws)
      const that = this
      this.stompClient.connect({}, function() {
        if (that.userId) {
          that.getAllConversationByUserId({ userId: that.userId, size: 10 })
          that.getNotifications({ userId: that.userId, size: 10 })
        }
        that.stompClient.subscribe(`/user/${that.userId}/queue/chat`, (message) => {
          that.getAllConversationByUserId({ userId: that.userId, size: 10 })
        })
        that.stompClient.subscribe(`/user/${that.userId}/queue/notification`, (message) => {
          that.getNotifications({ userId: that.userId, size: 10 })
          if (message.body) {
            const body = JSON.parse(message.body)
            console.log('body', body)
            switch (body.type) {
              case 'NEW_POST':
                that.$toasted.show(`${body.sender.firstName} ${body.sender.lastName} vừa đăng tin mới:
                ${body.data.additionalInfo.title}`, {
                  position: 'bottom-left',
                  duration: 5000,
                  action: {
                    text: 'Xem',
                    onClick: (e, toastObject) => {
                      that.$router.push('/advertising/' + body.data.id)
                      toastObject.goAway(0)
                    }
                  }})
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
</style>
