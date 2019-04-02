<template>
  <el-dialog
    :visible.sync="isVisible"
    @dragDialog="handleDrag"
    @close="handleClose"
  >
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

      <p>Connect with</p>
      <div class="social">
        <el-button class="el-button--primary">
          <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'facebook' }" size="2x" />
          <span>acebook</span>
        </el-button>
        <el-button class="el-button--danger">
          <font-awesome-icon :icon="{ prefix: 'fab', iconName: 'google' }" size="2x" />
          <span>oogle</span>
        </el-button>
      </div>

      <hr class="hr-text" data-content="or">

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          v-model="loginForm.username"
          :placeholder="$t('login.username')"
          name="username"
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
          @keyup.enter.native="handleLogin"
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
            :placeholder="$t('login.password')"
            name="confirmPassword"
            auto-complete="on"
            @keyup.enter.native="handleLogin"
          />
          <span class="show-pwd" @click="showPwd">
            <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
          </span>
        </el-form-item>
      </transition>

      <div class="additional">
        <el-checkbox>Remember me</el-checkbox>
        <a href="#">Forgot Password?</a>
      </div>

      <div class="button_group">
        <el-button v-if="checkMode('login')" :loading="loading" type="primary" @click.native.prevent="handleLogin">
          {{ $t('login.logIn') }}
        </el-button>
        <el-button v-if="checkMode('register')" :loading="loading" type="primary" @click.native.prevent="handleLogin">
          Sign up
        </el-button>
      </div>

      <h5 v-if="checkMode('login')">
        Don't have an account?&nbsp;
        <strong>
          <a href="#" @click="switchMode('register')">Sign up</a>
        </strong>
      </h5>

      <h5 v-if="checkMode('register')">
        Already have an account?&nbsp;
        <strong>
          <a href="#" @click="switchMode('login')">Log in</a>
        </strong>
      </h5>

    </el-form>
  </el-dialog>
</template>

<script>
import { validUsername } from '@/utils/validate'
export default {
  name: 'Login',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validUsername(value)) {
        callback(new Error('Please enter the correct user name'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('The password can not be less than 6 digits'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: 'admin',
        password: '1111111',
        confirmPassword: '1111111'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      passwordType: 'password',
      loading: false,
      isVisible: false,
      title: 'Login',
      mode: 'login'
    }
  },
  watch: {
    visible: function(newVal) {
      this.isVisible = newVal
    }
  },
  methods: {
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
    handleLogin() {
      this.$refs.loginForm.validate(valid => {})
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

<style lang="scss" scoped>
$bg:#283443;
$light_gray:#eee;
$cursor: #fff;
$dark_gray: #889aa4;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input{
    color: $cursor;
    &::first-line {
      color: $light_gray;
    }
  }
}
/deep/ .el-dialog {
  border-radius: .625rem;
  box-shadow: inset 0 4px 0 0 #007bff;
  background: #384955;
  width: 35%;
  overflow: hidden;
  color: #fff;
  .el-dialog__header {
    padding: 5px 20px 0;
  }
  .el-dialog__body {
    padding: 0 20px 5px 20px;
  }
  margin-top: 10vh !important;
}
@media screen and (max-width: 503px) {
  /deep/ .el-dialog {
    width: 95%;
  }
}
@media screen and (min-width: 504px) and (max-width: 600px) {
  /deep/ .el-dialog {
    width: 90%;
  }
}
@media screen and (min-width: 601px) and (max-width: 950px) {
  /deep/ .el-dialog {
    width: 80%;
  }
}
@media screen and (min-width: 951px) and (max-width: 1365px) {
  /deep/ .el-dialog {
    width: 50%;
  }
}
@media screen and (min-width: 1366px) {
  /deep/ .el-dialog {
    width: 35%;
  }
}

.login-form {
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
      background-color: #384955;
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
}
</style>
