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
  Typography
} from '@mui/material'
import { useFormik } from 'formik'
import { useEffect, useState } from 'react'
import { toast } from 'react-toastify'
import { BASE_URL } from '../../../../services/constraint'
import userApi from '../../../../services/userApi'
import axiosClient from '../../../../utils/axios-config'
import { validationSchema } from './util/validationSchema'
import { useSelector } from 'react-redux'
import { jwtDecode } from "jwt-decode";
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

  const [listDepartment, setListDepartment] = useState([])
  const [listRooms, setListRooms] = useState([])


  const currentUser = useSelector((state) => state.auth.login?.currentUser);


  useEffect(() => {
    const getAllDepartment = async () => {
      let res = await userApi.getAllDepartment()
      setListDepartment(res)
    }
    getAllDepartment()
  }, [])

  useEffect(() => {
    const fetchAllRooms = async () => {
      const res = await requestApi.getAllRoom()
      const updateRooms = res.filter((item) => {
        return item.roomName &&  item.roomName.startsWith('Tech')
      })
      setListRooms(updateRooms)
    }
    fetchAllRooms()
  }, [])

  const decoded = jwtDecode(currentUser?.jwtToken);

  console.log(format(new Date(), 'yyyy-dd-MM HH:mm:ss'));
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
        username: values.username,
        password: '123',
        role: values.role,
        departmentName: values.department,
        hrId: currentUser?.accountId,
        roomId: values.room === '' ? '' : values.room
      }
      console.log(data)
      try {
        const res = await axiosClient.post(`${BASE_URL}/register`, data)
        
        toast.success('Create account succesfully!')
        resetForm();
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


  return (
    <Modal
      open={openCreateDevice}
      onClose={handleCloseCreateDevice}
      aria-labelledby="parent-modal-title"
      aria-describedby="parent-modal-description">
      <Box sx={{ ...style, width: 400 }}>
        <Typography fontSize="25px" fontWeight="800" mb={2}>
          Create Account
        </Typography>
        <form onSubmit={formik.handleSubmit}>
          <Stack mb={3}>
            <TextField
              fullWidth
              label="Lcd Id"
              name="lcdId"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.username}
              type="lcdId"
            />
            {formik.touched.username && formik.errors.username && (
              <Typography sx={{color: 'red'}} className="error-message">{formik.errors.username}</Typography>
            )}
          </Stack>

          <Stack mb={3}>
            <TextField
              fullWidth
              label="Username"
              name="username"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.username}
              type="username"
            />
            {formik.touched.username && formik.errors.username && (
              <Typography sx={{color: 'red'}} className="error-message">{formik.errors.username}</Typography>
            )}
          </Stack>

          <Stack mb={3}>
            <TextField
              fullWidth
              label="Username"
              name="username"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.username}
              type="username"
            />
            {formik.touched.username && formik.errors.username && (
              <Typography sx={{color: 'red'}} className="error-message">{formik.errors.username}</Typography>
            )}
          </Stack>

          <Stack mb={3}>
            <TextField
              fullWidth
              label="Username"
              name="username"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.username}
              type="username"
            />
            {formik.touched.username && formik.errors.username && (
              <Typography sx={{color: 'red'}} className="error-message">{formik.errors.username}</Typography>
            )}
          </Stack>

  

          

        

          <Box width="100%" display="flex" justifyContent="flex-end">
            <LoadingButton
              variant="contained"userApiC
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
