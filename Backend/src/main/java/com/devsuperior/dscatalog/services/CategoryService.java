package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true) //para não travar o recurso no banco nas operacoes de somente leitura
    public List<CategoryDTO> findAll(){
        List<Category> listCategory = repository.findAll();
        List<CategoryDTO> listCategoryDto = new ArrayList<>();

        /*   Forma de implementar o DTO sem o lambda
        for(Category cat: listCategory){
            listCategoryDto.add(new CategoryDTO(cat)); // para cada item da lista de caregoria simples add a list DTO
        }
        */

        //usando expressão lambda
        listCategoryDto = listCategory.stream().map(cat -> new CategoryDTO(cat)).collect(Collectors.toList());

       return listCategoryDto;
    }

}
