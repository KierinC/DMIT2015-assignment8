package dmit2015.dto;

import dmit2015.dto.AircraftDto;
import dmit2015.entity.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AircraftMapper {

    AircraftMapper INSTANCE = Mappers.getMapper(AircraftMapper.class);

     @Mappings({
         @Mapping(target = "model", source = "model"),
         @Mapping(target = "manufacturer", source = "manufacturer"),
         @Mapping(target = "tailNumber", source = "tailNumber"),
         @Mapping(target = "version", source = "version"),
     })
    AircraftDto toDto(Aircraft entity);

     @Mappings({
         @Mapping(target = "model", source = "model"),
         @Mapping(target = "manufacturer", source = "manufacturer"),
         @Mapping(target = "tailNumber", source = "tailNumber"),
         @Mapping(target = "version", source = "version"),
     })
    Aircraft toEntity(AircraftDto dto);

}