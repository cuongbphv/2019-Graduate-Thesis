<template>
  <div class="login-container">
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <lang-select class="set-language" />

      <p>{{ $t('login.connectWith') }}</p>
      <div class="social">
        <el-button class="el-button--primary" @click="handleLoginOAuth2('facebook')">
          <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'facebook' }" size="2x" />
          <span>acebook</span>
        </el-button>
        <el-button class="el-button--danger" @click="handleLoginOAuth2('google')">
          <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'google' }" size="2x" />
          <span>oogle</span>
        </el-button>
      </div>

      <hr class="hr-text" :data-content="$t('login.or')">

      <el-form-item prop="phone">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          v-model="loginForm.phone"
          :placeholder="$t('login.phoneNumber')"
          name="phone"
          type="text"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          v-model="loginForm.password"
          :type="passwordType"
          :placeholder="$t('login.password')"
          name="password"
          auto-complete="on"
          @keyup.enter.native="handleLoginLocal"
        />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <transition name="fade-transform" mode="out-in">
        <el-form-item v-if="checkMode('register')" prop="confirmPassword">
          <span class="svg-container">
            <svg-icon icon-class="password" />
          </span>
          <el-input
            v-model="loginForm.confirmPassword"
            :type="passwordType"
            :placeholder="$t('login.confirmPassword')"
            name="confirmPassword"
            auto-complete="on"
            @keyup.enter.native="handleLoginLocal"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
      </transition>

      <div class="additional">
        <el-checkbox>{{ $t('login.rememberMe') }}</el-checkbox>
        <a href="#">{{ $t('login.forgotPassword') }}</a>
      </div>

      <div class="button_group">
        <el-button v-if="checkMode('login')" :loading="loading" type="primary" @click.native.prevent="handleLoginLocal">
          {{ $t('button.logIn') }}
        </el-button>
        <el-button v-if="checkMode('register')" :loading="loading" type="primary" @click.native.prevent="handleRegister">
          {{ $t('button.signUp') }}
        </el-button>
      </div>

      <h5 v-if="checkMode('login')">
        {{ $t('login.dontHaveAccount') }}&nbsp;
        <strong>
          <a href="#" @click="switchMode('register')">{{ $t('button.signUp') }}</a>
        </strong>
      </h5>

      <h5 v-if="checkMode('register')">
        {{ $t('login.haveAccount') }}&nbsp;
        <strong>
          <a href="#" @click="switchMode('login')">{{ $t('button.logIn') }}</a>
        </strong>
      </h5>

    </el-form>
  </div>
</template>

<script>
import { mapActions } from 'vuex'
import { showSuccess } from '@/utils/message'
import LangSelect from '@/components/LangSelect'

