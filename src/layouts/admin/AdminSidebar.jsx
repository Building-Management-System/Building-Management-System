import AnalyticsIcon from '@mui/icons-material/Analytics'
import DashboardIcon from '@mui/icons-material/Dashboard'
import MenuIcon from '@mui/icons-material/Menu'
import SourceIcon from '@mui/icons-material/Source'
import StyleIcon from '@mui/icons-material/Style'
import { Avatar, Box, Divider, IconButton, Typography } from '@mui/material'
import { Menu, MenuItem, Sidebar, SubMenu, useProSidebar } from 'react-pro-sidebar'

import AVATAR from '../../assets/images/user.png';
import SettingsIcon from '@mui/icons-material/Settings'
import { Link } from 'react-router-dom'
import { useState } from 'react'
const AdminSidebar = () => {
  const { collapseSidebar, toggleSidebar, broken, collapsed } = useProSidebar()
  const [activeIndex, setActiveIndex] = useState(() => { 
    const initialIndex = 
      window.location.pathname === '/admin' ? 0 
      : window.location.pathname === '/admin/profile' ? 4 : 
      window.location.pathname === '/admin/change-password' ? 5  
          : 0; 
    return initialIndex; 
  });
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
        <Box display="flex" justifyContent="space-between" alignItems="center" ml="15px" mb="25px">
          {!collapsed ? (
            <Typography variant="h3" color="#000">
              ADMINIS
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
        <Box height="60px" bgcolor="white" display='flex' alignItems='center' ml="20px" gap='20px'>
        <Avatar
              sx={{
                cursor: 'pointer',
                height: 40,
                width: 40
              }}
              src={AVATAR}
            />
              <Typography fontSize='15px' fontWeight='600'>Cristiano Ronaldo</Typography>
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
              active={activeIndex === 0}
              icon={<DashboardIcon />}
              component={<Link to="/admin" onClick={() => setActiveIndex(0)} />}>
              {' '}
              Dashboard{' '}
            </MenuItem>
            <MenuItem icon={<SourceIcon />} component={<Link to="/admin/contact"  />}>
              {' '}
              Contact{' '}
            </MenuItem>
            <MenuItem icon={<AnalyticsIcon />}> Analytics </MenuItem>
            <MenuItem active={activeIndex === 4} icon={<StyleIcon />} component={<Link to="/admin/profile" onClick={() => setActiveIndex(4)} />}>
              {' '}
              Profile{' '}
            </MenuItem>
            <MenuItem active={activeIndex === 5} icon={<SettingsIcon />} component={<Link to="/admin/change-password" onClick={() => setActiveIndex(5)} />}>
              {' '}
              Change Password <Link to="/FAQ"/>
            </MenuItem>
            <SubMenu label="Charts" icon={<DashboardIcon />}>
              <MenuItem icon={<StyleIcon fontSize="small" />}> Pie charts </MenuItem>
              <MenuItem icon={<SettingsIcon fontSize="small" />}> Pie charts </MenuItem>
            </SubMenu>
          </Menu>
          

      </Sidebar>
    </>
  )
}

export default AdminSidebar
