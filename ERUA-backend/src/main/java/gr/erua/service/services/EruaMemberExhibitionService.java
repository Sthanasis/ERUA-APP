package gr.erua.service.services;

import gr.erua.service.models.EruaMember;
import gr.erua.service.models.Exhibition;
import gr.erua.service.models.ExhibitionImage;
import gr.erua.service.repositories.EruaMemberRepository;
import gr.erua.service.repositories.ExhibitionRepository;
import gr.erua.service.repositories.ExhibitionImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class EruaMemberExhibitionService {

    @Autowired
    EruaMemberRepository eruaMemberRepository;
    @Autowired
    ExhibitionRepository exhibitionRepository;

    @Autowired
    ExhibitionImageRepository exhibitionImageRepository;

    public List<Exhibition> getAllExhibitionsOfErua(String id) {
        List<Exhibition> exhibitions = exhibitionRepository.findByOrganizerId(UUID.fromString(id));
        return exhibitions;
    }

    @Transactional
    public void createExhibitions(String auth_email, Exhibition exhibition) {
        EruaMember member = eruaMemberRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ auth_email +" doesn't exist!"
                    );
                });
        member.addExhibition(exhibition);
    }

    @Transactional
    public void addExhibitionImage(String auth_email, String exhibitionId, String name, MultipartFile file) {
        EruaMember member = eruaMemberRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ auth_email +" doesn't exist!"
                    );
                });
        Exhibition exhibition = exhibitionRepository.findById(Long.valueOf(exhibitionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Exhibition with id "+ exhibitionId +" doesn't exist!"
                    );
                });
        try {
            String pathParent= System.getProperty("user.dir") + File.separator + "data" + File.separator + "exhibitionImages" + File.separator + exhibitionId;
            Files.createDirectories(Paths.get(pathParent));

            ExhibitionImage exhibitionImage;
            if(!(new File(pathParent + File.separator + file.getOriginalFilename())).exists()) {
                Files.copy(file.getInputStream(), Paths.get(pathParent + File.separator + file.getOriginalFilename()));
                exhibitionImage =new ExhibitionImage(name, pathParent + File.separator + file.getOriginalFilename(), member);
            }
            else {
                String newName = "";
                String fileExt = "." + file.getOriginalFilename().split("\\.")[file.getOriginalFilename().split("\\.").length-1];
                String fileName = file.getOriginalFilename().replace(fileExt,"");
                int i=0;
                do{
                    i++;
                    newName = fileName + "_" + i + fileExt;
                } while ((new File(pathParent + File.separator + newName)).exists());
                Files.copy(file.getInputStream(), Paths.get(pathParent + File.separator + newName));
                exhibitionImage =new ExhibitionImage(name, pathParent + File.separator + newName, member);
            }

            exhibitionImageRepository.save(exhibitionImage);
            exhibition.addImagetoList(exhibitionImage);
            System.gc();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getExhibitionImage(String exhibitionId, String imageId) {
        Exhibition exhibition = exhibitionRepository.findById(Long.valueOf(exhibitionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Exhibition with id "+ exhibitionId +" doesn't exist!"
                    );
                });
        ExhibitionImage exhibitionImage = exhibitionImageRepository.findById(Long.valueOf(imageId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Image with id "+ imageId +" doesn't exist!"
                    );
                });
        if(!exhibition.getExhibitionImageList().contains(exhibitionImage)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Image with id "+ imageId +" doesn't exist in Exhibition with id "+ exhibitionId +"!"
            );
        }

        File file= new File(exhibitionImage.getPath());
        if(file.exists()){
            return file;
        }

        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Image doesn't exist!"
        );
    }

    public Exhibition getExhibitionFromExhibitionId(String exhibitionId) {
        return exhibitionRepository.findById(Long.valueOf(exhibitionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Exhibition with id "+ exhibitionId +" doesn't exist!"
                    );
                });
    }

    public List<Exhibition> getAllExhibitions() {
        return exhibitionRepository.findAll();
    }

    @Transactional
    public void removeExhibition(String auth_email, String exhibitionId) {
        EruaMember member = eruaMemberRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ auth_email +" doesn't exist!"
                    );
                });
        Exhibition exhibition = exhibitionRepository.findById(Long.valueOf(exhibitionId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Exhibition with id "+ exhibitionId +" doesn't exist!"
                    );
                });
        if(member.getExhibitionList().contains(exhibition)) {
            for(ExhibitionImage exhibitionImage: exhibition.getExhibitionImageList()){
                File file= new File(exhibitionImage.getPath());
                file.delete();
                exhibitionImageRepository.delete(exhibitionImage);
            }
            member.removeExhibition(exhibition);
            exhibitionRepository.delete(exhibition);
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Exhibition with id "+ exhibitionId +" doesn't exist for this user!"
            );
        }
    }

    @Transactional
    public void removeImage(String auth_email, String imageId) {
        EruaMember member = eruaMemberRepository.findByEmail(auth_email)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "EruaMember with email "+ auth_email +" doesn't exist!"
                    );
                });
        ExhibitionImage exhibitionImage = exhibitionImageRepository.findById(Long.valueOf(imageId))
                .orElseThrow(() -> {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Exhibition Image with id "+ imageId +" doesn't exist!"
                    );
                });
        Exhibition exhibition = exhibitionRepository.findById(exhibitionImage.getExhibition().getId()).get();
        if(exhibitionImage.getOwner().equals(member) || exhibition.getOrganizer().equals(member)){
            File file= new File(exhibitionImage.getPath());
            file.delete();
            exhibition.getExhibitionImageList().remove(exhibitionImage);
            exhibitionImageRepository.delete(exhibitionImage);
        }
        else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Exhibition Image can not be deleted by user with email "+ auth_email +"!"
            );
        }
    }
}
