package com.taller.bookstore.mapper;

import com.taller.bookstore.dto.request.BookRequest;
import com.taller.bookstore.dto.response.BookResponse;
import com.taller.bookstore.entity.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, CategoryMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "categories", ignore = true)
    Book toEntity(BookRequest request);

    BookResponse toResponse(Book book);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "categories", ignore = true)
    void updateEntity(@MappingTarget Book book, BookRequest request);
}
