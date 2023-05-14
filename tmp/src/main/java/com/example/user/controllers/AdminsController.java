package com.example.user.controllers;
import com.example.security.objects.Admin;
import com.example.security.services.AdminsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/")
public class AdminsController {
    private final AdminsService adminsService;

    @Autowired
    public AdminsController(AdminsService adminsService) {
        this.adminsService = adminsService;
    }

    @Operation(summary = "Get a list of admins based on 0 or more filters passed as queries. The format is property_from_admin_schema=value.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found admins that match the requirements",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Admin.class))
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Haven't found admins that match the requirements",
                    content = @Content
            )
    })
    @GetMapping(value = "/admins")
    public ResponseEntity<List<Admin>> getAdminsByParams(@RequestParam Map<String, Object> params) {
        List<Admin> admins = adminsService.getAdminsByParams(params);

        if (admins.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @Operation(summary = "Receive necessary data in order to update information about an admin in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource updated successfully",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Resource not found",
                    content = @Content)
    })
    @PatchMapping(value = "/admin/{id}")
    public ResponseEntity<Void> updateAdmin(@PathVariable UUID id, @RequestBody Admin admin) {
        if (!adminsService.getAdminsByParams(Map.of("id", id)).isEmpty()) {
            adminsService.updateAdmin(id, admin);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Receive necessary data in order to add a new admin in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource added successfully",
                    content = @Content)
    })
    @PostMapping(value = "/admins")
    public ResponseEntity<String> saveAdmin(@RequestBody Admin admin) {
        adminsService.saveAdmin(admin);
        return new ResponseEntity<>("Resource added successfully", HttpStatus.CREATED);
    }
}
