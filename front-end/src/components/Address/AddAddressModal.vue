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
            <el-form-item label="Tỉnh/Thành phố" prop="province">
              <el-select
                v-model="address.provinceId"
                style="width: 100%"
                placeholder="Chọn tỉnh/thành phố"
                @change="provinceChange"
              >
                <el-option v-for="item in listLocation" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16" :lg="16">
            <el-form-item label="Quận/Huyện" prop="district">
              <el-select
                v-model="address.districtId"
                style="width: 100%"
                :disabled="address.provinceId === ''"
                placeholder="Chọn quận/huyện"
                @change="districtChange"
              >
                <el-option v-for="item in listDistrictByProvinceId(address.provinceId)" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16" :lg="16">
            <el-form-item label="Xã/Phường" prop="ward">
              <el-select v-model="address.wardId" style="width: 100%" :disabled="address.districtId === ''" placeholder="Chọn xã/phường">
                <el-option v-for="item in listWardByDistrictId(address.provinceId, address.districtId)" :key="item.id" :label="item.name" :value="item.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :xs="24" :md="16" :lg="16">
            <el-form-item label="Chi tiết">
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
                v-if="mode === 'add'"
                type="success"
                @click="handleCreateAddress"
              >Thêm địa chỉ</el-button>
              <el-button
                v-else
                type="success"
                @click="handleUpdateAddress"
              >Cập nhật địa chỉ</el-button>
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
    },
    mode: {
      type: String,
      default: 'add'
    },
    passAddress: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      addAddressDialogVisible: false,
      title: this.mode === 'add' ? this.$t('label.add_address') : this.$t('label.update_address'),
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
    },
    passAddress: function(newVal) {
      if (newVal.id) {
        this.address = {
          id: newVal.id,
          provinceId: newVal.province.id,
          districtId: newVal.district.id,
          wardId: newVal.ward.id,
          detail: newVal.detail
        }
      } else {
        this.address = {
          provinceId: '',
          districtId: '',
          wardId: '',
          detail: ''
        }
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    ...mapActions('location', ['loadListLocation']),
    ...mapActions('address', ['createAddress', 'updateAddress']),
    getList() {
      this.loadListLocation({
        searchKey: ''
      })
    },
    provinceChange() {
      this.address.districtId = ''
      this.address.wardId = ''
    },
    districtChange() {
      this.address.wardId = ''
    },
    handleCreateAddress() {
      this.createAddress(this.address).then(res => {
        showSuccess('message.create_address_success')
        this.$emit('createAddressSuccess', res)
        this.address = {}
      })
    },
    handleUpdateAddress() {
      this.updateAddress(this.address).then(res => {
        showSuccess('message.update_address_success')
        this.$emit('updateAddressSuccess', res)
        this.passAddress = {}
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
