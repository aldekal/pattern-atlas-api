package io.github.patternatlas.api.rest.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.afford;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.text.CaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.patternatlas.api.rest.model.GraphModel;
import io.github.patternatlas.api.entities.PatternLanguage;
import io.github.patternatlas.api.entities.PatternSchema;
import io.github.patternatlas.api.rest.model.PatternLanguageModel;
import io.github.patternatlas.api.rest.model.shared.PatternSchemaModel;
import io.github.patternatlas.api.rest.model.PatternLanguageGraphModel;
import io.github.patternatlas.api.rest.model.shared.PatternLanguageSchemaModel;
import io.github.patternatlas.api.service.PatternLanguageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@CrossOrigin(allowedHeaders = "*", origins = "*")
@RequestMapping(value = "/patternLanguages", produces = "application/hal+json")
public class PatternLanguageController {

    private final PatternLanguageService patternLanguageService;

    private final ObjectMapper objectMapper;

    @Autowired
    public PatternLanguageController(PatternLanguageService patternLanguageService,
                                     ObjectMapper objectMapper) {
        this.patternLanguageService = patternLanguageService;
        this.objectMapper = objectMapper;
    }

    private static List<Link> getPatternLanguageCollectionLinks() {
        List<Link> links = new ArrayList<>();

        try {
            Link findByUriLink = linkTo(methodOn(PatternLanguageController.class).findPatternLanguageByUri(null)).withRel("findByUri");
            links.add(findByUriLink);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        links.add(linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withSelfRel()
                .andAffordance(afford(methodOn(PatternLanguageController.class).createPatternLanguage(null))));
        return links;
    }

    private static List<Link> getPatternLanguageLinks(UUID patternLanguageId) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withSelfRel()
                .andAffordance(afford(methodOn(PatternLanguageController.class).putPatternLanguage(patternLanguageId, null)))
                .andAffordance(afford(methodOn(PatternLanguageController.class).deletePatternLanguage(patternLanguageId)))
        );
        links.add(linkTo(methodOn(PatternController.class).getPatternsOfPatternLanguage(patternLanguageId)).withRel("patterns"));
        links.add(linkTo(methodOn(PatternLanguageController.class).getAllPatternLanguages()).withRel("patternLanguages"));
        links.add(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageGraph(patternLanguageId)).withRel("graph"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getDirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("directedEdges"));
        links.add(linkTo(methodOn(PatternRelationDescriptorController.class).getUndirectedEdgesOfPatternLanguage(patternLanguageId)).withRel("undirectedEdges"));
        return links;
    }

    @Operation(operationId = "getAllPatternLanguageModels", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve all pattern languages")
    @GetMapping(value = "")
    CollectionModel<EntityModel<PatternLanguageModel>> getAllPatternLanguages() {
        List<EntityModel<PatternLanguageModel>> patternLanguageModels = this.patternLanguageService.getPatternLanguages()
                .stream()
                .map(PatternLanguageModel::toModel)
                .map(patternLanguageModel -> new EntityModel<>(patternLanguageModel,
                        getPatternLanguageLinks(patternLanguageModel.getId())))
                .collect(Collectors.toList());
        return new CollectionModel<>(patternLanguageModels, getPatternLanguageCollectionLinks());
    }

    @Operation(operationId = "getPatternLanguageByURI", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Retrieve pattern language by URI")
    @GetMapping(value = "/findByUri")
    EntityModel<PatternLanguage> findPatternLanguageByUri(@RequestParam String encodedUri) throws UnsupportedEncodingException {
        String uri = URLDecoder.decode(encodedUri, StandardCharsets.UTF_8.toString());
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageByUri(uri);

        return new EntityModel<>(patternLanguage, getPatternLanguageLinks(patternLanguage.getId()));
    }

    @Operation(operationId = "getPatternLanguageByID", responses = {@ApiResponse(responseCode = "200"), @ApiResponse(responseCode = "404", content = @Content)}, description = "Retrieve pattern language by ID")
    @GetMapping(value = "/{patternLanguageId}")
    EntityModel<PatternLanguage> getPatternLanguageById(@PathVariable UUID patternLanguageId) {
        PatternLanguage patternLanguage = this.patternLanguageService.getPatternLanguageById(patternLanguageId);

        return new EntityModel<>(patternLanguage, getPatternLanguageLinks(patternLanguage.getId()));
    }

    @Operation(operationId = "createPatternLanguage", responses = {@ApiResponse(responseCode = "201")}, description = "Create pattern language")
    @PostMapping
    @CrossOrigin(exposedHeaders = "Location")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<EntityModel<PatternLanguage>> createPatternLanguage(@RequestBody PatternLanguage patternLanguage) {
        String patternLanguageNameAsCamelCase = CaseUtils.toCamelCase(patternLanguage.getName(), false);
        String uri = String.format("https://patternpedia.org/patternLanguages/%s", patternLanguageNameAsCamelCase);
        patternLanguage.setUri(uri);

        PatternLanguage createdPatternLanguage = this.patternLanguageService.createPatternLanguage(patternLanguage);
        return ResponseEntity
                .created(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(createdPatternLanguage.getId())).toUri())
                .build();
    }

    @Operation(operationId = "updatePatternLanguage", responses = {@ApiResponse(responseCode = "200")}, description = "Update pattern language")
    @PutMapping(value = "/{patternLanguageId}")
    ResponseEntity<Void> putPatternLanguage(@PathVariable UUID patternLanguageId, @RequestBody PatternLanguage patternLanguage) {
        this.patternLanguageService.updatePatternLanguage(patternLanguage);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "deletePatternLanguage", responses = {@ApiResponse(responseCode = "204")}, description = "Delete pattern language")
    @DeleteMapping(value = "/{patternLanguageId}")
    ResponseEntity<Void> deletePatternLanguage(@PathVariable UUID patternLanguageId) {
        this.patternLanguageService.deletePatternLanguage(patternLanguageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/patternSchemas")
    CollectionModel<EntityModel<PatternLanguageSchemaModel>> getAllPatternLanguagesWithSchema() {
        List<EntityModel<PatternLanguageSchemaModel>> patternLanguages = this.patternLanguageService.getPatternLanguages()
                .stream()
                .map(patternLanguage -> new EntityModel<>(new PatternLanguageSchemaModel(patternLanguage)))
                .collect(Collectors.toList());

        return new CollectionModel<>(patternLanguages);
    }

    @Operation(operationId = "getPatternSchema", responses = {@ApiResponse(responseCode = "200")}, description = "Get pattern schema by pattern language id")
    @GetMapping(value = "/{patternLanguageId}/patternSchema")
    EntityModel<PatternSchema> getPatternSchema(@PathVariable UUID patternLanguageId) {
        PatternSchema schema = this.patternLanguageService.getPatternSchemaByPatternLanguageId(patternLanguageId);

        Link selfLink = linkTo(methodOn(PatternLanguageController.class).getPatternSchema(patternLanguageId)).withSelfRel();
        selfLink.andAffordance(afford(methodOn(PatternLanguageController.class).updatePatternSchema(patternLanguageId, null)));

        return new EntityModel<>(schema,
                selfLink,
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"));
    }

    @Operation(operationId = "updatePatternSchema", responses = {@ApiResponse(responseCode = "204")}, description = "Update pattern schema by pattern language id")
    @PutMapping(value = "/{patternLanguageId}/patternSchema")
    ResponseEntity<Void> updatePatternSchema(@PathVariable UUID patternLanguageId, @RequestBody PatternSchema patternSchema) {
        this.patternLanguageService.updatePatternSchemaOfPatternLanguage(patternLanguageId, patternSchema);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "getPatternLanguageGraph", responses = {@ApiResponse(responseCode = "200")}, description = "Retrieve pattern language graph")
    @GetMapping(value = "/{patternLanguageId}/graph")
    HttpEntity<EntityModel<Object>> getPatternLanguageGraph(@PathVariable UUID patternLanguageId) {
        Object graph = this.patternLanguageService.getGraphOfPatternLanguage(patternLanguageId);

        GraphModel model = new GraphModel();
        if (null == graph) {
            model.setGraph(this.objectMapper.createArrayNode());
        } else {
            model.setGraph(graph);
        }
        EntityModel<Object> entityModel = new EntityModel<>(model, linkTo(methodOn(PatternLanguageController.class).getPatternLanguageById(patternLanguageId)).withRel("patternLanguage"),
                linkTo(methodOn(PatternLanguageController.class).getPatternLanguageGraph(patternLanguageId)).withSelfRel()
                        .andAffordance(afford(methodOn(PatternLanguageController.class).postPatternLanguageGraph(patternLanguageId, null)))
                        .andAffordance(afford(methodOn(PatternLanguageController.class).putPatternLanguageGraph(patternLanguageId, null)))
                        .andAffordance(afford(methodOn(PatternLanguageController.class).deletePatternLanguageGraph(patternLanguageId))));
        return ResponseEntity.ok(entityModel);
    }

    @Operation(operationId = "postPatternLanguageGraph", responses = {@ApiResponse(responseCode = "200")}, description = "Update pattern language graph")
    @PostMapping(value = "/{patternLanguageId}/graph")
    ResponseEntity<URI> postPatternLanguageGraph(@PathVariable UUID patternLanguageId, @RequestBody Object graph) {
        this.patternLanguageService.updateGraphOfPatternLanguage(patternLanguageId, graph);
        return ResponseEntity.ok(linkTo(methodOn(PatternLanguageController.class).getPatternLanguageGraph(patternLanguageId)).toUri());
    }

    @Operation(operationId = "putPatternLanguageGraph", responses = {@ApiResponse(responseCode = "204")}, description = "Update pattern language graph")
    @PutMapping(value = "/{patternLanguageId}/graph")
    ResponseEntity<Void> putPatternLanguageGraph(@PathVariable UUID patternLanguageId, @RequestBody Object graph) {
        this.patternLanguageService.updateGraphOfPatternLanguage(patternLanguageId, graph);
        return ResponseEntity.noContent().build();
    }

    @Operation(operationId = "deletePatternLanguageGraph", responses = {@ApiResponse(responseCode = "204")}, description = "Delete pattern language graph")
    @DeleteMapping(value = "/{patternLanguageId}/graph")
    ResponseEntity<Void> deletePatternLanguageGraph(@PathVariable UUID patternLanguageId) {
        this.patternLanguageService.deleteGraphOfPatternLanguage(patternLanguageId);
        return ResponseEntity.noContent().build();
    }
}
