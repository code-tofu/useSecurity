package tofu.code.useSecurity.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignInRequestDTO {

    private String username;
    private String password;


}
