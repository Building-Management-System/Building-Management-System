import CircleIcon from '@mui/icons-material/Circle'
import FilePresentIcon from '@mui/icons-material/FilePresent'
import PriorityHighIcon from '@mui/icons-material/PriorityHigh'
import StarIcon from '@mui/icons-material/Star'
import StarBorderIcon from '@mui/icons-material/StarBorder'
import { Box, Button, Typography } from '@mui/material'
import Checkbox from '@mui/material/Checkbox'
import { useEffect, useState } from 'react'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import Header from '../../../components/Header'
import { BASE_URL } from '../../../services/constraint'
import notificationApi from '../../../services/notificationApi'
import axiosClient from '../../../utils/axios-config'
import DataTableListUploadReceive from './components/DataTableUploadReceive'
import Swal from 'sweetalert2'
import { format } from 'date-fns'

const NotificationUploadReceive = () => {
  const userId = useSelector((state) => state.auth.login.currentUser.accountId)
  const [allNoti, setAllNoti] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const navigate = useNavigate()
  const label = { inputProps: { 'aria-label': 'Checkbox demo' } }

  const handelChangeStatusMarkAsRead = (notification) => {
    console.log(notification)
    if (notification.readStatus === false) {
      let data = {
        notificationId: notification.notificationId,
        userId: userId
      }
      console.log(userId)
      notificationApi.markToRead(data)
      toast.success('Mask as read successfully!!')
      setAllNoti((prevNoti) =>
        prevNoti.map((noti) => {
          if (noti.notificationId === notification.notificationId) {
            return {
              ...noti,
              readStatus: true
            }
          } else {
            return noti
          }
        })
      )
    }
  }
  const handleHidden = (user) => {
    Swal.fire({
      title: 'Are you sure to delete this notification?',
      icon: 'warning',
      cancelButtonText: 'Cancel',
      showCancelButton: true,
      cancelButtonColor: 'red',
      confirmButtonColor: 'green',
    }).then((result) => {
      if (result.isConfirmed) {
        let data = {
          notificationId: user.notificationId,
          userId: userId
        };

        axiosClient
          .post(`${BASE_URL}/setNotificationHidden`, data)
          .then(() => {
            const updatedNoti = allNoti.filter((item) => item.notificationId !== user.notificationId);
            setAllNoti(updatedNoti);
            toast.success('Hidden Successfully')
          })
          .catch((error) => {
            if (error.response.status === 400) {
              toast.error('Notification is null!');
            } else if (error.response.status === 404) {
              toast.error('Notification does not exist!');
            } else if (error.response.status === 500) {
              toast.error('Unable to delete the notification!');
            } else if (error.response.status === 409) {
              toast.error('Notification have been upload,cant delete');
            }
            else {
              toast.error('???');
            }
          });
      }
    });
  };
  const handelChangeStatusMarkAsUnRead = (notification) => {
    if (notification.readStatus === true) {
      let data = {
        notificationId: notification.notificationId,
        userId: userId
      }
      notificationApi.markToUnRead(data)
      toast.success('Mask as unread successfully!!')
    }
    setAllNoti((prevNoti) =>
      prevNoti.map((noti) => {
        if (noti.notificationId === notification.notificationId) {
          return {
            ...noti,
            readStatus: false
          }
        } else {
          return noti
        }
      })
    )
  }

  const handelSetPersonalPriority = async (notification) => {
    if (notification.personalPriority === false && !notification.personalPriority) {

      let data = {
        notificationId: notification.notificationId,
        userId: userId
      }
      await notificationApi.setPersonalPriority(data)
      const updatedAllNoti = allNoti.map((item) => {
        if (item.notificationId === notification.notificationId) {
          return { ...item, personalPriority: true }
        }
        return item
      })
      setAllNoti(updatedAllNoti)
    } else if (notification.personalPriority === true) {
      let data = {
        notificationId: notification.notificationId,
        userId: userId
      }
      await notificationApi.unSetPersonalPriority(data)
      const updatedAllNoti = allNoti.map((item) => {
        if (item.notificationId === notification.notificationId) {
          return { ...item, personalPriority: false }
        }
        return item
      })
      setAllNoti(updatedAllNoti)
    }
  }

  useEffect(() => {
    setIsLoading(true)
    const fetchAllNoti = async () => {
      const response = await axiosClient.get(`${BASE_URL}/getListUploadedNotification`, {
        params: {
          userId: userId
        }
      })
      setAllNoti(response)
      setIsLoading(false)
      console.log(response)
    }
    fetchAllNoti()
  }, [])

  console.log(allNoti)

  const columns = [
    {
      field: 'personalPriority',
      headerName: '',
      cellClassName: 'name-column--cell',
      headerAlign: 'center',
      align: 'center',
      width: 60,
      renderCell: (params) => {
        return (
          <Box
            margin="0 auto"
            p="5px"
            display="flex"
            justifyContent="center"
            alignItems="center"
            borderRadius="4px">
            <div>
              <Checkbox
                {...label}
                icon={
                  params.row.personalPriority ? (
                    <StarIcon color="warning" />
                  ) : (
                    <StarBorderIcon color="warning" />
                  )
                }
                checkedIcon={
                  params.row.personalPriority ? (
                    <StarIcon color="warning" />
                  ) : (
                    <StarBorderIcon color="warning" />
                  )
                }
                onChange={() => handelSetPersonalPriority(params.row)}
                checked={params.row.personalPriority}
              />
            </div>
          </Box>
        )
      }
    },
    {
      field: 'priority',
      headerName: 'Priority',
      cellClassName: 'name-column--cell',
      headerAlign: 'center',
      align: 'center',
      width: 80,
      renderCell: (params) => {
        return (
          <Box
            margin="0 auto"
            p="5px"
            display="flex"
            justifyContent="center"
            alignItems="center"
            borderRadius="4px">
            <div>
              {
                params.row.priority === true ? <PriorityHighIcon color="primary" /> : null
                //   <Checkbox {...label} icon={<StarBorderIcon color='warning' />} checkedIcon={<StarIcon color='warning' />} />
                // )
              }
            </div>
          </Box>
        )
      }
    },

    {
      field: 'departmentName',
      headerName: 'From',
      cellClassName: 'name-column--cell',
      headerAlign: 'center',
      align: 'center',
      width: 150,
      renderCell: (params) => (
        <Box
          margin="0 auto"
          p="5px"
          display="flex"
          justifyContent="center"
          alignItems="center"
          borderRadius="4px"
          color="#000">
          <div>{params.row.departmentUpload.departmentName}</div>
        </Box>
      )
    },
    {
      field: 'title',
      headerName: 'Title',
      headerAlign: 'center',
      align: 'center',
      width: 200
    },
    {
      headerName: 'Content',
      headerAlign: 'center',
      align: 'center',
      width: 300,
      renderCell: (params) => (
        <Typography dangerouslySetInnerHTML={{
          __html: params.row.content.substring(0, 20)
        }}></Typography>
      ),
    },
    {
      field: 'readStatus',
      headerName: '',
      headerAlign: 'center',
      align: 'center',
      width: 60,

      renderCell: (params) => {
        return (
          <Box
            margin="0 auto"
            p="5px"
            display="flex"
            justifyContent="center"
            alignItems="center"
            borderRadius="4px">
            <div>
              {
                params.row.readStatus === false ? (
                  <CircleIcon color="primary" fontSize="10px" />
                ) : null
                //   <Checkbox {...label} icon={<StarBorderIcon color='warning' />} checkedIcon={<StarIcon color='warning' />} />
                // )
              }
            </div>
          </Box>
        )
      }
    },
    {
      field: 'containImage',
      headerName: 'Attached File',
      headerAlign: 'center',
      align: 'center',
      width: 150,
      sortable: false,
      filterable: false,
      renderCell: (params) => {
        if (params.row.containFile === true || params.row.containImage === true) {
          return <FilePresentIcon fontSize="large" color="primary" />
        } else {
          return null
        }
      }
    },
    {
      field: 'uploadDate',
      headerName: 'Date',
      cellClassName: 'name-column--cell',
      headerAlign: 'center',
      align: 'center',
   
      width: 170,
      renderCell: (params) => (
        <Box
          margin="0 auto"
          p="5px"
          display="flex"
          justifyContent="center"
          alignItems="center"
          borderRadius="4px"
          color="#000">
          <div>{format(new Date(params.row.uploadDate), 'yyyy/MM/dd HH:mm:ss')}</div>
        </Box>
      )
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
        const handleDetailClick = () => {
          navigate(`/notification-detail/${params.row.notificationId}/${params.row.creatorId}`)
        }

        return (
          <Box
            gap={2}
            display="flex"
            justifyContent="space-between"
            alignItems="center"
            borderRadius="4px"
            width="100%"
          >
            <Button variant="contained" onClick={() => handleDetailClick(params.row)} style={{ fontSize: '12px' }}>
              Detail
            </Button>
            {params.row.readStatus === false ? (
              <Button variant="contained" onClick={() => handelChangeStatusMarkAsRead(params.row)} style={{ fontSize: '12px' }}>
                Mark as read
              </Button>
            ) : (
              <Button variant="contained" onClick={() => handelChangeStatusMarkAsUnRead(params.row)} style={{ fontSize: '12px' }}>
                Mark as unread
              </Button>
            )}
            <Button variant="contained" onClick={() => handleHidden(params.row)} style={{ fontSize: '12px' }}>
              Delete
            </Button>
          </Box>
        )
      }
    }
  ]
  return (
    <>
      <Header title="RECEIVE" />
      <DataTableListUploadReceive setAllNoti={setAllNoti} userId={userId} rows={allNoti} columns={columns} isLoading={isLoading} />
    </>
  )
}

export default NotificationUploadReceive
