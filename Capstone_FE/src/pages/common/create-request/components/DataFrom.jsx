import ClassicEditor from '@ckeditor/ckeditor5-build-classic'
import { CKEditor } from '@ckeditor/ckeditor5-react'
import {
  Box,
  Button,
  Checkbox,
  FormControlLabel,
  Grid,
  MenuItem,
  Select,
  TextField,
  Typography
} from '@mui/material'
import { DatePicker, LocalizationProvider, TimePicker } from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import dayjs from 'dayjs'
import { useFormik } from 'formik'
import { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import requestApi from '../../../../services/requestApi'
import { validationSchema } from '../util/validationSchema'
import overtimeApi from '../../../../services/overtimeApi'
import { toast } from 'react-toastify'

ClassicEditor.defaultConfig = {
  toolbar: {
    items: ['heading', '|', 'bold', '|', 'bulletedList', 'numberedList']
  },
  language: 'en'
}
const AttendenceFrom = ({ userId }) => {
  const [from, setFrom] = useState(dayjs(new Date()))
  const [to, setTo] = useState(dayjs(new Date()))
  const [date, setDate] = useState(dayjs(new Date()))
  const [receiveIdAndDepartment, setReceiveIdAndDepartment] = useState('')
  const [isFrom, setIsFrom] = useState(true)
  const [isTo, setIsTo] = useState(true)
  const currentDate = new Date()
  const navigate = useNavigate()
  // const lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
  const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
  useEffect(() => {
    const fetchReceiveIdAndDepartment = async () => {
      const response = await requestApi.getReceiveIdAndDepartment(userId)
      setReceiveIdAndDepartment(response)
    }
    fetchReceiveIdAndDepartment()
  }, [])

  const handleChangeFrom = (event) => {
    setIsFrom(event.target.checked)
  }

  const handleChangeTo = (event) => {
    setIsTo(event.target.checked)
  }
  const formik = useFormik({
    initialValues: {
      title: '',
      content: ''
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      try {
        const manualDate = date.format('YYYY-MM-DD');
        let showDateWarning = false;
        let data = {
          userId: userId,
          title: values.title,
          content: values.content,
          manualDate: manualDate,
          manualFirstEntry: isFrom ? from.format('HH:mm:ss') : null,
          manualLastExit: isTo ? to.format('HH:mm:ss') : null,
          departmentId: receiveIdAndDepartment?.managerInfoResponse?.managerDepartmentId,
          receivedId: receiveIdAndDepartment?.managerInfoResponse?.managerId
        }
        if (dayjs(manualDate).month() !== currentDate.getMonth() || dayjs(manualDate).year() !== currentDate.getFullYear()) {
          showDateWarning = true;
        }

        if (showDateWarning) {
          toast.warning("Dates are only allowed from the beginning of the month to the current date.");
          return;
        }
        if (!isFrom && !isTo) {
          toast.warning("The 'From' and 'To' cannot be null at the same time.")
          return
        }
        console.log(data)
        requestApi.requestAttendanceForm(data, navigate)
      } catch (error) {
        toast.warning('Error!')
      }
    }
  })
  return (
    <Box p={3} pl={0}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography fontWeight="700" fontSize="18px">
              Request details
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Title</Typography>
            <TextField
              name="title"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.title}
              sx={{ width: '100%' }}
              size="small"
              placeholder="Enter the request title"
            />
            {formik.touched.title && formik.errors.title ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.title}
              </Typography>
            ) : null}
          </Grid>
          <Grid item xs={4} mb={2}>
            <Typography fontWeight="500">Date</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                value={date}
                onChange={(date) => {
                  setDate(date)
                }}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
                minDate={firstDayOfMonth}
                maxDate={currentDate}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={4} mb={2}>
            <Box display="flex" gap="5px">
              <Typography fontWeight="500">From</Typography>
              <Checkbox sx={{ p: 0 }} checked={isFrom} onChange={handleChangeFrom} />
            </Box>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                disabled={isFrom ? false : true}
                value={from}
                onChange={(e) => setFrom(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
              />
            </LocalizationProvider>
          </Grid>

          <Grid item xs={4} mb={2}>
            <Box display="flex" gap="5px">
              <Typography fontWeight="500">To</Typography>
              <Checkbox sx={{ p: 0 }} checked={isTo} onChange={handleChangeTo} />
            </Box>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                disabled={isTo ? false : true}
                value={to}
                onChange={(e) => setTo(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Content</Typography>
            <CKEditor
              editor={ClassicEditor}
              data={formik.values.content}
              onChange={(event, editor) => {
                const data = editor.getData()
                formik.setFieldValue('content', data)
              }}
            />
            {formik.touched.content && formik.errors.content ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.content}
              </Typography>
            ) : null}
          </Grid>
        </Grid>
        <Box pt={2} display="flex" alignItems="flex-end" justifyContent="space-between">
          <Button
            variant="contained"
            onClick={() => navigate(-1)}
            sx={{ bgcolor: 'rgb(100, 149, 237)' }}>
            Back to Dashboard
          </Button>
          <Button type="submit" variant="contained">
            Save
          </Button>
        </Box>
      </form>
    </Box>
  )
}

