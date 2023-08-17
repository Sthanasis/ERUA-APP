package gr.erua.service.controllers;

import com.auth0.jwt.interfaces.DecodedJWT;
import gr.erua.service.models.EruaMember;
import gr.erua.service.models.Exhibition;
import gr.erua.service.models.Problem;
import gr.erua.service.models.Solution;
import gr.erua.service.services.EruaMemberExhibitionService;
import gr.erua.service.services.EruaMemberService;
import gr.erua.service.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/eruamember")
public class EruaMemberController {

    @Autowired
    EruaMemberService eruaMemberService;

    @Autowired
    EruaMemberExhibitionService eruaMemberExhibitionService;

    @PostMapping
    EruaMember createEruaMember(@RequestBody EruaMember eruaMember){
        return eruaMemberService.createEruaMember(eruaMember);
    }

    @GetMapping
    List<EruaMember> getAllEruaMembers(){
        return eruaMemberService.getAllEruaMembers();
    }

    @GetMapping("/{email}")
    EruaMember getEruaMember(@PathVariable String email){
        return eruaMemberService.getEruaMemberWithEmail(email);
    }

    @GetMapping("/solutions")
    ResponseEntity<Map<String, Object>> getAllEruaMemberSolutions(@RequestParam(required = false) String type,
                                                                  @RequestParam(required = false) String contain,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "100") int size){
        return eruaMemberService.getAllSolutions(type,contain,page,size);
    }

    @GetMapping("/solutions/types")
    List<String> getEruaMemberSolutionsTypes(){
        return eruaMemberService.getAllSolutionsTypes();
    }

    @GetMapping("/{email}/solutions")
    Set<Solution> getEruaMemberSolutions(@PathVariable String email){
        return eruaMemberService.getEruaMemberSolutions(email);
    }

    @PostMapping("/{email}/solutions")
    Set<Solution> createEruaMemberSolutions(HttpServletRequest request, @PathVariable String email, @RequestBody Solution solution){
        DecodedJWT decodedJWT= TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        return eruaMemberService.createEruaMemberSolutions(decodedJWT.getSubject(), email, solution);
    }

    @DeleteMapping("/solutions")
    String deleteEruaMemberSolutions(HttpServletRequest request, @RequestParam String solutionId){
        DecodedJWT decodedJWT= TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        return eruaMemberService.deleteEruaMemberSolutions(decodedJWT.getSubject(), solutionId);
    }

    @GetMapping("/{email}/friends")
    List<EruaMember> getEruaMemberFriends(@PathVariable String email){
        return eruaMemberService.getEruaMemberFriends(email);
    }

    @PostMapping("/{email}/friends/{friend_email}")
    List<EruaMember> createEruaMemberFriends(HttpServletRequest request, @PathVariable String email, @PathVariable String friend_email){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        return eruaMemberService.createEruaMemberFriends(decodedJWT.getSubject(), email,friend_email);
    }

    @GetMapping("/{solutionsId}/problems")
    Set<Problem> getProblemsOfSolution(@PathVariable String solutionsId){
        return eruaMemberService.getProblemsOfSolution(solutionsId);
    }

    @GetMapping("/solutions/notlinkedproblems")
    List<Problem> getNotLinkedProblemsToSolution(HttpServletRequest request, @RequestParam String solutionId){
        DecodedJWT decodedJWT= TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        return eruaMemberService.getNotLinkedProblemsToSolution(decodedJWT.getSubject(), solutionId);
    }

    @GetMapping("/addsolutiontoproblem")
    void addSolutionToProblem(HttpServletRequest request, @RequestParam String solutionId, @RequestParam String problemId){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        eruaMemberService.addSolutionToProblem(decodedJWT.getSubject(), solutionId, problemId);
    }

    @PostMapping("/exhibition")
    void createExhibitions(HttpServletRequest request, @RequestBody Exhibition exhibition){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        eruaMemberExhibitionService.createExhibitions(decodedJWT.getSubject(), exhibition);
    }

    @GetMapping("/exhibition/all")
    List<Exhibition> getAllExhibitions(){
        return eruaMemberExhibitionService.getAllExhibitions();
    }

    @GetMapping("/exhibition")
    Exhibition getExhibitionFromExhibitionId(@RequestParam String exhibitionId){
        return eruaMemberExhibitionService.getExhibitionFromExhibitionId(exhibitionId);
    }

    @GetMapping("/exhibitions")
    List<Exhibition> getAllExhibitionsOfErua(@RequestParam String eruaMemberId){
        return eruaMemberExhibitionService.getAllExhibitionsOfErua(eruaMemberId);
    }

    @PostMapping("/exhibition/image")
    void addExhibitionImage(HttpServletRequest request, @RequestParam String exhibitionId, @RequestParam String name, @RequestParam("file") MultipartFile file){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        eruaMemberExhibitionService.addExhibitionImage(decodedJWT.getSubject(), exhibitionId, name, file);
    }

    @GetMapping("/exhibition/image")
    ResponseEntity<?> getExhibitionImage(@RequestParam String exhibitionId, @RequestParam String imageId) throws IOException {
        File file = eruaMemberExhibitionService.getExhibitionImage(exhibitionId, imageId);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf(Files.probeContentType(Paths.get(file.getAbsolutePath()))))
                .body(new UrlResource(file.toURI()));
    }

    @DeleteMapping("/exhibition")
    void removeExhibition(HttpServletRequest request, @RequestParam String exhibitionId){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        eruaMemberExhibitionService.removeExhibition(decodedJWT.getSubject(), exhibitionId);
    }

    @DeleteMapping("/exhibition/image")
    void removeImage(HttpServletRequest request, @RequestParam String imageId){
        DecodedJWT decodedJWT=TokenUtil.getDecodedJWTfromToken(request.getHeader(AUTHORIZATION));
        eruaMemberExhibitionService.removeImage(decodedJWT.getSubject(), imageId);
    }
}
