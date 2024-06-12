package exercise.mapper;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.model.Product;
import org.mapstruct.*;

// BEGIN
@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductMapper {
    @Mapping(target = "category.id", source = "categoryId")
    public abstract Product map(ProductCreateDTO dto);
    @Mapping(source = "category.id", target = "categoryId")
    public abstract ProductDTO map(Product model);

    @InheritConfiguration
    public abstract void update(ProductUpdateDTO dto, @MappingTarget Product model);
}
// END
