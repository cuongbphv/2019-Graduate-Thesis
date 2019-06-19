const date = {
  formatDate(date) {
    const d = new Date(date)
    const compareDate = new Date(date).setHours(0, 0, 0, 0)
    if (compareDate !== new Date().setHours(0, 0, 0, 0)) {
      let month = '' + (d.getMonth() + 1)
      let day = '' + d.getDate()
      const year = d.getFullYear()

      if (month.length < 2) month = '0' + month
      if (day.length < 2) day = '0' + day

      return [day, month, year].join('/')
    } else {
      let hours = '' + d.getHours()
      let minutes = '' + d.getMinutes()

      if (hours.length < 2) hours = '0' + hours
      if (minutes.length < 2) minutes = '0' + minutes

      return [hours, minutes].join(':')
    }
  },
  formatTime(date) {
    const d = new Date(date)
    let hours = d.getHours()
    let minutes = '' + d.getMinutes()

    if (hours.length < 2) hours = '0' + hours
    if (minutes.length < 2) minutes = '0' + minutes

    return [hours, minutes].join(':')
  }
}
export default date
