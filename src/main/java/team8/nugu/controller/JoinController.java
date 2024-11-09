package team8.nugu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team8.nugu.dto.UserDTO;
import team8.nugu.service.UserService;

@RestController
@RequestMapping(value = "/join")
public class JoinController {

    private final UserService userService;

    public JoinController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> addUser(@RequestBody UserDTO dto) {
        if(!dto.getPassword().equals(dto.getPassword2())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Passwords do not match");
        }

        if(userService.create(dto)){
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User created successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("The username already exists");
        }
    }
}
