import EditIcon from '@mui/icons-material/Edit'
import ExitToAppIcon from '@mui/icons-material/ExitToApp'
import LensIcon from '@mui/icons-material/Lens'
import SearchIcon from '@mui/icons-material/Search'
import SendIcon from '@mui/icons-material/Send'
import SupervisorAccountIcon from '@mui/icons-material/SupervisorAccount'
import TextSnippetIcon from '@mui/icons-material/TextSnippet'
import {
  Autocomplete,
  Avatar,
  AvatarGroup,
  Box,
  Button,
  CircularProgress,
  Divider,
  FormControl,
  FormControlLabel,
  FormLabel,
  IconButton,
  InputAdornment,
  InputLabel,
  MenuItem,
  Modal,
  Radio,
  RadioGroup,
  Select,
  Stack,
  TextField,
  Tooltip,
  Typography
} from '@mui/material'
import { getDownloadURL, ref } from 'firebase/storage'
import moment from 'moment'
import { useEffect, useRef, useState } from 'react'
import InputEmoji from 'react-input-emoji'
import { useSelector } from 'react-redux'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import io from 'socket.io-client'
import Swal from 'sweetalert2'
import { storage } from '../../../firebase/config'
import useDebounce from '../../../hooks/useDebounce'
import chatApi from '../../../services/chatApi'
import { BASE_URL } from '../../../services/constraint'
import axiosClient from '../../../utils/axios-config'
import './components/Chat.css'
import ChatTopbar from './components/ChatTopbar'
const style = {
  position: 'absolute',
  top: '50%',
  left: '50%',
  transform: 'translate(-50%, -50%)',
  width: 500,
  bgcolor: 'background.paper',
  border: '1px solid #000',
  boxShadow: 24,
  p: 4
}
const SOCKET_URL = 'https://capstone-nodejs.onrender.com'
const Chat = () => {
  const [newMessage, setNewMessage] = useState('')
  const [allUser, setAllUser] = useState([])
  const [allUserSingleChat, setAllUserSingleChat] = useState([])
  const [allChatList, setAllChatList] = useState([])
  const [isActiveUser, setIsActiveUser] = useState('')
  const [messages, setMessages] = useState([])
  const [arrivalMessage, setArrivalMessage] = useState('')
  const [file, setFile] = useState()
  const [isLoadingChat, setIsLoadingChat] = useState(false)
  const [messageImage, setMessageImage] = useState('')
  const [searchTerm, setSearchTerm] = useState('')
  const [selectedUser, setSelectedUser] = useState([])
  const [selectedUserGroup, setSelectedUserGroup] = useState([])
  const [selectedUserSingleChat, setSelectedUserSingleChat] = useState([])
  const [chatName, setChatName] = useState('')
  const [chatNameMessage, setChatNameMessage] = useState('')
  const [userAvatar, setUserAvatar] = useState([])
  const [userChangeAdmin, setUserChangeAdmin] = useState('')
  const [userGroupUpdate, setUserGroupUpdate] = useState([])
  const socket = useRef()
  const imageRef = useRef()
  const scroll = useRef()
  const [open, setOpen] = useState(false)
  const [openUpdateGroup, setOpenUpdateGroup] = useState(false)
  const [openChangeAdmin, setOpenChangeAdmin] = useState(false)
  const [option, setOption] = useState('single')
  const handleChangeSelectOption = (event) => {
    setOption(event.target.value)
  }
  const handleOpen = () => setOpen(true)
  const handleClose = () => setOpen(false)
  const handleOpenUpdateGroup = () => setOpenUpdateGroup(true)
  const handleCloseUpdateGroup = () => setOpenUpdateGroup(false)
  const handleOpenChangeAdmin = () => setOpenChangeAdmin(true)
  const handleCloseChangeAdmin = () => setOpenChangeAdmin(false)
  const debouncedSearch = useDebounce(searchTerm, 500)
  const navigate = useNavigate()

  const currentUserId = useSelector((state) => state.auth.login.currentUser.accountId)

  useEffect(() => {
    const fetchAllUser = async () => {
      const response = await axiosClient.get(`${BASE_URL}/getAllUserInfoActive`)
      const updateAllUser = response.filter((item) => item.accountId !== currentUserId)
      setAllUser(updateAllUser)
    }
    fetchAllUser()
  }, [])

  useEffect(() => {
    const fetchAllUserSingleChat = async () => {
      const data = {
        userId: currentUserId
      }
      const response = await chatApi.getUserSingleChat(data)
      setAllUserSingleChat(response)
    }
    fetchAllUserSingleChat()
  }, [])

  console.log(messages)
  useEffect(() => {
    setChatName(isActiveUser?.chatName)
    setUserGroupUpdate(isActiveUser?.user)
    setSelectedUserGroup(isActiveUser?.user)
  }, [isActiveUser])

  const handleCreateNewChat = async () => {
    if (option === 'group') {
      if (selectedUser.length > 1 && chatName !== '' && chatNameMessage !== '') {
        const data = {
          from: currentUserId,
          to: selectedUser,
          chatName: chatName,
          message: chatNameMessage
        }
        const res = await chatApi.createNewChat(data)
        setAllChatList((prev) => [res, ...prev])
        setChatNameMessage('')
        setSelectedUser([])
        handleClose()
        toast.success('Create new Chat successfully!!!!')
      } else if (selectedUser.length <= 1) {
        toast.error('Please select at least two people')
      } else if (chatName === '' || chatNameMessage === '') {
        toast.error(`Chat name or message can't be blank`)
      } else {
        toast.error(`All field can't be blank`)
      }
    } else if (option === 'single') {
      if (selectedUserSingleChat.length > 0 && chatNameMessage !== '') {
        const data = {
          from: currentUserId,
          to: [selectedUserSingleChat],
          message: chatNameMessage
        }
        const res = await chatApi.createNewChat(data)
        setAllChatList((prev) => [res, ...prev])
        setChatNameMessage('')
        setAllUserSingleChat(
          allUserSingleChat.filter((user) => user.accountId !== selectedUserSingleChat)
        )
        handleClose()
        toast.success('Create new Chat successfully!!!!')
      } else {
        toast.error(`User or message can't be blank`)
      }
    }
  }

  useEffect(() => {
    const fetchAllChatList = async () => {
      const data = {
        userId: currentUserId
      }
      const response = await chatApi.getAllChatList(data)
      setAllChatList(response)
    }
    fetchAllChatList()
  }, [])
  
  useEffect(() => {
    if (isActiveUser !== '') {
      setIsLoadingChat(true)
      const getMessage = async () => {
        const response = await axiosClient.get(`${BASE_URL}/message`, {
          params: {
            userId: currentUserId,
            chatId: isActiveUser?.chatId
          }
        })
        setMessages(response.messageResponseList)
        setUserAvatar(response.users)
        socket.current.emit('join-chat', isActiveUser?.chatId)
        setIsLoadingChat(false)
      }
      getMessage()
    }
  }, [isActiveUser?.chatId])

  useEffect(() => {
    scroll?.current?.scrollIntoView({
      behavior: 'smooth'
    })
  }, [messages])
  const imgurlAvatar = async () => {
    try {
      if (allChatList.length > 0) {
        const downloadURLPromises = allChatList.map(async (item) => {
          if (item.avatar && Array.isArray(item.avatar)) {
            const avatarPromises = item.avatar.map(async (avatarItem) => {
              const storageRef = ref(storage, `/${avatarItem}`)
              return getDownloadURL(storageRef)
            })
            return Promise.all(avatarPromises)
          } else {
            return Promise.resolve(null)
          }
        })
        const downloadURLs = await Promise.all(downloadURLPromises)
        const result = allChatList.map((obj, index) => {
          return {
            ...obj,
            avatar: Array.isArray(downloadURLs[index]) ? downloadURLs[index] : null
          }
        })
        setAllChatList(result)
        console.log(result);
      }
    } catch (error) {
      console.error('Error getting download URLs:', error)
    }
  }
  useEffect(() => {
    imgurlAvatar()
  }, [allChatList])

  console.log(allChatList)
  const handleChange = (newMessage) => {
    setNewMessage(newMessage)
  }

  const handleActiveUser = async (item) => {
    let data = {
      userId: currentUserId,
      chatId: item.chatId
    }
    await chatApi.markReadChat(data)
    setIsActiveUser({ ...item, read: 'true' })
    let updatedChatList = allChatList.map((chatItem) => {
      if (chatItem.chatId === item.chatId && chatItem.read === 'false') {
        return { ...chatItem, read: 'true' }
      } else {
        return chatItem
      }
    })
    setIsActiveUser(item)
    setAllChatList(updatedChatList)
  }

  const handleSendMessage = async () => {
    let data = {
      from: currentUserId,
      chatId: isActiveUser?.chatId,
      message: newMessage
    }

    let dataFile = {
      from: currentUserId,
      chatId: isActiveUser?.chatId
    }

    let message = {
      myself: true,
      message: newMessage,
      type: 'text'
    }

    if (file !== undefined) {
      if (file.type === 'image/png' || file.type === 'image/jpeg') {
        try {
          let formData = new FormData()
          formData.append('data', JSON.stringify(dataFile))
          formData.append('image', file)
          const res = await axiosClient.post(`${BASE_URL}/createNewMessage2`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          let message2 = {
            myself: true,
            message: res.message,
            type: 'image'
          }
          setMessages(messages.concat(message2))
          socket.current.emit('send-msg', {
            from: currentUserId,
            chatId: isActiveUser?.chatId,
            message: res.message,
            to: isActiveUser.user.map((item) => {
              return item.accountId
            }),
            senderId: res.senderId,
            messageId: res.messageId,
            type: 'image'
          })
          setNewMessage('')
          setFile()
        } catch (error) {
          if (error.response.status === 400) {
            toast.error('User not found!')
          }
          if (error.response.status === 500) {
            toast.error('Null!')
          }
        }
      } else {
        try {
          let formData = new FormData()
          formData.append('data', JSON.stringify(dataFile))
          formData.append('file', file)
          const res = await axiosClient.post(`${BASE_URL}/createNewMessage3`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          let message2 = {
            myself: true,
            message: res.message,
            messageId: res.messageId,
            type: 'file'
          }
          setMessages(messages.concat(message2))
          socket.current.emit('send-msg', {
            from: currentUserId,
            chatId: isActiveUser?.chatId,
            to: isActiveUser.user.map((item) => {
              return item.accountId
            }),
            senderId: res.senderId,
            message: res.message,
            messageId: res.messageId,
            type: 'file'
          })
          setNewMessage('')
          setFile()
        } catch (error) {
          if (error.response.status === 400) {
            toast.error('User not found!')
          }
          if (error.response.status === 500) {
            toast.error('Null!')
          }
        }
      }
    } else {
      try {
        if (newMessage !== '') {
          const res = await axiosClient.post(`${BASE_URL}/createNewMessage`, data)
          setMessages(messages.concat(message))
          socket.current.emit('send-msg', {
            from: currentUserId,
            to: isActiveUser.user.map((item) => {
              return item.accountId
            }),
            user: isActiveUser.user,
            message: newMessage,
            senderId: res.senderId,
            messageId: res.messageId,
            type: 'text'
          })
          setNewMessage('')
        } else {
          toast.error("Message can't be blank")
        }
      } catch (error) {
        if (error.response.status === 400) {
          toast.error('User not found!')
        }
      }
    }
  }

  useEffect(() => {
    if (isActiveUser === '') {
      socket.current = io(SOCKET_URL)
      socket.current.emit('addUser', currentUserId)
    }
  }, [currentUserId])

  useEffect(() => {
    if (socket.current) {
      socket.current.on('msg-receive', (msg) => {
        setArrivalMessage({
          myself: false,
          message: msg.message,
          type: msg.type,
          senderId: msg.senderId,
          messageId: msg.messageId
        })
      })
    }
  }, [arrivalMessage])

  useEffect(() => {
    arrivalMessage && setMessages((pre) => [...pre, arrivalMessage])
  }, [arrivalMessage])

  console.log(messages)

  const handleFileChange = (e) => {
    setFile(e.target.files[0])
    setNewMessage(e.target.files[0].name)
  }

  const imgurlMessage = async () => {
    try {
      if (messages.length > 0) {
        const downloadURLPromisesImage = messages.map((item) => {
          if (item.type === 'image' && item?.message !== 'unknown') {
            const storageRef = ref(storage, `/${item?.message}`)
            return getDownloadURL(storageRef)
          }
        })
        const downloadURLsImage = await Promise.all(downloadURLPromisesImage)
        if (downloadURLsImage !== undefined) {
          setMessageImage(downloadURLsImage)
        }
      }
    } catch (error) {
      console.error('Error getting download URLs:', error)
    }
  }

  useEffect(() => {
    imgurlMessage()
  }, [messages])

  const handleDownloadFile = async (item) => {
    let data = {
      messageId: item.messageId,
      fileName: item.message
    }
    const res = await chatApi.downloadFile(data)
    const base64Data = res?.data
    if (!base64Data) {
      return
    }

    const binaryData = atob(base64Data)
    const byteNumbers = new Array(binaryData.length)
    for (let i = 0; i < binaryData.length; i++) {
      byteNumbers[i] = binaryData.charCodeAt(i)
    }
    const uint8Array = new Uint8Array(byteNumbers)

    const blob = new Blob([uint8Array], {
      type: res?.type
    })

    const blobUrl = URL.createObjectURL(blob)

    const downloadLink = document.createElement('a')
    downloadLink.href = blobUrl
    downloadLink.download = res?.name
    downloadLink.click()
  }

  const imgurlUserAvatar = async () => {
    try {
      if (userAvatar.length > 0) {
        const downloadURLPromises = userAvatar.map((item) => {
          const storageRef = ref(storage, `/${item.image}`)
          return getDownloadURL(storageRef)
        })
        const downloadURLs = await Promise.all(downloadURLPromises)
        const result = userAvatar.map((obj, index) => {
          return {
            ...obj,
            image: downloadURLs[index] !== undefined ? downloadURLs[index] : 'unknown image'
          }
        })
        setUserAvatar(result)
      }
    } catch (error) {
      console.error('Error getting download URLs:', error)
    }
  }

  useEffect(() => {
    imgurlUserAvatar()
  }, [userAvatar])

  console.log(userAvatar)
  const handleUpdateChat = async () => {
    if (selectedUserGroup.length > 1 && chatName === '') {
      const selectedUserGroupId = selectedUserGroup.map((item) => {
        return item.accountId
      })
      selectedUserGroupId.unshift(currentUserId)
      const data = {
        chatId: isActiveUser?.chatId,
        isGroup: 'true',
        chatName: chatName,
        userId: selectedUserGroupId
      }

      const res = await chatApi.updateChat(data)

      setAllChatList((prev) => {
        return prev.map((chat) => (chat.chatId === res.chatId ? res : chat))
      })

      setIsActiveUser(res)
      handleCloseUpdateGroup()
    } else {
      toast.error(`Chat name can't be blank or number member in the group must be greater than 1`)
    }
  }

  const handleChangeAdmin = () => {
    if (userChangeAdmin !== '') {
      const data = {
        chatId: isActiveUser?.chatId,
        userId: userChangeAdmin
      }
      chatApi.changeAdmin(data)
      setIsActiveUser({ ...isActiveUser, admin: userChangeAdmin })
      // setAllChatList((prev) => [res, ...prev])
      handleCloseChangeAdmin()
    } else {
      toast.error('Please choose user you want to change admin')
    }
  }

  const handleLeaveGroup = () => {
    handleCloseChangeAdmin()
    Swal.fire({
      title: 'Are you sure to leave this group?',
      icon: 'error',
      cancelButtonText: 'Cancel!',
      showCancelButton: true,
      cancelButtonColor: 'red',
      confirmButtonColor: 'green'
    }).then((result) => {
      if (result.isConfirmed) {
        const data = {
          chatId: isActiveUser?.chatId,
          userId: currentUserId
        }
        chatApi.leaveGroup(data)
        setAllChatList((prev) => {
          return prev.filter((chat) => chat.chatId !== isActiveUser?.chatId)
        })
        setIsActiveUser('')
      } else {
        navigate('/chat')
      }
    })
  }

  const allUserUpdate =
    isActiveUser && isActiveUser.user
      ? allUser.filter(
          (item) => !isActiveUser.user.some((user) => user.accountId === item.accountId)
        )
      : []

  return (
    <Box height="100vh">
      <ChatTopbar />
      <Box display="flex" gap="20px" p={1.5} bgcolor="#f5f7f9" height="calc(100vh - 65px)">
        <Box
          className="Left-side-chat"
          sx={{ overflowY: 'auto' }}
          bgcolor="#fff"
          width="22%"
          borderRadius="10px">
          <Box className="Chat-container">
            <Box display="flex" justifyContent="space-between" my={2}>
              <h2 style={{ padding: '16px', margin: 0 }}>Chats</h2>
              <Button
                onClick={handleOpen}
                variant="outlined"
                sx={{ p: '0px 5px', fontSize: '15px', mr: '16px', textTransform: 'capitalize' }}>
                Create New Chat
              </Button>
            </Box>
            <FormControl sx={{ width: '100%', bgcolor: '#fff', borderRadius: '10px', px: 2 }}>
              <TextField
                size="small"
                variant="outlined"
                sx={{ color: '#000', borderRadius: '10px', input: { color: '#000' } }}
                placeholder="Search User"
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                InputProps={{
                  style: {
                    borderRadius: '10px'
                  },
                  startAdornment: (
                    <InputAdornment position="start">
                      <SearchIcon sx={{ color: '#000' }} />
                    </InputAdornment>
                  )
                }}
              />
            </FormControl>

            {allChatList
              .filter((user) => user.chatName !== null && user.chatName.includes(debouncedSearch))
              .map((item) => (
                <>
                  {item?.isGroupChat == 'false' ? (
                    <div
                      style={{
                        backgroundColor: item?.chatId === isActiveUser?.chatId ? '#80808038' : '',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between'
                      }}
                      className="user-friend"
                      onClick={() => handleActiveUser(item)}>
                      <Stack p={1} display="flex" direction="row" spacing={1} mb={1}>
                        <Avatar sx={{ width: '50px', height: '50px' }} src={item.avatar[0]} />
                        <Box>
                          <Typography>{item.chatName}</Typography>
                          <Typography fontSize="15px" fontWeight="500">
                            {item.user[0].roleName}
                          </Typography>
                        </Box>
                      </Stack>
                      <Box pr={1}>
                        {item.read === 'false' ? (
                          <LensIcon
                            color="primary"
                            sx={{
                              fontSize: '16px'
                            }}
                          />
                        ) : (
                          item.read === 'true' && <></>
                        )}
                      </Box>
                    </div>
                  ) : (
                    <div
                      style={{
                        backgroundColor: item?.chatId === isActiveUser?.chatId ? '#80808038' : '',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between'
                      }}
                      className="user-friend"
                      onClick={() => handleActiveUser(item)}>
                      <Stack p={1} display="flex" direction="row" spacing={1} mb={1}>
                        {
                          <AvatarGroup max={2}>
                            {item.avatar.map((image, index) => (
                              <Avatar src={image} key={index} />
                            ))}
                          </AvatarGroup>
                        }
                        <Box>
                          <Typography>{item.chatName}</Typography>
                        </Box>
                      </Stack>
                      <Box pr={1}>
                        {item.read === 'false' ? (
                          <LensIcon
                            color="primary"
                            sx={{
                              fontSize: '16px'
                            }}
                          />
                        ) : (
                          item.read === 'true' && <></>
                        )}
                      </Box>
                    </div>
                  )}
                </>
              ))}
          </Box>
        </Box>
        <Box bgcolor="#fff" borderRadius="10px" flex={1}>
          {isLoadingChat ? (
            <Box display="flex" alignItems="center" justifyContent="center" height="100%">
              <CircularProgress />
            </Box>
          ) : (
            <>
              {isActiveUser !== '' ? (
                <Box
                  p={2}
                  display="flex"
                  flexDirection="column"
                  justifyContent="space-between"
                  gap="10px"
                  height="100%">
                  <div>
                    <Box display="flex" alignItems="center" gap="10px" mb={2}>
                      {isActiveUser?.isGroupChat === 'false' && isActiveUser?.avatar[0] !== null ? (
                        <>
                          <Avatar
                            src={`${isActiveUser?.avatar[0]}`}
                            sx={{ width: '50px', height: '50px' }}
                          />
                          <Box>
                            <Typography>{isActiveUser?.chatName}</Typography>
                            <Typography>{isActiveUser.user[0].roleName}</Typography>
                          </Box>
                        </>
                      ) : isActiveUser?.isGroupChat === 'false' &&
                        isActiveUser?.avatar[0] === null ? (
                        <>
                          <Avatar
                            src={`${isActiveUser?.avatar[0]}`}
                            sx={{ width: '50px', height: '50px' }}
                          />
                          <Box>
                            <Typography>{isActiveUser?.chatName}</Typography>
                            <Typography>Hr</Typography>
                          </Box>
                        </>
                      ) : (
                        isActiveUser?.isGroupChat === 'true' && (
                          <>
                            <Box display="flex" justifyContent="space-between" width="100%">
                              <Box display="flex" alignItems="center" gap="5px" flex={1}>
                                {
                                  <AvatarGroup max={2}>
                                    {isActiveUser.avatar.map((image, index) => (
                                      <Avatar src={image} key={index} />
                                    ))}
                                  </AvatarGroup>
                                }
                                <Box>
                                  <Typography>{isActiveUser?.chatName}</Typography>
                                </Box>
                              </Box>
                              <Box>
                                {isActiveUser?.admin === currentUserId && (
                                  <>
                                    <Tooltip title="Change Admin">
                                      <IconButton onClick={handleOpenChangeAdmin}>
                                        <SupervisorAccountIcon />
                                      </IconButton>
                                    </Tooltip>
                                    <Tooltip title="Update Group">
                                      <IconButton onClick={handleOpenUpdateGroup}>
                                        <EditIcon />
                                      </IconButton>
                                    </Tooltip>
                                  </>
                                )}
                                {isActiveUser?.admin !== currentUserId && (
                                  <Tooltip title="Leave Group">
                                    <IconButton onClick={handleLeaveGroup}>
                                      <ExitToAppIcon />
                                    </IconButton>
                                  </Tooltip>
                                )}
                              </Box>
                            </Box>
                          </>
                        )
                      )}
                    </Box>
                    <Divider />
                  </div>
                  {isActiveUser?.isGroupChat === 'false' ? (
                    <Box sx={{ overflowY: 'auto', pr: 1 }}>
                      {messages.map((item, index) =>
                        item?.myself === true ? (
                          <>
                            <div ref={scroll}>
                              <div className="message own">
                                <div className="messageTop">
                                  <div
                                    style={{
                                      backgroundColor:
                                        item?.type === 'image' ? '#f5f7f9' : '#1877f2'
                                    }}
                                    className="messageText">
                                    {item?.type === 'text' ? (
                                      <Typography
                                        color="#fff"
                                        sx={{ lineHeight: 1.3, letterSpacing: 0 }}>
                                        {item?.message}
                                      </Typography>
                                    ) : item?.type === 'image' ? (
                                      <img
                                        style={{ width: '100%', height: '100%' }}
                                        src={messageImage[index]}
                                        onLoad={() => {
                                          scroll.current.scrollIntoView({
                                            behavior: 'smooth',
                                            block: 'end',
                                            inline: 'nearest'
                                          })
                                        }}
                                      />
                                    ) : (
                                      item?.type === 'file' && (
                                        <div
                                          style={{
                                            display: 'flex',
                                            gap: '5px',
                                            cursor: 'pointer',
                                            color: '#fff'
                                          }}
                                          onClick={() => handleDownloadFile(item)}>
                                          <TextSnippetIcon />
                                          <Typography>{item.message}</Typography>
                                        </div>
                                      )
                                    )}
                                  </div>
                                </div>
                                <Typography
                                  sx={{
                                    lineHeight: 1.3,
                                    letterSpacing: 0,
                                    fontWeight: 500,
                                    fontFamily: 'none',
                                    fontSize: '13px'
                                  }}
                                  alignSelf="flex-end">
                                  {moment(item.createdAt).fromNow()}
                                </Typography>
                              </div>
                            </div>
                          </>
                        ) : (
                          <>
                            <div ref={scroll}>
                              <div style={{ alignItems: 'flex-start' }} className="message">
                                <div className="messageTop">
                                  <div
                                    style={{
                                      backgroundColor:
                                        item?.type === 'image' ? '#f5f7f9' : 'rgb(245, 241, 241)',
                                      color: '#000'
                                    }}
                                    className="messageText">
                                    {item?.type === 'text' ? (
                                      <Typography
                                        color="#000"
                                        sx={{ lineHeight: 1.3, letterSpacing: 0 }}>
                                        {item?.message}
                                      </Typography>
                                    ) : item?.type === 'image' ? (
                                      <img
                                        onLoad={() => {
                                          scroll.current.scrollIntoView({
                                            behavior: 'smooth',
                                            block: 'end',
                                            inline: 'nearest'
                                          })
                                        }}
                                        src={messageImage[index]}
                                      />
                                    ) : (
                                      item?.type === 'file' && (
                                        <div
                                          style={{ display: 'flex', gap: '5px', cursor: 'pointer' }}
                                          onClick={() => handleDownloadFile(item)}>
                                          <TextSnippetIcon />
                                          <Typography>{item.message}</Typography>
                                        </div>
                                      )
                                    )}
                                  </div>
                                </div>
                                <Typography
                                  sx={{
                                    lineHeight: 1.3,
                                    letterSpacing: 0,
                                    fontWeight: 500,
                                    fontFamily: 'none',
                                    fontSize: '12px',
                                    color: '#000',
                                    ml: '10px'
                                  }}
                                  alignSelf="flex-start">
                                  {moment(item.createdAt).fromNow()}
                                </Typography>
                              </div>
                            </div>
                          </>
                        )
                      )}
                    </Box>
                  ) : (
                    <Box sx={{ overflowY: 'auto', pr: 1 }}>
                      {messages.map((item, index) =>
                        item?.myself === true ? (
                          <>
                            <div ref={scroll}>
                              <Box
                                display="flex"
                                justifyContent="flex-end"
                                alignItems="center"
                                gap="5px">
                                <div className="message own">
                                  <div className="messageTop">
                                    <div
                                      style={{
                                        backgroundColor:
                                          item?.type === 'image' ? '#f5f7f9' : '#1877f2'
                                      }}
                                      className="messageText">
                                      {item?.type === 'text' ? (
                                        <>
                                          <Typography
                                            color="#fff"
                                            sx={{ lineHeight: 1.3, letterSpacing: 0 }}>
                                            {item?.message}
                                          </Typography>
                                        </>
                                      ) : item?.type === 'image' ? (
                                        <img
                                          style={{ width: '100%', height: '100%' }}
                                          src={messageImage[index]}
                                          onLoad={() => {
                                            scroll.current.scrollIntoView({
                                              behavior: 'smooth',
                                              block: 'end',
                                              inline: 'nearest'
                                            })
                                          }}
                                        />
                                      ) : (
                                        item?.type === 'file' && (
                                          <div
                                            style={{
                                              display: 'flex',
                                              gap: '5px',
                                              cursor: 'pointer',
                                              color: '#fff'
                                            }}
                                            onClick={() => handleDownloadFile(item)}>
                                            <TextSnippetIcon />
                                            <Typography>{item.message}</Typography>
                                          </div>
                                        )
                                      )}
                                    </div>
                                  </div>
                                  <Typography
                                    sx={{
                                      lineHeight: 1.3,
                                      letterSpacing: 0,
                                      fontWeight: 500,
                                      fontFamily: 'none',
                                      fontSize: '13px'
                                    }}
                                    alignSelf="flex-end">
                                    {moment(item.createdAt).fromNow()}
                                  </Typography>
                                </div>
                              </Box>
                            </div>
                          </>
                        ) : (
                          <>
                            <div ref={scroll}>
                              <Box display="flex" alignItems="center" gap="5px">
                                <Box mt="-14px">
                                  <Tooltip
                                    title={
                                      userAvatar[
                                        userAvatar.findIndex(
                                          (avatar) => avatar.userId === item.senderId
                                        )
                                      ].username
                                    }>
                                    <Avatar
                                      src={
                                        userAvatar[
                                          userAvatar.findIndex(
                                            (avatar) => avatar.userId === item.senderId
                                          )
                                        ].image
                                      }
                                    />
                                  </Tooltip>
                                </Box>
                                <div style={{ alignItems: 'flex-start' }} className="message">
                                  <div className="messageTop">
                                    <div
                                      style={{
                                        backgroundColor:
                                          item?.type === 'image' ? '#f5f7f9' : 'rgb(245, 241, 241)',
                                        color: '#000'
                                      }}
                                      className="messageText">
                                      {item?.type === 'text' ? (
                                        <Typography
                                          color="#000"
                                          sx={{ lineHeight: 1.3, letterSpacing: 0 }}>
                                          {item?.message}
                                        </Typography>
                                      ) : item?.type === 'image' ? (
                                        <img
                                          onLoad={() => {
                                            scroll.current.scrollIntoView({
                                              behavior: 'smooth',
                                              block: 'end',
                                              inline: 'nearest'
                                            })
                                          }}
                                          src={messageImage[index]}
                                        />
                                      ) : (
                                        item?.type === 'file' && (
                                          <div
                                            style={{
                                              display: 'flex',
                                              gap: '5px',
                                              cursor: 'pointer'
                                            }}
                                            onClick={() => handleDownloadFile(item)}>
                                            <TextSnippetIcon />
                                            <Typography>{item.message}</Typography>
                                          </div>
                                        )
                                      )}
                                    </div>
                                  </div>
                                  <Typography
                                    sx={{
                                      lineHeight: 1.3,
                                      letterSpacing: 0,
                                      fontWeight: 500,
                                      fontFamily: 'none',
                                      fontSize: '12px',
                                      color: '#000',
                                      ml: '10px'
                                    }}
                                    alignSelf="flex-start">
                                    {moment(item.createdAt).fromNow()}
                                  </Typography>
                                </div>
                              </Box>
                            </div>
                          </>
                        )
                      )}
                    </Box>
                  )}
                  <div className="chat-sender">
                    <div onClick={() => imageRef.current.click()}>+</div>
                    <InputEmoji value={newMessage} onChange={handleChange} />
                    <IconButton
                      size="large"
                      sx={{ bgcolor: '#1877f2' }}
                      onClick={handleSendMessage}>
                      <SendIcon sx={{ color: '#fff' }} />
                    </IconButton>
                    <input
                      onChange={(e) => handleFileChange(e)}
                      type="file"
                      name=""
                      id=""
                      style={{ display: 'none' }}
                      ref={imageRef}
                    />
                  </div>
                </Box>
              ) : (
                <p style={{ textAlign: 'center' }}>Tap on a chat to start conversation</p>
              )}
            </>
          )}
        </Box>
      </Box>
      <Modal
        open={open}
        onClose={handleClose}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Typography id="modal-modal-title" textAlign="center" fontSize="25px">
            Create New Chat
          </Typography>
          <FormControl sx={{ mt: 1 }}>
            <FormLabel id="demo-controlled-radio-buttons-group">Select option: </FormLabel>
            <RadioGroup
              aria-labelledby="demo-controlled-radio-buttons-group"
              name="controlled-radio-buttons-group"
              value={option}
              onChange={handleChangeSelectOption}>
              <FormControlLabel value="single" control={<Radio />} label="Single Chat" />
              <FormControlLabel value="group" control={<Radio />} label="Group Chat" />
            </RadioGroup>
          </FormControl>
          {option === 'group' ? (
            <>
              <TextField
                sx={{ mt: 2, mb: 3 }}
                value={chatName}
                fullWidth
                label="Chat Name"
                name="chatName"
                onChange={(e) => setChatName(e.target.value)}
              />
              <Autocomplete
                multiple
                id="tags-outlined"
                options={allUser}
                value={selectedUser.accountId}
                getOptionLabel={(option) => option.username}
                onChange={(event, newValue) =>
                  setSelectedUser(newValue.map((user) => user.accountId))
                }
                filterSelectedOptions
                renderInput={(params) => <TextField {...params} label="UserName" />}
              />
            </>
          ) : (
            <Autocomplete
              disablePortal
              id="combo-box-demo"
              options={allUserSingleChat}
              getOptionLabel={(option) => option.username}
              onChange={(event, newValue) => setSelectedUserSingleChat(newValue.accountId)}
              sx={{ mt: 2 }}
              renderInput={(params) => <TextField {...params} label="UserName" />}
            />
          )}
          <TextField
            sx={{ mt: 2, mb: 1 }}
            value={chatNameMessage}
            fullWidth
            label="Message"
            name="message"
            onChange={(e) => setChatNameMessage(e.target.value)}
          />
          <Button variant="contained" sx={{ mt: 2, width: '100%' }} onClick={handleCreateNewChat}>
            Create
          </Button>
        </Box>
      </Modal>
      <Modal
        open={openUpdateGroup}
        onClose={handleCloseUpdateGroup}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Typography id="modal-modal-title" textAlign="center" fontSize="25px">
            Update Group
          </Typography>
          <>
            <TextField
              sx={{ mt: 2, mb: 3 }}
              value={chatName}
              fullWidth
              label="Chat Name"
              name="chatName"
              onChange={(e) => setChatName(e.target.value)}
            />
            <Autocomplete
              multiple
              id="tags-outlined"
              options={allUserUpdate}
              defaultValue={userGroupUpdate}
              value={selectedUserGroup ? selectedUserGroup.accountId : []}
              getOptionLabel={(option) => option.username}
              onChange={(event, newValue) => setSelectedUserGroup(newValue)}
              filterSelectedOptions
              renderInput={(params) => <TextField {...params} label="UserName" />}
            />
          </>
          <Box display="flex" justifyContent="flex-end" gap="5px" mt={3}>
            <Button variant="contained" sx={{ bgcolor: 'green' }} onClick={handleUpdateChat}>
              Update
            </Button>
          </Box>
        </Box>
      </Modal>
      <Modal
        open={openChangeAdmin}
        onClose={handleCloseChangeAdmin}
        aria-labelledby="modal-modal-title"
        aria-describedby="modal-modal-description">
        <Box sx={style}>
          <Typography id="modal-modal-title" textAlign="center" fontSize="25px">
            Change Admin
          </Typography>
          <>
            <FormControl sx={{ mt: 2, width: '100%' }}>
              <InputLabel id="demo-simple-select-helper-label">Username</InputLabel>
              <Select
                labelId="demo-simple-select-helper-label"
                id="demo-simple-select-helper"
                value={userChangeAdmin}
                label="Username"
                onChange={(e) => setUserChangeAdmin(e.target.value)}>
                {isActiveUser &&
                  isActiveUser?.user.map((item) => (
                    <MenuItem key={item.accountId} value={item.accountId}>
                      {item.username}
                    </MenuItem>
                  ))}
              </Select>
            </FormControl>
          </>
          <Box display="flex" justifyContent="flex-end" gap="5px" mt={3}>
            <Button variant="contained" sx={{ bgcolor: 'green' }} onClick={handleChangeAdmin}>
              Save
            </Button>
          </Box>
        </Box>
      </Modal>
    </Box>
  )
}

export default Chat
