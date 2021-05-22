package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true) //para não travar o recurso no banco nas operacoes de somente leitura
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> pageProduct = repository.findAll(pageRequest);
        return  pageProduct.map(product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true) //para não travar o recurso no banco nas operacoes de somente leitura
    public ProductDTO findById(Long id) {

        //Captura objeto de um optional e tratar a exceção
        Optional<Product> optionalProduct = repository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Recurso nao encontrado"));

        //usando o construtor de ProductDTO que recebe Product e a lista de Categorias do mesmo
        ProductDTO productDto = new ProductDTO(product,product.getCategories());
        return productDto;
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();
      //  entity.setName(productDTO.getName());
        entity = repository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {

        try {
            Product entity = repository.getOne(id); //getOne atribui o id ao usuario sem ir ao banco, so vai no banco no save
         //  entity.setName(productDTO.getName());

            entity = repository.save(entity);

            return new ProductDTO(entity);


        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id nao encontrado " + id);

        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id nao encontrado " + id);
        }
        catch (DataIntegrityViolationException e){ //exception de integridade referencial/tentar deletar algo em uso por outros
            throw new DatabaseException("Violacao de integridade");
        }
    }
}
