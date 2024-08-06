package backend.tangsquad.controller;


import backend.tangsquad.domain.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.dto.request.MoimCreateRequest;
import backend.tangsquad.dto.request.MoimUpdateRequest;
import backend.tangsquad.dto.response.MoimReadResponse;
import backend.tangsquad.repository.MoimRepository;
import backend.tangsquad.repository.UserRepository;
import backend.tangsquad.service.MoimService;
import backend.tangsquad.swagger.global.CommonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/moim")
@RestController
public class MoimController {

    private final MoimService moimService;

    private final MoimRepository moimRepository;

    private final UserRepository userRepository;

    @Autowired
    public MoimController(MoimService moimService, MoimRepository moimRepository, UserRepository userRepository) {
        this.moimService = moimService;
        this.moimRepository = moimRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/new")
    public Moim createMoim(@PathVariable("username") String username, @RequestBody MoimCreateRequest request) {
        Moim moim = new Moim();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            // Handle user not found, you can throw an exception or return a specific response
            throw new UsernameNotFoundException("User not found");
        }


        // LogCreateRequest 에 있는 데이터들을 보고 모두 request에서 가지고 와 저장하여야 함.
        User user = userOptional.get();
        moim.setUser(user);
        moim.setMoimName(request.getMoimName());
        moim.setAge(request.getAge());
        moim.setMoimContents(request.getMoimContents());
        moim.setMoimIntro(request.getMoimIntro());
        moim.setMoimMembers(request.getMoimMembers());

        moimRepository.save(moim);

        return moim;
    }

    @GetMapping("")
    public List<Moim> getMoims(@PathVariable("username") String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            // Handle user not found, you can throw an exception or return a specific response
            throw new UsernameNotFoundException("User not found");
        }

        User user = userOptional.get();
        return moimService.getMoims(user);
    }

    @PutMapping("/update")
    public Moim updateMoim(@RequestParam(value = "username") String username,
                          @RequestParam(value = "id") Long id,
                          @RequestBody MoimUpdateRequest request) {
        Moim moim = moimService.updateMoim(username, id, request);
        return moim;
    }

    @DeleteMapping("/{id}")
    public CommonResponse<MoimReadResponse> deleteMoim(@RequestParam(value = "username") String username,
                                                      @RequestParam(value = "id") Long id) {
        moimService.deleteMoim(username,id);
        return CommonResponse.success();
    }

}
