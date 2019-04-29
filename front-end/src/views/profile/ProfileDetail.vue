<template>
  <div class="container">
    <header />
    <main>
      <el-row>
        <el-col class="left" :md="8" :lg="8">
          <div class="photo-left">
            <pan-thumb :image="image">
              <slot>
                <a href="javascript:void(0)" @click="imagecropperShow = true">Change Avatar</a>
              </slot>
            </pan-thumb>
            <!--            <div class="active"></div>-->
          </div>
          <h4 class="name"> {{ profile.firstName + ' ' + profile.lastName }}</h4>
          <p class="info"> Personal User </p>
          <p class="info">{{ profile.email }}</p>
          <el-row class="stats">
            <el-col class="stat" :md="8">
              <p class="number-stat">{{ profile.followedBy.length || 0 }}</p>
              <p class="desc-stat">Followers</p>
            </el-col>
            <el-col class="stat" :md="8">
              <p class="number-stat">{{ profile.following.length || 0 }}</p>
              <p class="desc-stat">Following</p>
            </el-col>
            <el-col class="stat" :md="8">
              <p class="number-stat">38</p>
              <p class="desc-stat">Uploads</p>
            </el-col>
          </el-row>
          <p class="desc"> {{ profile.description }} </p>
          <!--          <div class="social">-->
          <!--            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'facebook' }" />-->
          <!--            <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'google' }" />-->
          <!--          </div>-->
        </el-col>
        <el-col class="right" :md="16" :lg="16">
          <el-row>
            <ul class="nav">
              <li>
                <router-link :to="{path: 'history'}">Lịch sử</router-link>
              </li>
              <li>
                <router-link :to="{path: 'info'}">Thông tin</router-link>
              </li>
              <li>
                <router-link :to="{path: 'settings'}">Cài đặt</router-link>
              </li>
              <li>Cửa hàng</li>
              <li>Tin nhắn</li>
              <li>Trợ giúp</li>
            </ul>
          </el-row>
          <!--<span class="follow">Follow</span>-->
          <el-container>
            <router-view />
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
</template>

<script>
import ImageCropper from '@/components/ImageCropper'
import PanThumb from '@/components/PanThumb'
import { mapActions, mapState } from 'vuex'
export default {
  name: 'ProfileDetail',
  components: {
    ImageCropper,
    PanThumb
  },
  data() {
    return {
      imagecropperShow: false,
      imagecropperKey: 0,
      image: 'https://image.noelshack.com/fichiers/2017/38/2/1505775062-1505606859-portrait-1961529-960-720.jpg',
      profileId: this.$route.params.id
    }
  },
  computed: {
    ...mapState('profile', ['profile'])
  },
  created: function() {
    this.handleGetProfile()
  },
  methods: {
    ...mapActions('profile', ['getProfile']),
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
    }
  }
}
</script>

<style lang="scss" scoped>
  .container {
    margin: 0 auto;
    padding: 0 !important;
    background-color: #fff;
    box-shadow: 0 3px 6px rgba(0,0,0,0.10), 0 3px 6px rgba(0,0,0,0.10);
  }

  header {
    background: url("https://image.noelshack.com/fichiers/2017/38/2/1505775648-annapurnafocus.jpg") no-repeat center;
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

  .right {
    padding: 0 25px 0 25px !important;
  }

  .nav {
    display: inline-flex;
    list-style-type: none;
    li {
      margin: 0 30px 0 10px;
      cursor: pointer;
      font-size: 13pt;
      text-transform: uppercase;
      font-weight: 500;
      color: #888;
      /*&:hover {*/
        /*color: #999;*/
        /*border-bottom: 2px solid #999;*/
      /*}*/
      /*&:nth-child(1) {*/
        /*color: #999;*/
        /*border-bottom: 2px solid #999;*/
      /*}*/
      .router-link-active {
        color: #999;
        border-bottom: 2px solid #999;
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

  @media (max-width:990px) {
    .nav {
      display: none;
    }
    .follow {
      width: 50%;
      margin-left: 25%;
      display: block;
      position: unset;
      text-align: center;
    }
  }
</style>
