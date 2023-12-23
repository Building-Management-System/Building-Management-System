import CreateIcon from '@mui/icons-material/Create'
import ChangeCircleIcon from '@mui/icons-material/ChangeCircle'
import {
  Box,
  Button,
  MenuItem,
  Modal,
  Select,
  TextField,
  Typography,
  FormControl,
  InputLabel
} from '@mui/material'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import requestApi from '../../../services/requestApi'
import securityApi from '../../../services/securityApi'
import formatDate from '../../../utils/formatDate'
import DataTableDeviceManage from './component/DataTable'
import Header from '../../../components/Header'
import CreateDevice from './component/CreateDevice'

const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 400,
  bgcolor: 'background.paper',
  border: '2px solid #000',
  boxShadow: 24,
  p: 3
}

const DeviceManage = () => {
  const navigate = useNavigate()
  const [listDevice, setListDevice] = useState([])
  const [listRoom, setListRoom] = useState([])

  const [isShowStatus, setIsShowStatus] = useState(false)
  const [isShowUpdate, setIsShowUpdate] = useState(false)
  const [changeStatus, setChangeStatus] = useState('')

  const [name, setName] = useState('')
  const [roomDevice, setRoomDevice] = useState('')
  const [deviceLcdId, setDeviceLcdId] = useState('')
  const [url, setUrl] = useState('')
  const [id, setId] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [isShowView, setIsShowView] = useState(false)
  const [note, setNote] = useState('')
  const [noteAdd, setNoteAdd] = useState('')
  const [openCreateDevice, setOpenCreateDevice] = useState(false)


  useEffect(() => {
    const listAllDevice = async () => {
      try {
        setIsLoading(true)
        const response = await securityApi.getAllDevice()
        setListDevice(response)
        setIsLoading(false)
      } catch (error) {
        console.log(error)
      }
    }
    listAllDevice()
  }, [])

  const handleOpenCreateDevice = () => {
    setOpenCreateDevice(true)
  }
  const handleCloseCreateDevice = () => setOpenCreateDevice(false)

  useEffect(() => {
    const getAllRoom = async () => {
      try {
        const response = await requestApi.getAllRoom()
        setListRoom(response)
      } catch (error) {
        console.log(error)
      }
    }
    getAllRoom()
  }, [])

  console.log(listDevice)

  const columns = [
    {
      field: 'lcdId',
      headerName: 'Lcd ID',
      align: 'center',
      headerAlign: 'center',
      flex: 1
    },
    {
      field: 'deviceName',
      headerName: 'Device name',
      headerAlign: 'center',
      flex: 1
    },
    {
      field: 'roomName',
      headerName: 'Room',
      flex: 1
    },
    {
      field: 'updateDate',
      headerName: 'Active date',
      width: 220,
      renderCell: (params) => {
        return (
          <Box>
            <Typography>{formatDate(params.row.updateDate)}</Typography>
          </Box>
        )
      }
    },
    {
      field: 'status',
      headerName: 'Status',
      flex: 1,
      renderCell: (params) => {
        return (
          <Box>
            {params.row.status === 'ACTIVE' && (
              <>
                <Typography color="green">ACTIVE</Typography>
              </>
            )}
            {params.row.status === 'INACTIVE' && (
              <>
                <Typography color="red">IN ACTIVE</Typography>
              </>
            )}
            {params.row.status === 'ERROR' && (
              <>
                <Typography color="#E1A200">ERROR</Typography>
              </>
            )}
          </Box>
        )
      }
    },
    {
      field: 'deviceUrl',
      headerName: 'Go to',
      flex: 1
    },
    {
      field: 'action',
      headerName: 'Action',
      headerAlign: 'center',
      align: 'center',
      flex: 1,
      sortable: false,
      filterable: false,
      renderCell: (params) => {
        return (
          <Box
            display="flex"
            justifyContent="space-between"
            alignItems="center"
            borderRadius="4px"
            width="100%">
            <Box
              display="flex"
              justifyContent="center"
              alignItems="center"
              borderRadius="4px"
              width="100%">
              <Button onClick={() => handleOpenChangeUpdate(params.row)}>
                <CreateIcon sx={{ color: 'green' }} />
              </Button>
              <Button onClick={() => handleOpenChangeStatus(params.row)}>
                <ChangeCircleIcon />
              </Button>
            </Box>
          </Box>
        )
      }
    },
    {
      field: 'a',
      headerName: ' ',
      headerAlign: 'center',
      align: 'center',
      width: 250,
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
              {params.row.status === 'ERROR' && (
                <>
                  <Button variant="contained" onClick={() => handleOpenView(params.row)}>
                    View
                  </Button>
                </>
              )}
              <Button
                variant="contained"
                onClick={() => navigate(`/device-detail-admin/${params.row.deviceId}`)}>
                Detail
              </Button>
            </Box>
          </Box>
        )
      }
    }
  ]

  const handleOpenView = (e) => {
    setIsShowView(true)
    setNote(e?.deviceNote)
  }

  const handleCloseView = () => {
    setIsShowView(false)
    setNote('')
  }
  const handleOpenChangeStatus = (e) => {
    setIsShowStatus(true)
    setId(e?.deviceId)
    setChangeStatus(e?.status)
    setNoteAdd(e?.deviceNote)
  }

  const handleCloseStatus = () => {
    setIsShowStatus(false)
    setId('')
    setChangeStatus('')
  }

  const handleSaveChangeStatus = async () => {
    if(changeStatus === 'INACTIVE' || changeStatus === 'ACTIVE'){
      try {
        let data = {
          id: id,
          status: changeStatus,
          deviceNote: ''
        }
        await securityApi.updateDeviceStatus(data)
        toast.success('Change status successfully')
        setListDevice((prevDevice) =>
          prevDevice.map((device) => {
            if (device.deviceId === id) {
              return {
                ...device,
                status: changeStatus,
                deviceNote: noteAdd
              }
            } else {
              return device
            }
          })
        )
        handleCloseStatus()
      } catch (error) {
        if (error.response.status === 406) {
          toast.error("A device is used for 2 rooms or device is not belong to any room!")
        }
      }
    }else{
      try {
        let data = {
          id: id,
          status: changeStatus,
          deviceNote: noteAdd
        }
        await securityApi.updateDeviceStatus(data)
        toast.success('Change status successfully')
        setListDevice((prevDevice) =>
          prevDevice.map((device) => {
            if (device.deviceId === id) {
              return {
                ...device,
                status: changeStatus,
                deviceNote: noteAdd
              }
            } else {
              return device
            }
          })
        )
        handleCloseStatus()
      } catch (error) {
        if (error.response.status === 406) {
          toast.error("A device is used for 2 rooms or device is not belong to any room!")
        }
      }
    }  
    
  }

  const handleCloseUpdate = () => {
    setIsShowUpdate(false)
    setId('')
    setName('')
    setDeviceLcdId('')
    setUrl('')
    setRoomDevice('')
    setChangeStatus('')
  }

  const handleOpenChangeUpdate = (e) => {
    setIsShowUpdate(true)
    setId(e?.deviceId)
    setName(e?.deviceName)
    setDeviceLcdId(e?.lcdId)
    setUrl(e?.deviceUrl)
    setChangeStatus(e?.status)
    setRoomDevice(e?.roomId)

  }

  const handleChangeStatus = (e) => {
    setChangeStatus(e.target.value)
  }

  const handleSaveChangeUpdate = async () => {
    let data = {
      deviceId: id,
      newRoomId: roomDevice.toString(),
      deviceName: name,
      deviceLcdId: deviceLcdId,
      deviceUrl: url
    }
    try {
      await securityApi.updateDevice(data)
      navigate(0)
      handleCloseUpdate()
      toast.success('Update device successfully')
    } catch (error) {
      if (error.response.status === 400) {
        toast.error('Not found room')
      }
      if (error.response.status === 409) {
        toast.error('Room contained device active')
      }
      if (error.response.status === 406) {
        toast.error('Device_lcd_id is existed')
      }
    }
  }
  return (
    <>
      <Box mt={3}>
        <Header title="Manage Device" />
        <DataTableDeviceManage rows={listDevice} columns={columns} isLoading={isLoading} handleOpenCreateDevice={handleOpenCreateDevice} />
      </Box>

      <CreateDevice openCreateDevice={openCreateDevice} handleCloseCreateDevice={handleCloseCreateDevice} setListDevice={setListDevice} listDevice={listDevice} />
      {/* modal Change Status */}
      <Modal
        open={isShowStatus}
        onClose={handleCloseStatus}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Typography id="modal-modal-title" fontSize="25px">
            Change Status
          </Typography>
          <FormControl sx={{ width: '100%', my: 2 }}>
            <InputLabel id="demo-simple-select-label">Device</InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={changeStatus}
              label="Status"
              onChange={handleChangeStatus}>
              <MenuItem value="INACTIVE">IN ACTIVE</MenuItem>
              <MenuItem value="ERROR">ERROR</MenuItem>
              <MenuItem value="ACTIVE">ACTIVE</MenuItem>
            </Select>
          </FormControl>

          {changeStatus === 'ERROR' && (
            <>
              <Typography id="modal-modal-title" fontSize="20px" mb={1}>
                Note
              </Typography>
              <TextField
                multiline
                value={noteAdd}
                onChange={(e) => setNoteAdd(e.target.value)}
                sx={{ width: '100%', mb: 2 }}
                rows={4}
              />
            </>
          )}
          <Box display="flex" justifyContent="flex-end">
            <Button onClick={handleSaveChangeStatus} variant="contained">
              Save
            </Button>
          </Box>
        </Box>
      </Modal>
      {/* modal update Device */}
      <Modal
        open={isShowUpdate}
        onClose={handleCloseUpdate}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Box>
            <Typography id="modal-modal-title" fontSize="25px">
              Change Status
            </Typography>
            <TextField
              sx={{ marginTop: '10px', width: '100%' }}
              id="outlined-basic"
              onChange={(e) => setName(e.target.value)}
              value={name}
              label=" Device Name"
              variant="outlined"
            />

            {changeStatus === 'INACTIVE' && (
              <>
                <FormControl sx={{ width: '100%', my: 2 }}>
                  <InputLabel id="demo-simple-select-label">Room</InputLabel>
                  <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    value={roomDevice}
                    label="Room"
                    onChange={(e) => setRoomDevice(e.target.value)}>
                    {listRoom.map((item) => (
                      <MenuItem key={item.roomId} value={item.roomId}>
                        {item.roomName}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </>
            )}

            <TextField
              sx={{ marginTop: '10px', width: '100%', mb: '10px' }}
              id="outlined-basic"
              onChange={(e) => setDeviceLcdId(e.target.value)}
              value={deviceLcdId}
              label="Device Lcd Id"
              variant="outlined"
            />

            <TextField
              sx={{ marginTop: '10px', width: '100%' }}
              id="outlined-basic"
              onChange={(e) => setUrl(e.target.value)}
              value={url}
              label="Device Url"
              variant="outlined"
            />
          </Box>
          <Box display="flex" justifyContent="flex-end">
            <Button sx={{ marginTop: '10px' }} onClick={handleSaveChangeUpdate} variant="contained">
              Save
            </Button>
          </Box>

        </Box>
      </Modal>
      {/* Modal show note  */}
      <Modal
        open={isShowView}
        onClose={handleCloseView}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Typography id="modal-modal-title" variant="h6" component="h2">
            Note
          </Typography>
          <TextField
            id="outlined-basic"
            sx={{ width: '100%' }}
            InputProps={{
              readOnly: true
            }}
            value={note}
            variant="outlined"
          />
        </Box>
      </Modal>
    </>
  )
}

export default DeviceManage
