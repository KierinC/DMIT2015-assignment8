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

    // @Mappings({
    //     @Mapping(target = "dtoProperty1Name", source = "entityProperty1Name"),
    //     @Mapping(target = "dtoProperty2Name", source = "entityProperty2Name"),
    //     @Mapping(target = "dtoProperty3Name", source = "entityProperty3Name"),
    // })
    AircraftDto toDto(Aircraft entity);

    // @Mappings({
    //     @Mapping(target = "entityProperty1Name", source = "dtoProperty1Name"),
    //     @Mapping(target = "entityProperty1Name", source = "dtoProperty2Name"),
    //     @Mapping(target = "entityProperty1Name", source = "dtoProperty3Name"),
    // })
    Aircraft toEntity(AircraftDto dto);

}