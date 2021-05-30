package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
        //Parametros do Pegeable: page, size , sort
        // page -> Pagina requisitada pelo client
        // size -> Quantidade de elementos desejados por pagina
        // sort -> Classificação dos elementos da pagina , onde ASC (crescente) e DESC (decrescente)

        Page<ProductDTO> pageProduct = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(pageProduct);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO productDTO = service.findById(id);
        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> inserirProduto(@RequestBody ProductDTO productDTO) {
        productDTO = service.insert(productDTO);

        // preenchedo o header(location) de retorno com a uri do recurso criado
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(productDTO.getId()).toUri();


        return ResponseEntity.created(uri).body(productDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        productDTO = service.update(id, productDTO);

        return ResponseEntity.ok().body(productDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
