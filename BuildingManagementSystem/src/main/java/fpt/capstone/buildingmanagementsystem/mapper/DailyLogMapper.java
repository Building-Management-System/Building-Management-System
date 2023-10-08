package fpt.capstone.buildingmanagementsystem.mapper;


import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.response.DailyDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.GetAttendanceUserResponse;
import org.mapstruct.Mapper;
import java.util.List;
@Mapper(componentModel = "spring")
public abstract class DailyLogMapper {
    public abstract List<GetAttendanceUserResponse> convertGetAttendanceUserResponse(List<DailyLog> dailyLog);
    public abstract DailyDetailResponse convertGetAttendanceUserDetailResponse(DailyLog dailyLog);

}
