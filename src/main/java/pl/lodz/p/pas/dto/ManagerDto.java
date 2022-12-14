package pl.lodz.p.pas.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManagerDto extends UserDto {

    @Builder
    public ManagerDto(String name, String surname, String login, String position) {
        super(name, surname, login);
        this.position = position;
    }

    @NotEmpty
    private String position;
}

