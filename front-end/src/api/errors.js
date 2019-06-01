const Errors = {
  '403': 'Forbidden',
  '404': 'Not Found',
  '500': 'Internal Server Error',
  '503': 'Service Unavailable'
}

export const getErrorMessage = (errorCode) => Errors[errorCode] || `An error occurs: ${errorCode}`
export default Errors
