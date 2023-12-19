import * as Yup from "yup";
export const validationSchema = Yup.object({
    oldPassword: Yup.string()
        .required('Required'),
    newPassword: Yup.string()
        .required('Required').matches(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{6,}$/,
            'Password must contain at least one lowercase letter, one uppercase letter, one number, and one special character, and be at least 6 characters long'
        ),
    confirmPassword: Yup.string()
        .oneOf([Yup.ref('newPassword'), null], 'Passwords must match')

})
