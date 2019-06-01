<template>
  <div class="settings">
    <el-form ref="formSetting" style="padding-left: 50px" :model="settingForm" label-position="left" label-width="150px">
      <el-row>
        <el-col>
          <el-form-item :label="$t('userSetting.changePassword')">
            <el-button v-if="!allowChangePass" type="primary" icon="el-icon-edit" circle @click="allowChangePass = true" />
            <el-button v-if="allowChangePass" icon="el-icon-close" circle @click="allowChangePass = false" />
          </el-form-item>
        </el-col>
        <el-col>
          <el-form
            v-if="allowChangePass"
            ref="changePassForm"
            :model="changePassForm"
            :rules="changePassRules"
            style="padding-left: 50px"
            label-position="left"
            label-width="150px"
          >
            <el-row>
              <el-col :md="18">
                <el-form-item :label="$t('userSetting.currentPassword')" prop="currentPassword">
                  <el-input
                    v-model="changePassForm.currentPassword"
                    type="password"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :md="18">
                <el-form-item :label="$t('userSetting.newPassword')" prop="newPassword">
                  <el-input
                    v-model="changePassForm.newPassword"
                    :type="passwordType"
                  />
                  <span class="show-pwd" @click="showPwd">
                    <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                  </span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :md="18">
                <el-form-item :label="$t('userSetting.retypePassword')" prop="retypePassword">
                  <el-input
                    v-model="changePassForm.retypePassword"
                    :type="passwordType"
                    @keyup.enter.native="handleChangePassword"
                  />
                  <span class="show-pwd" @click="showPwd">
                    <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                  </span>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-form-item>
                <el-button
                  type="success"
                  icon="el-icon-circle-check-outline"
                  @click="handleChangePassword"
                >
                  {{ $t('button.save') }}
                </el-button>
              </el-form-item>
            </el-row>
          </el-form>
        </el-col>
      </el-row>
      <el-row>
        <el-form-item :label="$t('userSetting.enable')">
          <el-switch v-model="settingForm.enable" :width="40" />
        </el-form-item>
      </el-row>
      <el-row v-if="settingForm.enable">
        <el-form-item :label="$t('userSetting.notificationFor')">
          <el-checkbox
            v-for="item in settingForm.items"
            :key="item.key"
            v-model="item.enable"
            :label="$t('userSetting.' + item.key)"
          />
        </el-form-item>
      </el-row>
      <el-row>
        <el-form-item>
          <el-button
            type="success"
            :disabled="!formUpdated"
            icon="el-icon-circle-check-outline"
            @click="handleUpdateSetting"
          >
            {{ $t('button.save') }}
          </el-button>
          <el-button
            :disabled="!formUpdated"
            icon="el-icon-refresh"
            @click="handleResetButton"
          >
            {{ $t('button.reset') }}
          </el-button>
        </el-form-item>
      </el-row>
    </el-form>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import { showSuccess } from '../../../utils/message'
import auth from '@/api/auth'
import { Status } from '../../../utils/constants'

export default {
  name: 'Information',
  data() {
    return {
      profileId: this.$route.params.id,
      settingForm: {},
      changePassForm: {
        currentPassword: '',
        newPassword: '',
        retypePassword: ''
      },
      passwordType: 'password',
      formUpdated: false,
      allowChangePass: false,
      allowNotification: false,
      changePassRules: {
        currentPassword: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
          if (value.length < 6) {
            callback(new Error(this.$t('validator.password.max_length', { 'max_length': 6 })))
          }
          callback()
        } }],
        newPassword: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
          if (value.length < 6) {
            callback(new Error(this.$t('validator.password.max_length', { 'max_length': 6 })))
          }
          callback()
        } }],
        retypePassword: [{ required: true, trigger: 'blur', validator: (rule, value, callback) => {
          if (value.length < 6) {
            callback(new Error(this.$t('validator.password.max_length', { 'max_length': 6 })))
          }
          if (this.changePassForm.newPassword !== this.changePassForm.retypePassword) {
            callback(new Error(this.$t('validator.password.not_match')))
          }
          callback()
        } }]
      }
    }
  },
  computed: {
    ...mapGetters('profile', ['setting'])
  },
  watch: {
    settingForm: {
      handler: function(val) {
        console.log('new val', val)
        console.log('old val', this.setting)
        this.formUpdated = JSON.stringify(val) !== JSON.stringify(this.setting)
      },
      deep: true
    }
  },
  mounted: function() {
    this.settingForm = Object.assign({}, this.settingForm, this.setting)
  },
  methods: {
    ...mapActions('profile', ['updateSettings']),
    handleUpdateSetting() {
      this.loading = true
      this.updateSettings(this.settingForm).then(() => {
        this.loading = false
        showSuccess('userSetting.message.updateSettingSuccess')
        this.handleResetButton()
      })
    },
    handleResetButton() {
      this.settingForm = Object.assign({}, this.settingForm, this.setting)
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = 'text'
      } else {
        this.passwordType = 'password'
      }
    },
    handleChangePassword() {
      this.$refs.changePassForm.validate(valid => {
        this.loading = true
        if (valid) {
          const params = {
            currentPassword: this.changePassForm.currentPassword,
            newPassword: this.changePassForm.newPassword
          }
          auth.changePassword(params).then((res) => {
            if (res.status === Status.SUCCESS) {
              this.loading = false
              this.allowChangePass = false
              this.changePassForm = {}
              showSuccess('userSetting.message.updatePasswordSuccess')
            }
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
  $dark_gray:#889aa4;

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 0px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }

  .settings{
    margin-top: 20px;
  }

</style>
