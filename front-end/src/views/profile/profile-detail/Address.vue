<template>
  <div class="address">
    <el-row
      v-for="address in existedAddress"
      :key="address.id"
    >
      <div class="address-item">
        <el-col :xs="24" :md="16" :lg="16" style="text-align: left">
          <p>{{ address.detail + ', ' + address.ward.name + ', '
            + address.district.name + ', ' + address.province.name }}</p>
        </el-col>
        <el-col :xs="24" :md="8" :lg="8" style="text-align: right">
          <el-button
            type="primary"
            icon="el-icon-edit"
            circle
            @click="openUpdateAddressModal(address)"
          />
          <el-button
            type="danger"
            icon="el-icon-delete"
            circle
            @click="handleDeleteAddress(address.id)"
          />
        </el-col>
      </div>
    </el-row>
    <el-row style="text-align: center;">
      <el-button
        style="text-align: center; margin-top: 1rem"
        @click="openAddAddressModal"
      ><i class="el-icon-plus" /> {{ $t('label.add_address') }}</el-button>
    </el-row>
    <add-address-modal
      :mode="mode"
      :visible="updateAddressDialogVisible"
      :pass-address="selectedAddress"
      @updateAddressSuccess="handleUpdateAddressSuccess"
      @createAddressSuccess="handleAddAddressSuccess"
      @closeAddressModal="handleCloseAddressModal"
    />
  </div>
</template>

<script>
import { mapActions, mapState } from 'vuex'
import AddAddressModal from '../../../components/Address/AddAddressModal'
import { showSuccess } from '../../../utils/message'

export default {
  name: 'Address',
  components: {
    AddAddressModal
  },
  data() {
    return {
      updateAddressDialogVisible: false,
      selectedAddress: {},
      existedAddress: [],
      mode: 'add'
    }
  },
  computed: {
    ...mapState('address', ['addresses'])
  },
  watch: {
    addresses: function(newVal) {
      this.existedAddress = Object.assign({}, newVal)
    }
  },
  created() {
    this.getListAddress()
  },
  methods: {
    ...mapActions('address', ['getAddressByUserId', 'deleteAddressByIds']),
    getListAddress() {
      this.getAddressByUserId()
    },
    handleDeleteAddress(addressId) {
      this.$confirm(this.$t('message.confirm_delete'), this.$t('label.warning'), {
        confirmButtonText: this.$t('button.confirm'),
        cancelButtonText: this.$t('button.cancel'),
        type: 'warning'
      }).then(() => {
        this.deleteAddressByIds(addressId).then(() => {
          this.getListAddress()
          showSuccess('message.delete_address_success')
        })
      }).catch(() => {
      })
    },
    openAddAddressModal() {
      this.mode = 'add'
      this.selectedAddress = {}
      this.updateAddressDialogVisible = true
    },
    handleAddAddressSuccess() {
      this.selectedAddress = {}
      this.updateAddressDialogVisible = false
      this.getListAddress()
    },
    openUpdateAddressModal(address) {
      this.mode = 'update'
      this.selectedAddress = Object.assign({}, address)
      this.updateAddressDialogVisible = true
    },
    handleUpdateAddressSuccess() {
      this.selectedAddress = {}
      this.updateAddressDialogVisible = false
      this.getListAddress()
    },
    handleCloseAddressModal() {
      this.selectedAddress = {}
      this.updateAddressDialogVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
  .address  {
    margin-top: 20px;
    margin-bottom: 20px;
    padding-left: 50px;
    width: 100%;
    vertical-align: middle;

    .el-row{
      padding: 1rem;

      p{
        margin-top: 0.5rem;
      }
    }

    .el-row:nth-child(odd) {
        background: #edf2fc;
    }

    .el-row:last-child {
      background: none;
    }
  }
</style>
