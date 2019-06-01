<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="title"
    width="70%"
    top="3vh"
    @close="handleClose"
  >
    <el-form v-if="localMode.mode === 'province'">
      <el-form-item :label="$t('label.id')">
        <el-input v-model="province.id" placeholder="1, 23" />
      </el-form-item>
      <el-form-item :label="$t('label.name')">
        <el-input v-model="province.name" placeholder="Bình Dương, Hồ Chí Minh" @change="toSlug(province.name)" />
      </el-form-item>
      <el-form-item :label="$t('label.slug')">
        <el-input :value="toSlug(province.name)" :disabled="true" placeholder="binh-duong, ho-chi-minh" />
      </el-form-item>
      <el-form-item :label="$t('label.type')">
        <el-input v-model="province.type" placeholder="tinh, thanh-pho" />
      </el-form-item>
      <el-form-item :label="$t('label.name_with_type')">
        <el-input v-model="province.nameWithType" placeholder="Tỉnh Bình Dương, Thành phố Hồ Chí Minh" />
      </el-form-item>
      <el-collapse value="1">
        <el-collapse-item name="1">
          <template slot="title">
            <span>{{ $t('label.district_list') }}</span>
          </template>
          <el-form-item>
            <el-tooltip :content="$t('label.add_district')" effect="dark" placement="bottom">
              <el-button
                type="primary"
                size="small"
                icon="el-icon-plus"
                @click="addDistrictToProvince"
              >{{ $t('button.add') }}</el-button>
            </el-tooltip>
            <div v-for="(district, parentIndex) in province.districts" :key="district._hash" style="padding-left: 15px">
              <el-form-item :label="$t('label.id')">
                <el-input v-model="district.id" placeholder="789" />
              </el-form-item>
              <el-form-item :label="$t('label.name')">
                <el-input v-model="district.name" placeholder="Bến Cát" />
              </el-form-item>
              <el-form-item :label="$t('label.slug')">
                <el-input :value="toSlug(district.name)" :disabled="true" placeholder="ben-cat" />
              </el-form-item>
              <el-form-item :label="$t('label.type')">
                <el-input v-model="district.type" placeholder="huyen, thi-xa" />
              </el-form-item>
              <el-form-item :label="$t('label.name_with_type')">
                <el-input v-model="district.nameWithType" placeholder="Thị xã Bến Cát" />
              </el-form-item>
              <!-- wards -->
              <el-collapse value="2">
                <el-collapse-item name="2">
                  <template slot="title">
                    <span>{{ $t('label.ward_list') }}</span>
                  </template>
                  <el-form-item>
                    <el-tooltip :content="$t('label.add_ward')" effect="dark" placement="bottom">
                      <el-button
                        type="primary"
                        size="small"
                        icon="el-icon-plus"
                        @click="addWardToDistrict(parentIndex)"
                      >{{ $t('button.add') }}</el-button>
                    </el-tooltip>
                    <div v-for="(ward, index) in district.wards" :key="ward._hash" style="padding-left: 15px">
                      <el-form-item :label="$t('label.id')">
                        <el-input v-model="ward.id" placeholder="25840" />
                      </el-form-item>
                      <el-form-item :label="$t('label.name')">
                        <el-input v-model="ward.name" placeholder="An Điền" />
                      </el-form-item>
                      <el-form-item :label="$t('label.slug')">
                        <el-input :value="toSlug(ward.name)" :disabled="true" placeholder="an-dien" />
                      </el-form-item>
                      <el-form-item :label="$t('label.type')">
                        <el-input v-model="ward.type" placeholder="phuong, xa" />
                      </el-form-item>
                      <el-form-item :label="$t('label.name_with_type')">
                        <el-input v-model="ward.nameWithType" placeholder="Xã An Điền" />
                      </el-form-item>
                      <el-form-item style="text-align: right; margin-top: 5px">
                        <el-button
                          type="danger"
                          size="small"
                          icon="el-icon-delete"
                          @click="removeWardFromDistrict(parentIndex, index)"
                        >{{ $t('button.delete') }}</el-button>
                      </el-form-item>
                      <hr>
                    </div>
                  </el-form-item>
                </el-collapse-item>
              </el-collapse>
              <!-- end wards -->
              <el-form-item style="text-align: right; margin-top: 5px">
                <el-button
                  type="danger"
                  size="small"
                  icon="el-icon-delete"
                  @click="removeDistrictFromProvince(index)"
                >{{ $t('button.delete') }}</el-button>
              </el-form-item>
              <hr>
            </div>
          </el-form-item>
        </el-collapse-item>
      </el-collapse>
    </el-form>

    <el-form v-if="localMode.mode === 'district'">
      <el-collapse value="1">
        <el-collapse-item name="1">
          <template slot="title">
            <span>{{ $t('label.district_list') }}</span>
          </template>
          <el-form-item>
            <el-tooltip :content="$t('label.add_district')" effect="dark" placement="bottom">
              <el-button
                type="primary"
                size="small"
                icon="el-icon-plus"
                @click="addDistricts"
              >{{ $t('button.add') }}</el-button>
            </el-tooltip>
            <div v-for="(district, parentIndex) in districts" :key="district._hash" style="padding-left: 15px">
              <el-form-item :label="$t('label.id')">
                <el-input v-model="district.id" placeholder="789" />
              </el-form-item>
              <el-form-item :label="$t('label.name')">
                <el-input v-model="district.name" placeholder="Bến Cát" />
              </el-form-item>
              <el-form-item :label="$t('label.slug')">
                <el-input :value="toSlug(district.name)" :disabled="true" placeholder="ben-cat" />
              </el-form-item>
              <el-form-item :label="$t('label.type')">
                <el-input v-model="district.type" placeholder="huyen, thi-xa" />
              </el-form-item>
              <el-form-item :label="$t('label.name_with_type')">
                <el-input v-model="district.nameWithType" placeholder="Thị xã Bến Cát" />
              </el-form-item>
              <!-- wards -->
              <el-collapse value="2">
                <el-collapse-item name="2">
                  <template slot="title">
                    <span>{{ $t('label.ward_list') }}</span>
                  </template>
                  <el-form-item>
                    <el-tooltip :content="$t('label.add_ward')" effect="dark" placement="bottom">
                      <el-button
                        type="primary"
                        size="small"
                        icon="el-icon-plus"
                        @click="addWardToDistrict(parentIndex)"
                      >{{ $t('button.add') }}</el-button>
                    </el-tooltip>
                    <div v-for="(ward, index) in district.wards" :key="ward._hash" style="padding-left: 15px">
                      <el-form-item :label="$t('label.id')">
                        <el-input v-model="ward.id" placeholder="789" />
                      </el-form-item>
                      <el-form-item :label="$t('label.name')">
                        <el-input v-model="ward.name" placeholder="Bến Cát" />
                      </el-form-item>
                      <el-form-item :label="$t('label.slug')">
                        <el-input :value="toSlug(ward.name)" :disabled="true" placeholder="ben-cat" />
                      </el-form-item>
                      <el-form-item :label="$t('label.type')">
                        <el-input v-model="ward.type" placeholder="huyen, thi-xa" />
                      </el-form-item>
                      <el-form-item :label="$t('label.name_with_type')">
                        <el-input v-model="ward.nameWithType" placeholder="Thị xã Bến Cát" />
                      </el-form-item>
                      <el-form-item style="text-align: right; margin-top: 5px">
                        <el-button
                          type="danger"
                          size="small"
                          icon="el-icon-delete"
                          @click="removeWardFromDistrict(parentIndex, index)"
                        >{{ $t('button.delete') }}</el-button>
                      </el-form-item>
                      <hr>
                    </div>
                  </el-form-item>
                </el-collapse-item>
              </el-collapse>
              <!-- end wards -->
              <el-form-item style="text-align: right; margin-top: 5px">
                <el-button
                  type="danger"
                  size="small"
                  icon="el-icon-delete"
                  @click="removeDistrictFromProvince(index)"
                >{{ $t('button.delete') }}</el-button>
              </el-form-item>
              <hr>
            </div>
          </el-form-item>
        </el-collapse-item>
      </el-collapse>
    </el-form>

    <el-form v-if="localMode.mode === 'ward'">
      <el-collapse value="2">
        <el-collapse-item name="2">
          <template slot="title">
            <span>{{ $t('label.ward_list') }}</span>
          </template>
          <el-form-item>
            <el-tooltip :content="$t('label.add_ward')" effect="dark" placement="bottom">
              <el-button
                type="primary"
                size="small"
                icon="el-icon-plus"
                @click="addWardToDistrict()"
              >{{ $t('button.add') }}</el-button>
            </el-tooltip>
            <div v-for="(ward, index) in wards" :key="ward._hash" style="padding-left: 15px">
              <el-form-item :label="$t('label.id')">
                <el-input v-model="ward.id" placeholder="789" />
              </el-form-item>
              <el-form-item :label="$t('label.name')">
                <el-input v-model="ward.name" placeholder="Bến Cát" />
              </el-form-item>
              <el-form-item :label="$t('label.slug')">
                <el-input :value="toSlug(ward.name)" :disabled="true" placeholder="ben-cat" />
              </el-form-item>
              <el-form-item :label="$t('label.type')">
                <el-input v-model="ward.type" placeholder="huyen, thi-xa" />
              </el-form-item>
              <el-form-item :label="$t('label.name_with_type')">
                <el-input v-model="ward.nameWithType" placeholder="Thị xã Bến Cát" />
              </el-form-item>
              <el-form-item style="text-align: right; margin-top: 5px">
                <el-button
                  type="danger"
                  size="small"
                  icon="el-icon-delete"
                  @click="removeWardFromDistrict(index)"
                >{{ $t('button.delete') }}</el-button>
              </el-form-item>
              <hr>
            </div>
          </el-form-item>
        </el-collapse-item>
      </el-collapse>
    </el-form>

    <span slot="footer" class="dialog-footer">
      <el-button
        type="primary"
        @click="addNewLocation"
      >{{ $t('table.confirm') }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { mapActions } from 'vuex'
import { toSlug, uuidv4 } from '@/utils'
import { showMessageAfterCallAPI } from '@/utils/message'
export default {
  name: 'AddLocationModal',
  components: {
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    addMode: {
      type: Object,
      default: function() {
        return {
          mode: 'province',
          id: '00',
          name: 'Province'
        }
      }
    }
  },
  data() {
    return {
      dialogVisible: false,
      localMode: {},
      province: {
        id: '',
        name: '',
        slug: '',
        type: '',
        nameWithType: '',
        districts: []
      },
      districts: [], // add more districts
      wards: [], // add more wards
      title: ''
    }
  },
  watch: {
    visible: function(newVal) {
      this.dialogVisible = newVal
    },
    addMode: function(newVal) {
      this.localMode = Object.assign({}, newVal)
      if (this.localMode.mode === 'province') {
        this.title = this.$t('label.add_new_province')
      } else if (this.localMode.mode === 'district') {
        this.title = this.$t('label.add_new_district')
      } else if (this.localMode.mode === 'ward') {
        this.title = this.$t('label.add_new_ward')
      }
    }
  },
  created() {
  },
  methods: {
    ...mapActions('location', ['addNewProvince', 'addNewDistrict', 'addNewWard']),
    toSlug(name) {
      return toSlug(name)
    },
    handleClose() {
      this.$emit('closeModal', '')
    },
    addDistrictToProvince() {
      this.province.districts.push({
        _hash: uuidv4(),
        id: '',
        name: '',
        type: '',
        slug: '',
        nameWithType: '',
        wards: []
      })
    },
    addDistricts() {
      this.districts.push({
        _hash: uuidv4(),
        id: '',
        name: '',
        type: '',
        slug: '',
        nameWithType: '',
        wards: []
      })
    },
    removeDistrictFromProvince(index) {
      this.province.districts.splice(index, 1)
    },
    addWardToDistrict(index) {
      if (this.localMode.mode === 'province') {
        this.province.districts[index].wards.push({
          _hash: uuidv4(),
          id: '',
          name: '',
          type: '',
          slug: '',
          nameWithType: ''
        })
      } else if (this.localMode.mode === 'district') {
        this.districts[index].wards.push({
          _hash: uuidv4(),
          id: '',
          name: '',
          type: '',
          slug: '',
          nameWithType: ''
        })
      } else if (this.localMode.mode === 'ward') {
        this.wards.push({
          _hash: uuidv4(),
          id: '',
          name: '',
          type: '',
          slug: '',
          nameWithType: ''
        })
      }
    },
    removeWardFromDistrict(parentIndex, index) {
      if (this.localMode.mode === 'province') {
        this.province.districts[parentIndex].wards.splice(index, 1)
      } else if (this.localMode.mode === 'district') {
        this.districts[parentIndex].wards.splice(index, 1)
      } else if (this.localMode.mode === 'ward') {
        this.wards.splice(parentIndex, 1)
      }
    },
    addNewLocation() {
      if (this.localMode.mode === 'province') {
        this.addNewProvince(this.province).then(res => {
          showMessageAfterCallAPI(res, 'message.location.add_province_success')
        })
      } else if (this.localMode.mode === 'district') {
        this.addNewDistrict(this.districts)
      } else if (this.localMode.mode === 'ward') {
        this.addNewWard(this.wards)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
  /deep/ .el-form-item {
    margin-bottom: 0;
  }
  hr {
    margin-bottom: 0;
  }
  /deep/ .el-collapse-item__header {
    span {
      font-size: 14px;
      color: #606266;
      font-weight: 700;
    }
  }
</style>
