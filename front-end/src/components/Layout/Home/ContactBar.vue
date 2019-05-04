<template>
  <div v-if="device!=='mobile'" class="contact_bar">
    <div class="right-menu">
      <template>
        <span class="right-menu-item" style="font-size: 12px">Email: cuongbphv@gmail.com</span>
        <!--        <el-tooltip :content="$t('navbar.size')" effect="dark" placement="bottom">-->
        <!--          <size-select class="right-menu-item hover-effect" />-->
        <!--        </el-tooltip>-->

        <lang-select class="right-menu-item hover-effect" />

        <el-tooltip :content="$t('navbar.theme')" effect="dark" placement="bottom">
          <theme-picker class="right-menu-item hover-effect padding-top" />
        </el-tooltip>
      </template>
    </div>
  </div>
</template>

<script>
// import SizeSelect from '@/components/SizeSelect'
import LangSelect from '@/components/LangSelect'
import ThemePicker from '@/components/ThemePicker'
import { mapGetters } from 'vuex'
export default {
  name: 'ContactBar',
  components: {
    // SizeSelect,
    LangSelect,
    ThemePicker
  },
  computed: {
    ...mapGetters('layout', ['device'])
  },
  methods: {}
}
</script>

<style lang="scss" scoped>
.contact_bar {
  height: 35px;
  overflow: hidden;
  background: #292c2f;

  .right-menu {
    float: right;
    height: 100%;
    line-height: 35px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #DBE8E6;
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
      padding-top: 4px;
    }
  }
}
</style>

