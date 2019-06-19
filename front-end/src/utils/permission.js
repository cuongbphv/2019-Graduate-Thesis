function checkChildren(tmp, res, authorities) {
  if (tmp.children) {
    tmp.children = filterAsyncRoutes(tmp.children, authorities)
    if (tmp.children.length > 0) {
      return res.push(tmp)
    }
  }
}

export function filterAsyncRoutes(routes, authorities) {
  const res = []
  routes.forEach((route) => {
    const tmp = { ...route }
    if (!tmp.hidden) {
      if (tmp.meta) {
        if (tmp.meta.permission && authorities.includes(tmp.meta.permission)) {
          res.push(tmp)
        } else {
          checkChildren(tmp, res, authorities)
        }
      } else {
        checkChildren(tmp, res, authorities)
      }
    }
  })
  return res
}
