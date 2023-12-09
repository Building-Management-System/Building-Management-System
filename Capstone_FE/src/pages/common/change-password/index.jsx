import { LoadingButton } from '@mui/lab'
import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Divider,
  Grid,
  TextField,
  Typography
} from '@mui/material'
import { useFormik } from 'formik'
import { useDispatch, useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import Header from '../../../components/Header'
import userApi from '../../../services/userApi'
import { validationSchema } from './util/validationSchema'

const AdminChanagePassword = () => {
  const isLoading = useSelector((state) => state.user.changePassword?.isFetching)
  const accountId = useSelector((state) => state.auth.login?.currentUser?.accountId)
  const navigate = useNavigate()
  // console.log(accountId)
  const dispatch = useDispatch()

  const formik = useFormik({
    initialValues: {
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    },
    validationSchema: validationSchema,
    onSubmit: async (values) => {
      let data = {
        accountId: accountId,
        oldPassword: values.oldPassword,
        newPassword: values.newPassword
      }
      userApi.changePassword(data, dispatch)
    }
  })
  return (
    <Box height="100vh" bgcolor="seashell">
      <Box
        className="App"
        sx={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)'
          ,width:'1000px',
          height:'500px'
        }}>
        <Grid container>
          <Grid item xs={12}>
            <form onSubmit={formik.handleSubmit} autoComplete="off" noValidate>
              <Card>
                <CardContent>
                  <Header title="Change Password" subtitle="Update Password" />
                  <Box sx={{ mb: 1 }}>
                    <Grid item container spacing={3}>
                      <Grid item xs={7}>
                        <TextField
                          fullWidth
                          label="Old Password"
                          type="password"
                          onChange={formik.handleChange}
                          onBlur={formik.handleBlur}
                          name="oldPassword"
                          value={formik.values.oldPassword}
                          required
                        />
                        {formik.touched.oldPassword && formik.errors.oldPassword ? (
                          <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                          {formik.errors.oldPassword}
                        </Typography>
                        ) : null}
                      </Grid>
                      <Grid item xs={7}>
                        <TextField
                          fullWidth
                          label="New Password"
                          type="password"
                          onChange={formik.handleChange}
                          onBlur={formik.handleBlur}
                          value={formik.values.newPassword}
                          name="newPassword"
                          required
                        />
                        {formik.touched.newPassword && formik.errors.newPassword ? (
                          <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                          {formik.errors.newPassword}
                        </Typography>
                        ) : null}
                      </Grid>
                      <Grid item xs={7}>
                        <TextField
                          fullWidth
                          label="Confirm New Password"
                          type="password"
                          name="confirmPassword"
                          required
                          value={formik.values.confirmPassword}
                          onBlur={formik.handleBlur}
                          onChange={formik.handleChange}
                        />
                        {formik.touched.confirmPassword && formik.errors.confirmPassword ? (
                          
                          <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                          {formik.errors.confirmPassword}
                        </Typography>
                        ) : null}
                      </Grid>
                    </Grid>
                  </Box>
                </CardContent>
                <Divider />
                <CardActions sx={{ justifyContent: 'space-between', py: '8px' }}>
                  <Button
                    variant="contained"
                    onClick={() => navigate(-1)}
                    sx={{ bgcolor: 'rgb(100, 149, 237)' }}>
                    Back to Dashboard
                  </Button>
                  <LoadingButton
                    type="submit"
                    loading={isLoading}
                    variant="contained"
                    sx={{ bgcolor: 'rgb(94, 53, 177)' }}>
                    Save
                  </LoadingButton>
                </CardActions>
              </Card>
            </form>
          </Grid>
        </Grid>
      </Box>
    </Box>
  )
}

export default AdminChanagePassword
