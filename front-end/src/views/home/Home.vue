<template>
  <div class="home-container">
    <div class="main-container">
      <category />

      <div class="search-trending">
        <div class="header">
          <h6>Tìm kiếm hàng đầu</h6>
        </div>
        <div class="body">
          <el-row>
            <el-col v-for="(o, index) in 4" :key="o" :span="5" :offset="index > 0 ? 1 : 0">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image" alt="No image">
                <div style="padding: 14px;">
                  <span>Yummy hamburger</span>
                  <div class="bottom clearfix">
                    <el-button type="text" class="button">Operating</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>

      <div class="search-trending">
        <div class="header">
          <h6>Tin mới nhất</h6>
        </div>
        <div class="body">
          <el-row>
            <el-col v-for="(item, index) in topPagingNewClassifiedAds" :key="item.classifiedAdsId" :span="5" :offset="index > 0 ? 1 : 0">
              <el-card shadow="hover" :body-style="{ padding: '0px' }">
                <img src="https://shadow.elemecdn.com/app/element/hamburger.9cf7b091-55e9-11e9-a976-7f4d0b07eef6.png" class="image" alt="No image">
                <div style="padding: 14px;">
                  <span>Yummy hamburger</span>
                  <div class="bottom clearfix">
                    <el-button type="text" class="button">Operating</el-button>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import { About, Category } from '@/components/Layout/Home/index'
import { mapActions, mapGetters } from 'vuex'
import { Stomp } from '@stomp/stompjs'
import * as SockJS from 'sockjs-client'
export default {
  name: 'Home',
  components: {
    // eslint-disable-next-line
    About,
    Category
  },
  data() {
    return {
      query: {
        pageNumber: 1,
        pageSize: 4
      },
      socketUrl: 'http://localhost:8080/socket'
    }
  },
  computed: {
    ...mapGetters('advertising', ['topPagingNewClassifiedAds'])
  },
  beforeCreate() {
    const token = new URL(window.location.href).searchParams.get('token')
    if (token) {
      this.loginOAuth2(token).then(() => {
        this.initData().then(() => {
        })
      })
    }
  },
  created() {
    // this.getPagingNewClassifiedAds(this.query)
    const ws = new SockJS(this.socketUrl)
    this.stompClient = Stomp.over(ws)
    const that = this
    this.stompClient.connect({}, function() {
      that.stompClient.subscribe('/user/5cae4c886bf46d0b5453a66f/queue/notification', (message) => {
        if (message.body) {
          const body = JSON.parse(message.body)
          switch (body.type) {
            case 'NEW_POST':
              alert(body)
              break
            case 'MESSAGE':
            default:
            // code block
          }
        }
      })
      // Tell your username to the server
      // that.stompClient.send('/app/chat.create',
      //   {},
      //   JSON.stringify({ userId: '5cae4c886bf46d0b5453a66f' })
      // )
      // that.stompClient.send('/app/chat.sendMessage/5cfc2bf2d1ac505b8c860320',
      //   {},
      //   JSON.stringify({
      //     senderId: '5cae4c886bf46d0b5453a66f',
      //     content: 'ditmemay'
      //   })
      //   // '5cae4c886bf46d0b5453a66f'
      // )
    })
  },
  methods: {
    ...mapActions('auth', ['loginOAuth2']),
    ...mapActions('profile', ['initData']),
    ...mapActions('advertising', ['getPagingNewClassifiedAds'])
  }
}
</script>

<style lang="scss" scoped>
.home-container {
  position: relative;
  margin: 1.25rem 0;

  .about {
    position: absolute;
  }
  .main-container {
    position: absolute;
    margin: 10px 40px;
  }
  .time {
    font-size: 13px;
    color: #999;
  }
  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }
  .button {
    padding: 0;
    float: right;
  }
  .image {
    width: 100%;
    display: block;
  }
  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .search-trending {
    background: #FFF;
    margin-top: 1.25rem;

    .header {
      border-bottom: 1px solid #d8dce5;
      padding: 1.25rem;

      h6 {
        font-size: 1rem;
        color: rgba(0,0,0,.54);
        font-weight: 500;
      }
    }

    .body {
      padding: 1.25rem 1rem 1.25rem 3rem;
    }
  }
}
</style>
