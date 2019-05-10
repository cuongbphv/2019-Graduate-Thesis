<template>
  <div class="home-container">
    <div class="main-container">
      <category />
      <div style="padding: 20px;">
        <el-divider content-position="left">Tìm kiếm hàng đầu</el-divider>
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
        <el-divider content-position="left">Tin mới nhất</el-divider>
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

  </div>
</template>

<script>
import { About, Category } from '@/components/Layout/Home/index'
import { mapActions } from 'vuex'
export default {
  name: 'Home',
  components: {
    // eslint-disable-next-line
    About,
    Category
  },
  data() {
    return {}
  },
  beforeCreate() {
    const token = new URL(window.location.href).searchParams.get('token')
    if (token) {
      this.loginOAuth2(token).then(() => {
        this.initData()
      })
    }
  },
  methods: {
    ...mapActions('auth', ['loginOAuth2']),
    ...mapActions('profile', ['initData'])
  }
}
</script>

<style lang="scss" scoped>
.home-container {
  position: relative;
  .about {
    position: absolute;
  }
  .main-container {
    position: absolute;
    margin: 10px 15px;
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
}
</style>
