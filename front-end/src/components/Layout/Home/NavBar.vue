<template>
  <div class="navbar">
    <hamburger
      v-if="device === 'mobile'"
      :toggle-click="toggleSideBar"
      :is-active="sidebar.opened"
      class="hamburger-container"
    />

    <div class="menu menu_left">
      <template v-if="device !== 'mobile'">
        <search-bar />
      </template>
    </div>

    <div class="menu menu_right">
      <Login :visible="isVisible" @closeLoginModal="handleLoginModal" />
      <el-button v-if="!auth.userId" class="button el-button--primary" @click="handleLoginModal">Đăng nhập</el-button>
      <el-dropdown v-else class="avatar-container menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="(auth.avatarUrl ? 'src/assets/default-avatar.gif' : auth.avatarUrl) + '?imageView2/1/w/80/h/80'" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link :to="'/profile/' + auth.userId">
            <el-dropdown-item>
              {{ $t('navbar.profile') }}
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided>
            <span style="display:block;" @click="logout">{{ $t('navbar.logOut') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions } from 'vuex'
import Hamburger from '@/components/Hamburger'
import SearchBar from '@/components/Layout/Home/SearchBar'
import Login from '@/components/Auth/Login/LoginModal'

export default {
  name: 'NavBar',
  components: {
    Hamburger,
    SearchBar,
    Login
  },
  data() {
    return {
      isVisible: false
    }
  },
  computed: {
    ...mapState('auth', ['auth']),
    ...mapGetters('layout', ['sidebar', 'device']),
    ...mapGetters(['name'])
  },
  methods: {
    ...mapActions('layout', ['toggleSideBar']),
    ...mapActions('auth', ['clear']),
    logout() {
      this.clear().then(() => {
        // location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    },
    handleLoginModal() {
      this.isVisible = !this.isVisible
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;

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
    &_right {
      float: right;
    }
    &_left {
      left: 30%;
      position: absolute;
    }
    height: 100%;
    line-height: 50px;

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
        margin-top: 5px;
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
