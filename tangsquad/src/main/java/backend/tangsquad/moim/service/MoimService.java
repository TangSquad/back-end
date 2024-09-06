package backend.tangsquad.moim.service;

import backend.tangsquad.moim.entity.Moim;
import backend.tangsquad.domain.User;
import backend.tangsquad.moim.dto.request.MoimLeaderUpdateRequest;
import backend.tangsquad.moim.dto.request.MoimUpdateRequest;
import backend.tangsquad.moim.repository.MoimRepository;
import backend.tangsquad.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class MoimService  {
    private MoimRepository moimRepository;

    private UserRepository userRepository;

    @Autowired
    public MoimService (MoimRepository moimRepository, UserRepository userRepository) {
        this.moimRepository = moimRepository;
        this.userRepository = userRepository;
    }

    public Moim save(Moim moim) {
        return moimRepository.save(moim);
    }


    // 수정 필요
    public Moim updateMoim(Long moimId, MoimUpdateRequest request) {
        // Retrieve the existing Logbook by userId and logId
        Optional<Moim> moimOptional = moimRepository.findById(moimId);

        if (!moimOptional.isPresent()) {
            throw new NoSuchElementException(", moimId: " + moimId);
        }

        // Get the existing logbook
        Moim moim = moimOptional.get();

        // Update fields from the request if they are not null
        if (request.getAnonymous() != null) moim.setAnonymous(request.getAnonymous());
        if (request.getMoimName() != null) moim.setMoimName(request.getMoimName());
        if (request.getMoimIntro() != null) moim.setMoimIntro(request.getMoimIntro());
        if (request.getMoimDetails() != null) moim.setMoimDetails(request.getMoimDetails());
        if (request.getAge() != null) moim.setAge(request.getAge());
        if (request.getLimitPeople() != null) moim.setLimitPeople(request.getLimitPeople());
        if (request.getLicenseLimit() != null) moim.setLicenseLimit(request.getLicenseLimit());
        if (request.getLocationOne() != null) moim.setLocationOne(request.getLocationOne());
        if (request.getLocationTwo() != null) moim.setLocationTwo(request.getLocationTwo());
        if (request.getLocationThree() != null) moim.setLocationThree(request.getLocationThree());
        if (request.getExpense() != null) moim.setExpense(request.getExpense());
        if (request.getMoodOne() != null) moim.setMoodOne(request.getMoodOne());
        if (request.getMoodTwo() != null) moim.setMoodTwo(request.getMoodTwo());


        // Save and return the updated moim
        return moimRepository.save(moim);
    }

    public Optional<Moim> findById(Long moimId) {
        return moimRepository.findById(moimId);
    }

    public List<Moim> getRegisteredMoims(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        return moimRepository.findByRegisteredUsersContaining(user);
    }



    public Moim updateMoimLeader(Long moimId, MoimLeaderUpdateRequest request) {
        // Retrieve the existing Moim by moimId
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Moim not found for moimId: " + moimId));

        // Retrieve the new leader (user) by userId if it's not null
        if (request.getUserId() != null) {
            User newUser = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new NoSuchElementException("User not found for userId: " + request.getUserId()));

            // Update the Moim's user (leader) with the new User object
            moim.setUser(newUser);
        }

        // Save and return the updated Moim
        return moimRepository.save(moim);
    }

    // Method to check if the user is authorized to update the leader
    public boolean isAuthorizedToUpdateLeader(Long moimId, Long currentUserId) {
        return moimRepository.findById(moimId)
                .map(moim -> moim.getUser().getId().equals(currentUserId))
                .orElse(false);
    }





    public List<Moim> getMoims(Long userId)
    {
        return moimRepository.findByUserId(userId);
    }

    public Optional<Moim> getMoim(Long moimId) {
        return moimRepository.findById(moimId);
    }

    public void deleteMoim(Long moimId) {
        // Retrieve the logbook based on logId
        Moim moim = moimRepository.findById(moimId)
                .orElseThrow(() -> new NoSuchElementException("Logbook not found for moimId: " + moimId));

        // Delete the logbook
        moimRepository.delete(moim);
    }


    public List<Moim> getAllMoims() {
        return moimRepository.findAll();
    }
}
