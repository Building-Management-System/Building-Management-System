import {
  Box,
  Button,
  FormControl,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography
} from '@mui/material'
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers'
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import { format } from 'date-fns'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import useAuth from '../../../hooks/useAuth'
import logApi from '../../../services/logApi'
import userApi from '../../../services/userApi'
import formatDate from '../../../utils/formatDate'
import ChatTopbar from '../../common/chat/components/ChatTopbar'
import DataTableListChangeLog from './component/DataTable'
import { useParams } from 'react-router-dom';

const EmpLogEvaluate = () => {
  const [isLoading, setIsLoading] = useState(false)
  const [month, setMonth] = useState(new Date())
  const [listLog, setListLog] = useState([])
  const [listEmployees, setListEmployees] = useState([])
  const [employee, setEmployee] = useState('')
  const navigate = useNavigate()
  const userInfo = useAuth()
  const {date} = useParams()
  useEffect(() => {
    const getListEmpByDepartment = async () => {
      try {
        if (!userInfo || !userInfo.departmentId) {
          console.error('User information or departmentId is missing.')
          return
        }
        const response = await userApi.getAllEmployeeByDepartmentId(userInfo.departmentId)
        setListEmployees(response || [])
      } catch (error) {
        console.error('Error fetching employee list:', error)
      }
    }
    getListEmpByDepartment()
  }, [userInfo?.departmentId])
  const [isSearchClicked, setIsSearchClicked] = useState(false)
  const [employeeNames, setEmployeeNames] = useState([])
  useEffect(() => {
    const fetchEmployeeNames = async () => {
      try {
        const response = await userApi.getAllEmployeeNames(
          userInfo.departmentId,
          format(month, 'MM'),
          format(month, 'yyyy')
        )
        console.log('API Response:', response)
        setEmployeeNames(response || [])
      } catch (error) {
        console.error('Error fetching employee names:', error)
      }
    }

    fetchEmployeeNames()
  }, [userInfo?.departmentId, month])
  console.log('Employee Names:', employeeNames)
  const handleSearchLog = async () => {
    setIsLoading(true)
    setIsSearchClicked(true)
    try {
      if (!userInfo || !userInfo.departmentId) {
        console.error('User information or departmentId is missing.')
        return
      }

      const data = {
        departmentId: userInfo.departmentId,
        month: format(month, 'MM'),
        year: format(month, 'yyyy')
      }
      const response = await logApi.getEvaluateOfDepartment(
        data.departmentId,
        data.month,
        data.year
      )
      if (response.length === 0) {
        toast.error("This month haven't had evaluate yet")
      }

      setListLog(response)
      console.log(response)
      console.log(data.departmentId)
      console.log(data.month)
      console.log(data.year)
    } catch (error) {
      console.error('Error fetching department evaluation:', error)
    } finally {
      setIsLoading(false)
    }
  }
  useEffect(() => {
    console.log('Is Loading:', isLoading)
    console.log('List Log:', listLog)
  }, [isLoading, listLog])

  const columns = [
    {
      field: 'employeeUserName',
      headerName: 'Account',
      width: 150
    },
    {
      field: '',
      headerName: 'Name',
      width: 200,
      renderCell: (params) => {
        return (
          <Box>
            <Typography>
              {params.row.firstNameEmp} {params.row.lastNameEmp}
            </Typography>
          </Box>
        )
      }
    },
    {
      field: 'totalAttendance',
      headerName: 'Attendance(h)',
      width: 150
    },
    {
      field: 'workingDay',
      headerName: 'Working Day(day)',
      width: 180
    },
    {
      field: 'permittedLeave',
      headerName: 'Leave(h)',
      width: 150
    },
    {
      field: 'lateCheckin',
      headerName: 'Late(h)',
      width: 150
    },
    {
      field: 'overTime',
      headerName: 'Overtime(h)',
      width: 150
    },
    {
      field: 'violate',
      headerName: 'Violate(s)',
      width: 150
    },
    {
      field: 'evaluateEnum',
      headerName: 'Rate',
      width: 150,
      renderCell: (params) => {
        const evaluateEnum = params.row.evaluateEnum
        let textColor = ''
        let displayText = ''

        switch (evaluateEnum) {
          case 'BAD':
            textColor = 'red'
            displayText = 'BAD'
            break
          case 'GOOD':
            textColor = 'green'
            displayText = 'GOOD'
            break
          case 'NORMAL':
            textColor = '#00BFFF'
            displayText = 'NORMAL'
            break
          case null:
            textColor = 'black'
            displayText = 'unrate'
        }

        return <Typography style={{ color: textColor }}>{displayText}</Typography>
      }
    },
    {
      field: 'approvedDate',
      headerName: 'Approve Date',
      width: 180,
      renderCell: (params) => {
        if (params.row.approvedDate !== null || params.row.approvedDate === '1970-01-01 08:00:00') {
          return <Box>{formatDate(params.row.approvedDate)}</Box>
        } else {
          return <Box></Box>
        }
      }
    },

    {
      field: 'status',
      headerName: 'Status',
      width: 150,
      renderCell: (params) => {
        if (params.row.approvedDate !== null || params.row.approvedDate === '1970-01-01 08:00:00') {
          if (params.row.status === true) {
            return <Typography style={{ color: 'green' }}>Accepted</Typography>
          } else if (params.row.status === false) {
            return <Typography style={{ color: 'red' }}>Rejected</Typography>
          }
        } else {
          return <Typography></Typography>
        }
      }
    },

    {
      field: 'action',
      headerName: 'Action',
      headerAlign: 'center',
      align: 'center',
      width: 200,
      sortable: false,
      filterable: false,
      renderCell: (params) => {
        const buttonStyle = {
          width: '80px',
          marginLeft: '10px',
          fontSize: '12px'
        }
        if (
          params.row.evaluateEnum !== null &&
          params.row.approvedDate !== null &&
          params.row.status === false
        ) {
          return (
            <>
              <Button
                variant="contained"
                onClick={() =>
                  navigate(
                    `/log-attendance-emp/${params.row.employeeId}/${format(month, 'yyyy-MM')}`
                  )
                }
                style={buttonStyle}>
                Detail
              </Button>
              <Button
                variant="contained"
                onClick={() =>
                  navigate(`/update-evaluate/${params.row.employeeId}/${format(month, 'yyyy-MM')}`)
                }
                style={{ backgroundColor: 'red', fontSize: '12px', marginLeft: '10px' }}>
                Re-Evaluate
              </Button>
            </>
          )
        } else if (
          params.row.evaluateEnum !== null &&
          params.row.approvedDate === null &&
          params.row.status === false
        ) {
          return (
            <>
              <Button
                variant="contained"
                onClick={() =>
                  navigate(
                    `/log-attendance-emp/${params.row.employeeId}/${format(month, 'yyyy-MM')}`
                  )
                }
                style={buttonStyle}>
                Detail
              </Button>
              <Box sx={{ marginLeft: '0px' }}>
                <Button
                  variant="contained"
                  onClick={() =>
                    navigate(
                      `/update-evaluate/${params.row.employeeId}/${format(month, 'yyyy-MM')}`
                    )
                  }
                  style={buttonStyle}>
                  Edit
                </Button>
              </Box>
            </>
          )
        } else if (
          params.row.evaluateEnum !== null &&
          params.row.approvedDate !== null &&
          params.row.status === true
        ) {
          return (
            <>
              <Button
                variant="contained"
                onClick={() =>
                  navigate(
                    `/log-attendance-emp/${params.row.employeeId}/${format(month, 'yyyy-MM')}`
                  )
                }
                style={buttonStyle}>
                Detail
              </Button>
            </>
          )
        }
      }
    }
  ]
  return (
    <>
      <ChatTopbar />
      <Box p={3}>
      <Typography
        fontSize='30px'
        color="#000"
        fontWeight="bold"
        sx={{ m: "0 0 5px 0" }}
      >
        Evaluate employee (Date: {date})
      </Typography>
        <Box display="flex" alignItems="center" gap={3}>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
              maxDate={new Date()}
              value={month}
              views={['month', 'year']}
              onChange={(newDate) => setMonth(newDate.toDate())}
              renderInput={(props) => <TextField sx={{ width: '20%' }} {...props} />}
            />
          </LocalizationProvider>
          <Box>
            <Button variant="outlined" onClick={handleSearchLog}>
              Search
            </Button>
          </Box>
        </Box>

        <Box display="flex" alignItems="center" mt={3} sx={{ marginLeft: 'auto' }}>
          {isSearchClicked && (
            <FormControl sx={{ width: '280px' }}>
              <InputLabel id="demo-simple-select-label">Employee Remaining</InputLabel>
              <Select
                labelId="demo-simple-select-label"
                label="Employee Remaining"
                value={employee}
                onChange={(event) => {
                  const selectedEmployeeId = event.target.value
                  setEmployee(selectedEmployeeId)
                  navigate(`/create-evaluate/${selectedEmployeeId}/${format(month, 'yyyy-MM')}`)
                }}>
                {employeeNames.map((item, index) => (
                  <MenuItem key={index} value={item.employeeId}>
                    {item.userName}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
          )}
          {isSearchClicked &&
            listLog.length > 0 &&
            listLog[0].department &&
            listLog[0].department.departmentName && (
              <Typography variant="h6" sx={{ marginLeft: '30px' }}>
                <span>Department: </span>
                <span style={{ color: 'red' }}>{listLog[0].department.departmentName}</span>
                <span style={{ color: 'red' }}>{listEmployees[0].departmentName}</span>
              </Typography>
            )}
        </Box>

        {listLog && listLog.length !== 0 && (
          <Box mt={3}>
            <DataTableListChangeLog rows={listLog} columns={columns} isLoading={isLoading} />
          </Box>
        )}
      </Box>
    </>
  )
}

export default EmpLogEvaluate
