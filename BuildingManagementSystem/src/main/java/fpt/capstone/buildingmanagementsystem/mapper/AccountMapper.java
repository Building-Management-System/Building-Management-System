package fpt.capstone.buildingmanagementsystem.mapper;

import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.Role;
import fpt.capstone.buildingmanagementsystem.model.entity.Status;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    @Mappings({
            @Mapping(target = "password", expression = "java(fpt.capstone.buildingmanagementsystem.until.Until.encodePassword(registerRequest.password))"),
            @Mapping(target = "createdDate", expression = "java(fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime())"),
            @Mapping(target = "updatedDate", expression = "java(fpt.capstone.buildingmanagementsystem.until.Until.generateRealTime())"),
            @Mapping(target = "role", source = "role"),
    })
    public abstract Account convertRegisterAccount(RegisterRequest registerRequest, Status status, Role role);
}
