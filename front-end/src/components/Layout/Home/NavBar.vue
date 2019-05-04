<template>
  <div class="navbar">
    <div class="row menu">
      <div class="column-left">
        <hamburger
          v-if="device === 'mobile'"
          :toggle-click="toggleSideBar"
          :is-active="sidebar.opened"
          class="hamburger-container"
        />
        <router-link to="/"><h3>Smart<span>Market</span></h3></router-link>
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
          <el-tooltip :content="$t('label.message')" effect="dark" placement="bottom">
            <el-badge :value="3" class="item">
              <el-button class="badge" size="medium" icon="el-icon-message" />
            </el-badge>
          </el-tooltip>
          <el-tooltip :content="$t('label.notify')" effect="dark" placement="bottom">
            <el-badge :value="1" class="item">
              <el-button class="badge" size="medium" icon="el-icon-bell" />
            </el-badge>
          </el-tooltip>
          <el-tooltip :content="$t('label.create')" effect="dark" placement="bottom">
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
    ...mapState('profile', ['profile']),
    ...mapGetters('layout', ['sidebar', 'device'])
  },
  methods: {
    ...mapActions('layout', ['toggleSideBar']),
    ...mapActions('auth', ['clear']),
    logout() {
      this.clear().then(() => {
        location.reload()// In order to re-instantiate the vue-router object to avoid bugs
      })
    },
    handleLoginModal() {
      this.isVisible = !this.isVisible
    }
  }
}
</script>

<style lang="scss" scoped>
.column-left {
  float: left;
  width: 30%;
  h3 {
    line-height: 50px;
    display: inline-block;
    color:  #000;
    font: bold 25px 'Cookie', cursive;
    margin: 5px 10px;
    span {
      color:  #5383d3;
    }
  }
}
.column-center {
  float: left;
  width: 50%;
}
.column-right {
  float: right;
  width: 20%;
  .item {
    margin-left: -10px;
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
  }
  .column-right {
    width: 50%;
  }
}
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
      height: 50px;
      float: right;
    }
    line-height: 50px;

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
