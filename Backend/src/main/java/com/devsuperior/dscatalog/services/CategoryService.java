package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true) //para não travar o recurso no banco nas operacoes de somente leitura
    public List<CategoryDTO> findAll() {
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

    @Transactional(readOnly = true) //para não travar o recurso no banco nas operacoes de somente leitura
    public CategoryDTO findById(Long id) {

        //# 1 - Capturar objeto de um optional - sem tratar a exceção
        //uma opção para capturar o objeto dentro do "container"  Optional -> usando .get()
        //
        //Category category = repository.findById(id).get();

        //# 2 - Capturar objeto de um optional e tratar a exceção
        Optional<Category> optionalCategory = repository.findById(id);
        Category category = optionalCategory.orElseThrow(() -> new ResourceNotFoundException("Recurso nao encontrado"));

        CategoryDTO categoryDto = new CategoryDTO(category);
        return categoryDto;
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category entity = new Category();
        entity.setName(categoryDTO.getName());
        entity = repository.save(entity);

        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {

        try {
            Category entity = repository.getOne(id); //getOne atribui o id ao usuario sem ir ao banco, so vai no banco no save
            entity.setName(categoryDTO.getName());

            entity = repository.save(entity);

            return new CategoryDTO(entity);


        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id nao encontrado " + id);

        }
    }
}
