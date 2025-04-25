import { message, notification } from 'antd';

//Thông báo thành công 
export const notifySuccess = (msg = 'Thành công', desc ='',notificationApi, options = {}) => {
  notificationApi.open({
    message: msg,
    description: desc,
    showProgress: options.showProgress || false,
    pauseOnHover: options.pauseOnHover || true,
    ...options,
  });
}

//Thông báo thất bại
export const notifyError = (msg = 'Lỗi', desc ='',notificationApi,  options = {}) => {
  notificationApi.open({
      message: msg,
      description: desc,
      showProgress: options.showProgress || false,
      pauseOnHover: options.pauseOnHover || true,
      ...options,
    });
}

//Thông báo thành công với message
export const notifySuccessWithCustomMessage = (msg = 'Thành công ☺️', messageApi) => {
  messageApi ? messageApi.success(msg) : message.success(msg);
}

export const notifyErrorWithCustomMessage = (msg = 'Đã xảy ra lỗi 😢', messageApi) => {
  messageApi ? messageApi.error(msg) : message.error(msg);
}
//Xử lý thông báo dựa trên status code
export const handleHttpStatusCode = (statusCode, successMsg, errorMsg, messageApi) => {
    switch (statusCode) {
        case 200:
        case 201:
          notifySuccessWithCustomMessage(successMsg || 'Thành công ☺️', messageApi);
          break;
        case 400:
          notifyErrorWithCustomMessage(errorMsg || 'Yêu cầu không hợp lệ 😢', messageApi);
          break;
        case 401:
          notifyErrorWithCustomMessage(errorMsg || 'Không có quyền truy cập', messageApi);
          break;
        case 403:
          notifyErrorWithCustomMessage(errorMsg || 'Forbidden', messageApi);
          break;
        case 404:
          notifyErrorWithCustomMessage(errorMsg || 'Not Found', messageApi);
          break;
        case 500:
          notifyErrorWithCustomMessage(errorMsg || 'Internal Server Error', messageApi);
          break;
        default:
          notifyErrorWithCustomMessage(errorMsg || 'Có gì đó sai sai 🫠', messageApi);
    }
}