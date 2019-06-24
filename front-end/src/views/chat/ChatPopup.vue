<template>
  <el-dialog
    :visible.sync="chatPopupVisible"
    :title="title"
    width="50%"
    top="20vh"
    @close="handleClose"
  >
    <el-row>
      <p>Gửi tin nhắn đến {{ recipient.firstName }}</p>
      <el-input
        v-model="messageRequest.content"
        type="textarea"
        :rows="5"
        placeholder="Nhập nội dung tin nhắn"
      />
      <el-button style="margin-top: 1rem; float: right" type="success" @click="handleSendMsg">Gửi tin nhắn</el-button>
    </el-row>
  </el-dialog>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'ChatPopup',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    recipient: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      chatPopupVisible: false,
      title: 'Gửi tin nhắn',
      messageRequest: {
        receiverId: '',
        senderId: '',
        content: ''
      }
    }
  },
  computed: {
    ...mapGetters('profile', ['userId'])
  },
  watch: {
    visible: function(newVal) {
      this.chatPopupVisible = newVal
    },
    recipient: function(newVal) {
      this.messageRequest.receiverId = newVal.userId
    }
  },
  created() {
  },
  methods: {
    ...mapActions('chat', ['createNewConversation']),
    handleSendMsg() {
      this.messageRequest.senderId = this.userId
      this.createNewConversation(this.messageRequest).then(() => {
        this.$router.push({ path: '/chat' })
      })
    },
    handleClose() {
      this.$emit('closeChatModal', '')
      this.messageRequest = {}
    }
  }
}
</script>
<style lang="scss" scoped >
</style>
