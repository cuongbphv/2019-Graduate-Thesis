<template>
  <el-dialog
    :visible.sync="isVisible"
    @dragDialog="handleDrag"
    @close="handleClose"
  >
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">

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
  </el-dialog>
</template>

<script>
import { mapActions } from 'vuex'
import { showSuccess } from '../../../utils/message'
export default {
  name: 'Login',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      loginForm: {
        phone: '',
        password: '',
        confirmPassword: ''
      },
      loginRules: {
        phone: [{ required: true, message: 'Please input phone number', trigger: 'blur' }
          // { type: 'email', message: 'Please input correct email address', trigger: ['blur', 'change']}
        ],
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
    visible: function(newVal) {
      this.isVisible = newVal
    }
  },
  methods: {
    ...mapActions('auth', ['register', 'loginLocal', 'loginOAuth2', 'initData']),
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
            this.initData()
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

<style lang="scss" scoped>
@import "~@/styles/components/login";
</style>
