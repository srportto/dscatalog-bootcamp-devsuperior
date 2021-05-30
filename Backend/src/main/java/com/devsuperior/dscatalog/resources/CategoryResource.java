package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    private CategoryService service;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAll(Pageable pageable) {
        //Parametros do Pegeable: page, size , sort
        // page -> Pagina requisitada pelo client
        // size -> Quantidade de elementos desejados por pagina
        // sort -> Classificação dos elementos da pagina , onde ASC (crescente) e DESC (decrescente)
        Page<CategoryDTO> pageCategory = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(pageCategory);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO categoryDTO = service.findById(id);
        return ResponseEntity.ok().body(categoryDTO);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> inserirCategoria(@RequestBody CategoryDTO categoryDTO) {
        categoryDTO = service.insert(categoryDTO);

        // preenchedo o header(location) de retorno com a uri do recurso criado
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(categoryDTO.getId()).toUri();


        return ResponseEntity.created(uri).body(categoryDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> atualizarCategoria(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        categoryDTO = service.update(id, categoryDTO);

        return ResponseEntity.ok().body(categoryDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
