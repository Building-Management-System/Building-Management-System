import AccessTimeFilledIcon from '@mui/icons-material/AccessTimeFilled'
import CloseIcon from '@mui/icons-material/Close'
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
import RemoveRedEyeIcon from '@mui/icons-material/RemoveRedEye'
import { Button, Skeleton } from '@mui/material'
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
import { useEffect, useState } from 'react'
import requestApi from '../../../services/requestApi'
import { useNavigate } from 'react-router-dom'
function Row(props) {
  const { row } = props
  const [open, setOpen] = useState(false)
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
        <TableCell>{row.createDate}</TableCell>
        <TableCell>{row.updateDate}</TableCell>
        <TableCell>
          {row.status === false ? (
            <Box
              width="80%"
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
              width="80%"
              margin="0 auto"
              p="5px"
              display="flex"
              justifyContent="center"
              alignItems="center"
              borderRadius="4px">
              <Typography color="#000">AVALIABLE</Typography>
            </Box>
          ) : null}
        </TableCell>
        <TableCell>
          {row.topic === 'OTHER_REQUEST' ? (
            <Box
              width="80%"
              margin="0 auto"
              p="5px"
              display="flex"
              justifyContent="center"
              bgcolor={''}
              borderRadius="4px">
              <Button>
                <CloseIcon />
                <Typography fontSize={'13px'} color="#000">
                  Finish
                </Typography>
              </Button>
            </Box>
          ) : null}
        </TableCell>
        {/* <TableCell style={{ width: '20px', fontWeight: 'bold', fontSize: '18px' }}>
          <IconButton  >
            <AddIcon />
          </IconButton>
        </TableCell> */}
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
                    <TableCell style={{ width: '200px' }} align="center">
                      Status
                    </TableCell>
                    <TableCell style={{ width: '50px' }}>Receiver</TableCell>
                    <TableCell style={{ width: '100px' }}>Create Date</TableCell>
                    <TableCell style={{ width: '100px' }}>Update Date</TableCell>
                    <TableCell style={{ width: '100px' }}>Action</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {row.requestTickets.map((request_row) => (
                    <TableRow key={request_row.requestId}>
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
                            borderRadius="4px">
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
                            borderRadius="4px">
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
                            borderRadius="4px">
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
                            borderRadius="4px">
                            <AccessTimeFilledIcon />
                            <Typography color="#000">{request_row.requestStatus}</Typography>
                          </Box>
                        ) : null}
                      </TableCell>
                      <TableCell key={request_row.userId}>
                        {request_row.receiverFirstName}
                      </TableCell>
                      <TableCell>{request_row.requestCreateDate}</TableCell>
                      <TableCell>{request_row.requestUpdateDate}</TableCell>
                      <TableCell>
                        <IconButton
                          sx={{ color: '#1565c0' }}
                          onClick={() => navigate(`/request-detail/${request_row.requestId}`)}>
                          <RemoveRedEyeIcon />
                        </IconButton>
                        <Button variant="contained" sx={{ bgcolor: 'green' }}>
                          Accept
                        </Button>
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

export default function ManageTicketListHr() {
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

  useEffect(() => {
    setIsLoading(true)
    const fetchListRequestAndTicketByAdmin = async () => {
      const response = await requestApi.getAllRequestAndTicketByHr()
      setListRequestAndTicket(response)
      setIsLoading(false)
    }

    fetchListRequestAndTicketByAdmin()
  }, [])

  return (
    <Box display="flex" height="100vh" bgcolor="rgb(238, 242, 246)">
      <Box flex={1} sx={{ overflowX: 'hidden' }}>
        <Paper elevation={3} sx={{ padding: '16px' }}>
          <TextField
            label="Search"
            value={searchTerm}
            fullWidth
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </Paper>
        <Box display="flex" alignItems="center" gap={1} sx={{ marginTop: '16px' }}>
          {/* <Button variant="contained">
            <Typography>Create Ticket</Typography>
          </Button> */}
        </Box>

        <TableContainer component={Paper} sx={{ marginTop: '16px' }}>
          <Table aria-label="collapsible table">
            <TableHead>
              <TableRow>
                <TableCell style={{ width: '10px' }} />
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
                <TableCell
                  align="center"
                  style={{ width: '90px', fontWeight: 'bold', fontSize: '18px' }}>
                  Status
                </TableCell>

                <TableCell
                  align="center"
                  style={{ width: '10px', fontWeight: 'bold', fontSize: '18px' }}>
                  Action
                </TableCell>
              </TableRow>
            </TableHead>
            {isLoading ? (
              <TableRowsLoader rowsNum={5} />
            ) : (
              <TableBody>
                {listRequestAndTicket
                  .filter((row) => {
                    return Object.values(row)
                      .map((value) => (value || '').toString())
                      .join(' ')
                      .toLowerCase()
                      .includes(searchTerm.toLowerCase())
                  })
                  .slice(page * rowsPerPage, (page + 1) * rowsPerPage)
                  .map((row) => (
                    <Row key={row.ticketId} row={row} />
                  ))}
              </TableBody>
            )}
          </Table>
          <TablePagination
            component="div"
            count={listRequestAndTicket.length}
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
