<template>
  <el-dropdown trigger="click" class="international" @command="handleSetLanguage">
    <div>
      <svg-icon class-name="international-icon" icon-class="language" />
    </div>
    <el-dropdown-menu slot="dropdown">
      <el-dropdown-item :disabled="language==='vi'" command="vi">
        <img src="../../assets/vi.png" alt="No icon">
        <span>{{ $t('label.vi') }}</span>
      </el-dropdown-item>
      <el-dropdown-item :disabled="language==='en'" command="en">
        <img src="../../assets/en.png" alt="No icon">
        <span>{{ $t('label.en') }}</span>
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

<style lang="scss" scoped>
  /deep/ .el-dropdown-menu__item {
    display: table;
    img, span {
      display: table-cell;
      vertical-align: middle;
      margin-top: 2px;
    }
    span {
      padding-left: 5px;
    }
  }
</style>
