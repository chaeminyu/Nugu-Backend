package team8.nugu.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import team8.nugu.dto.NuguDTO;
import team8.nugu.dto.UserDTO;
import team8.nugu.entity.Users;
import team8.nugu.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean create(UserDTO dto){

        Users user = new Users();
        user.setUsername(dto.getUsername());

        // 동일한 usernmae 존재
        Boolean isExist = userRepository.existsByUsername(dto.getUsername());
        if(isExist){
            return false;
        }

        // 비밀번호 암호화 하여 저장
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setNickname(dto.getNickname());
        user.setMbti(dto.getMbti());
        user.setOrg(dto.getOrg());
        user.setInsta_url(dto.getInsta_url());
        user.setIntro(dto.getIntro());
        user.setKeyword1(dto.getKeyword1());
        user.setKeyword2(dto.getKeyword2());
        user.setKeyword3(dto.getKeyword3());

        // DB에 저장
        userRepository.save(user);
        return true;
    }

    public NuguDTO getInfo(String username) {
        // username으로 사용자 조회
        Users user = userRepository.findByUsername(username);

        NuguDTO nugu = new NuguDTO();
        nugu.setNickname(user.getNickname());
        nugu.setMbti(user.getMbti());
        nugu.setOrg(user.getOrg());
        nugu.setInsta_url(JsonNullable.of(user.getInsta_url()));
        nugu.setIntro(user.getIntro());
        nugu.setKeyword1(user.getKeyword1());
        nugu.setKeyword2(user.getKeyword2());
        nugu.setKeyword3(user.getKeyword3());

        return nugu;
    }

    @Transactional
    public void updateInfo(String username, NuguDTO nugu) {
        // username으로 사용자 조회
        Users user = userRepository.findByUsername(username);

        List<NuguDTO> list = new ArrayList<>();
        list.add(nugu);

        for(int i=0; i<list.size(); i++){
            if(list.get(i).getNickname() != null) user.setNickname(list.get(i).getNickname());
            if(list.get(i).getMbti() != null) user.setMbti(list.get(i).getMbti());
            if(list.get(i).getOrg() != null) user.setOrg(list.get(i).getOrg());

            // 인스타 url이 존재하는데 null 값
            if(list.get(i).getInsta_url().isPresent() && list.get(i).getInsta_url() == null) user.setInsta_url(null);
            // 인스타 url이 존재하는데 유효한 값
            else if(list.get(i).getInsta_url().isPresent() && list.get(i).getInsta_url() != null) user.setInsta_url(list.get(i).getInsta_url().get());

            if(list.get(i).getIntro() != null) user.setIntro(list.get(i).getIntro());
            if(list.get(i).getKeyword1() != null) user.setKeyword1(list.get(i).getKeyword1());
            if(list.get(i).getKeyword2() != null) user.setKeyword2(list.get(i).getKeyword2());
            if(list.get(i).getKeyword3() != null) user.setKeyword3(list.get(i).getKeyword3());
        }
        userRepository.save(user);
    }

    public String getUUID(String username) {
        // username으로 사용자 조회
        Users user = userRepository.findByUsername(username);

        String uuid = user.getUuid().toString();
        return uuid;
    }
}
