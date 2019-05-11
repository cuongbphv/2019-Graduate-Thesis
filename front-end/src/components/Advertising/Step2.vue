<template>
  <div class="step2">
    <h3>Chọn khu vực tỉnh, thành phố</h3>
    <el-radio-group v-model="locationType" size="small">
      <el-radio :label="1" border>Choose New Location</el-radio>
      <el-radio :label="2" border>Choose Your Address</el-radio>
    </el-radio-group>

    <div v-if="locationType === 1">
      <div class="search-form">
        <el-form>
          <el-form-item prop="title">
            <md-input
              v-model="searchKey"
              icon="search"
              name="title"
              placeholder="Nhập khu vực tìm kiếm"
              @keyup.enter.native="getList"
            >Tìm kiếm</md-input>
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

    <!--    <div v-else-if="locationType === 2" />-->

    <!--    </div>-->

    <el-row class="center-padding-top">
      <el-button type="primary" @click="changeStep(step - 1)">Prev</el-button>
      <el-button type="success" @click="changeStep(step + 1)">Continue</el-button>
    </el-row>
  </div>
</template>

<script>
import MdInput from '@/components/MDinput'
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'Step2',
  components: {
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
      originalWards: []
    }
  },
  computed: {
    ...mapGetters('location', ['listLocation', 'listDistrictByProvinceId', 'listWardByDistrictId'])
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
    }
  },
  created() {
    this.localLocation = Object.assign({}, this.location)
    if (Object.keys(this.localLocation).length > 0) {
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
  methods: {
    ...mapActions('location', ['loadListLocation']),
    getList() {
      this.loadListLocation({
        searchKey: this.searchKey
      })
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
}
</style>
