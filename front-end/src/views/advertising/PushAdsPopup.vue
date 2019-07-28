<template>
  <el-dialog
    :visible.sync="pushPopupVisible"
    :title="title"
    width="25%"
    top="25vh"
    @close="handleClose"
  >
    <el-row>
      <p>Chọn gói đẩy tin:</p>
      <el-radio-group v-model="period" style="line-height: 30px; padding: 0 30px">
        <el-radio :label="1">1 ngày = 5 Beecoin</el-radio>
        <el-radio :label="3">3 ngày = 10 Beecoin</el-radio>
        <el-radio :label="5">5 ngày = 15 Beecoin</el-radio>
      </el-radio-group>
      <el-button style="margin-top: 1rem; float: right" type="success" @click="handlePushAds">Thực hiện</el-button>
    </el-row>
  </el-dialog>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import { showError, showSuccess } from '../../utils/message'
export default {
  name: 'PushAdsPopup',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    adsId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      pushPopupVisible: false,
      title: 'Đẩy tin đăng',
      messageRequest: {
        receiverId: '',
        senderId: '',
        content: ''
      },
      period: 1
    }
  },
  computed: {
    ...mapGetters('profile', ['userId'])
  },
  watch: {
    visible: function(newVal) {
      this.pushPopupVisible = newVal
    },
    adsId: function(newVal) {
      this.adsId = newVal
    }
  },
  created() {
  },
  methods: {
    ...mapActions('advertising', ['pushAdvertising']),
    ...mapActions('payment', ['getAccount']),
    handlePushAds() {
      const params = {
        id: this.adsId,
        period: this.period
      }
      this.pushAdvertising(params).then((res) => {
        if (res) {
          this.handleClose()
          showSuccess('message.push_success')
        } else {
          showError('message.push_error')
        }
        this.getAccount()
      })
    },
    handleClose() {
      this.$emit('closePushAdsModal', '')
      this.period = 1
      this.adsId = ''
    }
  }
}
</script>
<style lang="scss" scoped >
</style>
