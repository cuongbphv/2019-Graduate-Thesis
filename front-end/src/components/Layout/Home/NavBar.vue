<template>
  <div class="navbar">

    <div v-if="device === 'mobile'" id="sideNavigation" class="sidenav">
      <el-menu
        default-active="2"
        class="el-menu-vertical-demo"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :active-text-color="variables.menuActiveText"
        style="border-right: 0;"
      >
        <el-menu-item index="2">
          <i class="el-icon-menu" />
          <span>Navigator Two</span>
        </el-menu-item>
        <el-menu-item index="4">
          <i class="el-icon-setting" />
          <span>Navigator Four</span>
        </el-menu-item>
        <el-menu-item index="5" @click="closeNav()">
          <i class="el-icon-close" />
          <span>Đóng</span>
        </el-menu-item>
      </el-menu>
    </div>

    <div class="row menu">
      <div class="column-left">
        <hamburger
          v-if="device === 'mobile'"
          id="hamburger"
          :is-active="opened"
          class="hamburger-container"
        />
        <router-link to="/"><img src="../../../assets/logo.png" alt="No logo"></router-link>
      </div>
      <div class="column-center">
        <template v-if="device !== 'mobile'">
          <search-bar />
        </template>
      </div>
      <div class="column-right">
        <div class="menu_right">
          <Login :visible="isVisible" @closeLoginModal="handleLoginModal" />
          <el-button v-if="!profile.userId" class="button el-button--primary" @click="handleLoginModal">Đăng nhập</el-button>
          <el-dropdown v-else class="avatar-container menu-item hover-effect" trigger="click">
            <div class="avatar-wrapper">
              <img :src="(profile.avatarUrl ? profile.avatarUrl : 'src/assets/default-avatar.gif') + '?imageView2/1/w/80/h/80'" class="user-avatar">
              <i class="el-icon-caret-bottom" />
            </div>
            <el-dropdown-menu slot="dropdown">
              <router-link :to="'/profile/' + profile.userId">
                <el-dropdown-item>
                  {{ $t('navbar.profile') }}
                </el-dropdown-item>
              </router-link>
              <router-link v-if="profile.role.name === 'ADMIN'" to="/dashboard">
                <el-dropdown-item>
                  {{ $t('navbar.dashboard') }}
                </el-dropdown-item>
              </router-link>
              <el-dropdown-item divided>
                <span style="display:block;" @click="logout">{{ $t('navbar.logOut') }}</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
        <div v-if="profile.userId">
          <el-dropdown trigger="click">
            <el-tooltip :content="$t('label.message')" effect="dark" placement="bottom">
              <el-badge :value="3" class="item">
                <el-button class="badge" size="medium" icon="el-icon-message" />
              </el-badge>
            </el-tooltip>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="display: inline-block">
                  <img src="https://pixel.nymag.com/imgs/daily/vulture/2018/11/02/02-avatar-2.w700.h700.jpg" class="img-rounded">
                </div>
                <div style="display: inline-block; margin-left: 5px; width: 300px">
                  <div class="review-block-name"><a href="#"><b>Huy Phạm</b></a></div>
                  <div class="review-block-date">Sáng nay đã ăn gì chưa đó?</div>
                  <div class="review-block-date">25 - 05 - 2019</div>
                </div>
              </el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-plus">Action 2</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-plus-outline">Action 3</el-dropdown-item>
              <el-dropdown-item icon="el-icon-check">Action 4</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-check-outline">Action 5</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-dropdown trigger="click">
            <el-tooltip :content="$t('label.notify')" effect="dark" placement="bottom">
              <el-badge :value="1" class="item">
                <el-button class="badge" size="medium" icon="el-icon-bell" />
              </el-badge>
            </el-tooltip>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <div style="display: inline-block">
                  <img src="https://pixel.nymag.com/imgs/daily/vulture/2018/11/02/02-avatar-2.w700.h700.jpg" class="img-rounded">
                </div>
                <div style="display: inline-block; margin-left: 5px;width: 300px">
                  <div class="review-block-name"><a href="#"><b>Huy Phạm Huy Phạm Huy Phạm Huy Phạm Huy Phạm Huy Phạm Huy Phạm Huy Phạm Huy Phạm Huy Phạm</b></a></div>
                  <div class="review-block-date">Sáng nay đã ăn gì chưa đó?</div>
                  <div class="review-block-date">25 - 05 - 2019</div>
                </div>
              </el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-plus">Action 2</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-plus-outline">Action 3</el-dropdown-item>
              <el-dropdown-item icon="el-icon-check">Action 4</el-dropdown-item>
              <el-dropdown-item icon="el-icon-circle-check-outline">Action 5</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-tooltip :content="$t('label.create')" effect="dark" placement="bottom" style="float: left!important;">
            <el-badge class="item">
              <router-link to="/advertising">
                <el-button class="badge" size="medium" icon="el-icon-edit" />
              </router-link>
            </el-badge>
          </el-tooltip>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from 'vuex'
