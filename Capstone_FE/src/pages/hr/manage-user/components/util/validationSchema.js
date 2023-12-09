import * as Yup from "yup";

export const validationSchema = Yup.object({
  username: Yup.string()
    .matches(/^[a-zA-Z0-9_]+$/, 'Username should only contain letters, numbers, underscores')
    .matches(/[a-zA-Z]/, 'Username must contain at least one letter')
    .min(5, 'Username must be at least 5 characters long')
    .max(25, 'Username must be less than 25 characters')
    .required('Username is required'),
  role: Yup.string().required("Role is required"),
  department: Yup.string().required("Department is required"),
  // room: Yup.string().required("Room is required"),
});
