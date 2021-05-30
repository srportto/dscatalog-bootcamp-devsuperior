package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true) //para não travar o recurso no banco nas operacoes de somente leitura
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> pageProduct = productRepository.findAll(pageable);
        return  pageProduct.map(product -> new ProductDTO(product));
    }

    @Transactional(readOnly = true) //para não travar o recurso no banco nas operacoes de somente leitura
    public ProductDTO findById(Long id) {

        //Captura objeto de um optional e tratar a exceção
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product product = optionalProduct.orElseThrow(() -> new ResourceNotFoundException("Recurso nao encontrado"));

        //usando o construtor de ProductDTO que recebe Product e a lista de Categorias do mesmo
        ProductDTO productDto = new ProductDTO(product,product.getCategories());
        return productDto;
    }

    @Transactional
    public ProductDTO insert(ProductDTO productDTO) {
        Product entity = new Product();

        // invocacao do metodo que transforma dto em entity ao moldes do BD para posterior save/update
        copyDtoToEntity(productDTO, entity);

        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {

        try {
            Product entity = productRepository.getOne(id); //getOne atribui o id ao usuario sem ir ao banco, so vai no banco no save

            // invocacao do metodo que transforma dto em entity ao moldes do BD para posterior save/update
            copyDtoToEntity(productDTO, entity);

            entity = productRepository.save(entity);

            return new ProductDTO(entity);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id nao encontrado " + id);

        }
    }

    public void delete(Long id) {
        try {
            productRepository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("Id nao encontrado " + id);
        }
        catch (DataIntegrityViolationException e){ //exception de integridade referencial/tentar deletar algo em uso por outros
            throw new DatabaseException("Violacao de integridade");
        }
    }

    private  void copyDtoToEntity(ProductDTO dto, Product entity){
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear(); // antes de adicionar as categorias advindas do Dto, limpa-se a do objeto em questao

        // para cada categoria (catDto) adivinda do list de Categorias (dto.getCategories), passe uma a uma para a entidade
        for (CategoryDTO catDdo : dto.getCategories()){
            Category category = categoryRepository.getOne(catDdo.getId()); //instanciando uma categoria sem tocar no banco de dados, mas gera aos moldes do banco
            entity.getCategories().add(category);
        }

     }

}