const OtFrom = () => {
  const [from, setFrom] = useState(dayjs(new Date()))
  const [to, setTo] = useState(dayjs(new Date()))
  const [date, setDate] = useState(dayjs(new Date()))
  const [topicOvertime, settopicOvertime] = useState('WEEKEND_AND_NORMAL_DAY')
  const [overtimeSystem, setOvertimeSystem] = useState({})
  const [receiveIdAndDepartment, setReceiveIdAndDepartment] = useState('')
  const currentDate = new Date()
  // const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
  const userId = useSelector((state) => state.auth.login?.currentUser?.accountId)
  const navigate = useNavigate()
  const handleChange = (event) => {
    settopicOvertime(event.target.value)
  }
  useEffect(() => {
    const fetchReceiveIdAndDepartment = async () => {
      const response = await requestApi.getReceiveIdAndDepartment(userId)
      setReceiveIdAndDepartment(response)
    }
    fetchReceiveIdAndDepartment()
  }, [])
  useEffect(() => {
    const fetchOvertimeSystem = async () => {
      const response = await overtimeApi.getOvertimeSystem(userId, date.format('YYYY-MM-DD'))
      setOvertimeSystem(response)
    }
    fetchOvertimeSystem()
  }, [date])
  console.log(overtimeSystem)
  const formik = useFormik({
    initialValues: {
      title: '',
      content: ''
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      try {

        const manualDate = date.format('YYYY-MM-DD');
        let showDateWarning = false;
        let data = {
          userId: userId,
          title: values.title,
          content: values.content,
          topicOvertime: topicOvertime,
          overtimeDate: date.format('YYYY-MM-DD'),
          fromTime: from.format('HH:mm:ss'),
          toTime: to.format('HH:mm:ss'),
          departmentId: receiveIdAndDepartment?.managerInfoResponse?.managerDepartmentId,
          receivedId: receiveIdAndDepartment?.managerInfoResponse?.managerId
        }
        if (dayjs(manualDate).month() < currentDate.getMonth() || dayjs(manualDate).year() < currentDate.getFullYear()) {
          showDateWarning = true;
        }
        if (showDateWarning) {
          toast.warning("Dates are only allowed from the beginning of the month to the current date.");
          return;
        }
        console.log(data)
        requestApi.requestOverTimeForm(data, navigate)
      } catch (error) {
        toast.warning('Error!!')
      }
    }
  })
  return (
    <Box p={3} pl={0}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography fontWeight="700" fontSize="18px">
              Request details
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Title</Typography>
            <TextField
              name="title"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.title}
              sx={{ width: '100%' }}
              size="small"
              placeholder="Enter the request title"
            />
            {formik.touched.title && formik.errors.title ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.title}
              </Typography>
            ) : null}
          </Grid>
          <Grid item xs={12}>
            <Select
              value={topicOvertime}
              sx={{ width: '100%' }}
              onChange={handleChange}
              displayEmpty>
              <MenuItem value="WEEKEND_AND_NORMAL_DAY">WEEKEND AND NORMAL DAY</MenuItem>
              <MenuItem value="HOLIDAY">HOLIDAY</MenuItem>
            </Select>
          </Grid>
          {overtimeSystem?.systemCheckin === null ? (
            <Grid item xs={6} mb={2}>
              <Typography fontWeight="500">System Check In</Typography>
              <TextField sx={{ width: '100%' }} disabled value="0:00" />
            </Grid>
          ) : (
            <Grid item xs={6} mb={2}>
              <Typography fontWeight="500">System Check In</Typography>
              <TextField sx={{ width: '100%' }} disabled value={overtimeSystem?.systemCheckin} />
            </Grid>
          )}

          {overtimeSystem?.systemCheckout === null ? (
            <Grid item xs={6} mb={2}>
              <Typography fontWeight="500">System Check Out</Typography>
              <TextField sx={{ width: '100%' }} disabled value="0:00" />
            </Grid>
          ) : (
            <Grid item xs={6} mb={2}>
              <Typography fontWeight="500">System Check Out</Typography>
              <TextField sx={{ width: '100%' }} disabled value={overtimeSystem?.systemCheckout} />
            </Grid>
          )}

          <Grid item xs={4} mb={2}>
            <Typography fontWeight="500">Date</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                value={date}
                onChange={(e) => setDate(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
               
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={4} mb={2}>
            <Typography fontWeight="500">From</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                value={from}
                onChange={(e) => setFrom(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={4} mb={2}>
            <Typography fontWeight="500">To</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <TimePicker
                value={to}
                onChange={(e) => setTo(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Reason</Typography>
            <CKEditor
              editor={ClassicEditor}
              data={formik.values.content}
              onChange={(event, editor) => {
                const data = editor.getData()
                formik.setFieldValue('content', data)
              }}
            />
            {formik.touched.content && formik.errors.content ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.content}
              </Typography>
            ) : null}
          </Grid>
        </Grid>
        <Box pt={2} display="flex" alignItems="flex-end" justifyContent="space-between">
          <Button
            variant="contained"
            onClick={() => navigate(-1)}
            sx={{ bgcolor: 'rgb(100, 149, 237)' }}>
            Back to Dashboard
          </Button>
          <Button type="submit" variant="contained">
            Save
          </Button>
        </Box>
      </form>
    </Box>
  )
}

const OtherRequest = ({ userId }) => {
  const [title, setTitle] = useState('')
  const [content, setContent] = useState('')
  const currentUser = useSelector((state) => state.auth.login?.currentUser)
  const [receiveIdAndDepartment, setReceiveIdAndDepartment] = useState('')
  const [role, setRole] = useState('')
  const [department, setDepartment] = useState()
  const [getAllManagerDepartment, setGetAllManagerDepartment] = useState([])
  const [manager, setManager] = useState('')
  const navigate = useNavigate()
  const handleChange = (event) => {
    setRole(event.target.value)
  }
  const handleChangeDepartment = (event) => {
    setDepartment(event.target.value)
  }

  useEffect(() => {
    const fetchReceiveIdAndDepartment = async () => {
      const response = await requestApi.getReceiveIdAndDepartment(userId)
      setReceiveIdAndDepartment(response)
      console.log(response)
    }
    fetchReceiveIdAndDepartment()
  }, [])

  useEffect(() => {
    const fetchAllManagerDepartment = async () => {
      const response = await requestApi.getAllManagerDepartment()
      setGetAllManagerDepartment(response)
    }
    fetchAllManagerDepartment()
  }, [])
  const callApiOther = (e, departmentId) => {
    e.preventDefault()
    let data = {
      userId: userId,
      title: title,
      content: content,
      departmentId: departmentId
    }
    console.log(data)
    setTitle('')
    setContent('')
    setDepartment('')
    requestApi.requestOtherForm(data, navigate)
  }

  const callApiToManager = (e, departmentId) => {
    e.preventDefault()
    let data = {
      userId: userId,
      title: title,
      content: content,
      departmentId: departmentId,
      receivedId: manager[0].accountId
    }
    console.log(data)
    setTitle('')
    setContent('')
    setDepartment('')
    requestApi.requestOtherForm(data)
  }

  const callApiEmployee = (e, managerId) => {
    e.preventDefault()
    let data = {
      userId: userId,
      title: title,
      content: content,
      departmentId: receiveIdAndDepartment?.managerInfoResponse?.managerDepartmentId,
      receivedId: managerId
    }
    setTitle('')
    setContent('')
    requestApi.requestOtherForm(data)
  }
  console.log(department)
  const handleCreateRequest = async (e) => {
    e.preventDefault()

    try {
      if (currentUser?.role === 'employee' && role === 'manager') {
        await callApiEmployee(e, receiveIdAndDepartment?.managerInfoResponse?.managerId)
      } else if (currentUser?.role === 'employee' && role === 'hr') {
        await callApiOther(e, 3)
      } else if (currentUser?.role === 'employee' && role === 'security') {
        await callApiOther(e, 10)
      } else if (currentUser?.role === 'employee' && role === 'admin') {
        await callApiOther(e, 9)
      } else if (currentUser?.role === 'manager' && role === 'admin') {
        await callApiOther(e, 9)
      } else if (currentUser?.role === 'manager' && role === 'security') {
        await callApiOther(e, 10)
      } else if (currentUser?.role === 'manager' && role === 'hr') {
        await callApiOther(e, 3)
      } else if (currentUser?.role === 'hr' && role === 'admin') {
        await callApiOther(e, 9)
      } else if (currentUser?.role === 'hr' && role === 'security') {
        await callApiOther(e, 10)
      } else if (currentUser?.role === 'hr' && role === 'manager') {
        await callApiToManager(e, department)
      } else if (currentUser?.role === 'security' && role === 'admin') {
        await callApiOther(e, 9)
      } else if (currentUser?.role === 'security' && role === 'hr') {
        await callApiOther(e, 3)
      } else if (currentUser?.role === 'security' && role === 'manager') {
        await callApiToManager(e, department)
      } else if (currentUser?.role === 'admin' && role === 'security') {
        await callApiOther(e, 10)
      } else if (currentUser?.role === 'admin' && role === 'hr') {
        await callApiOther(e, 3)
      } else if (currentUser?.role === 'admin' && role === 'manager') {
        await callApiToManager(e, department)
      }
      setTimeout(() => {
        navigate(-1)
      }, 800)
    } catch (error) {
      // Handle errors if needed
    }
  }

  useEffect(() => {
    if (getAllManagerDepartment.length !== 0) {
      const getManagerByDepartment = async () => {
        let res = await requestApi.getManagerByDepartment(department)
        setManager(res)
      }
      getManagerByDepartment()
    }
  }, [department])

  const handleDepartment = () => {
    if (currentUser?.role === 'admin' && role === 'manager') {
      return (
        <>
          <Typography mt={2} fontWeight="500">
            Department
          </Typography>
          <Select
            value={department}
            sx={{ width: '100%' }}
            onChange={handleChangeDepartment}
            displayEmpty>
            {getAllManagerDepartment.map((item) => (
              <MenuItem key={item.departmentId} value={item.departmentId}>
                {item.departmentName}{' '}
              </MenuItem>
            ))}
          </Select>
        </>
      )
    } else if (currentUser?.role === 'hr' && role === 'manager') {
      return (
        <>
          <Typography mt={2} fontWeight="500">
            Department
          </Typography>
          <Select
            value={department}
            sx={{ width: '100%' }}
            onChange={handleChangeDepartment}
            displayEmpty>
            {getAllManagerDepartment.map((item) => (
              <MenuItem key={item.departmentId} value={item.departmentId}>
                {item.departmentName}
              </MenuItem>
            ))}
          </Select>
        </>
      )
    } else if (currentUser?.role === 'security' && role === 'manager') {
      return (
        <>
          <Typography mt={2} fontWeight="500">
            Department
          </Typography>
          <Select
            value={department}
            sx={{ width: '100%' }}
            onChange={handleChangeDepartment}
            displayEmpty>
            {getAllManagerDepartment.map((item) => (
              <MenuItem key={item.departmentId} value={item.departmentId}>
                {item.departmentName}{' '}
              </MenuItem>
            ))}
          </Select>
        </>
      )
    }
  }

  return (
    <Box p={3} pl={0}>
      <form onSubmit={handleCreateRequest}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography fontWeight="700" fontSize="18px">
              Request details{' '}
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Title</Typography>
            <TextField
              onChange={(e) => setTitle(e.target.value)}
              value={title}
              sx={{ width: '100%' }}
              size="small"
              placeholder="Enter the request title"
            />
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Position</Typography>
            {currentUser?.role === 'employee' ? (
              <Select value={role} sx={{ width: '100%' }} onChange={handleChange} displayEmpty>
                <MenuItem value="admin">Admin</MenuItem>
                <MenuItem value="manager">Manager</MenuItem>
                <MenuItem value="hr">HR</MenuItem>
                <MenuItem value="security">Security</MenuItem>
              </Select>
            ) : currentUser?.role === 'hr' ? (
              <Select value={role} sx={{ width: '100%' }} onChange={handleChange} displayEmpty>
                <MenuItem value="admin">Admin</MenuItem>
                <MenuItem value="manager">Manager</MenuItem>
                <MenuItem value="security">Security</MenuItem>
              </Select>
            ) : currentUser?.role === 'admin' ? (
              <Select value={role} sx={{ width: '100%' }} onChange={handleChange} displayEmpty>
                <MenuItem value="manager">Manager</MenuItem>
                <MenuItem value="hr">HR</MenuItem>
                <MenuItem value="security">Security</MenuItem>
              </Select>
            ) : currentUser?.role === 'manager' ? (
              <Select value={role} sx={{ width: '100%' }} onChange={handleChange} displayEmpty>
                <MenuItem value="admin">Admin</MenuItem>
                <MenuItem value="hr">HR</MenuItem>
                <MenuItem value="security">Security</MenuItem>
              </Select>
            ) : currentUser?.role === 'security' ? (
              <Select value={role} sx={{ width: '100%' }} onChange={handleChange} displayEmpty>
                <MenuItem value="admin">Admin</MenuItem>
                <MenuItem value="manager">Manager</MenuItem>
                <MenuItem value="hr">HR</MenuItem>
              </Select>
            ) : (
              <></>
            )}

            {handleDepartment()}
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Content</Typography>
            <CKEditor
              data={content}
              editor={ClassicEditor}
              onChange={(event, editor) => {
                const data = editor.getData()
                setContent(data)
              }}
            />
          </Grid>
        </Grid>
        <Box pt={2} display="flex" alignItems="flex-end" justifyContent="space-between">
          <Button
            variant="contained"
            onClick={() => navigate(-1)}
            sx={{ bgcolor: 'rgb(100, 149, 237)' }}>
            Back to Dashboard
          </Button>
          <Button type="submit" variant="contained">
            Save
          </Button>
        </Box>
      </form>
    </Box>
  )
}

const LateRequest = () => {
  const [date, setDate] = useState(dayjs(new Date()))
  const [lateType, setLateType] = useState('LATE_MORNING')
  const [lateDuration, setLateDuration] = useState('')
  const [receiveIdAndDepartment, setReceiveIdAndDepartment] = useState('')
  const currentDate = new Date()
  const lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0)
  const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
  const userId = useSelector((state) => state.auth.login?.currentUser?.accountId)
  const navigate = useNavigate()
  const handleChange = (event) => {
    setLateType(event.target.value)
  }
  useEffect(() => {
    const fetchReceiveIdAndDepartment = async () => {
      const response = await requestApi.getReceiveIdAndDepartment(userId)
      setReceiveIdAndDepartment(response)
    }
    fetchReceiveIdAndDepartment()
  }, [])

  const formik = useFormik({
    initialValues: {
      title: '',
      content: '',
      lateType: '',
      lateDuration: ''
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      try {
        const manualDate = date.format('YYYY-MM-DD');
        let showDateWarning = false;
        let data = {
          userId: userId,
          title: values.title,
          content: values.content,
          lateType: lateType,
          lateDuration: lateDuration,
          requestDate: date.format('YYYY-MM-DD'),
          departmentId: receiveIdAndDepartment?.managerInfoResponse?.managerDepartmentId,
          receivedId: receiveIdAndDepartment?.managerInfoResponse?.managerId
        }
        if (dayjs(manualDate).month() !== currentDate.getMonth() || dayjs(manualDate).year() !== currentDate.getFullYear()) {
          showDateWarning = true;
        }
        if (showDateWarning) {
          toast.warning("Dates are only allowed in current month.");
          return;
        }

        console.log(data)
        requestApi.requestLateForm(data, navigate)
      } catch (error) {
        console.log(error);
      }
    }
  })

  return (
    <Box p={3} pl={0}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography fontWeight="700" fontSize="18px">
              Request details
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Title</Typography>
            <TextField
              name="title"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.title}
              sx={{ width: '100%' }}
              size="small"
              placeholder="Enter the request title"
            />
            {formik.touched.title && formik.errors.title ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.title}
              </Typography>
            ) : null}
          </Grid>
          <Grid item xs={12}>
            Type
            <Select value={lateType} sx={{ width: '100%' }} onChange={handleChange} displayEmpty>
              <MenuItem value="LATE_MORNING">LATE CHECK IN</MenuItem>
              <MenuItem value="EARLY_AFTERNOON">EARLY CHECK OUT</MenuItem>
            </Select>
          </Grid>
          <Grid item xs={4} mb={2}>
            <Typography fontWeight="500">Date</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                value={date}
                onChange={(e) => setDate(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
                minDate={firstDayOfMonth}
                maxDate={lastDayOfMonth}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={4} mb={2}>
            <Typography fontWeight="500">Duration (minutes)</Typography>
            <TextField
              value={lateDuration}
              onChange={(e) => {
                const inputValue = parseInt(e.target.value, 10)
                if (inputValue > 90) {
                  setLateDuration(90)
                } else if (inputValue < 0) {
                  setLateDuration(0)
                } else {
                  setLateDuration(inputValue)
                }
              }}
              sx={{ width: '100%' }}
              type="number"
            />
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Reason</Typography>
            <CKEditor
              editor={ClassicEditor}
              data={formik.values.content}
              onChange={(event, editor) => {
                const data = editor.getData()
                formik.setFieldValue('content', data)
              }}
            />
            {formik.touched.content && formik.errors.content ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.content}
              </Typography>
            ) : null}
          </Grid>
        </Grid>
        <Box pt={2} display="flex" alignItems="flex-end" justifyContent="space-between">
          <Button
            variant="contained"
            onClick={() => navigate(-1)}
            sx={{ bgcolor: 'rgb(100, 149, 237)' }}>
            Back to Dashboard
          </Button>
          <Button type="submit" variant="contained">
            Save
          </Button>
        </Box>
      </form>
    </Box>
  )
}
const LeaveRequest = ({ userId }) => {
  const [dateFrom, setDateFrom] = useState(dayjs(new Date()))
  const [dateTo, setDateTo] = useState(dayjs(new Date()))
  const [checked, setChecked] = useState(false)
  const [receiveIdAndDepartment, setReceiveIdAndDepartment] = useState('')
  const currentDate = new Date()
  const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
  const navigate = useNavigate()
  const handleChangeHalfDay = (event) => {
    setChecked(event.target.checked)
  }
  useEffect(() => {
    const fetchReceiveIdAndDepartment = async () => {
      const response = await requestApi.getReceiveIdAndDepartment(userId)
      setReceiveIdAndDepartment(response)
    }
    fetchReceiveIdAndDepartment()
  }, [])
  const formik = useFormik({
    initialValues: {
      title: '',
      durationEvaluation: 0
    },
    validationSchema: validationSchema,
    onSubmit: (values) => {
      try {
        let data
        const currentYear = currentDate.getFullYear();
        const currentMonth = currentDate.getMonth() + 1; 
        const fromYear = dateFrom.year();
        const fromMonth = dateFrom.month() + 1; 
        const toYear = dateTo.year();
        const toMonth = dateTo.month() + 1; 
        if (fromYear < currentYear || (fromYear === currentYear && fromMonth < currentMonth)) {
          toast.warning('Invalid start month. Please select a valid month.');
          setDateFrom(dayjs(new Date()));
          setDateTo(dayjs(new Date()));
          return;
        }
        if (toYear < currentYear || (toYear === currentYear && toMonth < currentMonth)) {
          toast.warning('Invalid start month. Please select a valid month.');
          setDateFrom(dayjs(new Date()));
          setDateTo(dayjs(new Date()));
          return;
        }
        if (dateFrom.format('YYYY-MM-DD') > dateTo.format('YYYY-MM-DD')) {
          toast.warning('Date form must later than date to.');
          return;
        }
        if (dateFrom.format('YYYY-MM-DD') === dateTo.format('YYYY-MM-DD')) {
          data = {
            userId: userId,
            title: values.title,
            content: values.content,
            fromDate: dateFrom.format('YYYY-MM-DD'),
            toDate: dateTo.format('YYYY-MM-DD'),
            halfDay: checked,
            durationEvaluation: values.durationEvaluation,
            departmentId: receiveIdAndDepartment?.managerInfoResponse?.managerDepartmentId,
            receivedId: receiveIdAndDepartment?.managerInfoResponse?.managerId
          }
        } else {
          data = {
            userId: userId,
            title: values.title,
            content: values.content,
            fromDate: dateFrom.format('YYYY-MM-DD'),
            toDate: dateTo.format('YYYY-MM-DD'),
            halfDay: false,
            durationEvaluation: 0,
            departmentId: receiveIdAndDepartment?.managerInfoResponse?.managerDepartmentId,
            receivedId: receiveIdAndDepartment?.managerInfoResponse?.managerId
          }
        }
 
        console.log(data)
        requestApi.requestLeaveForm(data, navigate)
      } catch (error) {
        toast.warning('Error!!')
      }
    }
  })

  // console.log(dateFrom.format('YYYY-MM-DD'))
  // console.log(dateTo.format('YYYY-MM-DD'))
  return (
    <Box p={3} pl={0}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography fontWeight="700" fontSize="20px">
              Leave Request
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Title</Typography>
            <TextField
              name="title"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.title}
              sx={{ width: '100%' }}
              size="small"
              placeholder="Enter the request title"
            />
            {formik.touched.title && formik.errors.title ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.title}
              </Typography>
            ) : null}
          </Grid>

          <Grid item xs={6} mb={2}>
            <Typography fontWeight="500">From</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                value={dateFrom}
                minDate={firstDayOfMonth}
                onChange={(e) => setDateFrom(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={6} mb={2}>
            <Typography fontWeight="500">To</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                value={dateTo}
                minDate={firstDayOfMonth}
                onChange={(e) => setDateTo(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
              />
            </LocalizationProvider>
          </Grid>
          {dateFrom.format('YYYY-MM-DD') === dateTo.format('YYYY-MM-DD') && (
            <>
              <Grid sx={{ display: 'flex', alignItems: 'center', gap: '10px' }} item xs={6}>
                <Typography fontWeight="500">Duration Evaluation (h)</Typography>
                <TextField
                  name="durationEvaluation"
                  onChange={(e) => {
                    const inputValue = parseInt(e.target.value, 10);
                    let newValue;
                    if (inputValue > 8) {
                      newValue = 8;
                    } else if (inputValue < 1) {
                      newValue = 1;
                    } else {
                      newValue = inputValue;
                    }
                    formik.handleChange({
                      target: {
                        name: 'durationEvaluation',
                        value: newValue,
                      },
                    });
                  }}
                  onBlur={formik.handleBlur}
                  value={checked ? 4 : formik.values.durationEvaluation}
                  sx={{ width: '60%' }}
                  size="small"
                  type="number"
                  disabled={checked}
                />
                {formik.touched.durationEvaluation && formik.errors.durationEvaluation && (
                  <div className="error-message">{formik.errors.durationEvaluation}</div>
                )}
              </Grid>
              <Grid sx={{ display: 'flex', alignItems: 'center' }} item xs={12}>
                <Typography fontWeight="500">Half Day</Typography>
                <Checkbox
                  sx={{ padding: '0 0 0 5px' }}
                  checked={checked}
                  onChange={handleChangeHalfDay}
                />
              </Grid>
            </>
          )}
          <Grid item xs={12}>
            <Typography fontWeight="500">Content</Typography>
            <CKEditor
              editor={ClassicEditor}
              data={formik.values.content}
              onChange={(event, editor) => {
                const data = editor.getData()
                formik.setFieldValue('content', data)
              }}
            />
            {formik.touched.content && formik.errors.content ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.content}
              </Typography>
            ) : null}
          </Grid>
        </Grid>
        <Box pt={2} display="flex" alignItems="flex-end" justifyContent="space-between">
          <Button
            variant="contained"
            onClick={() => navigate(-1)}
            sx={{ bgcolor: 'rgb(100, 149, 237)' }}>
            Back to Dashboard
          </Button>
          <Button type="submit" variant="contained">
            Save
          </Button>
        </Box>
      </form>
    </Box>
  )
}
const WorkingOutSideRequest = () => {
  const [date, setDate] = useState(dayjs(new Date()))
  const [outSideType, setOutSideType] = useState('HALF_MORNING')
  const [receiveIdAndDepartment, setReceiveIdAndDepartment] = useState('')
  const [isChecked, setIsChecked] = useState(true)
  const userId = useSelector((state) => state.auth.login?.currentUser?.accountId)
  const navigate = useNavigate()
  const currentDate = new Date()
  const lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0)
  const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1)
  const handleCheckboxChange = (event) => {
    setIsChecked(event.target.checked)
    if (!event.target.checked) {
      setOutSideType('ALL_DAY')
    }
  }
  const handleChange = (event) => {
    setOutSideType(event.target.value)
  }
  useEffect(() => {
    const fetchReceiveIdAndDepartment = async () => {
      const response = await requestApi.getReceiveIdAndDepartment(userId)
      setReceiveIdAndDepartment(response)
    }
    fetchReceiveIdAndDepartment()
  }, [])

  const formik = useFormik({
    initialValues: {
      title: '',
      content: '',
      type: ''
    },
    validationSchema: validationSchema,
    onSubmit: async (values) => {
      try {
        const manualDate = date.format('YYYY-MM-DD');
        let showDateWarning = false;
        let data = {
          userId: userId,
          title: values.title,
          content: values.content,
          type: outSideType,
          date: manualDate,
          departmentId: receiveIdAndDepartment?.managerInfoResponse?.managerDepartmentId,
          receivedId: receiveIdAndDepartment?.managerInfoResponse?.managerId
        }
        if (dayjs(manualDate).month() !== currentDate.getMonth() || dayjs(manualDate).year() !== currentDate.getFullYear()) {
          showDateWarning = true;
        }
        if (showDateWarning) {
          toast.warning("Dates are only allowed in current month.");
          return;
        }
        console.log(data)
        await requestApi.requestOutSideWorkForm(data, navigate)
      } catch (error) {
        toast.warning('Error!!')
      }
    }
  })
  return (
    <Box p={3} pl={0}>
      <form onSubmit={formik.handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12}>
            <Typography fontWeight="700" fontSize="18px">
              Request details
            </Typography>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Title</Typography>
            <TextField
              name="title"
              onChange={formik.handleChange}
              onBlur={formik.handleBlur}
              value={formik.values.title}
              sx={{ width: '100%' }}
              size="small"
              placeholder="Enter the request title"
            />
            {formik.touched.title && formik.errors.title ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.title}
              </Typography>
            ) : null}
          </Grid>
          <Grid item xs={3} mb={2}>
            <Typography fontWeight="500">Date</Typography>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                value={date}
                onChange={(e) => setDate(e)}
                renderInput={(props) => <TextField sx={{ width: '100%' }} {...props} />}
                minDate={firstDayOfMonth}
                maxDate={lastDayOfMonth}
              />
            </LocalizationProvider>
          </Grid>
          <Grid item xs={12} mt={-3}>
            <FormControlLabel
              control={<Checkbox checked={isChecked} onChange={handleCheckboxChange} />}
              label="Half Day"
            />
          </Grid>
          <Grid item xs={12}>
            Type
            <Select
              value={isChecked ? outSideType : 'ALL_DAY'}
              sx={{ width: '100%' }}
              onChange={handleChange}
              disabled={!isChecked}>
              <MenuItem value="HALF_MORNING">MORNING</MenuItem>
              <MenuItem value="HALF_AFTERNOON">AFTERNOON</MenuItem>
            </Select>
          </Grid>
          <Grid item xs={12}>
            <Typography fontWeight="500">Reason</Typography>
            <CKEditor
              editor={ClassicEditor}
              data={formik.values.content}
              onChange={(event, editor) => {
                const data = editor.getData()
                formik.setFieldValue('content', data)
              }}
            />
            {formik.touched.content && formik.errors.content ? (
              <Typography sx={{ color: 'red', textAlign: 'left', fontSize: '15px' }}>
                {formik.errors.content}
              </Typography>
            ) : null}
          </Grid>
        </Grid>
        <Box pt={2} display="flex" alignItems="flex-end" justifyContent="space-between">
          <Button
            variant="contained"
            onClick={() => navigate(-1)}
            sx={{ bgcolor: 'rgb(100, 149, 237)' }}>
            Back to Dashboard
          </Button>
          <Button type="submit" variant="contained">
            Save
          </Button>
        </Box>
      </form>
    </Box>
  )
}
export { AttendenceFrom, LeaveRequest, OtFrom, OtherRequest, LateRequest, WorkingOutSideRequest }