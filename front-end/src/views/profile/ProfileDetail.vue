<template>
  <transition name="fade-transform" mode="out-in">
    <div class="profile-container">
      <header />
      <main>
        <el-row>
          <el-col class="left" :xs="24" :md="8" :lg="8">
            <div class="photo-left">
              <pan-thumb :image="image">
                <slot>
                  <a href="javascript:void(0)" @click="imagecropperShow = true">Đổi ảnh đại diện</a>
                </slot>
              </pan-thumb>
              <!--            <div class="active"></div>-->
            </div>
            <h4 class="name"> {{ profile.firstName + ' ' + (profile.lastName || ' ') }}</h4>
            <!--<p class="info"> Personal User </p>-->
            <p class="info">{{ profile.email }}</p>
            <el-row class="stats">
              <el-col class="stat" :md="12">
                <p v-if="profile.followedBy" class="number-stat">{{ profile.followedBy.length }}</p>
                <p v-else class="number-stat">0</p>
                <p class="desc-stat">Được theo dõi</p>
              </el-col>
              <el-col class="stat" :md="12">
                <p v-if="profile.following" class="number-stat">{{ profile.following.length }}</p>
                <p v-else class="number-stat">0</p>
                <p class="desc-stat">Đang theo dõi</p>
              </el-col>
              <!--<el-col class="stat" :md="8">-->
              <!--<p class="number-stat">38</p>-->
              <!--<p class="desc-stat">Uploads</p>-->
              <!--</el-col>-->
            </el-row>
            <el-row v-if="profile.userId !== userId" class="stats">
              <el-button v-if="profile.followedBy && profile.followedBy.includes(userId)" type="danger" icon="el-icon-close-notification" round @click="handleUnFollowUser(profileId)">Bỏ theo dõi</el-button>
              <el-button v-else type="success" round icon="el-icon-bell" @click="handleFollowUser(profileId)">Theo dõi</el-button>
              <el-button v-if="profile.userId !== userId" type="primary" icon="el-icon-chat-dot-round" round @click="handleOpenChatModal()">Chat</el-button>
            </el-row>
            <p class="desc"> {{ profile.description }} </p>
            <chat-popup :recipient="profile" :visible="chatPopupVisible" @closeChatModal="handleCloseChatModal" />
            <!--          <div class="social">-->
            <!--            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'facebook' }" />-->
            <!--            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'google' }" />-->
            <!--          </div>-->
          </el-col>
          <el-col class="right" :xs="24" :md="14" :lg="14">
            <el-row>
              <ul class="nav">
                <li :class="{'is-active' : routerName === 'history'}">
                  <a @click="handleClickRouter('history')">Lịch sử</a>
                </li>
                <li v-if="userId === profileId" :class="{'is-active' : routerName === 'info'}">
                  <a @click="handleClickRouter('info')">Thông tin</a>
                </li>
                <li v-if="userId === profileId" :class="{'is-active' : routerName === 'settings'}">
                  <a @click="handleClickRouter('settings')">Cài đặt</a>
                </li>
                <li v-if="userId === profileId" :class="{'is-active' : routerName === 'address'}">
                  <a @click="handleClickRouter('address')">Địa chỉ</a>
                </li>
                <li :class="{'is-active' : routerName === 'rating'}">
                  <a @click="handleClickRouter('rating')">Đánh giá</a>
                </li>
                <li :class="{'is-active' : routerName === 'saved'}">
                  <a @click="handleClickRouter('saved')">Đã lưu</a>
                </li>
              </ul>
            </el-row>
            <!--<span class="follow">Follow</span>-->
            <el-container>
              <transition name="fade-transform" mode="out-in">
                <keep-alive :include="cachedViews">
                  <router-view :key="key" />
                </keep-alive>
              </transition>
            </el-container>
          </el-col>
        </el-row>
      </main>

      <image-cropper
        v-show="imagecropperShow"
        :key="imagecropperKey"
        :width="300"
        :height="300"
        url="/profile/avatar"
        lang-type="vi"
        @close="close"
        @crop-upload-success="cropSuccess"
      />

    </div>
  </transition>
</template>

<script>
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'
import { mapActions, mapGetters } from 'vuex'
import { scrollTo } from '@/utils/scrollTo'
import ChatPopup from '../chat/ChatPopup'

