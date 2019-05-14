<template>
  <div class="step3">
    <h3>Tải lên hình ảnh về sản phẩm</h3>

    <el-upload
      class="upload-form"
      drag
      :action="uploadModel.url"
      :on-change="handleOnChange"
      :headers="uploadModel.headers"
      :on-remove="handleRemove"
      :on-success="handleOnSuccess"
      :file-list="fileList"
      list-type="picture-card"
      accept="image/*"
      multiple
    >
      <i class="el-icon-upload" />
      <div class="el-upload__text">Drop file here or <em>click to upload</em></div>
      <div v-if="fileList.length === 0" slot="tip" class="el-upload__tip">(*) Bạn cần đăng ít nhất một hình về sản phẩm</div>
    </el-upload>

    <div class="el-alert--warning">
      <p><b>Để bán nhanh hơn:</b></p>
      <ul>
        <li>Xoay camera khổ ngang để hình hiển thị đẹp hơn</li>
        <li>Chụp từ nhiều góc của sản phẩm (mặt trước, sau, bên)</li>
      </ul>
      <p><b>Không nên</b></p>
      <ul>
        <li>Sử dụng hình ảnh trùng lặp hoặc lấy từ Internet</li>
        <li> Chèn số điện thoại/email/logo vào hình</li>
      </ul>
    </div>

    <el-row class="center-padding-top">
      <el-button type="primary" @click="changeStep('back')">{{ $t('button.previous') }}</el-button>
      <el-button type="success" @click="changeStep('next')">{{ $t('button.continue') }}</el-button>
    </el-row>
  </div>
</template>

<script>
import { API, Status } from '@/utils/constants'
import { getToken } from '@/utils/auth'
import { mapActions } from 'vuex'

export default {
  name: 'Step3',
  props: {
    images: {
      type: Array,
      default: () => {
        return []
      }
    }
  },
  data() {
    return {
      fileList: [],
      uploadModel: {
        url: process.env.VUE_APP_BASE_URL + API.UPLOAD_TEMP_IMAGE,
        headers: {
          'Authorization': 'Bearer ' + getToken()
        }
      }
    }
  },
  created() {
    if (this.images.length > 0) {
      this.fileList = this.images
    }
  },
  methods: {
    ...mapActions('advertising', ['removeTempImage']),
    handleOnChange() {

    },
    handleRemove(file, fileList) {
      const fileName = file.url.substring(file.url.lastIndexOf('/') + 1)
      this.removeTempImage({
        fileName: fileName
      }).then(res => {
        if (res.status === Status.SUCCESS) {
          const index = this.fileList.findIndex(f => f.uid === file.uid)
          this.fileList.splice(index, 1)
          this.$emit('submitFormImage', this.fileList)
          this.$message({
            message: 'Delete file successfully.',
            type: 'success'
          })
        }
      })
    },
    handleOnSuccess(res, file, fileList) {
      if (res.status === Status.SUCCESS) {
        file.url = res.data
        this.fileList.push(file)
        this.$emit('submitFormImage', this.fileList)
      }
    },
    changeStep(action) {
      if (action === 'next') {
        if (this.fileList.length === 0) {
          this.$message({
            message: 'Please upload less an image for classified advertising.',
            type: 'error'
          })
          return
        }
      }
      this.$emit('changeStep', action)
    }
  }
}
</script>

<style lang="scss" scoped>
.step3 {
  margin: 0 20px;
  text-align: left;

  h3 {
    text-align: center;
    font-weight: bold;
  }

  /deep/ .el-upload-list--picture-card {
    .el-upload-list__item {
      width: 180px;
      height: 180px;
    }
  }

  @media screen and (max-width: 494px) {
    /deep/ .el-upload-dragger {
      width: 180px !important;
    }
  }

  .upload-form {
    /deep/ .el-upload--picture-card {
      border: none;
      height: auto;
      line-height: 50px;
      background: none;
    }

    .el-upload__tip {
      color: red;
      font-size: 14px;
    }
  }

  .el-alert--warning {
    margin-top: 10px;
    padding: 20px;
    border-radius: 10px;
    background-color: #fdf6ec;
    color: #E6A23C;
  }
}
</style>
