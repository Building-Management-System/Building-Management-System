import MoreVertIcon from '@mui/icons-material/MoreVert'
import PriorityHighIcon from '@mui/icons-material/PriorityHigh'
import StarIcon from '@mui/icons-material/Star'
import StarBorderIcon from '@mui/icons-material/StarBorder'
import { Box, IconButton } from '@mui/material'
import Checkbox from '@mui/material/Checkbox'
import Menu from '@mui/material/Menu'
import MenuItem from '@mui/material/MenuItem'
import * as React from 'react'
import { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import Header from '../../../components/Header'
import { BASE_URL } from '../../../services/constraint'
import axiosClient from '../../../utils/axios-config'
import DataTableListNoti from './components/DataTable'
import { SignalWifiStatusbarNullRounded } from '@mui/icons-material'
import { format } from 'date-fns'
import notificationApi from '../../../services/notificationApi'
import FilePresentIcon from '@mui/icons-material/FilePresent';
import { toast } from 'react-toastify';

import Swal from 'sweetalert2';

const NotificationsList = (props) => {
  const { row } = props
  const userId = useSelector((state) => state.auth.login.currentUser.accountId)
  const dispatch = useDispatch()
  const [allNoti, setAllNoti] = useState([])
  const [user, setUser] = useState('')
  const [open, setOpen] = useState(false)
  const [openCreateAccount, setOpenCreateAccount] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const currentUser = useSelector((state) => state.auth.login?.currentUser)
  const navigate = useNavigate()
  const handleOpen = (data) => {
    setOpen(true)
    setUser(data)
  }
  const options = [
    'Delete',
    'Detail',
  ]
  const ITEM_HEIGHT = 58;
  const [anchorEl, setAnchorEl] = React.useState(null);
  const openMenu = Boolean(anchorEl);
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleClose2 = () => {
    setAnchorEl(null);
  };
  const handleClose = () => setOpen(false)
  const label = { inputProps: { 'aria-label': 'Checkbox demo' } };

  useEffect(() => {
    setIsLoading(true)
    const fetchAllNoti = async () => {
      const response = await axiosClient.get(`${BASE_URL}/getListNotificationByCreator`, {
        params: {
          userId: userId
        }
      })
      setAllNoti(response)
      setIsLoading(false)
      console.log(userId)
    }
    fetchAllNoti()
  }, [])

  console.log(userId)

  const handleDelete = (user) => {
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
          .post(`${BASE_URL}/deleteNotification`, data)
          .then(() => {
            const updatedNoti = allNoti.filter((item) => item.notificationId !== user.notificationId);
            setAllNoti(updatedNoti);
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

  const handelSetPersonalPriority = async (notification) => {
    if (notification.personalPriority === false && !notification.personalPriority) {
      let data = {
        notificationId: notification.notificationId,
        userId: userId
      };
      await notificationApi.setPersonalPriority(data);
      const updatedAllNoti = allNoti.map((item) => {
        if (item.notificationId === notification.notificationId) {
          return { ...item, personalPriority: true };
        }
        return item;
      });
      setAllNoti(updatedAllNoti);
    } else if (notification.personalPriority === true) {
      let data = {
        notificationId: notification.notificationId,
        userId: userId
      };
      await notificationApi.unSetPersonalPriority(data);
      const updatedAllNoti = allNoti.map((item) => {
        if (item.notificationId === notification.notificationId) {
          return { ...item, personalPriority: false };
        }
        return item;
      });
      setAllNoti(updatedAllNoti);
    }
  };


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
            borderRadius="4px"
          >
            <div>
              <Checkbox
                {...label}
                icon={params.row.personalPriority ? <StarIcon color='warning' /> : <StarBorderIcon color='warning' />}
                checkedIcon={params.row.personalPriority ? <StarIcon color='warning' /> : <StarBorderIcon color='warning' />}
                onChange={() => handelSetPersonalPriority(params.row)}
                checked={params.row.personalPriority}
              />
            </div>
          </Box>
        )
      }
    },
    {
      field: 'notificationStatus',
      headerName: '',
      cellClassName: 'name-column--cell',
      headerAlign: 'center',
      align: 'center',
      width: 150,
      flex: 1,
      renderCell: (params) => (
        <Box
          margin="0 auto"
          p="5px"
          display="flex"
          justifyContent="center"
          alignItems="center"
          borderRadius="4px"
          color={
            params.row.notificationStatus === 'UPLOADED'
              ? 'blue'
              : params.row.notificationStatus === 'DRAFT'
                ? 'red'
                : params.row.notificationStatus === 'SCHEDULED'
                  ? 'green'
                  : 'black'
          }
        >
          <div>{params.row.notificationStatus}</div>
        </Box>
      )

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
            borderRadius="4px"
          >
            <div>
              {params.row.priority === true ? (
                <PriorityHighIcon color='primary' />
              ) : null

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
          color='#000'
        >
          <div>
            {params.row.departmentUpload.departmentName}
          </div>
        </Box>
      )
    },
    {
      field: 'title',
      headerName: 'Title',
      headerAlign: 'center',
      align: 'center',
      width: 200,
    },
    {
      field: 'content',
      headerName: 'Content',
      headerAlign: 'center',
      align: 'center',
      width: 300,
    },
    {
      field: 'containImage',
      headerName: 'Attached File',
      headerAlign: 'center',
      align: 'center',
      width: 250,
      sortable: false,
      filterable: false,
      renderCell: (params) => {
        if (params.row.containFile === true || params.row.containImage === true) {
          return <FilePresentIcon fontSize='large' color='primary' />;
        } else {
          return null;
        }
      },
    }
    ,
    {
      field: 'uploadDate',
      headerName: 'Date',
      cellClassName: 'name-column--cell',
      headerAlign: 'center',
      align: 'center',
      flex: 1,
      width: 300,
      renderCell: (params) => (
        <Box
          margin="0 auto"
          p="5px"
          display="flex"
          justifyContent="center"
          alignItems="center"
          borderRadius="4px"
          color='#000'
        >
          <div>
            {format(new Date(params.row.uploadDate), 'yyyy/MM/dd HH:mm:ss')}
          </div>
        </Box>
      )
    },
    {
      field: 'action',
      headerName: '',
      headerAlign: 'center',
      align: 'center',
      width: 40,
      sortable: false,
      filterable: false,
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
              <IconButton
                aria-label="more"
                id="long-button"
                aria-controls={openMenu ? 'long-menu' : undefined}
                aria-expanded={openMenu ? 'true' : undefined}
                aria-haspopup="true"
                onClick={handleClick}
              >
                <MoreVertIcon />
              </IconButton>
              <Menu
                id="long-menu"
                MenuListProps={{
                  'aria-labelledby': 'long-button',
                }}
                anchorEl={anchorEl}
                open={openMenu}
                onClose={handleClose2}
                PaperProps={{
                  style: {
                    maxHeight: ITEM_HEIGHT * 4.5,
                    width: '25ch',
                    boxShadow: '1px 1px 1px #999'

                  },
                }}
              >
                {options.map((option) => (
                  <MenuItem key={option} selected={option === 'Pyxis'} onClick={
                    option === 'Detail' ?
                      () => navigate(`/notification-detail/${params.row.notificationId}`) :
                      option === 'Delete' ?
                        () => handleDelete(params.row) :
                        () => SignalWifiStatusbarNullRounded
                  }>
                    {option}
                  </MenuItem>
                ))}
              </Menu>
            </div>
          </Box>
        )
      }
    }
    ,

  ]
  return (
    <>
      <Header title="ALL NOTIFICATIONS" />
      <DataTableListNoti
        rows={allNoti}
        columns={columns}
        isLoading={isLoading}

      />

    </>
  )

}


export default NotificationsList
