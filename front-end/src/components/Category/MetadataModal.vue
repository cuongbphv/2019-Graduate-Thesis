<template>
  <el-dialog
    ref="dialog"
    :visible.sync="dialogVisible"
    :title="mode === 'metadata'?$t('label.metadata_details'):$t('label.filter_details')"
    width="70%"
    top="3vh"
    @close="handleClose"
  >
    <el-form label-position="left" label-width="120px">
      <el-tooltip
        :content="mode === 'metadata'?$t('label.add_new_metadata'):$t('label.add_new_filter')"
        style="margin-bottom: 1rem"
        effect="dark"
        placement="bottom"
      >
        <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="addMetadata"
        >{{ $t('button.add') }}</el-button>
      </el-tooltip>
      <div v-for="(subMeta, parentIndex) in metadataModel" :key="subMeta._hash" style="padding: 15px">
        <el-form-item :label="$t('label.name')">
          <el-input v-model="subMeta.label" />
        </el-form-item>
        <!--<el-form-item :label="$t('label.slug')">-->
        <!--<el-input :value="(subMeta.slug)" />-->
        <!--</el-form-item>-->
        <el-form-item :label="$t('label.type')">
          <el-select v-model="subMeta.type" :placeholder="$t('label.select')">
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('label.selection_type')">
          <el-radio-group v-model="subMeta.selectionType">
            <el-radio
              v-for="item in selectionTypeOptions"
              :key="item.value"
              :label="item.value"
            >{{ item.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('label.options')">
          <el-tooltip :content="$t('label.add_new_options')" style="margin-bottom: 1rem" effect="dark" placement="bottom">
            <el-button
              type="primary"
              size="small"
              icon="el-icon-plus"
              @click="addOption(parentIndex)"
            >{{ $t('button.add') }}</el-button>
          </el-tooltip>
          <div v-for="(subOption, subIndex) in subMeta.options" :key="subOption._hash" style="padding: 15px">
            <el-row>
              <el-col :md="11" :lg="11">
                <el-form-item :label="$t('label.name')">
                  <el-input v-model="subOption.label" />
                </el-form-item>
              </el-col>
              <el-col :md="{span: 11, offset: 2}" :lg="{span: 11, offset: 2}">
                <el-form-item :label="$t('label.value')">
                  <el-input v-if="subMeta.type === 'text'" v-model="subOption.value" />
                  <el-color-picker v-if="subMeta.type === 'color'" v-model="subOption.value" />
                  <!--<el-input v-if="subMeta.type === 'image'" v-model="(subOption.value)" />-->
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item style="text-align: right; margin-top: 5px">
              <el-button
                type="danger"
                size="small"
                icon="el-icon-delete"
                @click="removeOption(subIndex, parentIndex)"
              >{{ $t('button.delete') }}</el-button>
            </el-form-item>
            <hr style="margin-bottom: 1rem">
          </div>
        </el-form-item>
        <el-form-item style="text-align: right; margin-top: 5px">
          <el-button
            type="danger"
            size="small"
            icon="el-icon-delete"
            @click="removeMetadata(parentIndex)"
          >{{ $t('button.delete') }}</el-button>
        </el-form-item>
        <hr style="margin-bottom: 1rem">
      </div>
      <el-form-item style="text-align: center; margin-top: 1rem">
        <el-button
          type="success"
          size="medium"
          icon="el-icon-save"
          @click="save(mode)"
        >{{ $t('button.save') }}</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { uuidv4, toSlug } from '@/utils'
import category from '@/api/category'
import { Status } from '../../utils/constants'
import { showSuccess, showError } from '../../utils/message'
export default {
  name: 'MetadataModal',
  components: {},
  props: {
    mode: {
      type: String,
      default: 'metadata'
    },
    visible: {
      type: Boolean,
      default: false
    },
    categoryId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      dialogVisible: false,
      title: '',
      metadataModel: [],
      typeOptions: [
        {
          value: 'color',
          label: this.$t('label.color')
        },
        {
          value: 'text',
          label: this.$t('label.text')
        }
        // {
        //   value: 'image',
        //   label: this.$t('label.image')
        // },
        // {
        //   value: 'price',
        //   label: this.$t('label.price')
        // }
      ],
      selectionTypeOptions: [
        {
          value: 'single',
          label: this.$t('label.single_choice')
        },
        {
          value: 'multiple',
          label: this.$t('label.multi_choice')
        }
      ]
    }
  },
  watch: {
    visible: function(newVal) {
      this.dialogVisible = newVal
    },
    // metadata: function(newVal) {
    //   this.metadataModel = Object.assign({}, newVal)
    //   console.log('model', this.metadataModel)
    // }
    categoryId: function(newVal) {
      if (newVal !== '') {
        this.mode === 'metadata' ? this.getMetadata(newVal) : this.getFilter(newVal)
      } else {
        this.metadataModel = []
      }
    }
  },
  created() {
  },
  methods: {
    getMetadata(id) {
      category.getMetadata(id).then(res => {
        if (res.status === 200 && res.data) {
          this.metadataModel = res.data
        }
      })
    },
    getFilter(id) {
      category.getFilter(id).then(res => {
        if (res.status === 200 && res.data) {
          this.metadataModel = res.data
        }
      })
    },
    handleClose() {
      this.$emit('closeModal', '')
    },
    save(mode) {
      this.metadataModel.forEach(elem => {
        elem.slug = toSlug(elem.label)
      })
      if (mode === 'metadata') {
        category.updateMetadata(this.metadataModel, this.categoryId).then(res => {
          if (res.status === Status.SUCCESS) {
            showSuccess('message.update_metadata_success')
            this.$emit('updateMetadataSuccess', res.data)
            this.handleClose()
          } else {
            showError('errors.metadata.' + res.status)
          }
        })
      } else {
        category.updateFilter(this.metadataModel, this.categoryId).then(res => {
          if (res.status === Status.SUCCESS) {
            showSuccess('message.update_filter_success')
            this.$emit('updateFilterSuccess', res.data)
            this.handleClose()
          } else {
            showError('errors.metadata.' + res.status)
          }
        })
      }
    },
    addMetadata() {
      this.metadataModel.push({
        _hash: uuidv4(),
        label: '',
        slug: '',
        selectionType: '',
        type: '',
        options: []
      })
    },
    removeMetadata(index) {
      this.metadataModel.splice(index, 1)
    },
    addOption(parentIndex) {
      this.metadataModel[parentIndex].options.push({
        _hash: uuidv4(),
        label: '',
        value: ''
      })
    },
    removeOption(index, parentIndex) {
      this.metadataModel[parentIndex].options.splice(index, 1)
    }
  }
}
</script>

<style lang="scss" scoped>
  /deep/ .el-form-item {
    margin-bottom: 1rem;
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
  .el-input{
    /*width: 90%;*/
  }
</style>
