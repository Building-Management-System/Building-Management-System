import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import securityApi from '../../../services/securityApi'
import {
  Autocomplete,
  Box,
  Button,
  Checkbox,
  Divider,
  MenuItem,
  Modal,
  Select,
  TextField,
  Typography,
  FormControl,
  InputLabel
} from '@mui/material'
import ChatTopbar from '../../common/chat/components/ChatTopbar'
import DataTableDeviceDetail from './component/DataTable'
import formatDate, { formatDateNotTime } from '../../../utils/formatDate'
import { toast } from 'react-toastify'
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import { BASE_URL } from '../../../services/constraint'
import axiosClient from '../../../utils/axios-config'
import { useSelector } from 'react-redux'
import { format } from 'date-fns'
import { useNavigate } from 'react-router-dom';
import EditIcon from '@mui/icons-material/Edit'
import DeleteIcon from '@mui/icons-material/Delete';
import { IconButton } from '@mui/material';
const DeviceDetail = () => {
  const currentUserId = useSelector((state) => state.auth.login.currentUser.accountId)

  const { device_id } = useParams()
  const [device, setDevice] = useState()
  const [accountLcd, setAccountLCD] = useState([])
  const [isShowStatus, setIsShowStatus] = useState(false)
  const [deviceAccId, setDeviceAccId] = useState('')
  const [status, setStatus] = useState('')
  const [allUser, setAllUser] = useState([])
  const [isShowAddNew, setIsShowAddNew] = useState(false)
  const [accId, setAccId] = useState('')
  const [startDate, setStartDate] = useState(new Date())
  const [endDate, setEndDate] = useState(new Date())
  const [isStart, setIsStart] = useState(true)
  const [isEnd, setIsEnd] = useState(true)
  const navigate = useNavigate() 
  const [isLoading, setIsLoading] = useState(false)
  useEffect(() => {
    const getDeviceDetail = async () => {
      setIsLoading(true)
      let res = await securityApi.getDeviceDetail(device_id)
      setDevice(res)
      setAccountLCD(res.accountLcdResponses)
      setIsLoading(false)
    }
    getDeviceDetail()
  }, [])

  useEffect(() => {
    const fetchAllUser = async () => {
      const response = await axiosClient.get(`${BASE_URL}/getAllUserInfoActive`)
      const updateAllUser = response.filter((item) => item.accountId !== currentUserId)
      setAllUser(updateAllUser)
    }
    fetchAllUser()
  }, [])


  const columns = [
    {
      field: 'userName',
      headerName: 'Account',
      flex: 1
    },
    {
      headerName: 'Name',
      flex: 1,
      renderCell: (params) => {
        return (
          <Box>
            <Typography>
              {params?.row?.firstName} {params?.row?.lastName}
            </Typography>
          </Box>
        )
      }
    },
    {
      field: 'department',
      headerName: 'Department',
      flex: 1,
      renderCell: (params) => {
        return (
          <Box>
            <Typography>{params?.row?.department?.departmentName}</Typography>
          </Box>
        )
      }
    },
    {
      field: 'startDate',
      flex: 1,
      headerName: 'Start Time',
      renderCell: (params) => {
        return (
          <Box>
            <Typography>{formatDate(params.row.startDate)}</Typography>
          </Box>
        )
      }
    },
    {
      field: 'endDate',
      flex: 1,
      headerName: 'End Time',
      renderCell: (params) => {
        return (
          <Box>
            <Typography>{formatDate(params.row.endDate)}</Typography>
          </Box>
        )
      }
    },
    {
      field: 'status',
      flex: 1,
      headerName: 'Status'
    },
    {
      field: 'action',
      flex: 1,
      headerName: 'Action',
      headerAlign: 'center',
      align: 'center',
      sortable: false,
      filterable: false,
      renderCell: (params) => {
        return (
          <Box
            gap={2}
            display="flex"
            justifyContent="space-between"
            alignItems="center"
            borderRadius="4px"
            width="100%">
            <Box
              gap={2}
              display="flex"
              justifyContent="center"
              alignItems="center"
              borderRadius="4px"
              width="100%">
              <IconButton
                variant="contained"
                sx={{ fontSize: '14px' }}
                onClick={() => handleOpenViewStatus(params.row)}>
                <EditIcon sx={{ color: '#00FF00' }} />
              </IconButton>
              <IconButton variant="contained" sx={{ fontSize: '14px' }}>
              <DeleteIcon sx={{ color: 'red' }} />
              </IconButton>
            </Box>
          </Box>
        )
      }
    }
  ]

  const handleOpenViewStatus = (e) => {
    setIsShowStatus(true)
    setDeviceAccId(e?.deviceAccountId)
    setStatus(e?.status)
  }

  const handleOpenAddNew = () => {
    setIsShowAddNew(true)
  }

  const handleCloseStatus = () => {
    setIsShowStatus(false)
    setDeviceAccId('')
    setStatus('')
  }
  const handleCloseAddNew = () => {
    setIsShowAddNew(false)
  }

  const handleSaveChangeStatus = async () => {
    try {
      let data = {
        deviceAccountId: deviceAccId,
        status: status
      }
      await securityApi.changeRecordStatus(data)
      setAccountLCD((prevAccountLCD) =>
        prevAccountLCD.map((account) => {
          if (account.deviceAccountId === deviceAccId) {
            return {
              ...account,
              status: status
            }
          } else {
            return account
          }
        })
      )
      toast.success('Update successfully ')
      handleCloseStatus()
    } catch (error) {
     console.log(error);
    }
  }
  const handleChangeStart = (event) => {
    setIsStart(event.target.checked)
  }
  const handleChangeEnd = (event) => {
    setIsEnd(event.target.checked)
  }

  const handleSaveAddNew = async () => {
    try {
      if (accId === '') {
        toast.error('Please select user')
      } else {
        const data = {
          accountId: accId,
          roomIdString: device?.rooms[0]?.roomId.toString(),
          startDate: format(startDate, 'yyyy-MM-dd HH:mm:ss'),
          endDate: format(endDate, 'yyyy-MM-dd HH:mm:ss')
        }
        const res = await securityApi.createDeviceAccount(data)
        const updateAcountLcd = [res, ...accountLcd]
        console.log(updateAcountLcd);
        setAccountLCD(updateAcountLcd)
        toast.success('Create successfully')
        handleCloseAddNew()
      }
    } catch (error) {
      if (error.response.status === 400) {
        toast.error('Not found room')
      }
      if (error.response.status === 404) {
        toast.error('Some field are empty')
      }
      if (error.response.status === 406) {
        toast.error('Date to must be greater than date from')
      }
      if (error.response.status === 409) {
        toast.error('Account in range is existed')
      }
    }
  }

  console.log(accountLcd);

  return (
    <Box>
      <Modal
        open={isShowAddNew}
        onClose={handleCloseAddNew}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Select user
          </Typography>
          <Autocomplete
            disablePortal
            id="combo-box-demo"
            options={allUser}
            getOptionLabel={(option) => option.username}
            onChange={(event, newValue) => setAccId(newValue.accountId)}
            sx={{ mt: 2 }}
            renderInput={(params) => <TextField {...params} label="UserName" />}
          />
          <Typography mt={1} id="modal-modal-title" variant="h6" component="h2">
            Room : {device?.rooms && device?.rooms[0]?.roomName}
          </Typography>

          <Box mt={1} display="flex" justifyContent="space-between">
            <Box display="flex" alignItems="center">
              <Typography id="modal-modal-title" variant="h6" component="h2">
                Start time
              </Typography>
              <Checkbox checked={isStart} onChange={handleChangeStart} />
            </Box>

            <Box display="flex" alignItems="center">
              <Typography id="modal-modal-title" variant="h6" component="h2">
                End time
              </Typography>
              <Checkbox checked={isEnd} onChange={handleChangeEnd} />
            </Box>
          </Box>
          <Box mt={1} display="flex" justifyContent="space-between">
            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                disabled={isStart ? false : true}
                minDate={new Date()}
                value={startDate}
                views={['day', 'month', 'year']}
                onChange={(e) => setStartDate(e.toDate())}
                renderInput={(props) => <TextField sx={{ width: '200px' }} {...props} />}
              />
            </LocalizationProvider>

            <LocalizationProvider dateAdapter={AdapterDayjs}>
              <DatePicker
                disabled={isEnd ? false : true}
                minDate={new Date()}
                value={endDate}
                views={['day', 'month', 'year']}
                onChange={(e) => setEndDate(e.toDate())}
                renderInput={(props) => <TextField sx={{ width: '200px' }} {...props} />}
              />
            </LocalizationProvider>
          </Box>
          <Box display="flex" justifyContent="flex-end">
            <Button sx={{ marginTop: '15px' }} onClick={handleSaveAddNew} variant="contained">
              Save
            </Button>
          </Box>
        </Box>
      </Modal>

      <ChatTopbar />
      <Box display="flex" marginLeft="100px" marginBottom="20px">
        <Box flex="1" display="flex" marginTop="20px" height="160px">
          <Box flex="1" textAlign="left" borderRight="1px solid #999">
            <Typography>Device ID </Typography>
            <Typography mt={4}>Device Name </Typography>
            <Typography mt={4}>LCD ID </Typography>
          </Box>
          <Box flex="2" textAlign="left" marginLeft="20px">
            <Typography>{device?.deviceId}</Typography>
            <Typography mt={4} sx={{ textTransform: 'capitalize' }}>
              {device?.deviceName}{' '}
            </Typography>
            <Typography mt={4} sx={{ textTransform: 'capitalize' }}>
              {device?.deviceLcdId}{' '}
            </Typography>
          </Box>
        </Box>

        <Box flex="1" display="flex" marginTop="20px" height="80px">
          <Box flex="1" textAlign="left" borderRight="1px solid #999">
            {device?.rooms !== null && <Typography>Room </Typography>}
            <Typography mt={4}>Active Date </Typography>
          </Box>
          <Box flex="2" textAlign="left" marginLeft="20px">
            {device?.rooms !== null && <Typography>{device?.rooms[0]?.roomName}</Typography>}
            <Typography mt={4} sx={{ textTransform: 'capitalize' }}>
              {formatDateNotTime(device?.createdDate)}{' '}
            </Typography>
          </Box>
        </Box>
      </Box>
      <Divider />
      <Box margin={3}>
        <Box display="flex" justifyContent="space-between">
          <Button>All Register</Button>
      
            <Button onClick={handleOpenAddNew} variant="contained">
              Add new
            </Button>

        </Box>
        <Box marginTop="20px">
          <DataTableDeviceDetail rows={accountLcd} columns={columns} isLoading={isLoading} />
        </Box>
        <Button variant='contained' onClick={() => navigate(-1)}>Back</Button>
      </Box>
      {/* Change status   */}
      <Modal
        open={isShowStatus}
        onClose={handleCloseStatus}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Typography id="modal-modal-title" fontSize='25px'>
            Change Status
          </Typography>

          <FormControl sx={{ width: '100%', my: 2}}>
            <InputLabel id="demo-simple-select-label">Device</InputLabel>
            <Select
              
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={status}
              label="Status"
              onChange={(e) => setStatus(e.target.value)}>
              <MenuItem value="BLACK_LIST">BLACK LIST</MenuItem>
              <MenuItem value="WHITE_LIST">WHITE LIST</MenuItem>
            </Select>
          </FormControl>
          <Box display='flex' justifyContent='flex-end'>
          <Button onClick={handleSaveChangeStatus} variant="contained">
            Save
          </Button>
          </Box>
        </Box>
      </Modal>
    </Box>
  )
}

export default DeviceDetail
const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 600,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 3
}
