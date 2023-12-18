import { LoadingButton } from '@mui/lab'
import {
  Box,
  FormControl,
  InputLabel,
  MenuItem,
  Modal,
  Select,
  Stack,
  TextField,
  Typography,
  Checkbox
} from '@mui/material'
import { useFormik } from 'formik'
import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { BASE_URL } from '../../../../services/constraint'
import userApi from '../../../../services/userApi'
import axiosClient from '../../../../utils/axios-config'
import { validationSchema } from './util/validationSchema'
import { useSelector } from 'react-redux'
import { jwtDecode } from 'jwt-decode'
import { format } from 'date-fns'
import requestApi from '../../../../services/requestApi'
const CreateDevice = ({ handleCloseCreateDevice, openCreateDevice }) => {
  const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 2
  }
  const [isOpenRoom, setIsOpenRoom] = useState(false)
  const currentUser = useSelector((state) => state.auth.login?.currentUser)

  const decoded = jwtDecode(currentUser?.jwtToken)

  console.log(format(new Date(), 'yyyy-dd-MM HH:mm:ss'))
  const formik = useFormik({
    initialValues: {
      deviceLcdId: '',
      deviceName: '',
      deviceUrl: '',
      roomName: ''
    },
    validationSchema: validationSchema,
    onSubmit: async (values, { resetForm }) => {
      let data = {
        deviceLcdId: values.deviceLcdId,
        deviceName: values.deviceName,
        deviceUrl: values.deviceUrl,
        roomName: values.roomName
      }
      console.log(data)
      try {
        const res = await axiosClient.post(`${BASE_URL}/register`, data)

        toast.success('Create account succesfully!')
        resetForm()
        handleCloseCreateDevice()
      } catch (error) {
        if (error.response.status === 404) {
          toast.error('Role not found!')
        }
        if (error.response.status === 400) {
          toast.error('Username already exists!')
        }
        if (error.response.status === 409) {
          toast.error('Your department has manager already!')
        }
      }
    }
  })

  const handleChangeOpenRoom = (event) => {
    setIsOpenRoom(event.target.checked)
  }

  return (
    <Modal
      open={openCreateDevice}
      onClose={handleCloseCreateDevice}
      aria-labelledby="parent-modal-title"
      aria-describedby="parent-modal-description">
      <Box sx={{ ...style, width: 400 }}>
        <Typography fontSize="25px" fontWeight="800" mb={2}>
          Create Device
        </Typography>
        <form onSubmit={formik.handleSubmit}>
          <Stack mb={3}>
            <TextField
              fullWidth
              label="Lcd Id"
              name="lcdId"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.deviceLcdId}
              type="text"
            />
            {formik.touched.deviceLcdId && formik.errors.deviceLcdId && (
              <Typography sx={{ color: 'red' }} className="error-message">
                {formik.errors.deviceLcdId}
              </Typography>
            )}
          </Stack>

          <Stack mb={3}>
            <TextField
              fullWidth
              label="Device Name"
              name="deviceName"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.deviceName}
              type="text"
            />
            {formik.touched.deviceName && formik.errors.deviceName && (
              <Typography sx={{ color: 'red' }} className="error-message">
                {formik.errors.deviceName}
              </Typography>
            )}
          </Stack>

          <Stack mb={3}>
            <TextField
              fullWidth
              label="Device Url"
              name="deviceUrl"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.deviceUrl}
              type="text"
            />
            {formik.touched.deviceUrl && formik.errors.deviceUrl && (
              <Typography sx={{ color: 'red' }} className="error-message">
                {formik.errors.deviceUrl}
              </Typography>
            )}
          </Stack>
          <Stack mb={3}>
            <Checkbox checked={isOpenRoom} onChange={handleChangeOpenRoom} />
          </Stack>
          <Stack mb={3}>
            <TextField
              fullWidth
              label="Room Name"
              name="roomName"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.roomName}
              type="text"
            />
            {formik.touched.roomName && formik.errors.roomName && (
              <Typography sx={{ color: 'red' }} className="error-message">
                {formik.errors.roomName}
              </Typography>
            )}
          </Stack>

          <Box width="100%" display="flex" justifyContent="flex-end">
            <LoadingButton
              variant="contained"
              userApiC
              //   loading={isLoading}
              sx={{ bgcolor: 'rgb(94, 53, 177)' }}
              type="submit">
              Save
            </LoadingButton>
          </Box>
        </form>
      </Box>
    </Modal>
  )
}

export default CreateDevice
