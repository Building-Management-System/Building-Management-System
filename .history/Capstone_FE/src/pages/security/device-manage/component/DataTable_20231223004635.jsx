import { Box, LinearProgress, Button, Typography } from '@mui/material'
import { styled } from '@mui/material/styles'
import {
  DataGrid, GridToolbarContainer, GridToolbarExport, GridToolbarFilterButton
} from "@mui/x-data-grid";

const StripedDataGrid = styled(DataGrid)(() => ({
  '.late-checkin-cell .MuiDataGrid-cellContent': {
    color: 'red'
  },
  '.weekend-cell .MuiDataGrid-cellContent': {
    color: 'gray'
  },
  '.early-checkout-cell .MuiDataGrid-cellContent': {
    color: '#DAA520	'
  }
}))
const DataTableDeviceManage = ({  columns,rows,handleOpenCreateDevice, isLoading }) => {
  function CustomToolbar() {
    return (
      <GridToolbarContainer>
        <Box display="flex" justifyContent="space-between" width="100%">
          <Box display="flex" gap={1}>
            <GridToolbarFilterButton />
            <GridToolbarExport />
          </Box>
          <Button variant="contained" onClick={handleOpenCreateDevice}>
            <Typography>Add Device</Typography>
          </Button>
        </Box>
      </GridToolbarContainer>
    )
  }
  return (
    <>
      <Box
        sx={{
          '& .MuiDataGrid-root': {
            border: 'none'
          },
          '& .MuiDataGrid-cell': {
            borderBottom: 'none'
          },
          '& .name-column--cell': {
            color: '#94e2cd'
          },
          '& .MuiDataGrid-columnHeaders': {
            backgroundColor: 'rgb(248, 249, 250)',
            color: '#000'
          },
          '& .MuiDataGrid-virtualScroller': {
            backgroundColor: '#fff'
          },
          '& .MuiDataGrid-footerContainer': {
            borderTop: '1px solid rgba(224, 224, 224, 1)',
            backgroundColor: '#fff'
          },
          '& .MuiCheckbox-root': {
            color: `"#b7ebde" !important`
          },
          '& .MuiDataGrid-cellContent': {
            color: '#000'
          },
          '& .MuiButton-textPrimary': {
            color: '#000'
          },
          '& .MuiDataGrid-toolbarContainer': {
            marginBottom: '10px',
            justifyContent: 'flex-start'
          },
          '& .MuiDataGrid-columnHeaderTitle': {
            fontWeight: '700'
          }
        }}>
        <StripedDataGrid
          autoHeight
          disableRowSelectionOnClick
          slots={{toolbar: CustomToolbar,loadingOverlay: LinearProgress }}
          showCellVerticalBorder
          showColumnVerticalBorder
          initialState={{
            pagination: { paginationModel: { pageSize: 10 } },
          }}
          pageSizeOptions={[5, 10, 25]}
          loading={isLoading}
          columns={columns}
          rows={rows}
          getRowId={(row) => row.lcdId + row.roomId}
        />  
      </Box>
    </>
  )
}

export default DataTableDeviceManage
