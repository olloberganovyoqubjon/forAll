package uz.murojaat.appeal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.murojaat.appeal.model.Organization;
import uz.murojaat.appeal.payload.ApiResult;
import uz.murojaat.appeal.service.OrganizationService;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {


    private final OrganizationService organizationService;

    @PostMapping("add")
    public HttpEntity<?> add(@RequestBody Organization organization) {
        ApiResult apiResult = organizationService.add(organization);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @PutMapping("edit/{id}")
    public HttpEntity<?> edit(@RequestBody Organization organization, @PathVariable Long id) {
        ApiResult apiResult = organizationService.edit(organization,id);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @GetMapping("get")
    public HttpEntity<?> get() {
        ApiResult apiResult = organizationService.get();
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }

    @DeleteMapping("delete/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ApiResult apiResult = organizationService.delete(id);
        return ResponseEntity.status(apiResult.isSuccess() ? 200 : 409).body(apiResult);
    }
}
