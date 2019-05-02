<template>
  <el-dialog :visible.sync="dialogVisible" :title="title">
    <el-form v-if="localMode.mode === 'province'" :model="province">
      <el-form-item label="Province Id">
        <el-input v-model="province.id" autocomplete="off" />
      </el-form-item>
      <el-form-item label="Province Type">
        <el-input v-model="province.type" autocomplete="off" />
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
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
      province: {},
      title: ''
    }
  },
  created() {
    this.dialogVisible = this.visible
    this.localMode = Object.assign({}, this.addMode)
    this.init()
  },
  methods: {
    init() {
      if (this.localMode.mode === 'province') {
        this.title = 'Add New Province'
      }
    }
  }
}
</script>
