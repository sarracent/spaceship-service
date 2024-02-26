package w2m.travel.spaceshipsservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "name is required")
    @NotBlank(message = "name cannot be empty")
    private String name;

    @ManyToOne
    @JoinColumn(name="spaceship_id", nullable=false)
    @JsonIgnore
    private Spaceship spaceship;
}
