<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :title="title"
    width="70%"
    top="3vh"
    @close="handleClose"
  >
    <el-form :model="category">
      <el-form-item :label="$t('label.name')">
        <el-input v-model="category.name" placeholder="Đồ điện tử, Đồ gia dụng" />
      </el-form-item>
      <el-form-item :label="$t('label.slug')">
        <el-input v-model="category.slug" placeholder="do-dien-tu, do-gia-dung" :disabled="true" />
      </el-form-item>
      <el-form-item :label="$t('label.description')">
        <el-input
          v-model="category.description"
          type="textarea"
          :rows="3"
          :placeholder="$t('place_holder.category_description')"
        />
      </el-form-item>
      <el-form-item :label="$t('label.image')">
        <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="uploadCategoryImage"
        >{{ $t('button.upload') }}</el-button>
      </el-form-item>
      <el-form-item>
        <img
          v-if="image != null"
          style="width: 100px; height: 100px"
          :src="image"
          alt="category_img"
        >
      </el-form-item>
      <hr>
      <el-form-item style="text-align: center; margin-top: 1rem">
        <el-button
          type="success"
          size="medium"
          icon="el-icon-save"
          @click="updateCategory"
        >{{ $t('button.save') }}</el-button>
      </el-form-item>
    </el-form>
    <image-cropper
      v-show="imagecropperShow"
      :key="imagecropperKey"
      :width="100"
      :height="100"
      :no-circle="true"
      url="/category/image"
      lang-type="vi"
      @close="closeImgCropper"
      @crop-upload-success="cropImgSuccess"
      @image-url="receiveImageUrl"
    />
  </el-dialog>
</template>
<script>
import ImageCropper from '@/components/ImageCropper'
import { toSlug } from '@/utils'
import category from '@/api/category'
import { Status } from '../../utils/constants'
import { showSuccess, showError } from '../../utils/message'
export default {
  name: 'CategoryDetailModal',
  components: {
    ImageCropper
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    categoryId: {
      type: String,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      category: {},
      title: this.$t('label.create_category'),
      imagecropperShow: false,
      imagecropperKey: 0,
      image: null
    }
  },
  watch: {
    'category.name': function(newVal) {
      if (newVal !== '') {
        this.category.slug = toSlug(newVal)
      }
    },
    visible: function(newVal) {
      this.dialogVisible = newVal
    },
    categoryId: function(newVal) {
      if (newVal !== '') {
        this.getCategory(newVal)
      } else {
        this.category = {}
      }
    }
  },
  created() {
  },
  methods: {
    getCategory(id) {
      category.getCategoryById(id).then(res => {
        if (res.status === Status.SUCCESS && res.data) {
          this.category = res.data
          this.image = res.data.image
        } else {
          showError('errors.category.' + res.status)
        }
      })
    },
    updateCategory() {
      category.updateCategory(this.category, this.category.id).then(res => {
        if (res.status === Status.SUCCESS) {
          showSuccess('message.update_category_success')
          this.$emit('createCategorySuccess')
        } else {
          showError('errors.category.' + res.status)
        }
      })
    },
    handleClose() {
      this.$emit('closeModal', '')
    },
    uploadCategoryImage() {
      this.imagecropperShow = true
    },
    closeImgCropper() {
      this.imagecropperShow = false
    },
    cropImgSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.image = resData
    },
    receiveImageUrl(url) {
      this.category.image = url
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
</style>
