import * as Yup from "yup";
export const validationSchema =  Yup.object({
      deviceLcdId: Yup.string().required("Lcd id is require!"),
      deviceName: Yup.string().required("Device name is require!"),
      deviceUrl: Yup.string().required("Device url is required"),
      roomName: Yup.string().required("Room name is required"),
  });