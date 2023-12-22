import VisibilityIcon from '@mui/icons-material/Visibility'
import VisibilityOffIcon from '@mui/icons-material/VisibilityOff'
import { LoadingButton } from '@mui/lab'
import { IconButton,FormControlLabel,Checkbox } from '@mui/material'
import Box from '@mui/material/Box'
import Grid from '@mui/material/Grid'
import Stack from '@mui/material/Stack'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import Avatar from '@mui/material/Avatar'
import { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import authApi from '../../../services/authApi'
import BG from '../../../assets/images/bg-auth.png'
import logoImage from '../../../assets/images/vite.jpg'
export default function Login() {
  // const [open, setOpen] = useState(false);
  const [showPassword, setShowPassword] = useState(false)
  const isLoading = useSelector((state) => state.auth.login?.isFetching)
  const [rememberMe, setRememberMe] = useState(false);
  console.log(isLoading)
  const handleClickShowPassword = () => {
    setShowPassword(!showPassword)
  }
  const navigate = useNavigate()
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const dispatch = useDispatch()
  const handleSubmit = (e) => {
    e.preventDefault()
    let data = {
      username: username,
      password: password
    }
    authApi.loginUser(data, dispatch, navigate)
    if (rememberMe) {
      localStorage.setItem('rememberedUsername', username);
    } else {
      localStorage.removeItem('rememberedUsername');
    }
  }

  useEffect(() => {
    const rememberedUsername = localStorage.getItem('rememberedUsername');
    if (rememberedUsername) {
      setUsername(rememberedUsername);
      setRememberMe(true);
    }
  }, []);
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
                    <Typography sx={{ fontSize: '30px', fontWeight: '700', textAlign: 'center' }}>
                      Sign In
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
                      <TextField
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type={showPassword ? 'text' : 'password'}
                        id="password"
                        onChange={(e) => setPassword(e.target.value)}
                        InputProps={{
                          endAdornment: (
                            <IconButton
                              aria-label="toggle password visibility"
                              edge="end"
                              onClick={handleClickShowPassword}
                              size="large">
                              {showPassword ? <VisibilityIcon /> : <VisibilityOffIcon />}
                            </IconButton>
                          )
                        }}
                      />
                    </Stack>
                    <Stack direction="row" justifyContent="space-between" mt={1}>
                      <FormControlLabel
                        control={
                          <Checkbox
                            checked={rememberMe}
                            onChange={() => setRememberMe(!rememberMe)}
                            color="primary"
                          />
                        }
                        label="Remember Me"
                      />
                      <Typography
                        variant="body1"
                        component="span"
                        onClick={() => {
                          navigate('/reset-password')
                        }}
                        style={{
                          marginTop: '10px',
                          cursor: 'pointer',
                          color: 'rgb(99, 102, 241)'
                        }}>
                        Forgot password?
                      </Typography>
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
