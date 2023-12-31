import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn'
import CalendarMonthIcon from '@mui/icons-material/CalendarMonth'
import CalendarTodayIcon from '@mui/icons-material/CalendarToday'
import CalendarViewMonthIcon from '@mui/icons-material/CalendarViewMonth'
import ChecklistRtlIcon from '@mui/icons-material/ChecklistRtl'
import ClearAllIcon from '@mui/icons-material/ClearAll'
import ContactMailIcon from '@mui/icons-material/ContactMail'
import DraftsIcon from '@mui/icons-material/Drafts'
import EventAvailableIcon from '@mui/icons-material/EventAvailable'
import EventNoteIcon from '@mui/icons-material/EventNote'
import FactCheckIcon from '@mui/icons-material/FactCheck'
import ForwardToInboxIcon from '@mui/icons-material/ForwardToInbox'
import GroupIcon from '@mui/icons-material/Group'
import MarkunreadMailboxIcon from '@mui/icons-material/MarkunreadMailbox'
import MenuIcon from '@mui/icons-material/Menu'
import NotificationsIcon from '@mui/icons-material/Notifications'
import PermContactCalendarIcon from '@mui/icons-material/PermContactCalendar'
import UploadIcon from '@mui/icons-material/Upload'
import ViewSidebarIcon from '@mui/icons-material/ViewSidebar'
import { Avatar, Box, Divider, IconButton, Typography } from '@mui/material'
import { getDownloadURL, ref } from 'firebase/storage'
import { useState } from 'react'
import { Menu, MenuItem, Sidebar, SubMenu, useProSidebar } from 'react-pro-sidebar'
import { Link } from 'react-router-dom'
import logoImage from '../../assets/images/vite.jpg'
import { storage } from '../../firebase/config'
import useAuth from '../../hooks/useAuth'
const ManagerSidebar = () => {
  const { collapseSidebar, toggleSidebar, broken, collapsed } = useProSidebar()
  const [activeIndex, setActiveIndex] = useState(() => {
    const initialIndex =
      window.location.pathname === '/request-list-manager'
        ? 0
        : window.location.pathname === '/book-room-manager'
          ? 1
          : window.location.pathname === '/request-manager-list'
            ? 2
            : window.location.pathname === '/notification-list-manager'
              ? 3
              : window.location.pathname === '/notification-draft-manager'
                ? 4
                : window.location.pathname === '/notification-send-manager'
                  ? 5
                  : window.location.pathname === '/notification-receive-manager'
                    ? 6
                    : window.location.pathname === '/notification-scheduled-manager'
                      ? 8
                      : window.location.pathname === '/notification-department-manager'
                        ? 7 : window.location.pathname === '/check-attendance-manager'
                          ? 9
                          : window.location.pathname === '/log-management'
                            ? 10
                            : window.location.pathname === '/emp-log-management'
                              ? 11
                              : window.location.pathname === '/emp-attendance-log-list'
                                ? 12
                                : window.location.pathname === '/manage-user-by-manager'
                                  ? 13
                                  : 0
    return initialIndex
  })
  const [userProfileImage, setUserProfileImage] = useState('')
  const currentUser = useAuth()
  const imgurl = async () => {
    const storageRef = ref(storage, `/${currentUser.image}`)
    try {
      const url = await getDownloadURL(storageRef)
      setUserProfileImage(url)
    } catch (error) {
      console.error('Error getting download URL:', error)
    }
  }
  if (currentUser && currentUser.image) {
    imgurl()
  }
  return (
    <>
      <Sidebar
        style={{
          height: '100%',
          top: 'auto',
          border: 0
        }}
        breakPoint="md"
        backgroundColor="#fff">
        <Box
          display="flex"
          justifyContent="space-between"
          alignItems="center"
          ml="15px"
          height="65px">
          {!collapsed ? (
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
          ) : null}
          <IconButton
            onClick={() => {
              broken ? toggleSidebar() : collapseSidebar()
            }}
            sx={{ color: 'rgb(94, 53, 177)' }}>
            <MenuIcon />
          </IconButton>
        </Box>
        <Divider />
        <Box height="88px" bgcolor="white" display="flex" alignItems="center" ml="20px" gap="20px">
          <Avatar
            sx={{
              cursor: 'pointer',
              height: 40,
              width: 40
            }}
            src={`${userProfileImage}`}
          />
          <div style={{ display: 'flex', flexDirection: 'column' }}>
            <Typography fontSize="17px" fontWeight="600">
              {currentUser.firstName} {currentUser.lastName}
            </Typography>
            <Typography fontSize="15px" fontWeight="600">
              <span style={{ color: 'black' }}>Role: </span>
              <span style={{ color: '#66B2FF' }}>
                {currentUser && currentUser.roleName
                  ? currentUser.roleName.charAt(0).toUpperCase() + currentUser.roleName.slice(1)
                  : 'N/A'}
              </span>
            </Typography>
          </div>
        </Box>
        <Divider />
        {/* <Box mb="25px">
          <Box display="flex" justifyContent="center" alignItems="center">
            {!collapsed ? (
              <img
                alt="profile-user"
                width="100px"
                height="100px"
                src={USERICON}
                style={{
                  cursor: 'pointer',
                  borderRadius: '50%',
                  marginBottom: '20px'
                }}
              />
            ) : null}
          </Box>
          <Box textAlign="center">
            {!collapsed ? (
              <Typography
                color="#000"
                sx={{ m: '10px 0 10px 0', fontSize: '20px', fontWeight: '700' }}>
                Ed Roh
              </Typography>
            ) : null}
            {!collapsed ? (
              <Typography variant="h5" color="#000">
                VP Fancy Admin
              </Typography>
            ) : null}
          </Box>
        </Box> */}
        <Menu
          menuItemStyles={{
            button: ({ active }) => {
              return {
                backgroundColor: active ? 'rgb(237, 231, 246)' : undefined,
                color: '#000',
                '&:hover': {
                  backgroundColor: 'rgb(237, 231, 246)',
                  color: 'rgb(94, 53, 177)',
                  borderRadius: '10px'
                }
              }
            }
          }}>
          <MenuItem
            active={activeIndex === 13}
            icon={<GroupIcon />}
            component={<Link to="/manage-user-by-manager" onClick={() => setActiveIndex(13)} />}>
            {' '}
            Employee Profile
          </MenuItem>
          <SubMenu
            label="Attendance"
            icon={<CalendarMonthIcon />}
          >
            <MenuItem
              active={activeIndex === 9}
              icon={<EventAvailableIcon />}
              component={<Link to="/check-attendance-manager" onClick={() => setActiveIndex(9)} />}>
              {' '}
              Check Your Attendance
            </MenuItem>
            <MenuItem
              active={activeIndex === 10}
              icon={<EventNoteIcon />}
              component={<Link to="/log-management" onClick={() => setActiveIndex(10)} />}>
              {' '}
              Change Log
            </MenuItem>
            <MenuItem
              active={activeIndex === 11}
              icon={<PermContactCalendarIcon />}
              component={<Link to="/emp-log-management" onClick={() => setActiveIndex(11)} />}>
              {' '}
              Employee evaluate
            </MenuItem>
            <MenuItem
              active={activeIndex === 12}
              icon={<ViewSidebarIcon />}
              component={<Link to="/emp-attendance-log-list" onClick={() => setActiveIndex(12)} />}>
              {''}
              Employee Attendance Log
            </MenuItem>
          </SubMenu>
          <SubMenu label="Ticket"
            icon={<FactCheckIcon />}>
            <MenuItem
              active={activeIndex === 0}
              icon={<AssignmentTurnedInIcon />}
              component={<Link to="/request-list-manager" onClick={() => setActiveIndex(0)} />}>
              {' '}
              Ticket Management
            </MenuItem>
            <MenuItem
              active={activeIndex === 2}
              icon={<ChecklistRtlIcon />}
              component={<Link to="/request-manager-list" onClick={() => setActiveIndex(2)} />}>
              {' '}
              Check Your Ticket
            </MenuItem>
          </SubMenu>
          <SubMenu
            label="Notification"
            icon={<NotificationsIcon />}
          >
            <MenuItem
              active={activeIndex === 3}
              icon={<ClearAllIcon />}
              component={<Link to="/notification-list-manager" onClick={() => setActiveIndex(3)} />}>
              {' '}
              All Notification
            </MenuItem>
            <MenuItem
              active={activeIndex === 4}
              icon={<DraftsIcon />}
              component={<Link to="/notification-draft-manager" onClick={() => setActiveIndex(4)} />}>
              {' '}
              Draft
            </MenuItem>
            <SubMenu
              label='Send&Receive'
              icon={<UploadIcon />}>
              <MenuItem
                active={activeIndex === 5}
                icon={<ForwardToInboxIcon />}
                component={<Link to="/notification-send-manager" onClick={() => setActiveIndex(5)} />}>
                {' '}
                Send
              </MenuItem>
              <MenuItem
                active={activeIndex === 6}
                icon={<MarkunreadMailboxIcon />}
                component={<Link to="/notification-receive-manager" onClick={() => setActiveIndex(6)} />}>
                {' '}
                Receive
              </MenuItem>
            </SubMenu>

            <SubMenu
              label='Scheduled'
              icon={<CalendarTodayIcon />}
            >
              <MenuItem
                active={activeIndex === 8}
                icon={<ContactMailIcon />}
                component={<Link to="/notification-schedule-manager" onClick={() => setActiveIndex(8)} />}>
                {' '}
                Personal
              </MenuItem>
            </SubMenu>
          </SubMenu>

          <MenuItem
            active={activeIndex === 1}
            icon={<CalendarViewMonthIcon />}
            component={<Link to="/book-room-manager" onClick={() => setActiveIndex(1)} />}>
            {' '}
            Book Room
          </MenuItem>
        </Menu>
      </Sidebar>
    </>
  )
}
export default ManagerSidebar
