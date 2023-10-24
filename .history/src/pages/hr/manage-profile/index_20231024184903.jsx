import CheckIcon from '@mui/icons-material/Check'
import DeleteIcon from '@mui/icons-material/Delete'
import { Avatar, Box, IconButton } from '@mui/material'
import { getDownloadURL, ref } from 'firebase/storage'
import { useEffect, useState } from 'react'
import { useDispatch } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import Header from '../../../components/Header'
import { storage } from '../../../firebase/config'
import { BASE_URL } from '../../../services/constraint'
import profileApi from '../../../services/profileApi'
import axiosClient from '../../../utils/axios-config'
import DataTableManageProfile from './components/DataTable'
const ManageProfile = () => {
  const dispatch = useDispatch()

  const navigate = useNavigate()
  const [usersImage, setUsersImage] = useState('')
  const [usersProfile, setUsersProfile] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true)
      const res = await axiosClient.get(`${BASE_URL}/getAllUserInfoPending`)
      setUsersProfile(res)
      setIsLoading(false)
    }
    fetchData()
  }, [])
  const handleAcceptRequest = (userId) => {
    let choice = window.confirm('Do you want to accept this account profile?')
    if (choice == true) {
      profileApi.acceptUserInfo(userId, dispatch)
      const updatedUserList = usersProfile.filter((user) => user.accountId !== userId)
      setUsersProfile(updatedUserList)
    } else {
      navigate('/manage-profile')
    }
  }
    const imgurl = async () => {
      try {
        const downloadURLPromises = usersProfile.map((item) => {
          const storageRef = ref(storage, `/${item.image}`)
          return getDownloadURL(storageRef)
        })

        const downloadURLs = await Promise.all(downloadURLPromises)
        setUsersProfile(downloadURLs)
        const updateData = usersProfile.map((item) => {
          return { ...item, image: downloadURLs }
        })

        console.log(updateData);
      } catch (error) {
        console.error('Error getting download URLs:', error)
      }
    }

    console.log(usersImage);
    useEffect(() => {
      imgurl()
    }, [usersProfile])

    console.log(usersProfile);
  const columns = [
    {
      headerName: 'Avatar',
      headerAlign: 'center',
      align: 'center',
      width: 100,
      renderCell: () => <Avatar src={`${usersImage}`} />
    },
    {
      field: 'firstName',
      headerName: 'First Name',
      cellClassName: 'name-column--cell',
      width: 150
    },
    {
      field: 'lastName',
      headerName: 'Last Name',
      headerAlign: 'left',
      align: 'left',
      width: 120
    },
    {
      field: 'telephoneNumber',
      headerName: 'Phone Number',
      width: 160
    },
    {
      field: 'email',
      headerName: 'Email',
      width: 270
    },
    {
      field: 'country',
      headerName: 'Country',
      width: 100
    },
    {
      field: 'city',
      headerName: 'City',
      width: 100
    },
    {
      field: 'dateOfBirth',
      headerName: 'Date of Birth',
      width: 130
    },
    {
      field: 'gender',
      headerName: 'Gender',
      width: 100
    },

    {
      field: 'action',
      headerName: 'Action',
      headerAlign: 'center',
      align: 'center',
      width: 100,
      renderCell: (params) => {
        return (
          <Box
            margin="0 auto"
            p="5px"
            display="flex"
            justifyContent="center"
            alignItems="center"
            borderRadius="4px">
            <IconButton onClick={() => handleAcceptRequest(params.row.accountId)}>
              <CheckIcon sx={{ color: '#00FF00' }} />
            </IconButton>
            <IconButton>
              <DeleteIcon sx={{ color: 'red' }} />
            </IconButton>
          </Box>
        )
      }
    }
  ]
  return (
    <>
      <Header title="TEAM" subtitle="Managing the team Members" />
      <DataTableManageProfile rows={usersProfile} columns={columns} isLoading={isLoading}  />
    </>
  )
}

export default ManageProfile
