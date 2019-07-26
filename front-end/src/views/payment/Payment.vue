<template>
  <div class="wrapper" style="background: url('https://venngage-wordpress.s3.amazonaws.com/uploads/2018/09/Simple-Background-Images-2.png')">
    <el-row class="container">
      <el-col :md="{span: 2, offset: 2}">
        <div>
          <img src="@/assets/beecoin.png" alt="beecoin" style="width: 200px; border-radius: 50%">
        </div>
      </el-col>
      <el-col :md="{span: 3, offset: 2}">
        <div style="padding-top: 70px">
          <h2>Số dư: {{ account || 0 }} BeeCoin</h2>
          <a href="#history" style="color: blue; cursor: pointer">Xem lịch sử giao dịch</a>
        </div>
      </el-col>
      <el-col :md="{span: 7, offset: 3}">
        <div style="padding-top: 70px">
          <h2><b>Lợi ích của BeeCoin:</b></h2>
          <ul>
            <li>Đẩy bài viết lên nổi bật</li>
            <li>Mở rộng số lượng tin đăng</li>
          </ul>
        </div>
      </el-col>
    </el-row>
    <el-row class="container">
      <el-col :md="{span: 8, offset: 2}" :lg="{span: 8, offset: 2}">
        <el-table
          :data="tableData"
          style="width: 100%"
        >
          <el-table-column
            prop="price"
            label="Giá"
          />
          <el-table-column
            prop="bee"
            label="Bee Coin"
          />
        </el-table>
      </el-col>
      <el-col class="pay" :md="{span: 8, offset: 2}" :lg="{span: 8, offset: 2}">
        <h3><b>* Chọn số tiền cần nạp</b></h3>
        <hr>
        <el-form>
          <el-form-item label="Số tiền">
            <el-select v-model="amount" placeholder="Chọn số tiền" style="width: 350px">
              <el-option
                v-for="item in options"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <div style="text-align: center; cursor: pointer" @click="handleCreatePayment">
              <img src="https://www.braintreepayments.com/images/features/paypal/paypal-button@2x-d5ec2863.png" width="220px">
            </div>
          </el-form-item>
        </el-form>
      </el-col>
      <hr>
    </el-row>
    <el-row id="history" class="container">
      <el-col :md="{span: 20, offset: 2}" :lg="{span: 20, offset: 2}">
        <h3><b>Lịch sử giao dịch</b></h3>
        <el-table
          :data="history"
          height="250"
          style="width: 100%"
        >
          <el-table-column
            prop="createdDate"
            label="Ngày giao dịch"
          >
            <template slot-scope="scope">
              {{ scope.row.createdDate | parseTime('{d}-{m}-{y} | {h}:{i}') }}
            </template>
          </el-table-column>
          <el-table-column
            prop="type"
            label="Loại giao dịch"
          >
            <template slot-scope="scope">
              <el-tag>{{ scope.row.type | typeFilter }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            prop="beeCoin"
            label="BeeCoin"
          />
          <el-table-column
            prop="status"
            label="Trạng thái"
          >
            <template slot-scope="scope">
              <el-tag>{{ scope.row.status | statusFilter }}</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import date from '@/utils/date'
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'Payment',
  filters: {
    statusFilter(status) {
      const statusMap = {
        success: 'Thành công',
        pending: 'Đang xử lý',
        cancel: 'Đã hủy',
        failed: 'Thất bại'
      }
      return statusMap[status]
    },
    typeFilter(type) {
      const typeMap = {
        buy: 'Mua Beecoin',
        push: 'Đẩy tin đăng'
      }
      return typeMap[type]
    }
  },
  data() {
    return {
      tableData: [{
        price: '20.000 VNĐ',
        bee: '20'
      }, {
        price: '50.000 VNĐ',
        bee: '60'
      }, {
        price: '100.000 VNĐ',
        bee: '150'
      }, {
        price: '200.000 VNĐ',
        bee: '400'
      }],
      amount: '',
      options: [{
        value: '20000',
        label: '20.000 VNĐ'
      }, {
        value: '50000',
        label: '50.000 VNĐ'
      }, {
        value: '100000',
        label: '100.000 VNĐ'
      }, {
        value: '200000',
        label: '200.000 VNĐ'
      }],
      historyData: [{
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles'
      }, {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles'
      }, {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles'
      }, {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles'
      }, {
        date: '2016-05-08',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles'
      }, {
        date: '2016-05-06',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles'
      }, {
        date: '2016-05-07',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles'
      }]
    }
  },
  computed: {
    ...mapGetters('payment', ['rate', 'account', 'history'])
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
    this.getAccount()
    this.getPaymentHistory()
  },
  methods: {
    ...mapActions('payment', ['createPayment', 'getAccount', 'confirmPayment', 'getCurrencyRate', 'getPaymentHistory']),
    handleCreatePayment() {
      const am = this.amount * this.rate
      let coin = 0
      console.log('am', this.amount)
      switch (this.amount) {
        case '20000': coin = 20
          break
        case '50000': coin = 60
          break
        case '100000': coin = 150
          break
        case '200000': coin = 400
          break
      }
      const params = {
        amount: am,
        coin: coin
      }
      this.createPayment(params).then(res => {
        if (res && res !== 'OK') {
          window.open(res, '_self')
        }
      })
    },
    formatDate(rawDate) {
      return date.formatDate(rawDate)
    },
    scrollChatToBottom() {
      const container = this.$el.querySelector('#msg-container')
      container.scrollTop = container.scrollHeight
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
