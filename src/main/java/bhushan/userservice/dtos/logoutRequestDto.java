package bhushan.userservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class logoutRequestDto {
    private Long userId;
    private String token;

}
