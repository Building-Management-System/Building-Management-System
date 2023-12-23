import { EditingState, ViewState, IntegratedEditing } from '@devexpress/dx-react-scheduler'
import {
  AppointmentForm,
  AppointmentTooltip,
  Appointments,
  DateNavigator,
  MonthView,
  Scheduler,
  TodayButton,
  Toolbar,
  ConfirmationDialog
} from '@devexpress/dx-react-scheduler-material-ui'
import Paper from '@mui/material/Paper'
import moment from 'moment'
import { useEffect, useState } from 'react'
import holidayApi from '../../../services/holidayApi'
import { useSelector } from 'react-redux'
import { Typography, Grid, Divider } from '@mui/material'
import { jwtDecode } from 'jwt-decode'
import { toast } from 'react-toastify'
const ChangeLogView = () => {
  const [holidays, setHolidays] = useState([])
  const BoolEditor = () => {
    return null
  }
  const currentUser = useSelector((state) => state.auth.login?.currentUser)
  const decoded = jwtDecode(currentUser?.jwtToken)
  useEffect(() => {
    const fetchAllHolidays = async () => {
      const res = await holidayApi.getAllHoliday()
      const updatedData = res.map((item) => {
        const originalDate = new Date(item.toDate)

        // Subtract one day
        const modifiedDate = new Date(originalDate)
        modifiedDate.setDate(originalDate.getDate() + 1)

        // Format the result as 'YYYY-MM-DD'
        const formattedDate = modifiedDate.toISOString().split('T')[0]
        return {
          id: item.holidayId,
          startDate: item.fromDate,
          endDate: formattedDate,
          title: item.title,
          content: item.content,
          username: item.username
        }
      })
      setHolidays(updatedData)
    }
    fetchAllHolidays()
  }, [])

  const DayScaleCell = (props) => (
    <MonthView.DayScaleCell {...props} onClick={() => console.log(props.startDate)} />
  )
  const DateEditor = ({ ...restProps }) => {
    return <AppointmentForm.DateEditor {...restProps} excludeTime></AppointmentForm.DateEditor>
  }

  const BasicLayout = ({ onFieldChange, appointmentData, ...restProps }) => {
    const onCustomFieldChangeContent = (nextValue) => {
      onFieldChange({ content: nextValue })
    }

    return (
      <AppointmentForm.BasicLayout
        appointmentData={appointmentData}
        onFieldChange={onFieldChange}
        {...restProps}>
        <AppointmentForm.Label text="Content" />
        <AppointmentForm.TextEditor
          value={appointmentData.content}
          onValueChange={onCustomFieldChangeContent}
          placeholder="Write your content"
          type="multilineTextEditor"
        />
      </AppointmentForm.BasicLayout>
    )
  }
  const TextEditor = (props) => {
    if (props.type === 'multilineTextEditor') {
      return null
    }
    return <AppointmentForm.TextEditor {...props} />
  }

  const LabelComponent = (props) => {
    if (props.text === 'More Information') {
      return null
    }
  }

  const commitChanges = async ({ added, deleted }) => {
    if (added && added.title !== '' && added.content !== '') {
      const dateStart = moment(added.startDate.toString())
      const timeStart = dateStart.format('YYYY-MM-DD')
      const dateEnd = moment(added.endDate.toString())
      const timeEnd = dateEnd.format('YYYY-MM-DD')

      let data = {
        userId: currentUser?.accountId,
        title: added.title,
        content: added.content,
        fromDate: timeStart,
        toDate: timeEnd
      }      
      try {
        const res = await holidayApi.createHoliday(data)
      setHolidays((prevData) => {
        let dataAdd = {
          id: res,
          startDate: dateStart.format('YYYY-MM-DD'),
          endDate: dateEnd.add(1, 'days').format('YYYY-MM-DD'),
          title: added.title,
          content: added.content,
          username: decoded.sub
        }
        let newData = [...prevData]
        if (added && res) {
          newData = [...newData, dataAdd]
          toast.success('Create holiday successfully')
        }
        return newData
      })
      } catch (error) {
        if (error.response.status === 400) {
          toast.error("You can't book room before current time")
        }
        if (error.response.status === 404) {
          toast.error('User not found!')
        }
      }
    } else if ((added && added.title === '') || (added && added.content === '')) {
      toast.error(`All field can't be blank`)
    } else if (deleted !== undefined) {
      setHolidays((prevData) => {
        let newData = [...prevData]
        holidayApi.deleteHoliday(deleted)
        newData = newData.filter((appointment) => appointment.id !== deleted)
        return newData
      })
    }
  }

  const Content = ({ appointmentData }) => {
    console.log(appointmentData)
    const originalDate = new Date(appointmentData.endDate)
    const modifiedDate = new Date(originalDate)
    modifiedDate.setDate(originalDate.getDate() - 1)
    const formattedDate = modifiedDate.toISOString().split('T')[0]
    return (
      <Grid mt={1} container alignItems="center">
        <Grid display="flex" gap="8px" ml="25px" item xs={10}>
          <Typography>Title: </Typography>
          <Typography>{appointmentData.title}</Typography>
        </Grid>
        <Grid display="flex" gap="8px" ml="25px" mt={1} item xs={10}>
          <Typography>Date: </Typography>
          <Typography>
            {appointmentData.startDate} - {formattedDate}
          </Typography>
        </Grid>
        <Grid display="flex" gap="8px" ml="25px" mt={1} item xs={10}>
          <Typography>Created By: </Typography>
          <Typography>{appointmentData.username}</Typography>
        </Grid>
        <Grid display="flex" gap="8px" ml="25px" mt={1} pb={3} item xs={10}>
          <Typography>Content: </Typography>
          <Typography>{appointmentData.content}</Typography>
        </Grid>
      </Grid>
    )
  }

  const Header = ({ ...restProps }) => {
    return (
      <AppointmentTooltip.Header
        style={{ flexDirection: 'column', paddingLeft: '0px', alignItems: 'flex-end' }}
        {...restProps}>
        <Grid container>
          <Grid item xs={12}>
            <Divider />
          </Grid>
        </Grid>
        <Divider />
      </AppointmentTooltip.Header>
    )
  }
  return (
    <Paper>
      <Scheduler data={holidays} height="auto">
        <ViewState />
        <EditingState onCommitChanges={commitChanges} />
        <IntegratedEditing />
        <MonthView dayScaleCellComponent={DayScaleCell} />
        <Toolbar />
        <DateNavigator />
        <TodayButton />
        <Appointments />
        <AppointmentTooltip
          contentComponent={Content}
          headerComponent={Header}
          showDeleteButton
          showCloseButton
        />
        <ConfirmationDialog
          messages={{
            confirmDeleteMessage: () => (
              <Typography fontSize="20px">Are you sure to delete this holiday?</Typography>
            )
          }}
        />
        <AppointmentForm
          booleanEditorComponent={BoolEditor}
          labelComponent={LabelComponent}
          dateEditorComponent={DateEditor}
          textEditorComponent={TextEditor}
          basicLayoutComponent={BasicLayout}
        />
      </Scheduler>
    </Paper>
  )
}

export default ChangeLogView