export default {
  name: 'Login',
  components: { LangSelect },
  data() {
    return {
      loginForm: {
        phone: '',
        password: '',
        confirmPassword: ''
      },
      loginRules: {
        phone: [{ required: true, message: this.$t('validator.required'), trigger: 'blur' }],
        password: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
          if (value.length < 6) {
            callback(new Error(this.$t('validator.password.max_length', { 'max_length': 6 })))
          }
          callback()
        } }],
        confirmPassword: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
          if (value.length < 6) {
            callback(new Error(this.$t('validator.password.max_length', { 'max_length': 6 })))
          }
          if (this.loginForm.password !== this.loginForm.confirmPassword) {
            callback(new Error(this.$t('validator.password.not_match')))
          }
          callback()
        } }]
      },
      passwordType: 'password',
      loading: false,
      isVisible: false,
      title: 'Login',
      mode: 'login'
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    ...mapActions('auth', ['register', 'loginLocal', 'loginOAuth2']),
    ...mapActions('profile', ['initData']),
    ...mapActions('permission', ['loadRoutesByAuthorities']),
    handleDrag() {
    },
    handleClose() {
      this.$emit('closeLoginModal', '')
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    handleLoginLocal() {
      this.$refs.loginForm.validate(valid => {
        this.loading = true
        if (valid) {
          const params = {
            phone: this.loginForm.phone,
            password: this.loginForm.password
          }
          this.loginLocal(params).then(() => {
            this.loading = false
            this.isVisible = false
            showSuccess('message.login_success')
            this.initData().then((data) => {
              this.loadRoutesByAuthorities(data.authorities).then(() => {
                if (data.role.name === 'SYS_ADMIN') {
                  this.$router.push('role')
                } else if (data.role.name === 'ADMIN') {
                  this.$router.push('dashboard')
                } else if (data.role.name === 'MODERATOR') {
                  this.$router.push('dashboard')
                } else if (data.role.name === 'USER') {
                  this.$router.push('home')
                }
              })
            })
          })
        }
      })
    },
    handleLoginOAuth2(type) {
      window.open('http://localhost:8080/oauth2/authorize/' + type + '?redirect_uri=http://localhost:4040/#/home', '_blank')
    },
    handleRegister() {
      this.$refs.loginForm.validate(valid => {
        this.loading = true
        if (valid) {
          const params = {
            phone: this.loginForm.phone,
            password: this.loginForm.password
          }
          this.register(params).then((data) => {
            this.loading = false
            if (data) {
              this.loginLocal(params).then(() => {
                this.isVisible = false
                this.$emit('closeLoginModal', '')
                this.initData().then((authorities) => {
                  this.loadRoutesByAuthorities(authorities)
                })
              })
            }
          })
        }
      })
    },
    switchMode(mode) {
      this.mode = mode
    },
    checkMode(mode) {
      return this.mode === mode
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  $bg:#283443;
  $light_gray:#eee;
  $cursor: #fff;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .login-container .el-input input{
      color: $cursor;
      &::first-line {
        color: $light_gray;
      }
    }
  }

  /* reset element-ui css */
  .login-container {
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
      input {
        background: transparent;
        border: 0;
        -webkit-appearance: none;
        border-radius: 0;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;
        &:-webkit-autofill {
          box-shadow: 0 0 0 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
  $bg:#283443;
  $light_gray:#eee;
  $cursor: #fff;
  $dark_gray: #889aa4;

.login-container {
  position: relative;
  min-height: 100%;
  width: 100%;
  background-color: #fff;
  overflow: hidden;
  .login-form {
    border-radius: .625rem;
    box-shadow: inset 0 4px 0 0 #007bff;
    background: #232323;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%,-50%);
    width: 520px;
    max-width: 100%;
    padding: 50px 35px 50px;
    margin: auto;
    overflow: hidden;

    a {
      &:hover {
        color: #409EFF;
      }
    }
    h5 {
      line-height: 0;
      margin-top: 50px;
      color: #fff;
      text-align: center;
      a {
        font-size: 1.2em;
      }
    }
    p {
      margin: 0;
      text-align: center;
      color: #fff;
      margin-bottom: 10px;
    }
    .el-input {
      display: inline-block;
      width: 85%;
      input {
        background: transparent;
        border: 0;
        -webkit-appearance: none;
        border-radius: 0;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        caret-color: $cursor;
        &:-webkit-autofill {
          box-shadow: 0 0 0 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
      /deep/ .el-input__inner {
        margin-top: 7px;
      }
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
    .svg-container {
      padding: 6px 0 6px 10px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 10px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }
    .social {
      text-align: center;
      span {
        margin-left: -2px;
      }
    }
    .hr-text {
      line-height: 1em;
      position: relative;
      outline: 0;
      border: 0;
      text-align: center;
      height: 1.5em;
      &:before {
        content: '';
        background: linear-gradient(to right, transparent, #818078, transparent);
        position: absolute;
        left: 0;
        top: 50%;
        width: 100%;
        height: 1px;
      }
      &:after {
        content: attr(data-content);
        position: relative;
        display: inline-block;
        padding: 0 .5em;
        line-height: 1.5em;
        color: #fff;
        background: #232323;
      }
    }
    .button_group {
      margin-top: 5px;
      text-align: center;
      /deep/ .el-button {
        width: 40%;
      }
    }
    .additional {
      line-height: 20px;
      color: #fff;
      a {
        float: right;
        margin-right: 12px;
      }
      /deep/ .el-checkbox {
        margin-left: 12px;
        color: #fff;
      }
    }
    .set-language {
      color: #fff;
      position: absolute;
      font-size:18px;
      cursor: pointer;
      right: 10px;
    }
  }
}
</style>
