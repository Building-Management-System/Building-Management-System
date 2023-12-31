import AodIcon from '@mui/icons-material/Aod'
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn'
import CalendarTodayIcon from '@mui/icons-material/CalendarToday'
import ChecklistRtlIcon from '@mui/icons-material/ChecklistRtl'
import ClearAllIcon from '@mui/icons-material/ClearAll'
import ContactMailIcon from '@mui/icons-material/ContactMail'
import DensitySmallIcon from '@mui/icons-material/DensitySmall'
import DraftsIcon from '@mui/icons-material/Drafts'
import FactCheckIcon from '@mui/icons-material/FactCheck'
import ForwardToInboxIcon from '@mui/icons-material/ForwardToInbox'
import MarkunreadMailboxIcon from '@mui/icons-material/MarkunreadMailbox'
import MenuIcon from '@mui/icons-material/Menu'
import NotificationsIcon from '@mui/icons-material/Notifications'
import UploadIcon from '@mui/icons-material/Upload'
import DirectionsWalkIcon from '@mui/icons-material/DirectionsWalk';
import CameraFrontIcon from '@mui/icons-material/CameraFront';
import NoAccountsIcon from '@mui/icons-material/NoAccounts';
import { Avatar, Box, Divider, IconButton, Typography } from '@mui/material'
import { getDownloadURL, ref } from 'firebase/storage'
import { useState } from 'react'
import { Menu, MenuItem, Sidebar, SubMenu, useProSidebar } from 'react-pro-sidebar'
import { Link } from 'react-router-dom'
import { storage } from '../../firebase/config'
import useAuth from '../../hooks/useAuth'
import logoImage from '../../assets/images/vite.jpg'
const SecuritySideBar = () => {
    const { collapseSidebar, toggleSidebar, broken, collapsed } = useProSidebar()
    const [activeIndex, setActiveIndex] = useState(() => {
        const initialIndex =
            window.location.pathname === '/ticket-list-security'
                ? 0
                : window.location.pathname === '/security-ticket'
                    ? 2
                    : window.location.pathname === '/notification-list-security'
                        ? 3
                        : window.location.pathname === '/notification-draft-security'
                            ? 4
                            : window.location.pathname === '/notification-send-security'
                                ? 5
                                : window.location.pathname === '/notification-receive-security'
                                    ? 6
                                    : window.location.pathname === '/notification-scheduled-security'
                                        ? 8
                                        : window.location.pathname === '/notification-department-security'
                                            ? 7
                                            : window.location.pathname === '/control-log-security'
                                                ? 9
                                                : window.location.pathname === '/stranger-log-security'
                                                    ? 10
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
                    <SubMenu label="Ticket"
                        icon={<FactCheckIcon />}>
                        <MenuItem
                            active={activeIndex === 0}
                            icon={<ChecklistRtlIcon />}
                            component={<Link to="/ticket-list-security" onClick={() => setActiveIndex(0)} />}>
                            {' '}
                            Ticket Management
                        </MenuItem>
                        <MenuItem
                            active={activeIndex === 2}
                            icon={<AssignmentTurnedInIcon />}
                            component={<Link to="/security-ticket" onClick={() => setActiveIndex(2)} />}>
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
                            component={<Link to="/notification-list-security" onClick={() => setActiveIndex(3)} />}>
                            {' '}
                            All Notification
                        </MenuItem>
                        <MenuItem
                            active={activeIndex === 4}
                            icon={<DraftsIcon />}
                            component={<Link to="/notification-draft-security" onClick={() => setActiveIndex(4)} />}>
                            {' '}
                            Draft
                        </MenuItem>
                        <SubMenu
                            label='Send&Receive'
                            icon={<UploadIcon />}>
                            <MenuItem
                                active={activeIndex === 5}
                                icon={<ForwardToInboxIcon />}
                                component={<Link to="/notification-send-security" onClick={() => setActiveIndex(5)} />}>
                                {' '}
                                Send
                            </MenuItem>
                            <MenuItem
                                active={activeIndex === 6}
                                icon={<MarkunreadMailboxIcon />}
                                component={<Link to="/notification-receive-security" onClick={() => setActiveIndex(6)} />}>
                                {' '}
                                Receive
                            </MenuItem>
                        </SubMenu>

                        <SubMenu
                            label='Scheduled'
                            icon={<CalendarTodayIcon />}
                        >
                            <MenuItem
                                active={activeIndex === 7}
                                icon={<DensitySmallIcon />}
                                component={<Link to="/notification-department-security" onClick={() => setActiveIndex(7)} />}>
                                {' '}
                                All
                            </MenuItem>
                            <MenuItem
                                active={activeIndex === 8}
                                icon={<ContactMailIcon />}
                                component={<Link to="/notification-schedule-security" onClick={() => setActiveIndex(8)} />}>
                                {' '}
                                Personal
                            </MenuItem>
                        </SubMenu>

                    </SubMenu>

                    <SubMenu
                        label='LCD Log'
                        icon={<AodIcon />}
                    >
                        <MenuItem
                            active={activeIndex === 11}
                            icon={<CameraFrontIcon />}
                            component={<Link to="/security-viewlog-staff" onClick={() => setActiveIndex(11)} />}>
                            {' '}
                            View Log
                        </MenuItem>

                        <MenuItem
                            active={activeIndex === 9}
                            icon={<DirectionsWalkIcon />}
                            component={<Link to="/control-log-security" onClick={() => setActiveIndex(9)} />}>
                            {' '}
                            Control Log
                        </MenuItem>
                        <MenuItem
                            active={activeIndex === 10}
                            icon={<NoAccountsIcon />}
                            component={<Link to="/stranger-log-security" onClick={() => setActiveIndex(10)} />}>
                            {' '}
                            Stranger Log
                        </MenuItem>

                    </SubMenu>
                </Menu>



            </Sidebar>
        </>
    )
}
export default SecuritySideBar
