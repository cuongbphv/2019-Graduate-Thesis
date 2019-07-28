<template>
  <div class="wrapper" style="background: url('https://venngage-wordpress.s3.amazonaws.com/uploads/2018/09/Simple-Background-Images-2.png')">
    <el-row class="container">
      <el-col :md="{span: 2, offset: 2}">
        <div>
          <img src="@/assets/beecoin.png" alt="beecoin" style="width: 200px; border-radius: 50%">
        </div>
      </el-col>
      <el-col :md="{span: 7, offset: 2}">
        <div style="padding-top: 50px">
          <h2>Yêu cầu thanh toán đang được gửi đi</h2>
          <el-button type="success" @click="handlePayment">Xác nhận thanh toán</el-button>
        </div>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import { showError, showSuccess } from '../../utils/message'
export default {
  name: 'Chat',
  data() {
    return {
      paymentId: this.$route.query.paymentId,
      payerId: this.$route.query.PayerID,
      pId: this.$route.query.id
    }
  },
  computed: {
    ...mapGetters('payment', ['rate', 'account'])
  },
  watch: {
    selectedConversationId: function(newVal) {
      this.messageTemplate = ''
      this.messageSize = 10
      this.getConversation(newVal)
      this.getMessages(newVal, this.scrollChatToBottom())
    }
  },
  created() {
    // this.getAllConversation()
    // this.subscribeMessage()
  },
  mounted() {
    this.getCurrencyRate()
  },
  methods: {
    ...mapActions('payment', ['confirmPayment']),
    handlePayment() {
      if (this.paymentId && this.payerId) {
        const params = {
          paymentId: this.paymentId,
          payerId: this.payerId,
          id: this.pId
        }
        this.confirmPayment(params).then(res => {
          if (res && res === 'OK') {
            showSuccess('message.pay_success')
            this.$router.push({ path: '/payment' })
          } else {
            showError('message.pay_error')
            this.$router.push({ path: '/payment' })
          }
        })
      }
    }
  }
}
</script>
<style lang="scss" scoped>
  @import url(//fonts.googleapis.com/css?family=League+Script|Josefin+Slab);
  .container{
    margin: 2rem 5rem
  }
  .pay{
    padding: 1rem 1rem 0;
    background: #ffffff;
  }
  .promo{
    font-family: 'Josefin Slab', cursive;
    text-transform: uppercase
  }
</style>
