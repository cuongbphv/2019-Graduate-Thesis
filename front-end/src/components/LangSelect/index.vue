<template>
  <el-dropdown trigger="click" class="international" @command="handleSetLanguage">
    <div>
      <svg-icon class-name="international-icon" icon-class="language" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item :disabled="language==='vi'" command="vi">
        Tiếng Việt
      </el-dropdown-item>
      <el-dropdown-item :disabled="language==='en'" command="en">
        English
      </el-dropdown-item>
    </el-dropdown-menu>
  </el-dropdown>
</template>

<script>
import { mapGetters, mapActions } from 'vuex'
export default {
  computed: {
    ...mapGetters('layout', ['language'])
  },
  methods: {
    ...mapActions('layout', ['setLanguage']),
    handleSetLanguage(lang) {
      this.$i18n.locale = lang
      this.setLanguage(lang)
      this.$message({
        message: this.$t('message.change_language'),
        type: 'success'
      })
    }
  }
}
</script>
