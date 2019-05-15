<template>
  <div class="step2">
    <h3>{{ $t('advertising.step2') }}</h3>
    <el-row>
      <el-radio-group v-model="locationType" size="small">
        <el-col :xs="24" :md="12" style="padding: 0.5rem 1rem">
          <el-radio :label="1" border>{{ $t('advertising.new_location') }}</el-radio>
        </el-col>
        <el-col :xs="24" :md="12" style="padding: 0.5rem 1rem">
          <el-radio :label="2" border>{{ $t('advertising.exist_location') }}</el-radio>
        </el-col>
      </el-radio-group>
    </el-row>

    <div v-if="locationType === 1">
      <div class="search-form">
        <el-form>
          <el-form-item prop="title">
            <md-input
              v-model="searchKey"
              icon="search"
              name="title"
              :placeholder="$t('advertising.search_placeholder')"
              @keyup.enter.native="getList"
            >{{ $t('advertising.search') }}</md-input>
          </el-form-item>
        </el-form>
      </div>

      <div class="location-result">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item v-if="mode === 'district' || mode === 'ward'">
            <a href="javascript:void(0)" @click="setLocation(undefined, undefined, undefined)">{{ $t('label.list_province') }}</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item v-if="mode === 'district' || mode === 'ward'">
            <a href="javascript:void(0)" @click="setLocation(localLocation.province, undefined, undefined)">{{ localLocation.province.name }}</a>
          </el-breadcrumb-item>
          <el-breadcrumb-item v-if="mode === 'ward'">
            <a href="javascript:void(0)" @click="setLocation(undefined, localLocation.district, undefined)">{{ localLocation.district.name }}</a>
          </el-breadcrumb-item>
        </el-breadcrumb>

        <ul v-if="mode === 'province'">
          <li v-for="item in listLocation" :key="item.id" @click="setLocation(item, undefined, undefined)">{{ item.name }}</li>
        </ul>

        <ul v-if="mode === 'district'">
          <li v-for="item in listDistricts" :key="item.id" @click="setLocation(undefined, item, undefined)">{{ item.name }}</li>
        </ul>

        <ul v-if="mode === 'ward'">
          <li v-for="item in listWards" :key="item.id" :class="{'selected' : localLocation.ward && item.id === localLocation.ward.id}" @click="setLocation(undefined, undefined, item)">{{ item.name }}</li>
        </ul>

      </div>
    </div>

    <div v-else-if="locationType === 2">
      <div class="choose-location">
        <hr>
        <el-row>
          <el-radio-group v-model="selectedLocation" class="group-address">
            <el-col
              v-for="item in existedAddress"
              :key="item.id"
              :xs="24"
              :md="{offset: 6,span:12}"
              :class="{'selected' : selectedLocation === item.id}"
              @click="selectLocation(item.id)"
            >
              <el-radio :label="item.id">
                {{ item.detail + ', ' + item.ward.name + ', ' + item.district.name + ', ' + item.province.name }}
              </el-radio>
            </el-col>
          </el-radio-group>
          <el-col style="text-align: center;">
            <el-button
              style="text-align: center; margin-top: 1rem"
              @click="addAddressDialogVisible = true"
            ><i class="el-icon-plus" /> Thêm địa chỉ mới</el-button>
          </el-col>
        </el-row>
        <add-address-modal
          :visible="addAddressDialogVisible"
          @createAddressSuccess="handleCreateSuccess"
          @closeAddressModal="handleCloseAddressModal"
        />
        <hr>
      </div>
    </div>

    <el-row class="center-padding-top">
      <el-button type="primary" @click="changeStep('back')">{{ $t('button.previous') }}</el-button>
      <el-button type="success" @click="changeStep('next')">{{ $t('button.continue') }}</el-button>
    </el-row>
  </div>
</template>