import Hamburger from '@/components/Hamburger'
import SearchBar from '@/components/Layout/Home/SearchBar'
import Login from '@/components/Auth/LoginModal'
import variables from '@/styles/variables.scss'

window.addEventListener('click', function(e) {
  const hamburger = document.getElementById('hamburger')
  const sideBar = document.getElementById('sideNavigation')
  // check element available or not
  if (hamburger === null || hamburger === undefined || sideBar === null || sideBar === undefined) return
  // check click into hamburger icon
  if (hamburger.contains(e.target)) {
    sideBar.style.width = '210px'
    return
  }
  // if click outside sidebar area
  if (!sideBar.contains(e.target)) {
    sideBar.style.width = '0px'
  }
})

export default {
  name: 'NavBar',
  components: {
    Hamburger,
    SearchBar,
    Login
  },
  data() {
    return {
      isVisible: false,
      opened: false
    }
  },
  computed: {
    ...mapState('profile', ['profile']),
    ...mapGetters('layout', ['sidebar', 'device']),
    variables() {
      return variables
    }
  },
  created() {
  },
  methods: {
    ...mapActions('layout', ['toggleSideBar']),
    ...mapActions('auth', ['clear']),
    logout() {
      this.clear().then(() => {
        const token = new URL(window.location.href).searchParams.get('token')
        if (token) {
          this.$router.push('home')
        }
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    },
    handleLoginModal() {
      this.isVisible = !this.isVisible
    },
    openNav() {
      this.opened = true
      document.getElementById('sideNavigation').style.width = '210px'
    },
    closeNav() {
      this.opened = false
      document.getElementById('sideNavigation').style.width = '0px'
    }
  }
}
</script>

<style lang="scss" scoped>
.sidenav {
  height: 100%; /* 100% Full-height */
  width: 0; /* 0 width - change this with JavaScript */
  position: fixed; /* Stay in place */
  z-index: 1; /* Stay on top */
  top: 0;
  left: 0;
  background-color: #304156;
  overflow-x: hidden; /* Disable horizontal scroll */
  transition: 0.5s; /* 0.5 second transition effect to slide in the sidenav */
}

/* On smaller screens, where height is less than 450px, change the style of the sidenav (less padding and a smaller font size) */
@media screen and (max-height: 450px) {
  .sidenav {padding-top: 15px;}
}

.column-left {
  float: left;
  width: 30%;
  line-height: 55px;

  img {
    height: 50px;
    display: inline-block;
    margin: 0 0 0 30px;
  }
}
.img-rounded{
  width: 48px;
  height: 48px;
  border-radius: 50%;
}
.review-block-name{
  font-size:12px;
  line-height: 20px;
}
.review-block-date{
  font-size:12px;
  line-height: 20px;
}
.column-center {
  float: left;
  width: 50%;
  padding-top: 3px;
}
.column-right {
  float: right;
  width: 20%;
  .item {
    margin-left: 0px;
    padding: 0;
    float: right;
    margin-top: 3px;
  }
  .badge {
    background: none;
    border: none;
    font-size: 20px;
  }
  /deep/ .el-badge__content {
    &.is-fixed {
      top: 13px;
      right: 30px;
    }
  }
}
.row:after {
  content: "";
  display: table;
  clear: both;
}
@media screen and (max-width: 800px) {
  .column-left {
    width: 50%;
    img {
      margin: 0;
    }
  }
  .column-right {
    width: 50%;
  }
}
.navbar {
  height: 55px;
  overflow: hidden;
  position: relative;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .menu {
    line-height: 55px;

    &_right {
      height: 50px;
      float: right;
    }

    /deep/ .el-button--medium {
      padding: 10px 20px !important;
    }

    &:focus {
      outline: none;
    }

    .button {
      display: inline-block;
      padding: 0 8px;
      height: 80%;
      vertical-align: text-bottom;
      margin: 5px;
      color: #DBE8E6;
    }

    .menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .padding-top {
      padding-top: 12px;
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 6px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 15px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
