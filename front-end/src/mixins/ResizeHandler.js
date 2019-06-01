import { mapActions } from 'vuex'

const { body } = document
const WIDTH = 992 // refer to Bootstrap's responsive design

export default {
  watch: {
    $route(route) {
      if (this.device === 'mobile' && this.sidebar.opened) {
        this.closeSideBar({ withoutAnimation: false })
      }
    }
  },
  beforeMount() {
    window.addEventListener('resize', this.resizeHandler)
  },
  mounted() {
    const isMobile = this.isMobile()
    if (isMobile) {
      this.toggleDevice('mobile')
      this.closeSideBar({ withoutAnimation: true })
    }
  },
  methods: {
    ...mapActions('layout', ['toggleDevice', 'closeSideBar']),
    isMobile() {
      const rect = body.getBoundingClientRect()
      return rect.width - 1 < WIDTH
    },
    resizeHandler() {
      if (!document.hidden) {
        const isMobile = this.isMobile()
        this.toggleDevice(isMobile ? 'mobile' : 'desktop')
        if (isMobile) {
          this.closeSideBar({ withoutAnimation: true })
        }
      }
    }
  }
}
