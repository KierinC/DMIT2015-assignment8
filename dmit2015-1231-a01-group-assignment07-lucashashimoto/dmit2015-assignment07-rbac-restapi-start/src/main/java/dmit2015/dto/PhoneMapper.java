package dmit2015.dto;

import dmit2015.dto.PhoneDto;
import dmit2015.entity.Phone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhoneMapper {

    PhoneMapper INSTANCE = Mappers.getMapper(PhoneMapper.class);

     @Mappings({
         @Mapping(target = "name", source = "model"),
         @Mapping(target = "date", source = "releaseDate"),
         @Mapping(target = "brand", source = "brand"),
         @Mapping(target = "price", source = "price"),
         @Mapping(target = "operatingSystem", source = "operatingSystem"),
         @Mapping(target = "version", source = "version"),
     })
    PhoneDto toDto(Phone entity);

     @Mappings({
             @Mapping(target = "model", source = "name"),
             @Mapping(target = "releaseDate", source = "date"),
             @Mapping(target = "brand", source = "brand"),
             @Mapping(target = "price", source = "price"),
             @Mapping(target = "operatingSystem", source = "operatingSystem"),
             @Mapping(target = "version", source = "version"),
     })
    Phone toEntity(PhoneDto dto);

}