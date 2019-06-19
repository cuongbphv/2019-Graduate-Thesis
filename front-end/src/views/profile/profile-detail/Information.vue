<template>
  <div class="info">
    <el-form ref="formProfile" style="padding-left: 50px" :model="profileForm" label-position="left" label-width="150px">
      <el-row>
        <el-col :md="11" :lg="11">
          <el-form-item :label="$t('userProfile.firstName')">
            <el-input v-model="profileForm.firstName" />
          </el-form-item>
        </el-col>
        <el-col :md="{span: 11, offset: 2}" :lg="{span: 11, offset: 2}">
          <el-form-item :label="$t('userProfile.lastName')">
            <el-input v-model="profileForm.lastName" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :sm="11" :md="11" :lg="11">
          <el-form-item :label="$t('userProfile.gender')">
            <el-select v-model="profileForm.gender" placeholder="Select gender">
              <el-option :label="$t('userProfile.male')" :value="1" />
              <el-option :label="$t('userProfile.female')" :value="2" />
              <el-option :label="$t('userProfile.otherGender')" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :sm="{span: 11, offset: 2}" :md="{span: 11, offset: 2}" :lg="{span: 11, offset: 2}">
          <el-form-item :label="$t('userProfile.dob')">
            <el-date-picker
              v-model="profileForm.dob"
              type="date"
              placeholder="Pick a date"
              format="dd/MM/yyyy"
              value-format="yyyy-MM-dd"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="$t('userProfile.description')">
            <el-input
              v-model="profileForm.description"
              type="textarea"
              :rows="3"
              :placeholder="$t('userProfile.descriptionPlaceholder')"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :md="11" :lg="11">
          <el-form-item :label="$t('userProfile.createdDate')">
            <el-date-picker
              :disabled="true"
              :value="profileForm.createdDate"
              type="date"
              format="dd/MM/yyyy"
              style="width: 100%;"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button
          type="success"
          :disabled="!formUpdated"
          icon="el-icon-circle-check-outline"
          @click="handleUpdateProfile"
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
    </el-form>
  </div>
</template>

<script>
import { mapActions, mapGetters } from 'vuex'
import { showSuccess } from '../../../utils/message'
export default {
  name: 'Information',
  data() {
    return {
      profileId: this.$route.params.id,
      profileForm: {},
      formUpdated: false
    }
  },
  computed: {
    ...mapGetters('profile', ['profile'])
  },
  watch: {
    profileForm: {
      handler: function(val) {
        this.formUpdated = JSON.stringify(val) !== JSON.stringify(this.profile)
      },
      deep: true
    }
  },
  mounted: function() {
    this.getProfile(this.profileId).then(() => {
      this.profileForm = Object.assign({}, this.profile)
    })
  },
  methods: {
    ...mapActions('profile', ['updateProfile', 'getProfile']),
    handleUpdateProfile() {
      this.updateProfile(this.profileForm).then(() => {
        showSuccess('userProfile.message.updateSuccess')
        this.handleResetButton()
      })
    },
    handleResetButton() {
      this.profileForm = Object.assign({}, this.profile)
    }
  }
}
</script>

<style lang="scss" scoped>

  .info{
    margin-top: 20px;
  }
</style>
