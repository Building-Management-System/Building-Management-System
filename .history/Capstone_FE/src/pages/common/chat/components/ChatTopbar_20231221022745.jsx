import ChatIcon from '@mui/icons-material/Chat'
import { Box, Divider, IconButton, Typography, Link, Avatar, Badge } from '@mui/material'
import AccountPopover from '../../../../components/AccountPopover'
import NotificationsPopover from '../../../../components/NotificationsPopover'
import { Link as LinkRouter } from 'react-router-dom'
import { useSelector } from 'react-redux'
import logoImage from '../../../../assets/images/vite.jpg'
import { useEffect, useState } from 'react'
import chatApi from '../../../../services/chatApi'
const ChatTopbar = () => {
  const currentUser = useSelector((state) => state.auth.login?.currentUser)
  const [allChatList, setAllChatList] = useState([])
  useEffect(() => {
    const fetchAllChatList = async () => {
      const data = {
        userId: currentUser.accountId
      }
      const response = await chatApi.getAllChatList(data)
      setAllChatList(response)
    }
    fetchAllChatList()
  }, [])

  const countUnreadChats = () => {
    // Use Array.filter to get an array of chats where read is true
    const readChats = allChatList.filter(chat => chat.read === "false");
    // Get the length of the filtered array to get the count of read chats
    const count = readChats.length;
  
    return count;
  };

  const badgeCount = countUnreadChats();

  return (
    <>
      <Box
        display="flex"
        justifyContent="space-between"
        alignItems="center"
        px={2}
        height="65px"
        bgcolor="#fff">
        {currentUser?.role === 'hr' ? (
          <LinkRouter to="/manage-user" style={{ textDecoration: 'none' }}>
            <Typography sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
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
              <Typography fontWeight="800" color="#000" fontSize="22px">
                BMS
              </Typography>
            </Typography>
          </LinkRouter>
        ) : currentUser?.role === 'employee' ? (
          <LinkRouter to="/check-attendance-employee" style={{ textDecoration: 'none' }}>
            <Typography sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
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
              <Typography fontWeight="800" color="#000" fontSize="22px">
                BMS
              </Typography>
            </Typography>
          </LinkRouter>
        ) : currentUser?.role === 'manager' ? (
          <LinkRouter to="/request-list-manager" style={{ textDecoration: 'none' }}>
            <Typography sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
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
              <Typography fontWeight="800" color="#000" fontSize="22px">
                BMS
              </Typography>
            </Typography>
          </LinkRouter>
        ) : currentUser?.role === 'admin' ? (
          <LinkRouter to="/request-list-admin" style={{ textDecoration: 'none' }}>
            <Typography sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
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
              <Typography fontWeight="800" color="#000" fontSize="22px">
                BMS
              </Typography>
            </Typography>
          </LinkRouter>
        ) : currentUser?.role === 'security' ? (
          <LinkRouter to="/ticket-list-security" style={{ textDecoration: 'none' }}>
            <Typography sx={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}>
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
              <Typography fontWeight="800" color="#000" fontSize="22px">
                BMS
              </Typography>
            </Typography>
          </LinkRouter>
        ) : (
          <></>
        )}
        <Box display="flex" gap="10px" alignItems="center">
          <NotificationsPopover />
          <Link href="/chat">
            <IconButton sx={{ color: 'rgb(94, 53, 177)' }} size="small">
              <Badge
                badgeContent={
                  badgeCount
                }
                color="error">
                <ChatIcon />
              </Badge>
            </IconButton>
          </Link>
          <AccountPopover />
        </Box>
      </Box>
      <Divider />
    </>
  )
}

export default ChatTopbar
