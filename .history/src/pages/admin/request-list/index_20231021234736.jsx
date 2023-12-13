import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp'
import RemoveRedEyeIcon from '@mui/icons-material/RemoveRedEye'
import Box from '@mui/material/Box'
import Button from '@mui/material/Button'
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
import requestApi from '../../../services/requestApi'
import { CircularProgress, Skeleton } from '@mui/material'

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
          {row.ticketId}
        </TableCell>
        <TableCell>{row.requestTicketDtos[row.requestTicketDtos.length - 1].title}</TableCell>
        <TableCell>{row.createDate}</TableCell>
        <TableCell>{row.updateDate}</TableCell>
        <TableCell>{row.status}</TableCell>
        <TableCell>
          <IconButton
            sx={{ color: '#1565c0' }}
            onClick={() => navigate('/room-detail/RB_c4db0674-5ade-4de0-bab8-1727e144d63a')}>
            <RemoveRedEyeIcon />
          </IconButton>
        </TableCell>
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
                    <TableCell width="250px">Request ID</TableCell>
                    <TableCell>Status</TableCell>
                    <TableCell>Curator</TableCell>
                    <TableCell>Create Date</TableCell>
                    <TableCell>Update Date</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {row.requestTicketDtos.map((request_row) => (
                    <TableRow key={request_row.request_id}>
                      <TableCell component="th" scope="row">
                        {request_row.requestId}
                      </TableCell>
                      <TableCell>{request_row.requestStatus}</TableCell>
                      <TableCell>{request_row.userId}</TableCell>
                      <TableCell>{request_row.requestCreateDate}</TableCell>
                      <TableCell>{request_row.requestUpdateDate}</TableCell>
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
    </TableRow>
  ));
};

export default function RequestListAdmin() {
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
    const fetchListRequestAndTicket = async () => {
      const response = await requestApi.getAllRequestAndTicket()
      setListRequestAndTicket(response)
      setIsLoading(false)
    }
    fetchListRequestAndTicket()
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
          <Button variant="contained">
            <Typography>Create Ticket</Typography>
          </Button>
        </Box>

        <TableContainer component={Paper} sx={{ marginTop: '16px' }}>
          <Table aria-label="collapsible table">
            <TableHead>
              <TableRow>
                <TableCell style={{ width: '10px' }} /> {/* Adjust the width as needed */}
                <TableCell style={{ width: '160px' }}>TicketID</TableCell>
                <TableCell style={{ width: '200px' }}>Title</TableCell>
                <TableCell style={{ width: '150px' }}>Create Date</TableCell>
                <TableCell style={{ width: '150px' }}>Update Date</TableCell>
                <TableCell style={{ width: '100px' }}>Status</TableCell>
                <TableCell style={{ width: '100px' }}>Action</TableCell>
              </TableRow>
            </TableHead>
            {/* {isLoading ? ( */}
            <TableRowsLoader rowsNum={5} />
            {/* // ) : (
            //   <TableBody>
            //     {listRequestAndTicket
            //       .filter((row) => {
            //         return Object.values(row)
            //           .map((value) => (value || '').toString())
            //           .join(' ')
            //           .toLowerCase()
            //           .includes(searchTerm.toLowerCase())
            //       })
            //       .slice(page * rowsPerPage, (page + 1) * rowsPerPage)
            //       .map((row) => (
            //         <Row key={row.ticketId} row={row} />
            //       ))}
            //   </TableBody>
            // )} */}
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
