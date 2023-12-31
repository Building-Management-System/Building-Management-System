import { LoadingButton } from '@mui/lab'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
import Grid from '@mui/material/Grid'
import Stack from '@mui/material/Stack'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import { useState } from 'react'
import authApi from '../../../services/authApi'
import { Link, useNavigate } from 'react-router-dom'
import BG from '../../../assets/images/bg-auth.jpg'
import logoImage from '../../../assets/images/vite.jpg'
import Avatar from '@mui/material/Avatar'
const ResetPassword = () => {
  const [username, setUsername] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()
  const handleSubmit = (e) => {
    setIsLoading(true)
    e.preventDefault()
    let data = {
      username: username
    }
    authApi.resetPassword(data, navigate)
    setIsLoading(false)
  }
  return (
    <>
      <Box
        sx={{
          display: 'flex',
          flex: '1 1 auto',
          height: '100vh'
        }}>
        <Grid container sx={{ flex: '1 1 auto' }}>
          <Grid
            xs={12}
            lg={5}
            sx={{
              backgroundColor: 'background.paper',
              display: 'flex',
              flexDirection: 'column',
              position: 'relative'
            }}
            item>
            <Box
              sx={{
                backgroundColor: 'background.paper',
                flex: '1 1 auto',
                alignItems: 'center',
                display: 'flex',
                justifyContent: 'center'
              }}>
              <Box
                sx={{
                  maxWidth: 550,
                  px: 3,
                  py: '100px',
                  width: '100%'
                }}>
                <div>
                  <Stack spacing={1} sx={{ mb: 3 }} alignItems="center">
                    <Avatar
                      alt="BMS Logo"
                      src={logoImage}
                      sx={{
                        width: 40,
                        height: 40,
                        marginRight: 1,
                        borderRadius: '0%'
                      }}
                    />
                    <Typography sx={{ fontSize: '30px', fontWeight: '700' }}>
                      Reset Password
                    </Typography>
                  </Stack>
                  <form noValidate onSubmit={handleSubmit}>
                    <Stack spacing={3}>
                      <TextField
                        fullWidth
                        label="Username"
                        name="username"
                        type="username"
                        onChange={(e) => setUsername(e.target.value)}
                      />
                    </Stack>
                    <LoadingButton
                      fullWidth
                      loading={isLoading}
                      size="large"
                      sx={{
                        mt: 2,
                        bgcolor: 'rgb(99, 102, 241)',
                        p: '11px 24px',
                        borderRadius: '12px'
                      }}
                      type="submit"
                      variant="contained">
                      Submit
                    </LoadingButton>
                    <Button
                      component={Link}
                      to={`/login`}
                      color="primary"
                      fullWidth
                      sx={{ mt: 2 }}>
                      Back to login
                    </Button>
                  </form>
                </div>
              </Box>
            </Box>
          </Grid>
          <Grid
            item
            xs={12}
            lg={7}
            sx={{
              backgroundImage: `url(${BG})`,
              backgroundRepeat: 'no-repeat',
              backgroundSize: 'contained',
              backgroundPosition: 'center',
              backgroundColor: '#f5f7f9'
            }}
          />
        </Grid>
      </Box>
    </>
  )
}

export default ResetPassword
