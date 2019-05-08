<template>
  <div class="step3">
    <h3>Tải lên hình ảnh về sản phẩm</h3>

    <el-upload
      class="upload-form"
      drag
      action=""
      :on-change="handleOnChange"
      :on-remove="handleRemove"
      :file-list="fileList"
      :auto-upload="false"
      list-type="picture-card"
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
  </div>
</template>

<script>
export default {
  name: 'Step3',
  props: {
    images: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      fileList: []
    }
  },
  created() {
    if (this.images.keepData) {
      this.images.data.forEach(f => {
        this.handleOnChange(f.raw)
      })
    }
  },
  methods: {
    handleRemove(file, fileList) {
      const index = this.fileList.findIndex(f => f.uid === file.uid)
      this.fileList.splice(index, 1)
      this.$emit('submitFormImage', this.fileList)
    },
    handleOnChange(file) {
      this.fileList.push(file)
      this.$emit('submitFormImage', this.fileList)
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
  }
}
</style>
