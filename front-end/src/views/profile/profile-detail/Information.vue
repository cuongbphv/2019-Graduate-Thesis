<template>
  <el-form ref="formProfile" :model="profileForm" label-width="150px">
    <el-row>
      <el-col :md="12" :lg="12">
        <el-form-item label="First Name">
          <el-input v-model="profileForm.firstName" />
        </el-form-item>
      </el-col>
      <el-col :md="12" :lg="12">
        <el-form-item label="Last Name">
          <el-input v-model="profileForm.lastName" />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :md="12" :lg="12">
        <el-form-item label="Gender">
          <el-select v-model="profileForm.gender" placeholder="Select gender">
            <el-option label="Nữ" :value="0" />
            <el-option label="Nam" :value="1" />
            <el-option label="Khác" :value="2" />
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :md="12" :lg="12">
        <el-form-item label="Date of birth">
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
        <el-form-item label="Description">
          <el-input
            v-model="profileForm.description"
            type="textarea"
            :rows="3"
            placeholder="Type little info about yourself"
          />
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :md="12" :lg="12">
        <el-form-item label="Created Date">
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
      <el-button type="success" :disabled="!formUpdated" icon="el-icon-circle-check-outline" @click="handleUpdateProfile">Save</el-button>
      <el-button :disabled="!formUpdated" icon="el-icon-refresh" @click="handleResetButton">Reset</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { mapActions, mapState } from 'vuex'

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
    ...mapState('profile', ['profile'])
  },
  watch: {
    profileForm: {
      handler: function(val) {
        this.formUpdated = JSON.stringify(val) !== JSON.stringify(this.profile)
      },
      deep: true
    }
  },
  created: function() {
    this.profileForm = Object.assign({}, this.profile)
  },
  methods: {
    ...mapActions('profile', ['updateProfile', 'getProfile']),
    handleUpdateProfile() {
      this.updateProfile(this.profileForm).then(response => {
        if (response && response.status === 200) {
          this.handleResetButton()
        }
      })
    },
    handleResetButton() {
      this.profileForm = Object.assign({}, this.profile)
    }
  }
}
</script>

<style lang="scss" scoped>

</style>
