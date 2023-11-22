import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
import RemoveRedEyeIcon from '@mui/icons-material/RemoveRedEye'
import { InputAdornment, InputLabel, Skeleton } from '@mui/material'
import Box from '@mui/material/Box'
import Collapse from '@mui/material/Collapse'
import IconButton from '@mui/material/IconButton'
import Paper from '@mui/material/Paper'
import Table from '@mui/material/Table'
import TableBody from '@mui/material/TableBody'
import TableCell from '@mui/material/TableCell'
import TableContainer from '@mui/material/TableContainer'
import TableHead from '@mui/material/TableHead'
import TablePagination from '@mui/material/TablePagination'
import TableRow from '@mui/material/TableRow'
import TextField from '@mui/material/TextField'
import Typography from '@mui/material/Typography'
import * as React from 'react'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuth from '../../../hooks/useAuth'
import requestApi from '../../../services/requestApi'
import AccessTimeFilledIcon from '@mui/icons-material/AccessTimeFilled';
function formatDate(date) {
  const createDate = new Date(date);
  const year = createDate.getFullYear().toString().slice(-2);
  const month = String(createDate.getMonth() + 1).padStart(2, '0');
  const day = String(createDate.getDate()).padStart(2, '0');
  const hours = String(createDate.getHours()).padStart(2, '0');
  const minutes = String(createDate.getMinutes()).padStart(2, '0');
  const seconds = String(createDate.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}
function Row(props) {
  const { row } = props
  const [open, setOpen] = React.useState(false)

  const navigate = useNavigate()
  return (
    <>
      <TableRow sx={{ '& > *': { borderBottom: 'unset' } }}>
        <TableCell>
          <IconButton aria-label="expand row" size="small" onClick={() => setOpen(!open)}>
            {open ? <KeyboardArrowUpIcon /> : <KeyboardArrowDownIcon />}
          </IconButton>
        </TableCell>
        <TableCell component="th" scope="row">
          {row.ticketId.slice(0, 10)}
        </TableCell>
        <TableCell component="th" scope="row">
          {row.topic}
        </TableCell>
        <TableCell>{row.requestTickets[row.requestTickets.length - 1].title}</TableCell>
        <TableCell>{formatDate(row.createDate)}</TableCell>
        <TableCell>{formatDate(row.updateDate)}</TableCell>
        <TableCell> {row.status === false ? (
          <Box
            width="85%"
            margin="0 auto"
            p="5px"
            display="flex"
            justifyContent="center"
            alignItems="center"
            borderRadius="4px">
            <Typography color="#a9a9a9">CLOSE</Typography>
          </Box>
        ) : row.status === true ? (
          <Box
            width="85%"
            margin="0 auto"
            p="5px"
            display="flex"
            justifyContent="center"
            alignItems="center"
            borderRadius="4px">
            <Typography color="#000">AVALIABLE</Typography>
          </Box>
        ) : null}</TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1 }}>
              <Typography variant="h6" gutterBottom component="div">
                Request
              </Typography>
              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    <TableCell style={{ width: '120px' }}>Request ID</TableCell>
                    <TableCell style={{ width: '200px' }} align="center">Status</TableCell>
                    <TableCell style={{ width: '50px' }}>Receiver</TableCell>
                    <TableCell style={{ width: '100px' }} >Create Date</TableCell>
                    <TableCell style={{ width: '100px' }} >Update Date</TableCell>
                    <TableCell style={{ width: '100px' }}>Action</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {row.requestTickets.map((request_row) => (
                    <TableRow key={request_row.request_id}>
                      <TableCell component="th" scope="row">
                        {request_row.requestId.slice(0, 10)}
                      </TableCell>
                      <TableCell>
                        {request_row.requestStatus === 'PENDING' ? (
                          <Box
                            width="80%"
                            margin="0 auto"
                            p="5px"
                            display="flex"
                            justifyContent="center"
                            alignItems="center"
                            bgcolor={'#FAFAD2'}
                            borderRadius="4px"
                          >
                            <AccessTimeFilledIcon />
                            <Typography color="#000">{request_row.requestStatus}</Typography>
                          </Box>
                        ) : request_row.requestStatus === 'ANSWERED' ? (
                          <Box
                            width="80%"
                            margin="0 auto"
                            p="5px"
                            display="flex"
                            justifyContent="center"
                            alignItems="center"
                            bgcolor={'#2e7c67'}
                            borderRadius="4px"
                          >
                            <AccessTimeFilledIcon />
                            <Typography color="#fff">{request_row.requestStatus}</Typography>
                          </Box>
                        ) : request_row.requestStatus === 'EXECUTING' ? (
                          <Box
                            width="80%"
                            margin="0 auto"
                            p="5px"
                            display="flex"
                            justifyContent="center"
                            alignItems="center"
                            bgcolor={'#6495ED'}
                            borderRadius="4px"
                          >
                            <AccessTimeFilledIcon />
                            <Typography color="#000">{request_row.requestStatus}</Typography>
                          </Box>
                        ) : request_row.requestStatus === 'CLOSED' ? (
                          <Box
                            width="80%"
                            margin="0 auto"
                            p="5px"
                            display="flex"
                            justifyContent="center"
                            alignItems="center"
                            bgcolor={'#C0C0C0'}
                            borderRadius="4px"
                          >
                            <AccessTimeFilledIcon />
                            <Typography color="#000">{request_row.requestStatus}</Typography>
                          </Box>
                        ) : null}
                      </TableCell>
                      <TableCell>{request_row.receiverFirstName} {request_row.receiverLastName}</TableCell>
                      <TableCell>{formatDate(request_row.requestCreateDate)}</TableCell>
                      <TableCell>{formatDate(request_row.requestUpdateDate)}</TableCell>
                      <TableCell>
                        {' '}
                        <IconButton

                          sx={{ color: '#1565c0' }}
                          onClick={() =>
                            navigate(`/request-detail/${request_row.requestId}`)
                          }>
                          <RemoveRedEyeIcon />
                        </IconButton>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  )
}



const TableRowsLoader = ({ rowsNum }) => {
  return [...Array(rowsNum)].map((row, index) => (
    <TableRow key={index}>
      <TableCell component="th" scope="row">
        <Skeleton animation="wave" variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton animation="wave" variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton animation="wave" variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton animation="wave" variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton animation="wave" variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton animation="wave" variant="text" />
      </TableCell>
      <TableCell>
        <Skeleton animation="wave" variant="text" />
      </TableCell>
    </TableRow>
  ))
}


export default function RequestListManager() {
  const [listRequestAndTicket, setListRequestAndTicket] = useState([])
  const [page, setPage] = useState(0)
  const [rowsPerPage, setRowsPerPage] = useState(5)
  const [searchTerm, setSearchTerm] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const handleChangePage = (event, newPage) => {
    setPage(newPage)
  }

  const handleChangeRowsPerPage = (event) => {
    setRowsPerPage(parseInt(event.target.value, 10))
    setPage(0)
  }

  const userInfo = useAuth();

  useEffect(() => {
    setIsLoading(true)
    const fetchListRequestAndTicketByAdmin = async () => {
      const response = await requestApi.getTicketDepartment(userInfo.departmentName)
      setListRequestAndTicket(response)
      setIsLoading(false)

    }
    fetchListRequestAndTicketByAdmin()

  }, [userInfo.departmentName])

  return (
    <Box display="flex" height="100vh" bgcolor="rgb(238, 242, 246)">
      <Box flex={1} sx={{ overflowX: 'hidden' }}>
        <Paper elevation={3} sx={{ padding: '16px' }}>
          <TextField
            label="Search"
            value={searchTerm}
            fullWidth
            onChange={(e) => setSearchTerm(e.target.value)}
            InputLabelProps={{
              shrink: true,
            }}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <InputLabel >
                    Title, Topic, Date, ID
                  </InputLabel>
                </InputAdornment>
              ),
            }}
          />
        </Paper>

        <TableContainer component={Paper} sx={{ marginTop: '16px' }}>
          <Table aria-label="collapsible table">
            <TableHead>
              <TableRow>
                <TableCell style={{ width: '10px' }} /> {/* Adjust the width as needed */}
                <TableCell style={{ width: '160px', fontWeight: 'bold', fontSize: '18px' }}>
                  TicketID
                </TableCell>
                <TableCell style={{ width: '160px', fontWeight: 'bold', fontSize: '18px' }}>
                  Topic
                </TableCell>
                <TableCell style={{ width: '200px', fontWeight: 'bold', fontSize: '18px' }}>
                  Title
                </TableCell>
                <TableCell style={{ width: '150px', fontWeight: 'bold', fontSize: '18px' }}>
                  Create Date
                </TableCell>
                <TableCell style={{ width: '150px', fontWeight: 'bold', fontSize: '18px' }}>
                  Update Date
                </TableCell>
                <TableCell align='center' style={{ width: '100px', fontWeight: 'bold', fontSize: '18px' }}>
                  Status
                </TableCell>

              </TableRow>
            </TableHead>
            {isLoading ? (
              <TableRowsLoader rowsNum={5} />
            ) : (
              <TableBody>
                {listRequestAndTicket
                  .filter((row) => {
                    const searchString = searchTerm.toLowerCase();
                    const statusLowerCase = typeof row.status === 'string' ? row.status.toLowerCase() : '';

                    return (
                      row.ticketId.toLowerCase().includes(searchString) ||
                      row.topic.toLowerCase().includes(searchString) ||
                      row.requestTickets[row.requestTickets.length - 1].title.toLowerCase().includes(searchString) ||
                      formatDate(row.createDate).toLowerCase().includes(searchString) ||
                      formatDate(row.updateDate).toLowerCase().includes(searchString) ||
                      (statusLowerCase === "avaliable" || statusLowerCase === "close")
                    );
                  })
                  .filter((row) => row.topic !== 'ROOM_REQUEST')
                  .slice(page * rowsPerPage, (page + 1) * rowsPerPage)
                  .map((row) => (
                    <Row key={row.ticketId} row={row} />
                  ))}
              </TableBody>
            )}
          </Table>
          <TablePagination
            component="div"
            count={listRequestAndTicket
              .filter((row) => row.topic !== 'ROOM_REQUEST')
              .length}
            page={page}
            onPageChange={handleChangePage}
            rowsPerPage={rowsPerPage}
            onRowsPerPageChange={handleChangeRowsPerPage}
          />
        </TableContainer>
      </Box>
    </Box>
  )
}
