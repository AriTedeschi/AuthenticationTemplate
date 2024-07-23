package internal.management.accounts.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "TB_ROLE")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleEntity {
    @Id
    private Integer id;

    private String name;
}
