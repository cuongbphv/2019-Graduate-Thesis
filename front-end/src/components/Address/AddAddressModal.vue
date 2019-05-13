<template>
  <el-dialog
    :visible.sync="addAddressDialogVisible"
    :title="title"
    width="70%"
    top="20vh"
    @close="handleClose"
  >
    <el-row>
      <el-col :xs="24" :md="{span:18, offset: 6}">
        <el-form :model="address" label-position="left" label-width="120px">
          <el-col :xs="24" :md="16" :lg="16">
            <el-form-item label="Province" prop="province">
              <el-select v-model="address.provinceId" style="width: 100%" placeholder="Select Province">
                <el-option v-for="item in listLocation" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16" :lg="16">
            <el-form-item label="District" prop="district">
              <el-select v-model="address.districtId" style="width: 100%" :disabled="address.provinceId === ''" placeholder="Select District">
                <el-option v-for="item in listDistrictByProvinceId(address.provinceId)" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16" :lg="16">
            <el-form-item label="Ward" prop="ward">
              <el-select v-model="address.wardId" style="width: 100%" :disabled="address.districtId === ''" placeholder="Select Ward">
                <el-option v-for="item in listWardByDistrictId(address.provinceId, address.districtId)" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16" :lg="16">
            <el-form-item label="Detail">
              <el-input
                v-model="address.detail"
                type="textarea"
                :rows="3"
              />
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16" :lg="16" style="text-align: center;">
            <el-form-item>
              <el-button
                type="success"
                @click="handleCreateAddress"
              >Create Address</el-button>
            </el-form-item>
          </el-col>
        </el-form>
      </el-col>
    </el-row>
  </el-dialog>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import { showSuccess } from '../../utils/message'
export default {
  name: 'AddAddressModal',
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      addAddressDialogVisible: false,
      title: 'Add new Address',
      address: {
        provinceId: '',
        districtId: '',
        wardId: '',
        detail: ''
      }
    }
  },
  computed: {
    ...mapGetters('location', ['listLocation', 'listDistrictByProvinceId', 'listWardByDistrictId'])
  },
  watch: {
    visible: function(newVal) {
      this.addAddressDialogVisible = newVal
    }
  },
  created() {
    this.getList()
  },
  methods: {
    ...mapActions('location', ['loadListLocation']),
    ...mapActions('address', ['createAddress']),
    getList() {
      this.loadListLocation({
        searchKey: ''
      })
    },
    handleCreateAddress() {
      this.createAddress(this.address).then(res => {
        showSuccess('message.create_address_success')
        this.$emit('createAddressSuccess', res)
        this.address = {}
      })
    },
    handleClose() {
      this.$emit('closeAddressModal', '')
      this.address = {}
    }
  }
}
</script>
<style lang="scss" scoped >
</style>
