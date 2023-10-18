package fpt.capstone.buildingmanagementsystem.mapper;


import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.AttendanceRequestForm;
import fpt.capstone.buildingmanagementsystem.model.request.SendAttendanceFormRequest;
import org.mapstruct.Mapper;
@Mapper(componentModel = "spring")
public abstract class AttendanceRequestFormMapper {
    public abstract AttendanceRequestForm convert(SendAttendanceFormRequest sendAttendanceFormRequest);
}