<script>
import MdInput from '@/components/MDinput'
import { mapActions, mapGetters, mapState } from 'vuex'
import AddAddressModal from '../Address/AddAddressModal'
export default {
  name: 'Step2',
  components: {
    AddAddressModal,
    MdInput
  },
  props: {
    location: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      localLocation: {},
      locationType: 1,
      searchKey: '',
      mode: 'province',
      listDistricts: [],
      originalDistricts: [],
      listWards: [],
      originalWards: [],
      addAddressDialogVisible: false,
      existedAddress: [],
      selectedLocation: ''
    }
  },
  computed: {
    ...mapGetters('location', ['listLocation', 'listDistrictByProvinceId', 'listWardByDistrictId']),
    ...mapState('address', ['addresses'])
  },
  watch: {
    searchKey: function(newVal) {
      if (this.mode === 'province') {
        this.loadListLocation({
          searchKey: newVal
        })
      } else if (this.mode === 'district') {
        this.listDistricts = this.originalDistricts.filter(district => {
          return district.name.toLowerCase().includes(this.searchKey.toLowerCase())
        })
      } else if (this.mode === 'ward') {
        this.listWards = this.originalWards.filter(ward => {
          return ward.name.toLowerCase().includes(this.searchKey.toLowerCase())
        })
      }
    },
    addresses: function(newVal) {
      this.existedAddress = Object.assign([], this.existedAddress, newVal)
      if (this.existedAddress.length > 0) {
        this.selectedLocation = this.existedAddress[0].id
      }
    },
    location: function(newVal) {
      this.initData()
    }
  },
  created() {
    this.initData()

    // load existed address
    this.getListAddress()
  },
  methods: {
    ...mapActions('location', ['loadListLocation']),
    ...mapActions('address', ['getAddressByUserId']),
    initData() {
      if (Object.keys(this.location).length > 0) {
        this.localLocation = Object.assign({}, this.location)
        if (this.localLocation.province) {
          this.mode = 'province'
          this.originalDistricts = this.listDistrictByProvinceId(this.localLocation.province.id)
          this.listDistricts = [...this.originalDistricts]
        }
        if (this.localLocation.district) {
          this.mode = 'district'
          this.originalWards = this.listWardByDistrictId(this.localLocation.province.id, this.localLocation.district.id)
          this.listWards = [...this.originalWards]
        }
        if (this.localLocation.ward) {
          this.mode = 'ward'
        }
      } else {
        this.getList()
      }
    },
    getList() {
      this.loadListLocation({
        searchKey: this.searchKey
      })
    },
    getListAddress() {
      this.getAddressByUserId()
    },
    setLocation(province, district, ward) {
      this.searchKey = ''

      if (!province && !district && !ward) {
        this.getList()
        this.mode = 'province'
        return
      }

      if (province) {
        this.localLocation.province = province
        this.originalDistricts = this.listDistrictByProvinceId(this.localLocation.province.id)
        this.listDistricts = [...this.originalDistricts]
        this.mode = 'district'
      }
      if (district) {
        this.localLocation.district = district
        this.originalWards = this.listWardByDistrictId(this.localLocation.province.id, this.localLocation.district.id)
        this.listWards = [...this.originalWards]
        this.mode = 'ward'
      }
      if (ward) {
        this.localLocation.ward = ward
        this.$emit('submitFormLocation', this.localLocation)
      }
    },
    selectLocation(id) {
      this.selectedLocation = id
    },
    handleCreateSuccess(data) {
      this.addAddressDialogVisible = false
      this.getAddressByUserId()
      this.selectedLocation = data.id
    },
    handleCloseAddressModal() {
      this.addAddressDialogVisible = false
    },
    changeStep(action) {
      if (action === 'next') {
        if (!this.location.province || !this.location.district || !this.location.ward) {
          this.$message({
            message: 'Please select location for classified advertising.',
            type: 'error'
          })
          return
        }
      }
      this.$emit('submitFormLocation', this.localLocation)
    }
  }
}
</script>

<style lang="scss" scoped>
.step2 {
  margin: 0 20px;
  text-align: center;

  h3 {
    text-align: center;
    font-weight: bold;
  }

  .search-form {
    height: 100px;
    width: 50%;
    margin: 0 auto;
  }

  .location-result {
    width: 70%;
    margin: 0 auto;

    .el-breadcrumb {
      padding: 10px 40px;
      font-weight: 500;
    }

    ul{
      list-style: none;
      padding: 0;
    }
    li {
      background: #f5f7fa;
      color: #606266;
      padding: 10px;
      margin: 10px auto;
      position: relative;
      font-size: 1.2em;

      &:hover {
        background: #dee0e5;
        color: #409EFF;
        cursor: pointer;
      }

      &.selected {
        background: #dee0e5;
        color: #409EFF;
      }
    }
  }
  .choose-location{
    text-align: left;
    margin-top: 1rem;

    .el-radio-group {
      display: block;

      .el-col {
        padding: 0.5rem;
        margin-bottom: 0.25rem;
        margin-top: 0.25rem;
        line-height: 1rem;
        background: #f5f7fa;
        color: #606266;
        &:hover {
          background: #dee0e5;
          color: #409EFF;
          cursor: pointer;
        }

        &.selected {
          background: #dee0e5;
          color: #409EFF;
        }
      }

      .is-checked {
        font-weight: bold;
      }

      .el-radio {
        font-size: 1.2em;
      }
    }
  }
}
</style>
