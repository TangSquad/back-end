package backend.tangsquad.controller;

import backend.tangsquad.domain.Diving;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.DivingCreateRequest;
import backend.tangsquad.dto.request.DivingUpdateRequest;
import backend.tangsquad.dto.response.DivingReadResponse;
import backend.tangsquad.repository.DivingRepository;
import backend.tangsquad.repository.UserRepository;
import backend.tangsquad.service.DivingService;
import backend.tangsquad.swagger.global.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/diving")
@RestController

public class DivingController {
    private final DivingService divingService;

    private final DivingRepository divingRepository;

    private final UserRepository userRepository;

    @Autowired
    public DivingController(DivingService divingService, DivingRepository divingRepository, UserRepository userRepository) {
        this.divingService = divingService;
        this.divingRepository = divingRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("")
    public Diving createDiving(@PathVariable("username") String username, @RequestBody DivingCreateRequest request) {
        Diving diving = new Diving();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            // Handle user not found, you can throw an exception or return a specific response
            throw new UsernameNotFoundException("User not found");
        }


        // LogCreateRequest 에 있는 데이터들을 보고 모두 request에서 가지고 와 저장하여야 함.
        User user = userOptional.get();
        diving.setUser(user);
//        diving.setDivingName(request.getDivingName());
//        diving.setAge(request.getAge());
//        diving.setDivingContents(request.getDivingContents());
//        diving.setDivingIntro(request.getDivingIntro());
//        diving.setDivingMembers(request.getDivingMembers());

        divingRepository.save(diving);

        return diving;
    }

    @GetMapping("")
    public List<Diving> getDivings(@PathVariable("username") String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            // Handle user not found, you can throw an exception or return a specific response
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get();
        return divingService.getDivings(user);
    }

    @PutMapping("/{id}")
    public Diving updateDiving(@RequestParam(value = "username") String username,
                           @RequestParam(value = "id") Long id,
                           @RequestBody DivingUpdateRequest request) {
        Diving Diving = divingService.updateDiving(username, id, request);
        return Diving;
    }

    @DeleteMapping("/{id}")
    public CommonResponse<DivingReadResponse> deleteDiving(@RequestParam(value = "username") String username,
                                                       @RequestParam(value = "id") Long id) {
        divingService.deleteDiving(username,id);
        return CommonResponse.success();
    }
}