export default {
  name: 'ProfileDetail',
  components: {
    ImageCropper,
    PanThumb,
    ChatPopup
  },
  data() {
    return {
      imagecropperShow: false,
      imagecropperKey: 0,
      image: 'https://png.pngtree.com/svg/20161212/f93e57629c.svg',
      profileId: this.$route.params.id,
      routerName: 'history',
      chatPopupVisible: false
    }
  },
  computed: {
    ...mapGetters('profile', ['profile', 'currentProfile', 'userId']),
    cachedViews() {
      return this.$store.state.tagsView.cachedViews
    },
    key() {
      return this.$route.fullPath
    }
  },
  watch: {
    '$route.params.id': function(id) {
      this.profileId = id
      this.handleGetProfile()
    }
  },
  mounted: function() {
    this.handleGetProfile()
  },
  methods: {
    ...mapActions('profile', ['getProfile', 'followUser', 'unFollowUser']),
    handleUnFollowUser(id) {
      this.unFollowUser(id).then(() => {
        this.handleGetProfile()
      })
    },
    handleFollowUser(id) {
      this.followUser(id).then(() => {
        this.handleGetProfile()
      })
    },
    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.image = resData
    },
    close() {
      this.imagecropperShow = false
    },
    handleGetProfile() {
      this.getProfile(this.profileId).then(response => {
        if (response) {
          this.image = response.avatarUrl
        }
      })
    },
    handleClickRouter(name) {
      this.routerName = name
      this.$router.push(name)
      scrollTo(100)
    },
    handleCloseChatModal() {
      this.chatPopupVisible = false
    },
    handleOpenChatModal() {
      this.chatPopupVisible = true
    }
  }
}
</script>

<style lang="scss" scoped>
  .profile-container {
    margin: 10px 15px;
    padding: 0 !important;
    background-color: #fff;
    box-shadow: 0 3px 6px rgba(0,0,0,0.10), 0 3px 6px rgba(0,0,0,0.10);
  }

  header {
    background: url("../../assets/profile_background.jpg") no-repeat center;
    background-size: cover;
    height: 200px;
    i {
      position: relative;
      cursor: pointer;
      right: -96%;
      top: 25px;
      font-size: 18px !important;
      color: #fff;
    }
  }

  @media (max-width:800px) {
    header {
      height: 150px;
    }

    header i {
      right: -90%;
    }
  }

  main {
    padding: 20px 20px 0 20px;
  }

  .left {
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
  }

  .pan-item {
    width: 200px;
    height: 200px;
    margin-top: -120px;
    border-radius: 100px;
    border: 4px solid #fff;
    a {
      font-weight: 900;
      color: #fff;
    }
  }

  .active {
    width: 20px;
    height: 20px;
    border-radius: 20px;
    position: absolute;
    right: calc(50% - 70px);
    top: 75px;
    background-color: #FFC107;
    border: 3px solid #fff;
  }

  @media (max-width:990px) {
    .active {
      right: calc(50% - 60px);
      top: 50px;
    }
  }

  .name {
    margin-top: 20px;
    font-weight: 600;
    font-size: 18pt;
    color: #777;
  }

  .info {
    margin-top: -5px;
    margin-bottom: 5px;
    font-size: 11pt;
    color: #aaa;
  }

  .stats {
    width: 80%;
    margin-top: 25px;
    text-align: center;
    padding-bottom: 20px;
    border-bottom: 1px solid #ededed;
    .number-stat {
      padding: 0;
      font-size: 14pt;
      font-weight: bold;
      color: #aaa;
    }
    .desc-stat {
      margin-top: -15px;
      font-size: 10pt;
      color: #bbb;
    }
    /deep/ .el-col {
      text-align: center;
    }
  }

  .desc {
    text-align: center;
    margin: 25px 40px;
    color: #999;
    font-size: 11pt;
    padding-bottom: 25px;
    border-bottom: 1px solid #ededed;
  }

  .social {
    margin: 5px 0 12px 0;
    text-align: center;
    display: inline-block;
    font-size: 20pt;
    i {
      cursor: pointer;
      margin: 0 15px;
    }
  }

  .social i:nth-child(1)  { color: #4267b2; }
  .social i:nth-child(2)  { color: #1da1f2; }
  .social i:nth-child(3)  { color: #bd081c; }
  .social i:nth-child(4)  { color: #36465d; }

  .nav {
    display: inline-flex;
    list-style-type: none;
    li {
      margin: 0 30px 0 5px;
      cursor: pointer;
      font-size: 13pt;
      text-transform: uppercase;
      font-weight: 500;
      color: #888;
      &.is-active {
        color: #42b1fa;
        border-bottom: 2px solid #42b1fa;
      }
    }
  }

  .follow {
    position: absolute;
    right: 8%;
    top: 35px;
    font-size: 11pt;
    background-color: #42b1fa;
    color: #fff;
    padding: 8px 15px;
    cursor: pointer;
    transition: all .4s;
    font-weight: 400;
    &:hover {
      box-shadow: 0 0 15px rgba(0,0,0,0.2), 0 0 15px rgba(0,0,0,0.2);
    }
  }

  @media (max-width: 1250px) {
    .nav {
      li {
        margin: 0 20px 0 0;
      }
    }
  }

  @media (min-width: 691px) and (max-width:990px) {
    .right {
      padding: 0 25px;
      .nav {
        padding-left: 2rem;
        li {
          margin: 0 20px;
        }
      }
    }

    .follow {
      width: 50%;
      margin-left: 25%;
      display: block;
      position: unset;
      text-align: center;
    }
  }

  @media (min-width: 501px) and (max-width: 690px) {
    .right {
      .nav {
        padding: 20px;
      }
      .el-container {
        form {
          padding-left: 20px !important;
        }
      }
    }
  }

  @media (max-width: 500px) {
    .right {
      overflow-y: scroll;
    }
  }
</style>
