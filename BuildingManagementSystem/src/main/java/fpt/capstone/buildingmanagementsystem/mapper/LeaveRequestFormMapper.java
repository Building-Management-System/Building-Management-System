package fpt.capstone.buildingmanagementsystem.mapper;

import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.LeaveRequestForm;
import fpt.capstone.buildingmanagementsystem.model.request.SendLeaveFormRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class LeaveRequestFormMapper {
    public abstract LeaveRequestForm convert(SendLeaveFormRequest sendLeaveFormRequest);
}
