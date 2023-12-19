import * as Yup from "yup";
export const validationSchema =  Yup.object({
      deviceLcdId: Yup.string().matches(/^[0-9]+$/, 'Lcd Id must be numberic').required("Lcd id is require!"),
      deviceName: Yup.string().required("Device name is require!"),
      deviceUrl: Yup.string().required("Device url is required"),
  });